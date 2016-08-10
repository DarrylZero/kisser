package ru.steamengine.rtti.defaultimplementors;

import ru.steamengine.rtti.basetypes.CountedIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Steam engine corp. in 23.04.2011 4:40:04
 *
 * @author Christopher Marlowe
 */
public class DefaultCountedIterator<T> implements CountedIterator<T> {

    private static final DefaultCountedIterator DEFAULT_COUNTED_ITERATOR = new DefaultCountedIterator();

    public static <T> CountedIterator<T> nullIterator(Class<T> clazz) {
        return DEFAULT_COUNTED_ITERATOR;
    }

    private final Iterator<T> iterator;
    private int iteration = 0;

    public DefaultCountedIterator(Iterator<T> iterator) {
        if (iterator == null)
            throw new NullPointerException("iterator is null");
        this.iterator = iterator;
    }

    private DefaultCountedIterator() {
        this.iterator = new Iterator<T>() {

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
        };
    }

    @Override
    public int nextIndex() {
        return iteration;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        iteration++;
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();        
    }
}