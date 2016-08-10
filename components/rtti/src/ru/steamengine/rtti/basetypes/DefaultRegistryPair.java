package ru.steamengine.rtti.basetypes;

/**
 * Created by Steam engine corp. in 04.04.2010 0:46:42
 *
 * Public API
 * @author Christopher Marlowe
 */
public class DefaultRegistryPair implements RegistryFactory.RegistryPair {

    private final Registry registry;

    private final RegistryUser registryUser;

    public DefaultRegistryPair(Registry registry,
                               RegistryUser registryUser) {
        if (registry == null)
            throw new NullPointerException("registry is null");

        if (registryUser == null)
            throw new NullPointerException("registryUser is null");

        this.registry = registry;
        this.registryUser = registryUser;
    }


    @Override
    public Registry getRegistry() {
        return registry;
    }

    @Override
    public RegistryUser getRegistryUser() {
        return registryUser;
    }
}
