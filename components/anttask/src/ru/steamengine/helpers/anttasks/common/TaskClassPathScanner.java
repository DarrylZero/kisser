package ru.steamengine.helpers.anttasks.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Steam engine corp. in 24.06.2010 23:34:25
 * <p/>
 * Public API
 *
 * @author Christopher Marlowe
 */
public class TaskClassPathScanner {

    public static final String APP_CLASS_PATH = System.getProperty("java.class.path");

    public static final String PATH_SEPARATOR = System.getProperty("path.separator");

    private static final String CLASS_EXT = ".class";

    public static List<String> findClasses(String cp) throws IOException {
        List<String> classes = new ArrayList<String>();
        findClasses(cp, classes);
        return classes;
    }

    public static void findClasses(String cp,
                                   List<String> classes) throws IOException {
        String[] entries = getPathItems(cp);
        for (String entryName : entries) {
            File file = new File(entryName);
            if (file.isDirectory()) {
                scanDir("", file, classes);
            } else if (file.getName().endsWith(".jar")) {
                scanJar(file, classes);
            } else {
                scanFileName(entryName, file.getPath(), classes);
            }
        }
    }

    private static String[] getPathItems(String cp) {
        List<String> list = new ArrayList<String>();
        while (cp != null) {
            int index = cp.indexOf(PATH_SEPARATOR);
            String cur;
            if (index >= 0) {
                cur = cp.substring(0, index);
                cp = cp.substring(index + PATH_SEPARATOR.length(), cp.length());
            } else {
                cur = cp;
                cp = null;
            }

            if (cur.trim().isEmpty()) {
                cp = null;
            } else {
                list.add(cur);
            }

        }

        return list.toArray(new String[list.size()]);
    }

    private static void scanJar(File jarFile,
                                List<String> classes)
            throws IOException {
        JarFile jar = new JarFile(jarFile);

        for (Enumeration<JarEntry> e = jar.entries(); e.hasMoreElements();) {
            JarEntry entry = e.nextElement();
            if (entry.isDirectory())
                continue;

            scanFileName("", entry.getName(), classes);
        }
        jar.close();
    }

    private static void scanDir(
            String pkg,
            File dir,
            List<String> classes) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                scanDir(pkg + file.getName() + "/", file, classes);

            } else {
                scanFileName(pkg, file.getName(), classes); 

            }
        }
    }



    private static void scanFileName(String base,
                                     String name,
                                     List<String> files) {
        String fullName = base + name;
        if (!fullName.endsWith(CLASS_EXT))
            return;

        files.add(fullName);
    }

    public static String getClassName(String name) {
        return name.substring(0, name.length() - CLASS_EXT.length()).
                replace('/', '.').
                replace(' ', '.');
    }


}