package ru.steamengine.streaming.basetypes;

/**
 * Created by Steam engine corp. in 21.08.2011 21:32:16
 *
 * @author Christopher Marlowe
 */
public interface PropertyFixup {

    /**
     *
     * @return resolved object
     */
    Object getObject();

    /**
     * Path to object
     * @return
     */
    String getPropPath();

    /**
     * performs resolving
     */
    void resolve();

}