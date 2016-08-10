package ru.steamengine.rtti.defaultimplementors;

import ru.steamengine.rtti.basetypes.LoaderMark;
import ru.steamengine.rtti.basetypes.LoaderMarkIdent;
import ru.steamengine.rtti.basetypes.Registry;

/**
 * Created by Steam engine corp. in 26.06.2010 20:49:39
 *
 * @author Christopher Marlowe
 */
public class DefaultLoaderMark implements LoaderMark, LoaderMarkIdent {

    @Override
    public void perform(Registry registry) {
    }

    @Override
    public String getIdent() {
        return "";
    }
}
