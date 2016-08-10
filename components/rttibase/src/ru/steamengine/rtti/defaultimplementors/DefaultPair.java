package ru.steamengine.rtti.defaultimplementors;

import ru.steamengine.rtti.basetypes.Pair;

/**
 * Created by Steam engine corp in 01.12.2009 20:05:38
 *
 * @author Christopher Marlowe
 */
public final class DefaultPair<ObjOne, ObjTwo> implements Pair<ObjOne, ObjTwo> {

    private final ObjOne objOne;
    private final ObjTwo objTwo;

    public DefaultPair(ObjOne objOne, ObjTwo objTwo) {
        if (objOne == null)
            throw new IllegalArgumentException("objOne is null");
        if (objTwo == null)
            throw new IllegalArgumentException("objTwo is null");

        this.objOne = objOne;
        this.objTwo = objTwo;
    }

    public ObjOne getObjOne() {
        return objOne;
    }

    public ObjTwo getObjTwo() {
        return objTwo;
    }

}
