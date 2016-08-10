package ru.steamengine.rtti.basetypes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A helper interface used for gathering information about accessible class members
 * According to strategy field or method is considered accessible when :
 * <p/>
 * Inside the class all members are accessible except for static members, final fields if they
 * are used for reading/writing.
 * protected, package local, public fields of ancestors (if they are not static of final(and used for reading
 * and writing)).
 * <p/>
 * for the methods.
 * all methods are accessible inside methods (except for static ones),
 * all methods (except for static and private ones) are accessible in child classes
 * <p/>
 * <p/>
 * If methods with same name are defined both in ancestor and child classes - the method of child class has a higher
 * priority
 * <br>
 * <br>
 * <br>
 * Created by Steam engine corp in 02.12.2009 15:10:19
 * <p/>
 * Public API
 *
 * @author Christopher Marlowe
 */
public interface RTTIUtilities {

    /**
     * find accessible field by its name in given class.
     *
     * @param clazz     class.
     * @param fieldName field name.
     * @return accessible class field (with name fieldName).
     * @throws NullPointerException if clazz or fieldName is null.
     */
    Field findAccessibleField(Class clazz, String fieldName);


    /**
     * get accesible method of the class.
     *
     * @param clazz      class
     * @param methodName method name
     * @return accessible method
     * @throws NullPointerException if clazz or method name is null.
     */
    Method findAccessibleMethod(Class clazz, String methodName);


    /**
     * get all acessible methods of passed class.
     *
     * @param clazz class.
     * @return acessible methods of clazz.
     * @throws NullPointerException if clazz is null
     */
    Method[] getAllAccessibleMethods(Class clazz);

    /**
     * get all acessible fields of passed class.
     *
     * @param clazz class
     * @return all acessible fields of clazz
     * @throws NullPointerException if clazz is null
     */
    Field[] getAllAccessibleFields(Class clazz);

}
