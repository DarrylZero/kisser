package ru.steamengine.rtti.basetypes;

import ru.steamengine.rtti.defaultimplementors.NullIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Steam engine corp in 14.10.2009 0:06:34
 * <p/>
 * Public API
 *
 * @author Christopher Marlowe
 */
public interface RTTIEntries {

    public static final RTTIEntry[] EMPTY_RTTI_ENTRIES = {};
    public static final ListSlot[] EMPTY_LISTED_ENTRIES = {};
    public static final NullIterator<RTTIEntry> NULL_ITERATOR = new NullIterator<RTTIEntry>();
    public static final Iterator<RTTIEntry> NO_PROPS_ITERATOR = new Iterator<RTTIEntry>() {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public RTTIEntry next() {
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    };

    public static final Iterable<RTTIEntry> NO_PROPS_ITERABLE = new Iterable<RTTIEntry>() {
        @Override
        public Iterator<RTTIEntry> iterator() {
            return NO_PROPS_ITERATOR;
        }
    };

    public static final Iterable<ListSlot> ITERABLE_NO_LISTS = new Iterable<ListSlot>() {
        @Override
        public Iterator<ListSlot> iterator() {
            return new Iterator<ListSlot>() {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public ListSlot next() {
                    throw new NoSuchElementException();
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    };
    public static final RTTIEntries NO_PROPS = new RTTIEntries() {
        @Override
        public RTTIEntry findEntry(String propertyName) {
            return null;
        }

        @Override
        public Iterable<RTTIEntry> getProperties() {
            return NO_PROPS_ITERABLE;
        }

        @Override
        public Iterable<ListSlot> getLists() {
            return ITERABLE_NO_LISTS;
        }

        @Override
        public ListSlot findList(String propertyName) {
            return null;
        }
    };


    /**
     * find an item by name
     *
     * @param propertyName name
     * @return an item or null
     */
    RTTIEntry findEntry(String propertyName);

    /**
     * get an iterator over all elements of RTTIEntry
     *
     * @return an iterator over all elements of RTTIEntry
     */
    Iterable<RTTIEntry> getProperties();

    /**
     * @param propertyName property name .
     * @return a listed item interface or null if property is not a list proprty.
     */
    ListSlot findList(String propertyName);

    /**
     * get all list item iterator
     *
     * @return get all list item iterator
     */
    Iterable<ListSlot> getLists();

}
