package ru.steamengine.rtti.basetypes;

/**
 * Created by Steam engine corp. in 11.12.2009 21:25:12
 * <p/>
 * Public API
 *
 * @author Christopher Marlowe
 */
public interface RegistryUser {

    public interface Transaction {
        void inTransaction();
    }


    /**
     * Creates new instance of the class with ident classIdent
     *
     * @param classIdent class identifier.
     * @return new instance.
     */
    Object getInstance(String classIdent);

    /**                   
     * gets a class by registered name.
     *
     * @param className a registered class name
     * @return a class
     * @exception  IllegalArgumentException if class not found
     */
    Class getInstanceClass(String className);


    /**
     * finds a class by registered name.
     *
     * @param className a registered class name
     * @return a class (null isf not found)
     */
    Class findInstanceClass(String className);

    /**
     * get a registered class name by it's reference
     *
     * @param clazz class
     * @return registered class name
     * @throws NullPointerException     if class is null
     * @throws IllegalArgumentException if class not found
     */
    String getClassIdent(Class clazz);


    /**
     * @param clazz a class
     * @return an associated processor (always not null)
     */
    ObjectProcessor getObjProcessor(Class<?> clazz);

    /**
     * gets associated load notifier
     * @param clazz a class
     * @return associated object of {@link LoadNotifier} (null is never returned )
     */
    <T> LoadNotifier getLoadNotfier(Class<T> clazz);


    /**
     *
     * gets associated read notifier 
     *
     * @param clazz a class
     * @return associated object of {@link ReadNotifier} (null is never returned )
     */
    <T> ReadNotifier getReadNotfier(Class<T> clazz);


    /**
     *
     * gets associated read notifier
     *
     * @param clazz a class
     * @return associated object of {@link ShutDownNotifier} (null is never returned )
     */
    <T> ShutDownNotifier getShutDownNotifier(Class<T> clazz);

    /**
     * Gets a naming strategy for class
     *
     * @param clazz - a class - mustn't be an array or interface
     * @param <T>
     * @return naming strategy
     */
    <T> ObjectNameProcessor getNameProcessor(Class<T> clazz);

    /**
     * get properies for class clazz
     *
     * @param clazz class
     * @return properies for class (null is never returned)
     */
    RTTIEntries getRTTI(Class clazz);

    /**
     * get property recoder for simple type
     *
     * @param clazz class
     * @return property recoder if class is simple and null otherwise
     */
    PropertyRecoder getSimpleType(Class clazz);

    /**
     * @param transaction a transaction interface instance
     */
    void doTransaction(Transaction transaction);
}
