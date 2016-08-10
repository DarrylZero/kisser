package ru.steamengine.rtti.basetypes;

/**
 * Created by Steam engine corp. in 11.12.2009 21:24:57
 * <p/>
 * Public API
 *
 * @author Christopher Marlowe
 */

public interface Registry {

    /**
     * Registers an object creator
     *
     * @param creator interface
     * @return this registry
     */
    Registry regObjCreator(InstanceCreator creator);


    /**
     * Registers an object processor
     *
     * @param processor processor
     * @param clazz     a class
     * @return this registry
     */
    <Parent> Registry regObjProcessor(Class<Parent> clazz, ObjectProcessor<Parent> processor);


    /**
     *
     * if during a reading process class can not be found this ident is searched in aliases
     *
     * @param classAlias class alias
     * @param classIdent class ident
     * @return this registry
     */
    Registry regClassAlias(String classAlias, String classIdent);


    /**
     * Registers a load-notifier
     *
     * @param notifier a load-notifier
     * @param clazz    class
     * @return this registry
     */
    <T> Registry regLoadNotfier(Class<T> clazz, LoadNotifier<T> notifier);

    /**
     * Registers a read notifier
     *
     * @param notifier a read notifier
     * @param clazz    class
     * @return this registry
     */
    <T> Registry regReadNotfier(Class<T> clazz, ReadNotifier<T> notifier);


    /**
     *
     * Registers a shut down notifier
     *
     * @param notifier a read notifier
     * @param clazz    class
     * @return this registry
     */
    <T> Registry regShutDownNotifier(Class<T> clazz, ShutDownNotifier<T> notifier);

    /**
     * Rergisters a object name strategy
     *
     * @param clazz - class for which a name strategey is registered
     * @param objectNameProcessor - an object name
     * @param <T>
     * @return
     */
    <T> Registry regNameProcessor(Class<T> clazz, ObjectNameProcessor<T> objectNameProcessor);

    /**
     * get class registry
     *
     * @param clazz a class
     * @return registry slot
     */

    ClassPropRegistrySlot classProps(Class clazz);


    /**
     * register a "simple" type
     *
     * @param clazz   a class .
     * @param recoder a recoder.
     * @return this registry
     */
    <T> Registry registerSimpleType(Class<T> clazz, PropertyRecoder<T> recoder);


    /**
     * forcr the process of rebuilding properties
     *
     * @return this registry
     */
    Registry force();


    /**
     * does nothing
     *
     * @return this registry
     */
    Registry nop();

}
