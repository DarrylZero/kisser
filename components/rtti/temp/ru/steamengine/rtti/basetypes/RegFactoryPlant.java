package ru.steamengine.rtti.basetypes;

import ru.steamengine.rtti.defaultimplementors.DefaultPair;
import ru.steamengine.rtti.defaultimplementors.secondregistryfactory.SecondRegistryFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Steam engine corp. in 24.09.2010 22:24:11
 *
 * @author Christopher Marlowe
 *         <p/>
 *         Public API
 */
public class RegFactoryPlant {
                             
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

    private static String curFactoryName = null;

    private static final Map<String, RegistryFactory> storage = new HashMap<String, RegistryFactory>();

    public static RegistryFactory getFactory(String name) {
        if (name == null)
            throw new NullPointerException("passed name is null");

        lock.readLock().lock();
        try {
            if (!storage.containsKey(name))
                throw new IllegalArgumentException("Factory with name (" + name + ") does not exist");
            return storage.get(name);
        } finally {
            lock.readLock().unlock();
        }
    }

    public static RegistryFactory getFactory() {
        lock.readLock().lock();
        try {
            if (curFactoryName == null)
                throw new IllegalArgumentException("there is no rtti-factory");
            return getFactory(curFactoryName);
        } finally {
            lock.readLock().unlock();
        }
    }

    public static void addFactory(RegistryFactory factory, String name) {
        lock.writeLock().lock();
        try {
            if (factory == null || name == null)
                throw new NullPointerException("factory is null or name is null");
            if (storage.containsKey(name))
                throw new IllegalArgumentException("Factory named (" + name + ") is already registered");
            storage.put(name, factory);
            if (curFactoryName == null)
                curFactoryName = name;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static Pair<String, RegistryFactory>[] getFactories() {
        lock.readLock().lock();
        try {
            @SuppressWarnings({"unchecked"})
            Pair<String, RegistryFactory>[] result = new Pair[storage.size()];
            int i = 0;
            for (Map.Entry<String, RegistryFactory> entry : storage.entrySet())
                result[i++] = new DefaultPair<String, RegistryFactory>(entry.getKey(), entry.getValue());
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    public static void setDefault(RegistryFactory factory) {
        lock.writeLock().lock();
        try {
            for (Map.Entry<String, RegistryFactory> entry : storage.entrySet())
                if (entry.getValue() == factory) {
                    curFactoryName = entry.getKey();
                    return;
                }
            throw new IllegalArgumentException("the factory is not registered.");
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void setDefault(String factoryName) {
        lock.writeLock().lock();
        try {
            for (Map.Entry<String, RegistryFactory> entry : storage.entrySet())
                if (entry.getKey().equals(factoryName)) {
                    curFactoryName = entry.getKey();
                    return;
                }
            throw new IllegalArgumentException("the factory named " + factoryName + " is not registered.");
        } finally {
            lock.writeLock().unlock();
        }
    }

    static {
        addFactory(new SecondRegistryFactory(), "version2");
        setDefault("version2");
    }

}
