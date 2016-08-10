package ru.steamengine.xmlstreaming;

import ru.steamengine.rtti.basetypes.RegistryUser;
import ru.steamengine.streaming.basetypes.ObjectReader;
import ru.steamengine.streaming.basetypes.ObjectStreamer;
import ru.steamengine.streaming.basetypes.ObjectWriter;

/**
 * Created by Steam engine corp. in 05.09.2010 20:56:15
 *
 * [kisser]xmlstreamer
 * @author Christopher Marlowe
 */
public class XMLObjectStreamer  implements ObjectStreamer {

        @Override
        public ObjectReader reader(RegistryUser registryUser) {
            return new XMLObjectReader(registryUser);
        }

        @Override
        public ObjectWriter writer(RegistryUser registryUser) {
            return new XMLObjectWriter(registryUser);
        }
    }