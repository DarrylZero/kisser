package ru.steamengine.rtti.defaultimplementors.secondregistryfactory;

import ru.steamengine.rtti.basetypes.Registry;
import ru.steamengine.rtti.basetypes.RegistryFactory;
import ru.steamengine.rtti.basetypes.RegistryUser;

/**
 * Created by Steam engine corp. in 24.09.2010 22:25:49
 *
 * @author Christopher Marlowe
 */
public class SecondRegistryFactory implements RegistryFactory {

    @Override
    public RegistryPair getRegistryPair() {
        final SecondClassAndPropertyRegistry pairItem = new SecondClassAndPropertyRegistry(null);
        return new RegistryPair() {

            @Override
            public Registry getRegistry() {
                return pairItem;
            }

            @Override
            public RegistryUser getRegistryUser() {
                return pairItem;
            }
        };

    }

    @Override
    public RegistryPair newRegistryPair() {
        return getRegistryPair();
    }
}