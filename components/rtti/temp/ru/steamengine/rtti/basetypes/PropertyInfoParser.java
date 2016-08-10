package ru.steamengine.rtti.basetypes;

/**
 * Created by Steam engine corp in 06.12.2009 13:54:26
 * Public API
 * @author Christopher Marlowe
 */

public interface PropertyInfoParser {

    /**
     * Parse info
     *
     * @param clazz class
     * @param info info
     * @return RTTI information
     */
    RTTIEntry parseInfo(Class clazz, String info);

    /**
     * checks the property integrity
     *
     * @param propName property name
     * @return property name.
     */

    String checkPropName(String propName);
}
