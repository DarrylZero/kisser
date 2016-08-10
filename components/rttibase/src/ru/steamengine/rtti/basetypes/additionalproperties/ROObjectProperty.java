package ru.steamengine.rtti.basetypes.additionalproperties;

/**
 * Created by Steam engine corp. in 20.02.2011 17:32:14
 *
 * @author Christopher Marlowe
 */
public interface ROObjectProperty<Result, Obj> {

    /**
     * read(get) property value.
     *
     * @param obj object which property is read
     * @return value value of property
     */
    Result getValue(Obj obj);
}
