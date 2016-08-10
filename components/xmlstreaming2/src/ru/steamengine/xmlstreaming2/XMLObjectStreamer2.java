package ru.steamengine.xmlstreaming2;

import ru.steamengine.rtti.basetypes.RegistryUser;
import ru.steamengine.streaming.basetypes.ObjectReader;
import ru.steamengine.streaming.basetypes.ObjectStreamer;
import ru.steamengine.streaming.basetypes.ObjectWriter;

/**
 * Created by Steam engine corp. in 05.09.2010 20:57:29
 *
 * @author Christopher Marlowe
 */
public class XMLObjectStreamer2 implements ObjectStreamer {

    @Override
    public ObjectReader reader(RegistryUser registryUser) {
        return new XMLObjectReader2(registryUser);
    }

    @Override
    public ObjectWriter writer(RegistryUser registryUser) {
        return new XMLObjectWriter2(registryUser);
    }
}