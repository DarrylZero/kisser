package ru.steamengine.rtti.basetypes.additionalproperties;

/**
 * Created by Steam engine corp. in 20.02.2011 17:33:43
 *
 * @author Christopher Marlowe
 */
public interface RWObjectProperty<Result, Obj> {

    /**
     * read(get) property value.
     *
     * @param obj object which property is read
     * @return value   typed property value
     */
    Result getValue(Obj obj);


    /**
     * write(set) property value.
     *
     * @param obj   object which property is written
     * @param value typed property value  
     */
    void setValue(Obj obj, Result value);

    /**
     * checks if the property stored
     *
     * @param obj object which property is checked
     * @return true if the property stored false otherwise.
     */
    boolean isStored(Obj obj);


    /**
     * checks if the property has default value for object
     *
     * @param obj object which property is checked.
     * @return true if property value has default value
     */
    boolean isDefault(Obj obj);

}
