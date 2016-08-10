package ru.steamengine.rtti.defaultimplementors;

import ru.steamengine.rtti.basetypes.Registry;
import ru.steamengine.rtti.basetypes.Registered;


public class RegClass {

    public static final Object lock = RegClass.class;

    private static int count = 0;

    public static void reset() {
        synchronized (lock) {
            count = 0;
        }
    }

    @Registered
    public static void reg1(Registry registry) {
        synchronized (lock) {
            count++;
        }
    }

    @Registered
    protected static void reg2(Registry registry) {
        synchronized (lock) {
            count++;
        }
    }

    @Registered
    private static void reg3(Registry registry) {
        synchronized (lock) {
            count++;
        }
    }

    @Registered
    static void reg4(Registry registry) {
        synchronized (lock) {
            count++;
        }
    }

    static void reg5(Registry registry) {
        synchronized (lock) {
            count++;
        }
    }


    public static void checkCorectcount() {
        synchronized (lock) {
            if (count != 4)
                throw new IllegalArgumentException();
        }
    }
}
