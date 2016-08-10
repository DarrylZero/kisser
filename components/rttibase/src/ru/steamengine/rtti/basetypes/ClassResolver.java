package ru.steamengine.rtti.basetypes;

/**
 * Created by Steam engine corp. in 27.06.2010 0:13:08
 *
 * @author Christopher Marlowe
 *
 * Public Api
 */
public interface ClassResolver {

    /**
     * load class by its full name
     * @param className class name - mustn't be null
     * @return a class or null if it's not found
     */
    Class<?> loadClass(String className);
}
