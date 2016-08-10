package ru.steamengine.rtti.development;

import ru.steamengine.rtti.basetypes.Getter;
import ru.steamengine.rtti.basetypes.Setter;

/**
 * Created by Steam engine corp in 01.12.2009 20:05:38
 *
 * @author Christopher Marlowe
 */

public class Example extends Ancestor{

    @Setter(properties = {"p1"})
    @Getter(properties = {"p1"})    
    long l;             

    @Getter(properties = {"p2"})
    long getL() {
        return l;
    }

    @Setter(properties = {"p2"})
    void setL(long l) {
        this.l = l;
    }
}
