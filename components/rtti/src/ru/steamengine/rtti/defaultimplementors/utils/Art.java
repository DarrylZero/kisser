/**
 * Created by Steam engine corp. in 24.10.2011 23:07:23
 *
 * @author Christopher Marlowe
 */


package ru.steamengine.rtti.defaultimplementors.utils;

import ru.steamengine.rtti.basetypes.CommonListedItem;
import ru.steamengine.rtti.basetypes.ListedItem;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Art {

    public static Class<?> getListedItemTypeOld(ListedItem listedItem) {
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


    @Deprecated
    public static Class<?> getListedItemTypeOld(CommonListedItem listedItem) {
        Class<? extends CommonListedItem> aClass = listedItem.getClass();

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

    private static class T {

    }

    private class TT extends T {

    }


    private interface Interface<I extends T> {
        T getT();

    }

    public static void main(String[] args) {
        Interface<TT> i = new Interface<TT>() {
            @Override
            public T getT() {
                return null;               
            }
        };

        Method[] methods = i.getClass().getMethods();
        for (Method method : methods) {
            Object o = method;
            Type type = method.getGenericReturnType();
            if (type instanceof ParameterizedType) {

            }
        }


    }

}
