package ru.steamengine.rtti.defaultimplementors.firstregistryfactory;

import ru.steamengine.rtti.basetypes.*;
import ru.steamengine.rtti.defaultimplementors.RTTIEntriesAssistant;
import ru.steamengine.rtti.defaultimplementors.SimplePropRec;
import static ru.steamengine.rtti.defaultimplementors.utils.CommonStreamingUtils.getClassLadder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Steam engine corp in 05.12.2009 22:02:13
 *
 * @author Christopher Marlowe
 */

public class DefaultClassAndPropertyRegistry implements Registry, RegistryUser, SimpleEvent {


    private enum RegState {
        empty,
        registered,
        retreived
    }


    private final Map<String, InstanceCreator> identToClass = new HashMap<String, InstanceCreator>();

    private final Map<Class, String> classToIdent = new HashMap<Class, String>();

    /**
     * Collected properties.
     */
    private final Map<Class, RTTIEntries> combinedProps = new HashMap<Class, RTTIEntries>();

    /**
     * csm - class slot map an info object properties.
     */
    private final Map<Class, DefaultClassPropRegistrySlot> csm = new HashMap<Class, DefaultClassPropRegistrySlot>();

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
     * lock object
     */
    private final Object lock = csm;

    private DefaultClassAndPropertyRegistry() {
        super();
        registerCommonSimpleProps();
    }

    public DefaultClassAndPropertyRegistry(Object fake) {
        super();
        registerCommonSimpleProps();
    }

    @Override
    public RTTIEntries getRTTI(Class clazz) {
        return doGetRTTI(clazz);
    }


    @Override
    public final ClassPropRegistrySlot classProps(Class clazz) {
        synchronized (lock) {
            DefaultClassPropRegistrySlot slot = csm.get(clazz);
            if (slot == null) {
                slot = new DefaultClassPropRegistrySlot(clazz, lock, this, this);
                csm.put(clazz, slot);
                fire();
            }
            return slot;
        }
    }

    @Override
    public final <T> Registry registerSimpleType(Class<T> clazz,
                                                 final PropertyRecoder<T> recoder) {
        if (clazz == null)
            throw new NullPointerException("clazz is null");
        if (recoder == null)
            throw new NullPointerException("recoder is null");
        synchronized (lock) {

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
        synchronized (lock) {
            simpleTypes.remove(clazz);
            fire();
        }
        return this;
    }


    @Override
    public final PropertyRecoder getSimpleType(Class clazz) {
        synchronized (lock) {
            return doGetSimpleType(clazz);
        }
    }

    @Override
    public void doTransaction(Transaction transaction) {
        synchronized (lock) {
            transaction.inTransaction();
        }
    }

    @Override
    public void fire() {
        regState = RegState.registered;
    }

    @Override
    public final Registry regObjCreator(InstanceCreator creator) {
        synchronized (lock) {
            checkCandidate(creator);
            identToClass.put(creator.classIdent(), creator);
            classToIdent.put(creator.instanceClass(), creator.classIdent());
            return this;
        }
    }

    private static final Object[] NO_OBJECTS = {};

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

        synchronized (lock) {
            objectProcessors.put(clazz, processor);
            return this;
        }
    }

    @Override
    public final ObjectProcessor getObjProcessor(Class<?> clazz) {
        synchronized (lock) {
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
        }
    }

    private static Class<?>[] getInterfaces(Class<?> clazz) {
        List<Class> list = new java.util.ArrayList<Class>();
        Class<?> temp = clazz;
        while (temp != null) {
            Class<?>[] interfaces = temp.getInterfaces();
            for (Class<?> interf : interfaces) {
                list.add(interf);
            }
            temp = temp.getSuperclass();
        }
        return list.toArray(new Class<?>[list.size()]);
    }


    @Override
    public <T> Registry regLoadNotfier(Class<T> clazz, LoadNotifier<T> notifier) {
        if (notifier == null || clazz == null)
            throw new NullPointerException("notifier or clazz is null ");

        synchronized (lock) {
            loadNotifiers.put(clazz, notifier);
            return this;
        }
    }

    @Override
    public LoadNotifier getLoadNotfier(Class<?> clazz) {
        synchronized (lock) {

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
        }
    }


    public final void unRegisterClassCreator(String classIdent) {
        synchronized (lock) {
            identToClass.remove(classIdent);
            for (Map.Entry<Class, String> entry : classToIdent.entrySet())
                if (entry.getValue().equals(classIdent)) {
                    classToIdent.remove(entry.getKey());
                    return;
                }
        }
    }

    public final void unRegisterClassCreator(Class clazz) {
        synchronized (lock) {
            classToIdent.remove(clazz);
            for (Map.Entry<String, InstanceCreator> entry : identToClass.entrySet())
                if (entry.getValue().instanceClass() == clazz) {
                    identToClass.remove(entry.getKey());
                    return;
                }
        }
    }

    public final String getClassIdent(Class clazz) {
        if (clazz == null)
            throw new NullPointerException("clazz is null");

        String result;
        synchronized (lock) {
            result = classToIdent.get(clazz);
            if (result == null)
                throw new IllegalArgumentException("class " + clazz.getName() + " is not registered with creator");
        }
        return result;
    }


    public final Object getInstance(String className) {
        Object result = null;
        synchronized (lock) {
            InstanceCreator creator = identToClass.get(className);
            if (creator != null)
                result = creator.newInstance();
        }
        if (result == null)
            throw new IllegalArgumentException("result is null");
        return result;
    }

    public final Class getInstanceClass(String className) {
        Class result = null;
        synchronized (lock) {
            InstanceCreator creator = identToClass.get(className);
            if (creator != null)
                result = creator.instanceClass();
        }
        return result;
    }

    private boolean isSimpleProperty(Class clazz) {
        return getSimpleType(clazz) != null;
    }

    private PropertyRecoderEx doGetSimpleType(Class clazz) {
        synchronized (lock) {
            return retrieveSimpleType(clazz);
        }
    }

    private PropertyRecoderEx retrieveSimpleType(Class clazz) {
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
    }


    /**
     * @param clazz class
     * @return property registry. null is never returned
     */
    private RTTIEntries doGetRTTI(Class clazz) {
        synchronized (lock) {
            switch (regState) {
                case empty:
                    return DefaultClassPropRegistrySlot.NO_PROPS;


                case registered:
                    rebuildProperties();
                    checkConsistansy();
                    return retrieveClassProperty(clazz);

                case retreived:
                    return retrieveClassProperty(clazz);


                default:
                    throw new IllegalStateException("unknown state " + regState);
            }
        }
    }


    /**
     * the method is called once after the last property is registered.
     */
    private void rebuildProperties() {
        synchronized (lock) {
            clearProperties();
            for (Map.Entry<Class, DefaultClassPropRegistrySlot> entry : csm.entrySet()) {
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
    }

    private void checkConsistansy() {
        synchronized (lock) {
            for (Map.Entry<Class, RTTIEntries> item : combinedProps.entrySet()) {
                RTTIEntry[] entries = item.getValue().getEntries();
                for (RTTIEntry entry : entries) {

                    if (entry.getPropertyKind() == PropertyKind.RO &&
                            isSimpleProperty(entry.getPropType()))
                        throw new IllegalArgumentException("Simple property must be (read / write )" + entry);
                }
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

    private void clearProperties() {
        combinedProps.clear();
    }

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
        return DefaultClassPropRegistrySlot.NO_PROPS;
    }


    private void checkCandidate(InstanceCreator creator) {
        synchronized (lock) {
            if (creator == null)
                throw new IllegalArgumentException("Can not register null class");
            if (creator.classIdent() == null)
                throw new IllegalArgumentException("creator.classIdent() is null");
            InstanceCreator testCreator = identToClass.get(creator.classIdent());
            if ((testCreator != null && creator.instanceClass() != testCreator.instanceClass()))
                throw new IllegalArgumentException("Can not register null class");
        }
    }

    private void registerCommonSimpleProps() {
        simpleTypes.clear();
        for (ClassConsts aConst : ClassConsts.values())
            simpleTypes.put(aConst.clazz, new SimplePropRec(aConst.clazz));
    }

}
















