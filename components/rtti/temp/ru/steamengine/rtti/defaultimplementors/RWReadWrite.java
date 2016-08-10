package ru.steamengine.rtti.defaultimplementors;

import ru.steamengine.rtti.basetypes.IsStoredAndDefaultGetter;
import ru.steamengine.rtti.basetypes.PropertyKind;
import ru.steamengine.rtti.basetypes.PropertyValueException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Steam engine corp in 04.12.2009 20:15:37
 *
 * @author Christopher Marlowe
 */
public class RWReadWrite extends BaseRTTIEntry {

    private final AccessibleObject getter;

    private final AccessibleObject setter;

    private final Class<?> propType;

    private final String propertyName;

    private final IsStoredAndDefaultGetter isPropertyStored;

    private final IsStoredAndDefaultGetter isPropertyDefault;

    public RWReadWrite(AccessibleObject getter,
                       AccessibleObject setter,
                       String propertyName,
                       IsStoredAndDefaultGetter isPropertyStored,
                       IsStoredAndDefaultGetter isPropertyDefault) {
        if (!(getter instanceof Field) && !(getter instanceof Method))
            throw new NullPointerException("getter value is not correct");
        if (!(setter instanceof Field) && !(setter instanceof Method))
            throw new NullPointerException("setter value is not correct");
        if (isPropertyStored == null)
            throw new NullPointerException("isPropertyStored is null");
        if (isPropertyDefault == null)
            throw new NullPointerException("isPropertyDefault is null");
        if (propertyName == null)
            throw new NullPointerException("propertyName is null");


        final Class<?> getterType;
        if (getter instanceof Field) {
            getterType = ((Field) getter).getType();
        } else {
            getterType = ((Method) getter).getReturnType();
        }

        final Class<?> setterType;
        if (setter instanceof Field) {
            setterType = ((Field) setter).getType();
        } else {
            Class<?>[] types = ((Method) setter).getParameterTypes();
            if (types.length != 1)
                throw new IllegalArgumentException("getter and setter types must match");
            setterType = types[0];
        }

        if (setterType != getterType)
            throw new IllegalArgumentException("getter and setter types must match");

        this.propType = getterType;
        this.getter = getter;
        this.setter = setter;
        this.propertyName = propertyName;
        this.isPropertyStored = isPropertyStored;
        this.isPropertyDefault = isPropertyDefault;
    }

    @Override
    public Object getVal(Object obj) throws PropertyValueException {
        try {
            if (getter instanceof Field) {
                Field field = (Field) getter;
                field.setAccessible(true);
                return field.get(obj);
            } else if (getter instanceof Method) {
                Method method = (Method) getter;
                method.setAccessible(true);
                return method.invoke(obj);
            } else
                throw new IllegalAccessException();
        } catch (InvocationTargetException e) {
            throw new PropertyValueException(e);
        } catch (IllegalAccessException e) {
            throw new PropertyValueException(e);
        }
    }

    @Override
    public void setVal(Object obj, Object value) throws PropertyValueException {
        try {
            if (setter instanceof Field) {
                Field field = (Field) setter;
                field.setAccessible(true);
                field.set(obj, value);
            } else if (setter instanceof Method) {
                Method method = (Method) setter;
                method.setAccessible(true);
                method.invoke(obj, value);
            } else
                throw new PropertyValueException("Unknown type");
        } catch (InvocationTargetException e) {
            throw new PropertyValueException(e);
        } catch (IllegalAccessException e) {
            throw new PropertyValueException(e);
        }

    }

    @Override
    public String getPropName() {
        return propertyName;
    }

    @Override
    public Class getPropType() {
        return propType;
    }

    @Override
    public boolean isStored(Object obj) {
        return isPropertyStored.getValue(obj);
    }

    @Override
    public boolean isDefault(Object obj) {
        return isPropertyDefault.getValue(obj);
    }

    @Override
    public PropertyKind getPropertyKind() {
        return PropertyKind.RW;
    }


    @Override
    public String toString() {
        return "RWReadWrite{" +
                "getter=" + getter +
                ", setter=" + setter +
                ", propType=" + propType +
                ", propertyName='" + propertyName + '\'' +
                ", isPropertyStored=" + isPropertyStored +
                ", isPropertyDefault=" + isPropertyDefault +
                '}';
    }
}
