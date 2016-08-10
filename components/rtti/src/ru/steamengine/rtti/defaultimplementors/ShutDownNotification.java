/**
 * Created by Steam engine corp. in 26.10.2011 21:47:56
 *
 * @author Christopher Marlowe
 */


package ru.steamengine.rtti.defaultimplementors;

import ru.steamengine.rtti.basetypes.RegistryUser;
import ru.steamengine.rtti.basetypes.ShutDownNotifier;
import ru.steamengine.rtti.defaultimplementors.utils.CommonStreamingUtils;

import java.util.*;

public class ShutDownNotification {

    private static interface Duos {
        Object getObject();

        Iterator<Object> getIterator();
    }

    private static class ArrayIterable<T> implements Iterable<T> {
        private final T[] items;

        public ArrayIterable(T[] items) {
            if (items == null)
                throw new NullPointerException("items is null");
            this.items = items;
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                private int pos = 0;

                @Override
                public boolean hasNext() {
                    return pos < items.length;
                }

                @Override
                public T next() {
                    if (!hasNext())
                        throw new NoSuchElementException();
                    return items[pos++];
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException("remove() is not supported");
                }
            };
        }
    }


    /**
     *
     * @param root a root object
     * @param registryUser registry user
     */
    public static void notifyShutDown(
            Object root,
            RegistryUser registryUser) {

        Stack<Duos> stack = new Stack<Duos>();
        stack.push(getItem(registryUser, root));

        while (!stack.isEmpty()) {
            Duos item = stack.peek();
            if (item.getIterator().hasNext()) {
                Object subObject = item.getIterator().next();
                stack.push(getItem(registryUser, subObject));
            } else {
                ShutDownNotifier notifier = registryUser.getShutDownNotifier(item.getObject().getClass());
                stack.pop();

                try {
                    //noinspection unchecked
                    notifier.shutingDown(item.getObject());
                } catch (Throwable ignored) {                
                }
            }
        }
    }

    private static Duos getItem(
            RegistryUser registryUser,
            Object object) {

        final Iterator<Object> iterator =
                CommonStreamingUtils.getObjectIterator(registryUser.getObjProcessor(object.getClass()), object);
        final Object obj = object;

        return new Duos() {
            @Override
            public Object getObject() {
                return obj;
            }

            @Override
            public Iterator<Object> getIterator() {
                return iterator;
            }
        };

    }

}
