package ru.steamengine.streaming.basetypes;

import java.io.OutputStream;

/**
 * Created by Steam engine corp in 07.07.2009 21:09:53
 *
 * Public API
 * @author Christopher Marlowe
 */
public interface ObjectWriter {

    /**
     * write an object to a stream
     * @param root an object from which writing is started
     * @param stream a stream to write to
     * @throws WriteError if an operation not successed
     */
    void write(Object root, OutputStream stream) throws WriteError;

    /**
     * write an object to a stream
     * @param root an object from which writing is started
     * @param objectResolver - object resolver
     * @param stream a stream to write to
     * @throws WriteError if an operation not successed
     */
    void write(Object root, ObjectResolver objectResolver, OutputStream stream) throws WriteError;


}
