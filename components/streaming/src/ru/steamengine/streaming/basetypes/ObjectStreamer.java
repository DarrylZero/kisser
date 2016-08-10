package ru.steamengine.streaming.basetypes;

import ru.steamengine.rtti.basetypes.RegistryUser;

/**
 * Created by Steam engine corp. in 03.03.2010 0:05:16
 *
 * @author Christopher Marlowe
 */
public interface ObjectStreamer {

    ObjectReader reader(RegistryUser registryUser);

    ObjectWriter writer(RegistryUser registryUser);

}
