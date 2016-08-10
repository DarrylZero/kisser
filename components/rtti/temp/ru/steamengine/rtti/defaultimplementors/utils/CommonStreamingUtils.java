package ru.steamengine.rtti.defaultimplementors.utils;

import ru.steamengine.rtti.basetypes.*;
import ru.steamengine.rtti.defaultimplementors.DefaultPair;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steam engine corp in 01.12.2009 21:22:21
 *
 * @author Christopher Marlowe
 */
public final class CommonStreamingUtils {

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


    public static void main(String[] args) {
        String[] pathsItems = divideByDot("1.2.3.4.5.6.7.8.9.10.11.12.13.14.15.16.17");
        for (String s : pathsItems)
            System.out.println(s);

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
        Class<?> childType = null;

        Method[] methods = listedItem.getClass().getMethods();
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
     * @param obj      an object
     * @param propPath full dot devided path to property
     * @return item or null if not found.
     */
    public static Pair<Object, RTTIEntry> findPropertyEntry(RegistryUser registryUser,
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


    public static Pair<Object, ListedItemSlot> findListedEntry(RegistryUser registryUser, Object obj, String propPath) {
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

                ListedItem listedItem = props.findListedItem(path);
                return listedItem != null ? new DefaultPair<Object, ListedItemSlot>
                        (tempObj, new ListedItemSlot(path, listedItem)) : null;

            }

        }
        return null;
    }

    private static boolean isAllowedChar(char c) {
        return ALLOWED_SYMBOLS.indexOf(c) != -1;
    }


}
