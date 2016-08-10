package ru.steamengine.xmlstreaming3;

import ru.steamengine.rtti.basetypes.RegistryUser;
import ru.steamengine.streaming.basetypes.ObjectReader;
import ru.steamengine.streaming.basetypes.ObjectStreamer;
import ru.steamengine.streaming.basetypes.ObjectWriter;

/**
 * Created by Steam engine corp. in 05.09.2010 20:57:40
 *
 * @author Christopher Marlowe
 */
public class XMLObjectStreamer3 implements ObjectStreamer {

    @Override
    public ObjectReader reader(RegistryUser registryUser) {
        return new XMLObjectReader3(registryUser);
    }

    @Override
    public ObjectWriter writer(RegistryUser registryUser) {
        return new XMLObjectWriter3(registryUser);
    }
}
