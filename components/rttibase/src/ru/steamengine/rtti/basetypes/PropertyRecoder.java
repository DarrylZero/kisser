package ru.steamengine.rtti.basetypes;

/**
 * Created by Steam engine corp. in 09.09.2010 22:35:19
 *
 * @author Christopher Marlowe
 */
public interface PropertyRecoder<Obj> {

    /**
     * to get value as a set of byte
     * @param value an object which value is read (not null) 
     * @return a set of byte (not null)
     */
    byte[] getValue(Obj value);

    /**
     *
     * @param value property value
     * @param actualType property type
     * @return value
     */
    Obj getObject(byte[] value, Class<Obj> actualType);



}
