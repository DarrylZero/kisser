package ru.steamengine.rttiutils;


import ru.steamengine.rtti.basetypes.ClassFilter;
import ru.steamengine.rtti.basetypes.ClassResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Steam engine corp. in 29.06.2010 22:35:04
 *
 * @author Christopher Marlowe
 */
public class Test {

    public static void main(String[] args) throws IOException {
        

        //System.getProperty("java.class.path")


        String cp = "N:/arch/v0.9/research/build/components/pack2.jar;" +
                "N:/arch/v0.9/research/build/components/classes.crl";

        List<PathName> list = ClassPathScanner.scan(cp, ".crl");
        for (PathName item : list) {
            InputStream stream = ResourceLoader.loadResource(item);
            try {
                InfoLoader.scanStream(
                        stream,
                        new ClassFilter() {
                            @Override
                            public boolean canUse(Class clazz) {
                                return false;  //To change body of implemented methods use File | Settings | File Templates.
                            }

                            @Override
                            public void doClass(Class clazz, Object param) {
                                //To change body of implemented methods use File | Settings | File Templates.
                            }
                        },
                        new ClassResolver() {
                            @Override
                            public Class<?> loadClass(String className) {
                                return null;  //To change body of implemented methods use File | Settings | File Templates.
                            }
                        },
                        item);

            } finally {
                stream.close();
            }


        }

        list = ClassPathScanner.scan("N:/arch/v0.9/research/build/components/anttask", ".clr");
        for (PathName item : list) {
            InputStream stream = ResourceLoader.loadResource(item);
            try {
            } finally {
                stream.close();
            }
        }
    }


}
