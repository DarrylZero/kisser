package ru.steamengine.rttiutils;

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
public class ClassPathScanner {

    public static final String APP_CLASS_PATH = System.getProperty("java.class.path");
    public static final String CLASS_EXT = ".class";
    public static final String PATH_SEPARATOR = System.getProperty("path.separator");

    /**
     * Scan a classpath string and return a list of classes
     *
     * @param cp  classpath
     * @param ext file extension
     * @return list of found files
     * @throws java.io.IOException
     */

    public static List<PathName> scan(String cp,
                                      String ext) throws IOException {

        List<PathName> classes = new ArrayList<PathName>();
        scan(cp, ext, classes);
        return classes;
    }

    /**
     * Scan all files and add them to list
     *
     * @param cp      classpath
     * @param ext     file extension
     * @param classes list of files where result is added
     * @throws java.io.IOException
     */
    public static void scan(String cp,
                            String ext,
                            List<PathName> classes) throws IOException {
        String[] entries = getPathItems(cp);
        for (String entryName : entries) {
            File file = new File(entryName);
            if (file.isDirectory()) {
                scanDir(entryName, ext, file, classes);
            } else if (file.getName().endsWith(".jar")) {
                scanJar(file, ext, classes);
            } else {
                scanFileName(PathName.Kind.file,
                        file.getParentFile().getAbsolutePath(),
                        file.getName(), ext, classes);
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
                                String ext,
                                List<PathName> classes)
            throws IOException {
        JarFile jar = new JarFile(jarFile); 

        for (Enumeration<JarEntry> e = jar.entries(); e.hasMoreElements();) {
            JarEntry entry = e.nextElement();
            if (entry.isDirectory()) // Skipping dirs
                continue;

            scanFileName(PathName.Kind.jar, jarFile.getAbsolutePath(), entry.getName(), ext, classes);
        }
        jar.close();
    }

    private static void scanDir(String pkg,
                                String ext,
                                File dir,
                                List<PathName> classes) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                scanDir(pkg + file.getName() + "/", ext, file, classes); 

            } else {
                scanFileName(PathName.Kind.file, pkg, file.getName(), ext, classes);

            }
        }
    }


    private static void scanFileName(PathName.Kind kind,
                                     String path,
                                     String name,
                                     String ext,
                                     List<PathName> files) {
        if (!name.endsWith(ext))
            return;

        boolean add = findPathName(kind, path, name, files) == null;
        if (add) {
            files.add(new PathName(kind, path, name));
        } else {
            add = false;
        }
    }


    public static PathName findPathName(PathName.Kind kind,
                                        String path,
                                        String name,
                                        List<PathName> files) {

        for (PathName pathName : files) {
            if (pathName.getKind().equals(kind) &&
                    pathName.getPath().equals(path) &&
                    pathName.getName().equals(name))
                return pathName;
        }
        return null;

    }


    public static String getClassName(String name) {
         return name.substring(0, name.length() - CLASS_EXT.length()).
                replace('/', '.').
                replace(' ', '.');
    }


}