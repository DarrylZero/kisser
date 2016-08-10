package ru.steamengine.streaming.basetypes;

/**
 * Public Api
 */
public interface ObjectResolver {

    String getObjectName(Object o);

    Object findObject(String name);

}
