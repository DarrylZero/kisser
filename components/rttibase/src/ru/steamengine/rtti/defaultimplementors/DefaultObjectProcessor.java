package ru.steamengine.rtti.defaultimplementors;

import ru.steamengine.rtti.basetypes.ObjectIterable;
import ru.steamengine.rtti.basetypes.ObjectProcessor;

import java.util.Iterator;

/**
 * Created by Steam engine corp. in 10.04.2011 1:14:15
 *
 * @author Christopher Marlowe
 *         Public API
 */
abstract public class DefaultObjectProcessor<Parent> implements ObjectProcessor<Parent>,
        ObjectIterable<Parent, Object> {

    @Override
    public Iterator<Object> getIterator(
            final Parent parent) {

        return new Iterator<Object>() {
            private final Object[] children = getObjectChildren(parent);
            private int pos = 0;

            @Override
            public boolean hasNext() {
                return pos < children.length;
            }

            @Override
            public Object next() {
                return children[pos++];  
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove() is not supported");
            }
        };
    }
}
