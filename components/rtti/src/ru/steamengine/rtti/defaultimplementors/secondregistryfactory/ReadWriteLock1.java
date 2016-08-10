package ru.steamengine.rtti.defaultimplementors.secondregistryfactory;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Steam engine corp. in 13.02.2011 0:31:21
 *
 * @author Christopher Marlowe
 */
public class ReadWriteLock1 implements MultyReadExclusiveWriteLock {

    private final ReentrantReadWriteLock l = new ReentrantReadWriteLock();

    @Override
    public void lockRead() {
        l.readLock().lock();
    }

    @Override
    public void unlockRead() {
        l.readLock().unlock();
    }

    @Override
    public void lockWrite() {
        l.writeLock().lock();
    }

    @Override
    public void unlockWrite() {
        l.writeLock().unlock();
    }
}
