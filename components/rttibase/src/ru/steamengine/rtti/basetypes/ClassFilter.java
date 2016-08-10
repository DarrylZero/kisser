package ru.steamengine.rtti.basetypes;

/**
 * Created by Steam engine corp. in 26.06.2010 23:58:16
 *
 * @author Christopher Marlowe
 *
 * Public API
 */
public interface ClassFilter<T> {
                                   
    boolean canUse(Class clazz);

    void doClass(Class clazz, T param);

}
