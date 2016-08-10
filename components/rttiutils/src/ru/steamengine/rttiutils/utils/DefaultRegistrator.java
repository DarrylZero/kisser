package ru.steamengine.rttiutils.utils;

import ru.steamengine.rtti.basetypes.Registry;
import ru.steamengine.rtti.basetypes.LoaderMark;
import ru.steamengine.rtti.basetypes.ClassResolver;
import ru.steamengine.rttiutils.PathName;
import ru.steamengine.rttiutils.ClassPathScanner;
import ru.steamengine.rttiutils.ResourceLoader;
import ru.steamengine.rttiutils.InfoLoader;
import ru.steamengine.rttiutils.defaultimplementors.LoaderClassFilter;

import java.util.List;
import java.io.IOException;
import java.io.InputStream;

/**
 * Public api
 */

public class DefaultRegistrator {

    public static void register(final Registry r) {
        String classPath = System.getProperty("java.class.path");
        try {
            List<PathName> list = ClassPathScanner.scan(classPath, ".clr");
            for (PathName pathName : list)
                scanFile(pathName, r);
            r.force();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static void scanFile(PathName pathName, Registry registry) {
        InputStream inputStream = ResourceLoader.loadResource(pathName);
        InfoLoader.scanStream(
                inputStream, new LoaderClassFilter(),
                new ClassResolver() {
                    @Override
                    public Class<?> loadClass(String s) {
                        return loadTheClass(s);
                    }
                },
                registry
        );
    }

    private static Class<?> loadTheClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return  null;
        }
    }

}
