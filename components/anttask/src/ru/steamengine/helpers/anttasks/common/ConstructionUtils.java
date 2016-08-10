package ru.steamengine.helpers.anttasks.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Steam engine corp. in 12.01.2011 23:28:10
 *
 * @author Christopher Marlowe
 */
public class ConstructionUtils {

    public static <T> T newInstance(Constructor constructor) {
        try {
            return (T) constructor.newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        }
    }

    public static Constructor getDefaultConstructor(Class clazz) {
        final Constructor[] constructors = clazz.getConstructors();
        Constructor ctr = null;
        for (Constructor c : constructors) {
            final Class[] params = c.getParameterTypes();
            if (params.length == 0) {
                ctr = c;
                break;
            }
        }
        return ctr;
    }

}
