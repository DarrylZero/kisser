package ru.steamengine.rtti.defaultimplementors;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Steam engine corp. in 19.04.2011 23:27:49
 *
 * @author Christopher Marlowe
 */
public class NullIterator<T> implements Iterator<T> {
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public T next() {
        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
