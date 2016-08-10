package ru.steamengine.rtti.defaultimplementors;

import ru.steamengine.rtti.basetypes.PropertyInfoParser;

/**
 * Created by Steam engine corp in 06.12.2009 14:00:04
 *
 * @author Christopher Marlowe
 */
public final class DefaultPropInfoParser extends BaseInfoParser  implements PropertyInfoParser {

    public static final PropertyInfoParser INSTANCE = new DefaultPropInfoParser();

}
