package ru.steamengine.rtti.defaultimplementors.firstregistryfactory;

import ru.steamengine.rtti.basetypes.*;
import ru.steamengine.rtti.basetypes.additionalproperties.*;
import ru.steamengine.rtti.defaultimplementors.BaseInfoParser;
import ru.steamengine.rtti.defaultimplementors.DefaultPropInfoParser;
import ru.steamengine.rtti.defaultimplementors.additionalproperties.*;
import static ru.steamengine.rtti.defaultimplementors.utils.CommonStreamingUtils.getListedItemType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steam engine corp in 26.10.2009 23:22:51
 *
 * @author Christopher Marlowe
 */

// describe
public class DefaultClassPropRegistrySlot implements ClassPropRegistrySlot, RTTIEntries {

    private final Class clazz;

    private final Object lockObject;

    private final SimpleEvent event;

    private final Registry registry;

    private final List<RTTIEntry> itemsList = new ArrayList<RTTIEntry>();

    private final List<ListedItemSlot> listedItems = new ArrayList<ListedItemSlot>();

    public DefaultClassPropRegistrySlot(Class clazz,
                                        Object lockObject,
                                        SimpleEvent event,
                                        Registry registry) {
        if (clazz == null)
            throw new IllegalArgumentException("parametr 'clazz' is not defined");
        if (lockObject == null)
            throw new IllegalArgumentException("parametr 'lockObject' is not defined");
        if (registry == null)
            throw new IllegalArgumentException("parametr 'registry' is not defined");

        this.clazz = clazz;
        this.lockObject = lockObject;
        this.event = event;
        this.registry = registry;
    }


    public ClassPropRegistrySlot regProp(String propInfo) {
        synchronized (lockObject) {
            BufferedReader reader = new BufferedReader(new StringReader(propInfo));
            String line;
            try {
                while ((line = reader.readLine()) != null)
                    if (!line.trim().isEmpty())
                        doRegInfo(line);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }

        }
        return this;
    }


    @SuppressWarnings({"unchecked"})
    public ClassPropRegistrySlot regProp(String propertyName, LongProperty property) {
        synchronized (lockObject) {
            DefaultPropInfoParser.INSTANCE.checkPropName(propertyName);
            LongRTTIAdapter info = new LongRTTIAdapter(propertyName, property);
            removePropItem(info);
            removeListedItem(propertyName);
            addItem(info);
            fireChangeEvent();
        }
        return this;
    }

    @SuppressWarnings({"unchecked"})
    public ClassPropRegistrySlot regProp(String propertyName, BinaryProperty property) {
        synchronized (lockObject) {
            DefaultPropInfoParser.INSTANCE.checkPropName(propertyName);
            BinaryRTTIAdapter info = new BinaryRTTIAdapter(propertyName, property);
            removePropItem(info);
            removeListedItem(propertyName);
            addItem(info);
            fireChangeEvent();
        }
        return this;
    }


    @SuppressWarnings({"unchecked"})
    public ClassPropRegistrySlot regProp(String propertyName, IntegerProperty property) {
        synchronized (lockObject) {
            DefaultPropInfoParser.INSTANCE.checkPropName(propertyName);
            IntegerRTTIAdapter info = new IntegerRTTIAdapter(propertyName, property);
            removePropItem(info);
            removeListedItem(propertyName);
            addItem(info);
            fireChangeEvent();
        }
        return this;
    }

    @SuppressWarnings({"unchecked"})
    public ClassPropRegistrySlot regProp(String propertyName, ByteProperty property) {
        synchronized (lockObject) {
            DefaultPropInfoParser.INSTANCE.checkPropName(propertyName);
            ByteRTTIAdapter info = new ByteRTTIAdapter(propertyName, property);
            removePropItem(info);
            removeListedItem(propertyName);
            addItem(info);
            fireChangeEvent();
        }
        return this;
    }

    @SuppressWarnings({"unchecked"})
    public ClassPropRegistrySlot regProp(String propertyName, ShortProperty property) {
        synchronized (lockObject) {
            DefaultPropInfoParser.INSTANCE.checkPropName(propertyName);
            ShortRTTIAdapter info = new ShortRTTIAdapter(propertyName, property);
            removePropItem(info);
            removeListedItem(propertyName);
            addItem(info);
            fireChangeEvent();
        }
        return this;
    }

    @SuppressWarnings({"unchecked"})
    public ClassPropRegistrySlot regProp(String propertyName, StringProperty property) {
        synchronized (lockObject) {
            DefaultPropInfoParser.INSTANCE.checkPropName(propertyName);
            StringRTTIAdapter info = new StringRTTIAdapter(propertyName, property);
            removePropItem(info);
            removeListedItem(propertyName);
            addItem(info);
            fireChangeEvent();
        }
        return this;
    }

    @SuppressWarnings({"unchecked"})
    public ClassPropRegistrySlot regProp(String propertyName, CharProperty property) {
        synchronized (lockObject) {
            DefaultPropInfoParser.INSTANCE.checkPropName(propertyName);
            CharRTTIAdapter info = new CharRTTIAdapter(propertyName, property);
            removePropItem(propertyName);
            removeListedItem(propertyName);
            addItem(info);
            fireChangeEvent();
        }
        return this;
    }


    @Override
    public <Result, Obj> ClassPropRegistrySlot regProp(String propertyName,
                                                       Class<Result> propertyClass,
                                                       ROObjectProperty<Result, Obj> property)
            throws IllegalArgumentException {

        synchronized (lockObject) {
            DefaultPropInfoParser.INSTANCE.checkPropName(propertyName);
            ROObjectPropertyAdapter<Result, Obj> info =
                    new ROObjectPropertyAdapter<Result, Obj>(propertyName, property, propertyClass);
            removePropItem(propertyName);
            removeListedItem(propertyName);
            addItem(info);
            fireChangeEvent();
        }
        return this;
    }


    @Override
    public <Result, Obj> ClassPropRegistrySlot regProp(String propertyName,
                                                       Class<Result> propertyClass,
                                                       RWObjectProperty<Result, Obj> property)
            throws IllegalArgumentException {
        synchronized (lockObject) {
            DefaultPropInfoParser.INSTANCE.checkPropName(propertyName);
            RWObjectPropertyAdapter<Result, Obj> info =
                    new RWObjectPropertyAdapter<Result, Obj>(propertyName, property, propertyClass);
            removePropItem(propertyName);
            removeListedItem(propertyName);
            addItem(info);
            fireChangeEvent();
        }
        return this;
    }

    @Override
    public ClassPropRegistrySlot regProp(String propertyName, ListedItem listedItem) {
        synchronized (lockObject) {
            Class<?> itemClass = getListedItemType(listedItem);
//            Class<?> itemClass = listedItem.getItemClass();
//            if (itemClass == null)
//                throw new NullPointerException("listedItem.getItemClass() == null");
            DefaultPropInfoParser.INSTANCE.checkPropName(propertyName);
            removePropItem(propertyName);
            removeListedItem(propertyName);
            listedItems.add(new ListedItemSlot(propertyName, listedItem));
        }
        return this;
    }


    @Override
    public ListedItem findListedItem(String propertyName) {
        synchronized (lockObject) {
            for (ListedItemSlot slot : listedItems)
                if (slot.propertyName.equals(propertyName))
                    return slot.item;
        }
        return null;
    }

    @Override
    public ListedItemSlot[] getListedItems() {
        return listedItems.toArray(new ListedItemSlot[listedItems.size()]);
    }


    public boolean checkInfo(String propInfo) {
        return BaseInfoParser.checkInfo(clazz, propInfo);
    }

    @Override
    public RTTIEntry findEntry(String propertyName) {
        synchronized (lockObject) {
            for (RTTIEntry entry : itemsList)
                if (entry.getPropName().equals(propertyName))
                    return entry;
            return null;
        }
    }

    @Override
    public ClassPropRegistrySlot nop() {
        return this;
    }

    @Override
    public RTTIEntry[] getEntries() {
        return itemsList.toArray(new RTTIEntry[itemsList.size()]);
    }

    public final void contributeItems(final RTTIEntry[] items) {
        if (items == null)
            throw new IllegalArgumentException("items is null");

        synchronized (lockObject) {
            for (RTTIEntry item : items)
                removePropItem(item);

            for (RTTIEntry item : items)
                addItem(item);
        }
    }

    public final Registry registry() {
        return registry;
    }

    private void removePropItem(RTTIEntry item) {
        if (item == null)
            throw new IllegalArgumentException("item is null");
        removePropItem(item.getPropName());
    }

    private void removePropItem(String itemName) {
        if (itemName == null)
            throw new IllegalArgumentException("itemName is null");

        for (int i = itemsList.size() - 1; i >= 0; i--)
            if (itemsList.get(i).getPropName().equals(itemName))
                itemsList.remove(i);

    }

    private void removeListedItem(String propertyName) {
        for (int i = listedItems.size() - 1; i >= 0; i--)
            if (listedItems.get(i).propertyName.equals(propertyName))
                listedItems.remove(i);
    }

    private void addItem(RTTIEntry item) {
        itemsList.add(item);
    }

    private void fireChangeEvent() {
        if (event != null)
            event.fire();
    }


    private void doRegInfo(String propInfo) {
        RTTIEntry info = DefaultPropInfoParser.INSTANCE.parseInfo(clazz, propInfo);
        removePropItem(info.getPropName());
        removeListedItem(info.getPropName());
        addItem(info);
        fireChangeEvent();
    }


}
