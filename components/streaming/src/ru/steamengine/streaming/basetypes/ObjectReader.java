package ru.steamengine.streaming.basetypes;

import java.io.InputStream;

/**
 * Created by Steam engine corp in 07.07.2009 21:10:01
 * <p/>
 * Public API
 *
 * @author Christopher Marlowe
 */

public interface ObjectReader {

    /**
     * @param stream                  a stream to read from
     * @param readerExceptionHandler an error handler
     * @return read object
     * @throws ReadError if an object can not be read
     */
    <T> T read(InputStream stream,
               ReaderExceptionHandler readerExceptionHandler) throws ReadError;

    /**
     * @param stream                  a stream to read from
     * @param objectResolver
     * @param readerExceptionHandler an error handler
     * @return read object
     * @throws ReadError if an object can not be read
     */
    <T> T read(InputStream stream,
               ObjectResolver objectResolver,
               ReaderExceptionHandler readerExceptionHandler) throws ReadError;

}
