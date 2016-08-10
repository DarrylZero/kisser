package ru.steamengine.rtti.defaultimplementors.secondregistryfactory;

import org.junit.Test;

/**
 * Created by Steam engine corp. in 13.02.2011 1:09:53
 *
 * @author Christopher Marlowe
 */
public class ReadWriteLock2Test {

    static ReadWriteLock2 lock2 = new ReadWriteLock2();

    public static void main(String[] args) {
        lock2.lockRead();
        try {
            lock2.lockWrite();
            try {


            } finally {
                lock2.unlockWrite();
            }

        } finally {
            lock2.unlockRead();
        }
    }
}
