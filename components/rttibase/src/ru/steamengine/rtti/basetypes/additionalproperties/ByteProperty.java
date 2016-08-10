package ru.steamengine.rtti.basetypes.additionalproperties;

/**
 * Interface represents property of byte type.
 * <p/>
 * Created by Steam engine corp. in 16.12.2009 22:20:32
 * <p/>
 * Public API
 *
 * @author Christopher Marlowe
 */

public interface ByteProperty<Obj> {

    /**
     * read(get) property value.
     *
     * @param obj object which property is read
     * @return value value of property
     */
    byte getValue(Obj obj);

    /**
     * write(set) property value.
     *
     * @param obj   object which property is written
     * @param value value of property.
     */
    void setValue(Obj obj, byte value);


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
