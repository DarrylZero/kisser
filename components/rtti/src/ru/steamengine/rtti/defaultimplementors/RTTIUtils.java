package ru.steamengine.rtti.defaultimplementors;

import ru.steamengine.rtti.basetypes.RTTIUtilities;

/**
 * Created by Steam engine corp in 06.12.2009 13:49:38
 *
 * @author Christopher Marlowe
 */
public class RTTIUtils extends BaseRTTIUtilsImplementation implements RTTIUtilities {

    public static final RTTIUtilities INSTANCE = new RTTIUtils();

}
