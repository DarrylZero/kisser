package ru.steamengine.rtti.defaultimplementors;

import ru.steamengine.rtti.basetypes.Registry;

/**
 * Created by Steam engine corp in 25.10.2009 22:21:54
 *
 * @author Christopher Marlowe
 */
public abstract class DefaultRegistrator {

    protected final Registry registry;

    abstract public void register();

    public DefaultRegistrator(Registry registry) {
        this.registry = registry;
    }
}
