package ru.steamengine.helpers.anttasks.filechecker;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Java;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steam engine corp. in 17.09.2010 21:31:06
 *
 * @author Christopher Marlowe
 */
public class FileChecker extends Java {

    private static final String RUS = "АБВГДЕЁЖЗИКЛМНОПРСТУФХЦЧШЩЭЮЯабвгдеёжзиклмнопрстуфхцчшщэюя";

    private static final String FILE_EXCLUSION = "#FE";

    private static final String LINE_EXCLUSION = "#LE";

    private File startpath;

    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public File getStartpath() {
        return startpath;
    }

    public void setStartpath(File startpath) {
        this.startpath = startpath;
    }

    private void logMessage(String message) {
        log(message);
    }

    private void checkConditions() {
        if (this.startpath == null)
            throw new BuildException("startPath is not defined");
        if (!this.startpath.isDirectory())
            throw new BuildException("startPath is not a directory");

                 
    }

    @Override
    public void execute() throws BuildException {
//        log("getCommandLine() " + getCommandLine());

        if (!isEnabled())
            return;

        checkConditions();

        try {
            ArrayList<String> files = new ArrayList<String>();
            scanDir("", startpath, files);
            logMessage("" + files);

            byte[] bytes = new byte[512];
            StringBuilder builder = new StringBuilder();
            StringBuffer buffer = new StringBuffer();
            for (String fileName : files) {
                logMessage("Exemining file " + fileName);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                FileInputStream fileInputStream = new FileInputStream(fileName);
                try {
                    int i;
                    while ((i = fileInputStream.read(bytes)) > 0)
                        outputStream.write(bytes, 0, i);

                } finally {
                    fileInputStream.close();
                }

                BufferedReader reader =
                        new BufferedReader(new StringReader(
                                new String(
                                        outputStream.toByteArray())));
                try {
                    String line;
                    int pos = 1;
                    List<Integer> list = new ArrayList<Integer>();
                    boolean exc = false;
                    while ((line = reader.readLine()) != null) {
                        if ((line.indexOf(LINE_EXCLUSION) < 0) && hasWrongSymbols(line))
                            list.add(pos);

                        exc = exc || (line.indexOf(FILE_EXCLUSION) >= 0);
                        pos++;
                    }


                    if (!list.isEmpty()) {
                        StringBuilder b = new StringBuilder();
                        for (int k = 0; k < list.size(); k++) {
                            b.append(list.get(k));
                            if (k != list.size() - 1)
                                b.append(", ");
                        }

                        String message = "file " + fileName +
                                " has wrong symbols : at line(s) " + b.toString();
                        if (!exc) {
                            throw new BuildException(message);
                        } else {
                            logMessage(message);
                        }
                    }
                } finally {
                    reader.close();
                }


            }


        } catch (Throwable throwable) {
            throw new BuildException(throwable.toString(), throwable);
        }
    }


    private static boolean hasWrongSymbols(String line) {
        for (int i = 0; i < line.length(); i++)
            if (RUS.indexOf(line.charAt(i)) >= 0)
                return true;

        return false;
    }

    private static void scanDir(String pkg, File dir, List<String> files) {
        for (File file : dir.listFiles()) { // Перебираем все файлы в директории
            if (file.isDirectory()) {
                scanDir(pkg + file.getName() + "/", file, files); // Сканируем директорию пакета

            } else {
                if (file.getName().endsWith(".java"))
                    files.add(file.getAbsolutePath());

            }
        }
    }


}
