package ru.steamengine.rtti.basetypes;

import ru.steamengine.rtti.basetypes.additionalproperties.*;

/**
 * Created by Steam engine corp 26.10.2009 23:21:15
 * <p/>
 * Public API
 *
 * @author Christopher Marlowe
 */
public interface ClassPropRegistrySlot {

    /**
     * Registers property info
     *
     * @param propInfo property info
     * @return registration interface
     * @exception IllegalArgumentException if registering is not possible
     */
    ClassPropRegistrySlot regProp(String propInfo);

    /**
     * Registers the info of property of type long
     *
     * @param propertyName property name
     * @param property     property value getter-setter object
     * @return registration interface
     * @exception IllegalArgumentException if registering is not possible
     */
    ClassPropRegistrySlot regProp(String propertyName, LongProperty property);


    /**
     * Registers the info of property of binary type .
     *
     * @param propertyName property name
     * @param property     property value getter-setter object
     * @return registration interface
     * @exception IllegalArgumentException if registering is not possible
     */
    ClassPropRegistrySlot regProp(String propertyName, BinaryProperty property);


    /**
     * Registers the info of property of type int.
     *
     * @param propertyName property name
     * @param property     property value getter-setter object
     * @return registration interface
     * @exception IllegalArgumentException if registering is not possible
     */
    ClassPropRegistrySlot regProp(String propertyName, IntegerProperty property);

    /**
     * Registers the info of property of type byte.
     *
     * @param propertyName property name
     * @param property     property value getter-setter object
     * @return registration interface
     * @exception IllegalArgumentException if registering is not possible
     */
    ClassPropRegistrySlot regProp(String propertyName, ByteProperty property);


    /**
     * Registers the info of property of type Short.
     *
     * @param propertyName property name
     * @param property     property value getter-setter object
     * @return registration interface
     * @exception IllegalArgumentException if registering is not possible
     */
    ClassPropRegistrySlot regProp(String propertyName, ShortProperty property);


    /**
     * Registers the info of property of type String.
     *
     * @param propertyName property name
     * @param property     property value getter-setter object
     * @return registration interface
     * @exception IllegalArgumentException if registering is not possible
     */
    ClassPropRegistrySlot regProp(String propertyName, StringProperty property);

    /**
     * Registers the info of property of type char.
     *
     * @param propertyName property name
     * @param property     property value getter-setter object
     * @return registration interface
     * @exception IllegalArgumentException if registering is not possible
     */
    ClassPropRegistrySlot regProp(String propertyName, CharProperty property);


    /**
     * Registers readonly object property of given type.
     *
     * @param propertyName  property name
     * @param propertyClass property result class
     * @param property      property value getter object
     * @return registration interface
     * @exception IllegalArgumentException if registering is not possible
     */
    <Result, Obj> ClassPropRegistrySlot regProp(String propertyName,
                                                Class<Result> propertyClass,
                                                ROObjectProperty<Result, Obj> property);

    /**
     * Registers readwrite object property of given type.
     *
     * @param propertyName  property name
     * @param propertyClass property result class
     * @param property      property value getter-setter object
     * @return registration interface
     * @exception IllegalArgumentException if registering is not possible
     */
    <Result, Obj> ClassPropRegistrySlot regProp(String propertyName,
                                                Class<Result> propertyClass,
                                                RWObjectProperty<Result, Obj> property);


    /**
     * Registers the info of property of nested list object.
     *
     * @param propertyName property name
     * @param listedItem   property value getter-setter object
     * @return registration interface
     * @exception IllegalArgumentException if registering is not possible
     */
    ClassPropRegistrySlot regProp(String propertyName, ListedItem listedItem);


    /**
     * Registers the info of property of nested list object.
     *
     * @param propertyName property name
     * @param listedItem   property value getter-setter object
     * @return registration interface
     * @exception IllegalArgumentException if registering is not possible
     */
    ClassPropRegistrySlot regProp(String propertyName,
                                  CommonListedItem listedItem);


    /**
     * check if passed param is correct property info
     *
     * @param propInfo property info type string
     * @return true if describing string is allowable
     * @exception NullPointerException if class or info string is not defined
     */

    boolean checkInfo(String propInfo);


    /**
     * does nothing
     *
     * @return registration interface
     */
    ClassPropRegistrySlot nop();

    /**
     * @return registration interface
     */
    Registry registry();


}
