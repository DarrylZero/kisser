package ru.steamengine.rtti.defaultimplementors.utils;

import ru.steamengine.rtti.basetypes.*;
import ru.steamengine.rtti.defaultimplementors.DefaultCountedIterator;
import ru.steamengine.rtti.defaultimplementors.DefaultPair;

import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Steam engine corp in 01.12.2009 21:22:21
 *
 * @author Christopher Marlowe
 */
public final class CommonStreamingUtils {

    private static class PropertyEntryI implements PropertyEntry {
        private final RTTIEntry entry;
        private final Object object;

        private PropertyEntryI(
                Object object,
                RTTIEntry entry) {
            if (object == null)
                throw new NullPointerException("object is null");
            if (entry == null)
                throw new NullPointerException("entry is null");

            this.entry = entry;
            this.object = object;
        }

        @Override
        public Object getObject() {
            return object;
        }

        @Override
        public RTTIEntry getEntry() {
            return entry;
        }
    }


    private static final String ALLOWED_SYMBOLS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_1234567890";
    private static final String DOT = ".";


    /**
     * Splits string into words (with whitespace devider)
     *
     * @param source source string.
     * @return words
     */

    public static String[] splitToWords(String source) {
        if (source == null)
            throw new IllegalArgumentException("propPath is null");

        String temp = source;
        List<String> list = new ArrayList<String>();
        while (temp != null) {
            String curr;
            temp = temp.trim();
            int index = temp.indexOf(" ");
            if (index == -1) {
                curr = temp;
                temp = null;
            } else {
                curr = temp.substring(0, index);
                temp = temp.substring(index + 1);
            }

            curr = curr.trim();
            list.add(curr);
        }
        return list.toArray(new String[list.size()]);
    }

    public static String[] divideByDot(String source) {
        if (source == null)
            throw new IllegalArgumentException("propPath is null");

        String temp = source;
        List<String> list = new ArrayList<String>();
        while (temp != null) {
            String curr;
            temp = temp.trim();
            int index = temp.indexOf(DOT);
            if (index == -1) {
                curr = temp;
                temp = null;
            } else {
                curr = temp.substring(0, index);
                temp = temp.substring(index + 1);
            }

            curr = curr.trim();
            list.add(curr);
        }
        return list.toArray(new String[list.size()]);
    }


    /**
     * get a class "ladder"
     *
     * @param startClass exemined class
     * @return ancestor an class itself
     */
    public static List<Class> getClassLadder(final Class startClass) {
        if (startClass == null)
            throw new IllegalArgumentException("startClass is null");

        List<Class> tempList = new ArrayList<Class>();
        Class temp = startClass;
        while (temp != null) {
            tempList.add(temp);
            temp = temp.getSuperclass();
        }

        List<Class> result = new ArrayList<Class>();
        for (int i = tempList.size() - 1; i >= 0; i--)
            result.add(tempList.get(i));
        return result;
    }


    /**
     * checks wether a passed string is valid ident
     *
     * @param ident ident.
     * @return true if the string is valid ident`.
     */
    public static boolean isValidIdent(String ident) {
        if (ident == null)
            return false;

        char[] chars = ident.toCharArray();

        if (chars.length < 1 || Character.isDigit(chars[0]))
            return false;

        for (char c : chars)
            if (!isAllowedChar(c))
                return false;

        return true;
    }


    /**
     * @param pathPrefix prefix
     * @param newPart    nw part.
     * @return full path to property
     * @throws IllegalArgumentException if one of params is null
     */
    public static String getPropPath(final String pathPrefix, final String newPart) {
        if (pathPrefix == null)
            throw new IllegalArgumentException("oldPath is null");
        if (newPart == null)
            throw new IllegalArgumentException("newPart is null");

        return pathPrefix.isEmpty() ? newPart : pathPrefix + DOT + newPart;
    }



    public static Class<?> getListedItemType(ListedItem listedItem) {
        Class<? extends ListedItem> aClass = listedItem.getClass();
        Class<?> childType = null;
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {

            if (method.getReturnType().isArray())
                continue;

            if (!method.getName().equals("newItem"))
                continue;

            Class<?>[] paramTypes = method.getParameterTypes();
            if (paramTypes.length != 1)
                continue;

            Class<?> tempChild = method.getReturnType();
            if (tempChild == null)
                continue;

            if (childType == null || childType.isAssignableFrom(tempChild)) {
                childType = tempChild;
            }
        }

        if (childType == null)
            throw new IllegalStateException();

        return childType;
    }

    @Deprecated
    public static Class<?> getListedItemTypeNew(ListedItem listedItem) {
        if (listedItem == null)
            throw new NullPointerException("listedItem is null");

        Class<? extends ListedItem> aClass = listedItem.getClass();
        Class<?> childType = null;
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {

            if (!method.getReturnType().isArray())
                continue;

            if (!method.getName().equals("getItems"))
                continue;

            Class<?>[] paramTypes = method.getParameterTypes();
            if (paramTypes.length != 1)
                continue;

            Class<?> tempChild = method.getReturnType().getComponentType();
            if (tempChild == null)
                continue;

            if (childType == null || childType.isAssignableFrom(tempChild)) {
                childType = tempChild;
            }
        }

        if (childType == null)
            throw new IllegalStateException();

        return childType;
    }

    public static Class<?> getListedItemType(CommonListedItem listedItem) {
        Class<? extends CommonListedItem> aClass = listedItem.getClass();
        TypeVariable<? extends Class<? extends CommonListedItem>>[] typeVariables = aClass.getTypeParameters();


        Class<?> childType = null;
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {

            if (method.getReturnType().isArray())
                continue;

            if (!method.getName().equals("newItem"))
                continue;

            Class<?>[] paramTypes = method.getParameterTypes();
            if (paramTypes.length != 1)
                continue;

            Class<?> tempChild = method.getReturnType();
            if (tempChild == null)
                continue;

            if (childType == null || childType.isAssignableFrom(tempChild)) {
                childType = tempChild;
            }
        }

        if (childType == null)
            throw new IllegalStateException();

        return childType;
    }

    @Deprecated
    public static Class<?> getListedItemTypeNew(CommonListedItem listedItem) {
        Class<? extends CommonListedItem> aClass = listedItem.getClass();
        TypeVariable<? extends Class<? extends CommonListedItem>>[] typeVariables = aClass.getTypeParameters();


        Class<?> childType = null;
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {

            if (!method.getReturnType().isArray())
                continue;

            if (!method.getName().equals("getItems"))
                continue;

            Class<?>[] paramTypes = method.getParameterTypes();
            if (paramTypes.length != 1)
                continue;

            Class<?> tempChild = method.getReturnType().getComponentType();
            if (tempChild == null)
                continue;

            if (childType == null || childType.isAssignableFrom(tempChild)) {
                childType = tempChild;
            }
        }

        if (childType == null)
            throw new IllegalStateException();

        return childType;
    }


    /**
     * get a proiperty type by its entry.
     *
     * @param entry        property entry
     * @param registryUser registry user interface
     * @return property type.
     */
    public static PropertyTypeIdent getPropTypeIdent(RTTIEntry entry,
                                                     RegistryUser registryUser) {
        Class propClass = entry.getPropType();
        PropertyKind kind = entry.getPropertyKind();
        switch (kind) {
            case RW:
                return registryUser.getSimpleType(propClass) != null ?
                        PropertyTypeIdent.value :
                        PropertyTypeIdent.reference;

            case RO:
                return PropertyTypeIdent.nested;

            default:
                throw new IllegalArgumentException("unknown type " + kind.name());
        }
    }


    /**
     * @param registryUser a registry user interface
     * @param obj          an object
     * @param propPath     full dot devided path to property
     * @return item or null if not found.
     */
    public static Pair<Object, RTTIEntry> findPropertyEntryO2(RegistryUser registryUser,
                                                             Object obj,
                                                             String propPath) {
        if (obj == null)
            throw new IllegalArgumentException("clazz is null");
        if (propPath == null)
            throw new IllegalArgumentException("propPath is null");

        String[] paths = divideByDot(propPath);
        Object tempObj = obj;
        RTTIEntry tempEntry = null;
        for (int i = 0; i < paths.length; i++) {
            if (tempObj == null)
                return null;

            String path = paths[i];
            RTTIEntries props = registryUser.getRTTI(tempObj.getClass());
            if (props == null)
                return null;

            tempEntry = props.findEntry(path);
            if (tempEntry == null)
                return null;

            PropertyTypeIdent propTypeIdent = getPropTypeIdent(tempEntry, registryUser);
            if (i < paths.length - 1) {
                if (propTypeIdent != PropertyTypeIdent.nested)
                    return null;
            } else {
                return new DefaultPair<Object, RTTIEntry>(tempObj, tempEntry);
            }

            tempObj = tempEntry.getVal(tempObj);
        }
        return new DefaultPair<Object, RTTIEntry>(tempObj, tempEntry);
    }


    /**
     * @param registryUser a registry user interface
     * @param obj          an object
     * @param propPath     full dot devided path to property
     * @return item or null if not found.
     */
    public static PropertyEntry findPropertyEntry(RegistryUser registryUser,
                                                  Object obj,
                                                  String propPath) {
        if (obj == null)
            throw new IllegalArgumentException("clazz is null");
        if (propPath == null)
            throw new IllegalArgumentException("propPath is null");

        String[] paths = divideByDot(propPath);
        Object tempObj = obj;
        RTTIEntry tempEntry = null;
        for (int i = 0; i < paths.length; i++) {
            if (tempObj == null)
                return null;

            String path = paths[i];
            RTTIEntries props = registryUser.getRTTI(tempObj.getClass());
            if (props == null)
                return null;

            tempEntry = props.findEntry(path);
            if (tempEntry == null)
                return null;

            PropertyTypeIdent propTypeIdent = getPropTypeIdent(tempEntry, registryUser);
            if (i < paths.length - 1) {
                if (propTypeIdent != PropertyTypeIdent.nested)
                    return null;
            } else {
                return new PropertyEntryI(tempObj, tempEntry);
            }

            tempObj = tempEntry.getVal(tempObj);
        }
        return new PropertyEntryI(tempObj, tempEntry);
    }


    /**
     * Find a list by full property
     *
     * @param registryUser - reg user
     * @param obj          object
     * @param propPath     full path
     * @return a list item or null if nothing found
     */
    public static Pair<Object, ListSlot> findListedEntry(RegistryUser registryUser, Object obj, String propPath) {
        if (obj == null)
            throw new IllegalArgumentException("clazz == null");
        if (propPath == null)
            throw new IllegalArgumentException("propPath == null");

        String[] paths = divideByDot(propPath);
        Object tempObj = obj;
        RTTIEntry tempEntry;
        for (int i = 0; i < paths.length; i++) {
            if (tempObj == null)
                return null;

            String path = paths[i];
            RTTIEntries props = registryUser.getRTTI(tempObj.getClass());
            if (props == null)
                return null;

            if (i < paths.length - 1) {
                tempEntry = props.findEntry(path);
                if (tempEntry == null)
                    return null;
                PropertyTypeIdent propTypeIdent = getPropTypeIdent(tempEntry, registryUser);
                if (propTypeIdent != PropertyTypeIdent.nested)
                    return null;
                tempObj = tempEntry.getVal(tempObj);

            } else {

                ListSlot slot = props.findList(path);
                return slot != null ? new DefaultPair<Object, ListSlot>(tempObj, slot) : null;
            }

        }
        return null;
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


    public static Iterable getListIterator(
            final ListSlot slot,
            final Object obj) {
        return new Iterable() {
            @Override
            public Iterator iterator() {
                return slot.getIterator(obj);
            }
        };
    }

    public static <T> CountedIterator<T> getCI(
            final Iterator<T> iterator) {
        return new DefaultCountedIterator<T>(iterator);
    }

    public static CountedIterator<Object> getObjectIterator(
            ObjectProcessor proc,
            final Object parent) {
        if (proc instanceof ObjectIterable) {
            final ObjectIterable iterable = (ObjectIterable) proc;
            return new DefaultCountedIterator(iterable.getIterator(parent));
        } else {
            return new DefaultCountedIterator(new ArrayIterable(proc.getObjectChildren(parent)).iterator());
        }
    }

    public static CountedIterator<RTTIEntry> getPropertiesIterator(
            final RTTIEntries entries) {
        return new CountedIterator<RTTIEntry>() {
            private int pos = 0;
            private Iterator<RTTIEntry> iterator = entries.getProperties().iterator();

            @Override
            public int nextIndex() {
                return pos;
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public RTTIEntry next() {
                pos++;
                return iterator.next();
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }

    public static CountedIterator<ListSlot> getListSlotIterator(ListSlot[] listSlots) {
        return getCI(new ArrayIterable<ListSlot>(listSlots).iterator());
    }

    public static CountedIterator<ListSlot> getListSlotIterator(RTTIEntries listSlots) {
        return getCI(listSlots.getLists().iterator());
    }

    private static boolean isAllowedChar(char c) {
        return ALLOWED_SYMBOLS.indexOf(c) != -1;
    }

}
