package ru.steamengine.rtti.basetypes;

/**
 * Created by Steam engine corp. in 03.01.2010 22:32:36
 * <p/>
 * Public API
 *
 * @author Christopher Marlowe
 */
public interface RegistryFactory {

    /**
     * a registry pair interface.
     */
    public interface RegistryPair {

        /**
         * Returns a registry.
         * Each call of a method returns the same registry.
         *
         * @return registry
         */
        Registry getRegistry();


        /**
         * Returns a registry user corresponding to a registry.
         * Each call of a method returns the same registry user.
         *
         * @return registry user
         */
        RegistryUser getRegistryUser();

    }

    /**
     * @return new instance of pair. Deprecated, use newRegistryPair instead
     */
    @Deprecated
    RegistryPair getRegistryPair();

    /**
     * @return new instance of pair.
     */
    RegistryPair newRegistryPair();

}
