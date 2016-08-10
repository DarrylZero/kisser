package ru.steamengine.rtti.basetypes;

/**
 * property types.
 * property types can be :
 * <p/>
 * 1. "pure "value.
 * <p/>
 * 2. a reference to an object
 * <p/>
 * 3. nested property.
 * <p/>
 * Created by Steam engine corp in 05.12.2009 14:25:00
 *
 * Public API
 * @author Christopher Marlowe
 */
public enum PropertyTypeIdent {
    /**
     * value is a "pure" value
     */
    value,

    /**
     * value is a nested object.
     */
    nested,

    /**
     * value is a reference to an object.
     */
    reference

}
