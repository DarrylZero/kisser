package ru.steamengine.rttiutils;

import java.io.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Created by Steam engine corp. in 29.06.2010 21:57:26
 *
 * @author Christopher Marlowe
 */
public class ResourceLoader {

    public static InputStream loadResource(PathName pathName) {
        if (pathName == null)
            throw new NullPointerException("pathName is null");

        switch (pathName.getKind()) {
            case file:
                try {
                    File file = new File(pathName.getPath(), pathName.getName());
                    FileInputStream stream = new FileInputStream(file);
                    InputStream result;
                    try {
                        result = copyStream(stream);
                    } finally {
                        stream.close();
                    }

                    return result;
                } catch (FileNotFoundException e) {
                    throw new IllegalArgumentException(e);
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }


            case jar:
                File file = new File(pathName.getPath());
                try {
                    JarFile jarFile = new JarFile(file);
                    ZipEntry entry = jarFile.getEntry(pathName.getName());
                    InputStream jarResource = jarFile.getInputStream(entry);
                    InputStream jarResult;
                    try {
                        jarResult = copyStream(jarResource);
                    } finally {
                        jarResource.close();
                    }
                    return jarResult;


                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
                
            default:
                throw new IllegalArgumentException("unknown kind " + pathName.getKind());
        }
    }

    private static InputStream copyStream(InputStream stream) throws IOException {
        byte[] bytes = new byte[1024];
        int read;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        while ((read = stream.read(bytes)) > 0)
            outputStream.write(bytes, 0, read);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }


}
