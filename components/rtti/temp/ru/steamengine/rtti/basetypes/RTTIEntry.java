package ru.steamengine.rtti.basetypes;

/**
 *
 *
 * Created by Steam engine corp in 14.10.2009 0:06:08
 *
 * Public API
 * @author Christopher Marlowe
 */
public interface RTTIEntry {

    /**
     * get a property value.
     *
     * @param obj an object from which property is gotten
     * @return value a value
     * @throws PropertyValueException if not possible to get a property value
     */
    Object getVal(Object obj) throws PropertyValueException;

    /**
     * set a property value
     *
     * @param obj  an object
     * @param value a value
     * @throws PropertyValueException if not possible to set a property value
     */
    void setVal(Object obj, Object value) throws PropertyValueException;


    /**
     * get property name
     *
     * @return property name. null is never returned. (prop1, prop3 and so on but not prop1.prop2)
     */
    String getPropName();

    /**
     * get a property type
     *
     * @return class of the property. null is never returned
     */
    Class getPropType();

    /**
     *
     * @param obj an object to check
     * @return true if passed value is default
     */
    boolean isDefault(Object obj);


    /**
     * a property kind
     *
     * @return get a property kind null is never returned
     */
    PropertyKind getPropertyKind();


    /**
     * get a sign wether a property is stored
     *
     * @param obj an object to check
     * @return true if property is stored
     */
    boolean isStored(Object obj);

}
