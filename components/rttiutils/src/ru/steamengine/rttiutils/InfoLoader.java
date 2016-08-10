package ru.steamengine.rttiutils;

import ru.steamengine.rtti.basetypes.ClassFilter;
import ru.steamengine.rtti.basetypes.ClassResolver;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Steam engine corp. in 27.06.2010 0:12:13
 * <p/>
 * loads classes from definitions described in stream then passes them to filter
 *
 * @author Christopher Marlowe
 */
public class InfoLoader {

    private static final Charset CHARSET = Charset.forName("utf-8");

     public static <T> void scanStream(
            InputStream stream,
            ClassFilter<T>  filter,
            ClassResolver classResolver,
            T param) {

        if (filter == null)
            throw new NullPointerException("filter is null");
        if (stream == null)
            throw new NullPointerException("stream is null");
        if (classResolver == null)
            throw new NullPointerException("classResolver is null");

        byte[] bytes = new byte[100];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            int read;
            while ((read = stream.read(bytes)) > 0)
                outputStream.write(bytes, 0, read);

        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        List<String> list = new ArrayList<String>();
        StringTokenizer stringTokenizer = new StringTokenizer(new String(outputStream.toByteArray(), CHARSET));
        while (stringTokenizer.hasMoreTokens()) {
            String className = stringTokenizer.nextToken();

            if (!className.trim().isEmpty()) {
                Class<?> aClass = classResolver.loadClass(className);
                if (aClass == null) {
                    list.add(className);
                } else if (filter.canUse(aClass)) {
                    //noinspection unchecked
                    filter.doClass(aClass, param);
                }
            }
        }
        if (list.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (String s : list)
                builder.append(s).append("\r\n");
            throw new IllegalArgumentException("some classes can not be loaded :" + builder);
        }

    }

}
