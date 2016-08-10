package ru.steamengine.rtti.defaultimplementors.secondregistryfactory;

/**
 * Created by Steam engine corp. in 12.02.2011 23:14:23
 *
 * @author Christopher Marlowe
 */
public interface MultyReadExclusiveWriteLock {

    /**
     * lock reading operation
     */
    void lockRead();

    /**
     * unlock reading operation
     */
    void unlockRead();

    /**
     * lock writing operation
     */
    void lockWrite();

    /**
     * unlock writing operation
     */
    void unlockWrite();


}
