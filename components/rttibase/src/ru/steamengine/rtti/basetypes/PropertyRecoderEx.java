package ru.steamengine.rtti.basetypes;

/**
 * The property recoder
 *
 * Created by Steam engine corp in 02.12.2009 21:50:21
 *
 * @author Christopher Marlowe
 */

public interface PropertyRecoderEx<Obj> extends PropertyRecoder<Obj> {

    /**
     * to get value as a set of byte
     * @param value an object which properties are read 
     * @return a set of byte
     */
    byte[] getValue(Obj value);

    /**
     *
     * @param value property value
     * @param actualType property type
     * @return value
     */
    Obj getObject(byte[] value, Class actualType);

    /**
     * defines wether can a property be nullable
     * @return true if so and false otherwise.
     */
    boolean isNullable();

}
