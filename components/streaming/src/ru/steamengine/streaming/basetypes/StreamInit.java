package ru.steamengine.streaming.basetypes;

import ru.steamengine.rtti.basetypes.RegistryUser;

/**
 * Created by Steam engine corp. in 05.07.2010 23:13:07
 *
 * @author Christopher Marlowe
 */
public interface StreamInit {

    <T> T initialize(RegistryUser registryUser);

}
