package ru.steamengine.helpers.anttasks.smartcopy;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Java;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steam engine corp. in 11.12.2009 20:09:37
 *
 * @author Christopher Marlowe
 */
public class SmartCopy extends Java {


    public interface FileFilter {
        boolean filterFile(File file);
    }

    private final List<CopyItem> items = new ArrayList<CopyItem>();

    private boolean debug = true;

    private File sourcedir;

    private File targetdir;

    private boolean cleartargetdir = true;

    public void setCleartargetdir(boolean cleartargetdir) {
        this.cleartargetdir = cleartargetdir;
    }

    public File getSourcedir() {
        return sourcedir;
    }

    public void setSourcedir(File sourcedir) {
        this.sourcedir = sourcedir;
    }

    public File getTargetdir() {
        return targetdir;
    }

    public void setTargetdir(File targetdir) {
        this.targetdir = targetdir;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    private void copyDirectory(String sourceLocation,
                               String targetLocation,
                               List<CopyItem> symbols) throws BuildException {
        File source = new File(sourceLocation);
        File target = new File(targetLocation);
        logMessage("Copynig files from " + source.getAbsolutePath() +
                " to " + target.getAbsolutePath());
        if (!source.isDirectory())
            throw new IllegalArgumentException(sourceLocation + " is not a directory");
        doCopyDirectory(source, target, symbols);
    }


    @SuppressWarnings({"UnusedDeclaration"})
    private static boolean filterFile(File file, List<CopyItem> words) {
        if (file.isDirectory())
            return false;

        try {
            InputStream in = new FileInputStream(file);
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                copyStreams(in, out);
                String content = new String(out.toByteArray());

                for (CopyItem word : words) {
                    if (word.matches(content))
                        return true;
                }

            } finally {
                in.close();
            }

        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }

        return false;
    }


    private static void doCopyDirectory(File sourceLocation, File targetLocation, List<CopyItem> symbols)
            throws BuildException {

        try {
            if (sourceLocation.isDirectory()) {
                if (!targetLocation.exists())
                    targetLocation.mkdir();

                String[] children = sourceLocation.list();
                for (String aChildren : children) {
                    doCopyDirectory(new File(sourceLocation, aChildren),
                            new File(targetLocation, aChildren), symbols);

                }
            } else {
                if (filterFile(sourceLocation, symbols)) {
                    System.out.println("Copynig file from " + sourceLocation.getAbsolutePath() +
                            " to " + targetLocation.getAbsolutePath());

                    InputStream in = new FileInputStream(sourceLocation);
                    try {
                        OutputStream out = new FileOutputStream(targetLocation);
                        try {
                            copyStreams(in, out);
                        } finally {
                            out.close();
                        }
                    } finally {
                        in.close();
                    }
                }
            }
        } catch (IOException e) {
            throw new BuildException(e + "", e);
        }

    }

    private static void copyStreams(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0)
            out.write(buf, 0, len);
    }

    public void addItem(CopyItem item) {
        items.add(item);
    }

    private void checkStartConditions() throws BuildException {
        if (sourcedir == null)
            throw new BuildException("source is not defined");
        if (!sourcedir.isDirectory())
            throw new BuildException("source must be dirctory");
        if (!sourcedir.exists())
            throw new BuildException("source does not exist");
        if (targetdir == null)
            throw new BuildException("target is not defined");
        if (!targetdir.isDirectory())
            throw new BuildException("target must be dirctory");
        if (!targetdir.exists())
            throw new BuildException("target does not exist");
    }

    private void logMessage(String s) {
        if (debug)
            log(s);
    }

    @Override
    public String toString() {
        return "SmartCopy{" +
                "items=" + items +
                ", debug=" + debug +
                ", sourcedir=" + sourcedir +
                ", targetdir=" + targetdir +
                ", cleartargetdir=" + cleartargetdir +
                '}';
    }

    @Override
    public void execute() throws BuildException {
        logMessage("" + this);

        checkStartConditions();

        if (cleartargetdir && targetdir.exists()) {
            logMessage("deleting directory " + targetdir.getAbsolutePath());
            targetdir.delete();
        }

        copyDirectory(sourcedir.getAbsolutePath(), targetdir.getAbsolutePath(), items);
    }

}