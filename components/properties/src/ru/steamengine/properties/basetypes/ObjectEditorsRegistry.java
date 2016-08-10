package ru.steamengine.properties.basetypes;

import ru.steamengine.rtti.basetypes.InstanceCreator;

/**
 * Created by Steam engine corp. in 18.04.2010 16:00:51
 *
 * @author Christopher Marlowe
 */

public interface ObjectEditorsRegistry {

    void register(Class aClass, InstanceCreator creator);    

}
