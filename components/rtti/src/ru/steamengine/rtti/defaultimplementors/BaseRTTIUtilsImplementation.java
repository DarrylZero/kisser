package ru.steamengine.rtti.defaultimplementors;

import ru.steamengine.rtti.basetypes.Pair;
import ru.steamengine.rtti.defaultimplementors.DefaultPair;
import static ru.steamengine.rtti.defaultimplementors.utils.CommonStreamingUtils.getClassLadder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by Steam engine corp in 02.12.2009 15:06:03
 *
 * @author Christopher Marlowe
 */
public class BaseRTTIUtilsImplementation {

    private final Object lock = new Object();

    private Map<Class, Pair<Map<String, Field>, Map<String, Method>>> classInfos =
            new HashMap<Class, Pair<Map<String, Field>, Map<String, Method>>>();

    public Field findAccessibleField(Class clazz, String fieldName) {
        if (clazz == null)
            throw new NullPointerException("clazz is null");
        if (fieldName == null)
            throw new NullPointerException("fieldName is null");
        synchronized (lock) {
            checkInfo(clazz);
            return classInfos.get(clazz).getObjOne().get(fieldName);
        }
    }

    public Method findAccessibleMethod(Class clazz, String methodName) {
        if (clazz == null)
            throw new NullPointerException("clazz is null");
        if (methodName == null)
            throw new NullPointerException("methodName is null");

        synchronized (lock) {
            checkInfo(clazz);
            return classInfos.get(clazz).getObjTwo().get(methodName);
        }
    }

    public Method[] getAllAccessibleMethods(Class clazz) {
        final Map<String, Method> methods;

        synchronized (lock) {
            checkInfo(clazz);
            methods = classInfos.get(clazz).getObjTwo();
        }
        return methods.values().toArray(new Method[methods.values().size()]);
    }


    public Field[] getAllAccessibleFields(Class clazz) {
        final Map<String, Field> fields;

        synchronized (lock) {
            checkInfo(clazz);
            fields = classInfos.get(clazz).getObjOne();
        }
        return fields.values().toArray(new Field[fields.values().size()]);
    }


    /**
     * @param clazz class
     * @throws NullPointerException if clazz is null
     */
    private void checkInfo(Class clazz) {
        if (clazz == null)
            throw new NullPointerException("clazz is null");

        if (!classInfos.containsKey(clazz)) {
            Pair<Map<String, Field>, Map<String, Method>> pair = scanClass(clazz);
            classInfos.put(clazz, pair);
        }
    }


    private Pair<Map<String, Field>, Map<String, Method>> scanClass(Class clazz) {
        Map<String, Field> fields = getAccessibleFields(clazz);
        Map<String, Method> methods = getAccessibleMethods(clazz);
        return new DefaultPair<Map<String, Field>, Map<String, Method>>(fields, methods);
    }


    private Map<String, Field> getAccessibleFields(Class clazz) {
        if (clazz == null)
            throw new NullPointerException("clazz is null");

        List<Class> classLadder = getClassLadder(clazz);
        Map<String, Field> result = new HashMap<String, Field>();
        for (Class searchType : classLadder) {
            Field[] fields = getAccessibleClassFields(searchType, searchType != clazz);
            for (Field field : fields)
                result.put(field.getName(), field);
        }
        return result;
    }


    private Map<String, Method> getAccessibleMethods(Class clazz) {
        if (clazz == null)
            throw new NullPointerException("clazz is null");

        List<Class> classLadder = getClassLadder(clazz);
        Map<String, Method> result = new HashMap<String, Method>();
        for (Class searchType : classLadder) {
            Method[] methods = getAccessibleClassMethods(searchType, searchType != clazz);
            for (Method method : methods)
                result.put(method.getName(), method);
        }
        return result;
    }


    /**
     * get all SETTABLE class fields (not final and not static)
     *
     * @param clazz      class
     * @param isAncestor a sign that tells how to get fields
     * @return class fields , "own" private fields, inherited protected,
     * @throws NullPointerException if clazz is null.
     */
    public static Field[] getAccessibleClassFields(Class clazz,
                                                    boolean isAncestor) {

        if (clazz == null)
            throw new NullPointerException("clazz is null");

        List<Field> fieldList = new ArrayList<Field>();
        fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
        for (int i = fieldList.size() - 1; i >= 0; i--)
            if (!isFieldAccessible(fieldList.get(i), isAncestor))
                fieldList.remove(i);
        return fieldList.toArray(new Field[fieldList.size()]);
    }

    /**
     * get class setters
     *
     * @param clazz      class
     * @param isAncestor a sign that tells how to get fields
     * @return class methods, "own" private fields, inherited protected,
     * @throws NullPointerException if clazz is null.
     */
    private static Method[] getAccessibleClassMethods(Class clazz,
                                                      boolean isAncestor) {

        if (clazz == null)
            throw new NullPointerException("clazz is null");

        List<Method> methodList = new ArrayList<Method>();
        methodList.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        for (int i = methodList.size() - 1; i >= 0; i--)
            if (!isMethodAccessible(methodList.get(i), isAncestor))
                methodList.remove(i);
        return methodList.toArray(new Method[methodList.size()]);
    }

    private static boolean isFieldAccessible(Field field, boolean isAncestor) {

        if (!isAncestor) {
            //  in exemined class .
            return !isStatic(field);
        } else {
            // in class ancestor.
            return !isStatic(field) && !isPrivate(field);
        }
    }

    private static boolean isAlowedArray(Field field, boolean isAncestor) {

        if (!isAncestor) {
            //  in exemined class .
            return !isStatic(field);
        } else {
            // in class ancestor.
            return !isStatic(field) && !isPrivate(field);
        }
    }

    private static boolean isMethodAccessible(Method method, boolean isAncestor) {

        if (!isAncestor) {
            //  in exemined class .
            return !isStatic(method);
        } else {
            // in class ancestor.
            return !isStatic(method) && !isPrivate(method);
        }
    }


    private static int getModifiers(Field field) {
        return field.getModifiers();
    }

    private static int getModifiers(Method method) {
        return method.getModifiers();
    }

    private static boolean isFinal(Field field) {
        return Modifier.isFinal(getModifiers(field));
    }

    private static boolean isPrivate(Field field) {
        return Modifier.isPrivate(getModifiers(field));
    }

    private static boolean isStatic(Field field) {
        return Modifier.isStatic(getModifiers(field));
    }

    private static boolean isStatic(Method method) {
        return Modifier.isStatic(getModifiers(method));
    }

    private static boolean isPrivate(Method method) {
        return Modifier.isPrivate(getModifiers(method));
    }
   
}
