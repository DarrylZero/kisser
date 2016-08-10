package ru.steamengine.rtti.defaultimplementors.secondregistryfactory;

import ru.steamengine.rtti.basetypes.*;
import ru.steamengine.rtti.defaultimplementors.RTTIEntriesAssistant;
import ru.steamengine.rtti.defaultimplementors.SimplePropRec;
import static ru.steamengine.rtti.defaultimplementors.utils.CommonStreamingUtils.getClassLadder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Steam engine corp in 05.12.2009 22:02:13
 *
 * @author Christopher Marlowe
 */

public class SecondClassAndPropertyRegistry implements Registry, RegistryUser, SimpleEvent {


    private enum RegState {
        empty,
        registered,
        retreived
    }

    private static final Object[] NO_OBJECTS = {};

    /**
     * Map contains class ident as a key and corresponding creator as a value
     */
    private final Map<String, InstanceCreator> identToClass = new HashMap<String, InstanceCreator>();

    private final Map<Class, String> classToIdent = new HashMap<Class, String>();

    /**
     * Collected properties.
     */
    private final Map<Class, RTTIEntries> combinedProps = new HashMap<Class, RTTIEntries>();

    /**
     * csm - class slot map an info object properties.
     */
    private final Map<Class, SecondClassPropRegistrySlot> csm = new HashMap<Class, SecondClassPropRegistrySlot>();

    /**
     * internal "ready" flag
     */
    private RegState regState = RegState.empty;

    /**
     * registry for simple types
     */
    private final Map<Class, PropertyRecoderEx> simpleTypes = new HashMap<Class, PropertyRecoderEx>();


    private static final LoadNotifier NULL_LOAD_NOTIFIER = new LoadNotifier() {
        @Override
        public void objectLoaded(Object object) {
        }
    };

    private final Map<Class<?>, LoadNotifier> loadNotifiers = new HashMap<Class<?>, LoadNotifier>();

    private final Map<Class<?>, ObjectProcessor> objectProcessors = new HashMap<Class<?>, ObjectProcessor>();

    /**
     * lock objects
     */
    private final MultyReadExclusiveWriteLock rwl = new ReadWriteLock2();


    private SecondClassAndPropertyRegistry() {
        super();
        registerCommonSimpleProps();
    }

    public SecondClassAndPropertyRegistry(Object fake) {
        super();
        registerCommonSimpleProps();
    }

    @Override
    public RTTIEntries getRTTI(Class clazz) {
        return doGetRTTI(clazz);
    }


    @Override
    public final ClassPropRegistrySlot classProps(Class clazz) {
        SecondClassPropRegistrySlot slot;

        lockRead();
        try {
            slot = csm.get(clazz);
        } finally {
            unlockRead();
        }

        if (slot == null) {
            lockWrite();
            try {
                slot = new SecondClassPropRegistrySlot(clazz, rwl, this, this);
                csm.put(clazz, slot);
                fire();
            } finally {
                unlockWrite();
            }
        }
        return slot;
    }

    private void lockWrite() {
        rwl.lockWrite();
    }

    private void unlockWrite() {
        rwl.unlockWrite();
    }

    private void lockRead() {
        rwl.lockRead();
    }

    private void unlockRead() {
        rwl.unlockRead();
    }


    @Override
    public final <T> Registry registerSimpleType(Class<T> clazz,
                                                 final PropertyRecoder<T> recoder) {
        if (clazz == null)
            throw new NullPointerException("clazz is null");
        if (recoder == null)
            throw new NullPointerException("recoder is null");

        lockWrite();
        try {
            PropertyRecoderEx<T> recoderEx = new PropertyRecoderEx<T>() {
                @Override
                public byte[] getValue(T value) {
                    //noinspection unchecked
                    return recoder.getValue(value);
                }

                @Override
                public T getObject(byte[] value, Class actualType) {
                    return recoder.getObject(value, actualType);
                }

                @Override
                public boolean isNullable() {
                    return true;
                }
            };

            simpleTypes.put(clazz, recoderEx);
            fire();
        } finally {
            unlockWrite();
        }
        return this;
    }

    @Override
    public final Registry force() {
        doGetRTTI(Object.class);
        return this;
    }

    @Override
    public final Registry nop() {
        return this;
    }

    public final Registry unRegisterSimpleType(Class clazz) {
        lockWrite();
        try {
            simpleTypes.remove(clazz);
            fire();
        } finally {
            unlockWrite();
        }
        return this;
    }


    @Override
    public final PropertyRecoder getSimpleType(Class clazz) {
        lockRead();
        try {
            if (clazz == null)
                throw new NullPointerException("clazz is null");

            if (classToIdent.get(clazz) == null) {
                Class temp = clazz;
                while (temp != null) {
                    PropertyRecoderEx result = simpleTypes.get(temp);
                    if (result != null)
                        return result;
                    temp = temp.getSuperclass();
                }
            }
            return null;

        } finally {
            unlockRead();
        }


        // keep intact !!!
//        synchronized (lock) {
//            if (clazz == null)
//                throw new NullPointerException("clazz is null");
//            Class temp = clazz;
//            boolean superClassUsed = false;
//            while (temp != null) {
//                PropertyRecoderEx result = simpleTypes.get(temp);
//                if (result != null) {
//                    if (superClassUsed)
//                        simpleTypes.put(clazz, result);
//                    return result;
//                }
//
//                superClassUsed = true;
//                temp = temp.getSuperclass();
//            }
//            return null;
//        }
    }

    @Override
    public void doTransaction(Transaction transaction) {
        lockRead();
        try {
            transaction.inTransaction();
        } finally {
            unlockRead();
        }
    }

    @Override
    public void fire() {
        regState = RegState.registered;
    }

    @Override
    public final Registry regObjCreator(InstanceCreator creator) {

        lockWrite();
        try {
            checkCandidate(creator);
            identToClass.put(creator.classIdent(), creator);
            classToIdent.put(creator.instanceClass(), creator.classIdent());
        } finally {
            unlockWrite();
        }

        return this;
    }

    private static final ObjectProcessor NULL_OBJECT_PROCESSOR = new ObjectProcessor() {
        @Override
        public Object[] getObjectChildren(Object obj) {
            return NO_OBJECTS;
        }


        @Override
        public boolean setChild(Object object, Object parent, int position) {
            return false;
        }
    };

    @Override
    public final <Parent> Registry regObjProcessor(Class<Parent> clazz,
                                                   ObjectProcessor<Parent> processor) {
        if (processor == null || clazz == null)
            throw new NullPointerException("processor is null or clazz is null");
        lockWrite();
        try {
            objectProcessors.put(clazz, processor);
        } finally {
            unlockWrite();
        }
        return this;
    }

    @Override
    public final ObjectProcessor getObjProcessor(Class<?> clazz) {
        lockRead();
        try {
            if (clazz == null)
                return NULL_OBJECT_PROCESSOR;

            Class<?> temp = clazz;
            do {
                if (temp == null)
                    break;

                ObjectProcessor result = objectProcessors.get(temp);
                if (result != null)
                    return result;

                temp = temp.getSuperclass();
            } while (true);


            Class<?>[] interfaces = getInterfaces(clazz);
            for (Class<?> interf : interfaces) {
                Class<?> tempIntf = interf;
                do {
                    if (tempIntf == null)
                        break;

                    ObjectProcessor result = objectProcessors.get(tempIntf);
                    if (result != null)
                        return result;

                    tempIntf = tempIntf.getSuperclass();
                } while (true);
            }
            return NULL_OBJECT_PROCESSOR;
        } finally {
            unlockRead();
        }
    }

    private static Class<?>[] getInterfaces(Class<?> clazz) {
        List<Class> list = new java.util.ArrayList<Class>();
        Class<?> temp = clazz;
        while (temp != null) {
            Class<?>[] interfaces = temp.getInterfaces();
            list.addAll(Arrays.asList(interfaces));
            temp = temp.getSuperclass();
        }
        return list.toArray(new Class<?>[list.size()]);
    }


    @Override
    public <T> Registry regLoadNotfier(Class<T> clazz, LoadNotifier<T> notifier) {
        if (notifier == null || clazz == null)
            throw new NullPointerException("notifier or clazz is null ");
        lockWrite();
        try {
            loadNotifiers.put(clazz, notifier);
        } finally {
            unlockWrite();
        }
        return this;
    }

    @Override
    public LoadNotifier getLoadNotfier(Class<?> clazz) {
        lockRead();
        try {
            if (clazz != null) {
                Class<?> temp = clazz;
                do {
                    if (temp == null)
                        break;

                    LoadNotifier result = loadNotifiers.get(temp);
                    if (result != null)
                        return result;
                    temp = temp.getSuperclass();
                } while (true);


                Class<?>[] interfaces = getInterfaces(clazz);
                for (Class<?> interf : interfaces) {
                    do {
                        if (interf == null)
                            break;

                        LoadNotifier result = loadNotifiers.get(interf);
                        if (result != null)
                            return result;
                        interf = interf.getSuperclass();
                    } while (true);
                }
            }

            return NULL_LOAD_NOTIFIER;
        } finally {
            unlockRead();
        }
    }


    public final void unRegisterClassCreator(String classIdent) {
        lockWrite();
        try {
            identToClass.remove(classIdent);
            for (Map.Entry<Class, String> entry : classToIdent.entrySet())
                if (entry.getValue().equals(classIdent)) {
                    classToIdent.remove(entry.getKey());
                    return;
                }
        } finally {
            unlockWrite();
        }
    }

    public final void unRegisterClassCreator(Class clazz) {
        lockWrite();
        try {
            classToIdent.remove(clazz);
            for (Map.Entry<String, InstanceCreator> entry : identToClass.entrySet())
                if (entry.getValue().instanceClass() == clazz) {
                    identToClass.remove(entry.getKey());
                    return;
                }
        } finally {
            unlockWrite();
        }
    }

    public final String getClassIdent(Class clazz) {
        if (clazz == null)
            throw new NullPointerException("clazz is null");
        String result;
        lockRead();
        try {
            result = classToIdent.get(clazz);
            if (result == null)
                throw new IllegalArgumentException("class " + clazz.getName() + " is not registered with creator");
        } finally {
            unlockRead();
        }
        return result;
    }


    public final Object getInstance(String className) {
        Object result = null;
        final InstanceCreator creator;
        lockRead();
        try {
            creator = identToClass.get(className);
        } finally {
            unlockRead();
        }

        if (creator != null)
            result = creator.newInstance();

        if (result == null)
            throw new IllegalArgumentException("result is null");
        return result;
    }

    public final Class getInstanceClass(String className) {
        Class result = null;
        final InstanceCreator creator;
        lockRead();
        try {
            creator = identToClass.get(className);
        } finally {
            unlockRead();
        }
        if (creator != null)
            result = creator.instanceClass();
        return result;
    }

    private boolean isSimpleProperty(Class clazz) {
        return getSimpleType(clazz) != null;
    }


    /**
     * @param clazz class
     * @return property registry. null is never returned
     */
    private RTTIEntries doGetRTTI(Class clazz) {
        lockRead();
        try {
            switch (regState) {
                case empty:
                    return SecondClassPropRegistrySlot.NO_PROPS;

                case registered:
                    unlockRead();
                    try {
                        if (regState == RegState.registered) {
                            rebuildProperties();
                            checkConsistansy();
                        }
                    } finally {
                        lockRead();
                    }
                    return retrieveClassProperty(clazz);

                case retreived:
                    return retrieveClassProperty(clazz);


                default:
                    throw new IllegalStateException("unknown state " + regState);
            }
        } finally {
            unlockRead();
        }
    }


    /**
     * the method is called once after the last property is registered.
     * <p/>
     * (method is not thread safe and there is no locking inside)
     */
    private void rebuildProperties() {
        clearProperties();
        for (Map.Entry<Class, SecondClassPropRegistrySlot> entry : csm.entrySet()) {
            Class clazz = entry.getKey();
            List<Class> classes = getClassLadder(clazz);

            RTTIEntriesAssistant result = new RTTIEntriesAssistant();
            for (Class temp : classes) {
                RTTIEntries curSlot = csm.get(temp);
                if (curSlot != null)
                    result.addEntries(curSlot);
            }
            putProperties(clazz, result);
        }
        regState = RegState.retreived;
    }

    /**
     * (method is not thread safe and there is no locking inside)
     */
    private void checkConsistansy() {
        for (Map.Entry<Class, RTTIEntries> item : combinedProps.entrySet()) {
            RTTIEntry[] entries = item.getValue().getEntries();
            for (RTTIEntry entry : entries) {

                if (entry.getPropertyKind() == PropertyKind.RO &&
                        isSimpleProperty(entry.getPropType()))
                    throw new IllegalArgumentException("Simple property must be (read / write )" + entry);
            }
        }
    }


    private void putProperties(Class clazz, RTTIEntries properties) {
        if (clazz == null)
            throw new NullPointerException("clazz is null");
        if (properties == null)
            throw new NullPointerException("properties is null");
        combinedProps.put(clazz, properties);
    }

    /**
     * (method is not thread safe and there is no locking inside)
     */
    private void clearProperties() {
        combinedProps.clear();
    }

    /**
     * (method is not thread safe and there is no locking inside)
     *
     * @param clazz class for which props are retrieved
     * @return all properties for given class
     */
    private RTTIEntries retrieveClassProperty(Class clazz) {
        if (clazz == null)
            throw new NullPointerException("clazz is null");


        Class temp = clazz;
        while (temp != null) {
            RTTIEntries result = combinedProps.get(temp);
            if (result != null)
                return result;
            temp = temp.getSuperclass();
        }

        Class[] interfaces = getInterfaces(clazz);
        for (Class<?> intf : interfaces) {
            Class<?> tempIntf = intf;
            while (tempIntf != null) {
                RTTIEntries result = combinedProps.get(tempIntf);
                if (result != null)
                    return result;
                tempIntf = tempIntf.getSuperclass();
            }
        }
        return SecondClassPropRegistrySlot.NO_PROPS;
    }


    private void checkCandidate(InstanceCreator creator) {
        lockRead();
        try {
            if (creator == null)
                throw new IllegalArgumentException("Can not register null class");
            if (creator.classIdent() == null)
                throw new IllegalArgumentException("creator.classIdent() is null");
            InstanceCreator testCreator = identToClass.get(creator.classIdent());
            if ((testCreator != null && creator.instanceClass() != testCreator.instanceClass()))
                throw new IllegalArgumentException("Can not register null class");
        } finally {
            unlockRead();
        }
    }

    private void registerCommonSimpleProps() {
        simpleTypes.clear();
        for (ClassConsts aconst : ClassConsts.values())
            simpleTypes.put(aconst.clazz, new SimplePropRec(aconst.clazz));
    }

}