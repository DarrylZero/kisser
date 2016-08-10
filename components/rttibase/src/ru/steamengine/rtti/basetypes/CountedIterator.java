package ru.steamengine.rtti.basetypes;

import java.util.Iterator;

/**
 * The iteratar that is aware of object's interation
 * <p/>
 * Created by Steam engine corp. in 23.04.2011 4:40:04
 *
 * @author Christopher Marlowe
 */
public interface CountedIterator<T> extends Iterator<T> {

    /**
     * Gets next iteration number(index). So if no iteration has yet been made - 0 is returned.
     * Value <B>must</B> be retrieved before next() method is called.
     *
     * @return
     */
    int nextIndex();
}
