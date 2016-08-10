package ru.steamengine.helpers.anttasks.updateclassdefinition;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.resources.FileResource;
import ru.steamengine.helpers.anttasks.common.ConstructionUtils;
import ru.steamengine.helpers.anttasks.common.TaskClassPathScanner;
import ru.steamengine.rtti.basetypes.ClassFilter;

import java.io.*;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Steam engine corp. in 10.06.2010 19:20:29.
 *
 * @author Christopher Marlowe
 */
public class UpdateClassDefinition extends Java {

    private static final Charset CHARSET = Charset.forName("utf-8");

    private boolean overrideonchange = true;

    private boolean debug = false;

    private File dest;

    private Path compileclasspath;

    private Path searchclasspath;

    private String classfilter;

    private static final String VERSION = "14.01.2011 23:13";

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        log("setDebug( " + debug + ")");
        this.debug = debug;
    }

    public void setClassfilter(String classfilter) {
        logMessage("setClassfilter(" + classfilter + ")");
        this.classfilter = classfilter;
    }

    private void checkStartConditions() throws BuildException {
        if (compileclasspath == null)
            throw new BuildException("compileClasspath is null");

        if (searchclasspath == null)
            throw new BuildException("searchClasspath is null");

        if (dest == null)
            throw new BuildException("destination file is not defined");

        if (dest.isDirectory())
            throw new BuildException("destination file can not be a directory");

        if (classfilter == null)
            throw new BuildException("classfilter is null");

    }

    private void logMessage(String message) {
        if (debug)
            log(message);
    }

    private void logMessage() {
        logMessage("");
    }

    ///////////////////////////////////////////////////

    public void setCompileclasspath(File compileclasspath) {
        logMessage("setCompileclasspath(File compileclasspath)");
        createCompileclasspath().setLocation(compileclasspath);
    }

    public Path createCompileclasspath() {
        if (compileclasspath == null) {
            compileclasspath = new Path(getProject());
        }
        return compileclasspath.createPath();
    }


    public void setCompileclasspathref(Reference r) {
        logMessage("setSourcepathRef(Reference r)");
        createCompileclasspath().setRefid(r);
        logMessage(compileclasspath.toString());
    }


    ///////////////////////////////////////////////////

    public void setSearchclasspath(Path searchclasspath) {
        logMessage("setSearchclasspath(Path searchclasspath)");
        createSearchclasspath().add(searchclasspath);
    }

    public Path createSearchclasspath() {
        if (searchclasspath == null)
            searchclasspath = new Path(getProject());

        return searchclasspath.createPath();
    }

    public void setSearchclasspathref(Reference r) {
        logMessage("setSearchClasspathRef(Reference r)");
        createSearchclasspath().setRefid(r);
    }

    ///////////////////////////////////////////////////

    public void setDest(File dest) {
        this.dest = dest;
    }
    ///////////////////////////////////////////////////

    public void setOverrideonchange(boolean overrideonchange) {
        this.overrideonchange = overrideonchange;
    }

    @Override
    public void execute() throws BuildException {
        log("version = " + VERSION);

        log("debug = " + debug);
        checkStartConditions();


        try {

            List<URL> urls = new ArrayList<URL>();
            Iterator<FileResource> iterator;
            List<String> list = new ArrayList<String>();

            logMessage("compileclasspath");
            //noinspection unchecked

            logMessage(" compileclasspath.list() : " + compileclasspath.list());

            iterator = compileclasspath.iterator();
            String cp = "";
            for (; iterator.hasNext();) {

                File file = iterator.next().getFile();
                URL url = file.toURI().toURL();
                urls.add(url);
                logMessage(url.toString());

                if (file.isDirectory()) {
                    logMessage(file.getAbsolutePath());
                    cp = cp + file.getAbsolutePath() + TaskClassPathScanner.PATH_SEPARATOR;
                } else {
                    cp = cp + file.getAbsolutePath() + TaskClassPathScanner.PATH_SEPARATOR;
                    /// ? ? ? ? ? ? ? 
                }

            }
            logMessage("cp = " + cp);
            TaskClassPathScanner.findClasses(cp, list);


            logMessage("searchclasspath");
            //noinspection unchecked
            iterator = searchclasspath.iterator();
            for (; iterator.hasNext();) {
                File file = iterator.next().getFile();
                URL url = file.toURI().toURL();
                urls.add(url);
                logMessage(url.toString());
            }

            logMessage("" + urls);

//            URLClassLoader loader = new URLClassLoader(urls.toArray(new URL[urls.size()]));

            logMessage("classfilter = " + classfilter);
            Class<?> classFilterClass = Class.forName(classfilter);
            logMessage("loaded class filter class = " + classFilterClass);

            if (!ClassFilter.class.isAssignableFrom(classFilterClass))
                throw new BuildException("!ClassFilter.class.isAssignableFrom(classFilterClass)");

            logMessage("------------------------------------------------------------------------");

            Constructor defaultConstructor = ConstructionUtils.getDefaultConstructor(classFilterClass);
            if (defaultConstructor == null)
                throw new BuildException("Class " + classFilterClass + " does not have a default constructor");

            ClassFilter filter = ConstructionUtils.newInstance(defaultConstructor);
            if (filter == null)
                throw new BuildException("filter is null");


            logMessage();
            logMessage("" + filter);
            logMessage();

            logMessage("candidates  " + list);

            logMessage("checked classes:");
            List<String> classes = new ArrayList<String>();
            for (int i = list.size() - 1; i >= 0; i--) {
                String pathName = list.get(i);
                logMessage("pathName " + pathName);
                String className = TaskClassPathScanner.getClassName(pathName);
                logMessage("candidate class : " + className);

//                Class<?> clazz = Class.forName(className);
                Class<?> clazz = Class.forName(className, false, getClass().getClassLoader());
                logMessage("loaded class = " + clazz.getName());
                if (!filter.canUse(clazz)) {
                    list.remove(i);
                    logMessage("class " + className + " is not a loader");
                } else {
                    className = clazz.getName();
                    if (!classes.contains(className))
                        classes.add(className);
                    logMessage("marked class " + className);
                }

                logMessage();
            }

            if (!dest.exists() || (!overrideonchange) || (!isEqualToFile(classes)))
                writeToFile(classes);

        } catch (MalformedURLException e) {
            throw new BuildException(e);
        } catch (IOException e) {
            throw new BuildException(e);
        } catch (ClassNotFoundException e) {
            throw new BuildException(e);
        }

    }

    private static boolean isClass(Class clazz, Class testedClass) {
        Class temp = clazz;
        while (temp != null) {
            if (temp == testedClass)
                return true;
            temp = temp.getSuperclass();
        }
        return false;
    }

    private void writeToFile(List<String> list) throws IOException {
        logMessage("writing to file " + dest.getAbsolutePath());
        FileOutputStream stream = new FileOutputStream(dest);
        try {

            logMessage("classes to write");
            if (list.size() == 0) {
                logMessage("no classes to write .. ");
            } else
                for (String item : list)
                    logMessage(item);


            for (String item : list) {
                stream.write(item.getBytes(CHARSET));
                stream.write("\r\n".getBytes(CHARSET));
            }
            stream.flush();
        } finally {
            stream.close();
        }
    }

    private boolean isEqualToFile(List<String> list) throws IOException {
        FileInputStream stream = new FileInputStream(dest);
        try {
            byte[] buff = new byte[100];

            int i;
            ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
            while ((i = stream.read(buff)) > 0)
                inputStream.write(buff, 0, i);

            StringTokenizer tokenizer = new StringTokenizer(new String(inputStream.toByteArray(), CHARSET));
            List<String> list2 = new ArrayList<String>();
            for (; tokenizer.hasMoreTokens();)
                list2.add(tokenizer.nextToken());

            return areListsEqual(list, list2);
        } finally {
            stream.close();
        }
    }

    private static boolean areListsEqual(List<String> list1, List<String> list2) {
        for (String item : list1)
            if (!contains(list2, item))
                return false;

        for (String item : list2)
            if (!contains(list1, item))
                return false;

        return true;
    }

    private static boolean contains(List<String> list, String item) {
        for (String elem : list)
            if (elem.equals(item))
                return true;
        return false;
    }
}