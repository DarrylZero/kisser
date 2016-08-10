package ru.steamengine.rtti.basetypes;


/**
 * Created by Steam engine corp. in 23.06.2010 23:11:29
 * A loader marker interface.
 * <p/>
 * Public API
 *
 * @author Christopher Marlowe
 */
public interface LoaderMark {
    void perform(Registry r);
}
