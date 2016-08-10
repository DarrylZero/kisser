package ru.steamengine.streaming.basetypes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Utility class for storing a set of serializers
 * Created by Steam engine corp. in 02.03.2010 23:57:41
 *
 * @author Christopher Marlowe
 */
public class Streamers {

    private static final Streamers STREAMERS = new Streamers();
    private final List<ObjectStreamer> list = new ArrayList<ObjectStreamer>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

    public void add(ObjectStreamer serializer) {
        if (serializer == null)
            throw new NullPointerException("serializer is null");

        lock.writeLock().lock();
        try {
            list.add(serializer);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void clear(ObjectStreamer serializer) {
        if (serializer == null)
            throw new NullPointerException("serializer is null");

        lock.writeLock().lock();
        try {
            list.add(serializer);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public ObjectStreamer[] getStreamers() {
        lock.readLock().lock();
        try {
            return list.toArray(new ObjectStreamer[list.size()]);
        } finally {
            lock.readLock().unlock();
        }
    }

    public static Streamers getInstance() {
        return STREAMERS;
    }

}
