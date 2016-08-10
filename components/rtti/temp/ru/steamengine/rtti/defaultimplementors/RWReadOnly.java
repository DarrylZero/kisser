package ru.steamengine.rtti.defaultimplementors;

import ru.steamengine.rtti.basetypes.IsStoredAndDefaultGetter;
import ru.steamengine.rtti.basetypes.PropertyKind;
import ru.steamengine.rtti.basetypes.PropertyValueException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Steam engine corp in 04.12.2009 20:58:18
 *
 * @author Christopher Marlowe
 */
class RWReadOnly extends BaseRTTIEntry {

    private final AccessibleObject getter;

    private final Class<?> propType;

    private final String propertyName;

    private final IsStoredAndDefaultGetter isPropertyStored;

    private final IsStoredAndDefaultGetter isPropertyDefault;

    public RWReadOnly(AccessibleObject getter,
                      String propertyName,
                      IsStoredAndDefaultGetter isPropertyStored,
                      IsStoredAndDefaultGetter isPropertyDefault) {
        if (!(getter instanceof Field) && !(getter instanceof Method))
            throw new NullPointerException("getter value is not correct");
        if (propertyName == null)
            throw new NullPointerException("propertyName is null");
        if (isPropertyStored == null)
            throw new NullPointerException("isPropertyStored is null");
        if (isPropertyDefault == null)
            throw new NullPointerException("isPropertyDefault is null");


        final Class<?> getterType;
        if (getter instanceof Field) {
            getterType = ((Field) getter).getType();
        } else {
            Method method = (Method) getter;
            getterType = method.getReturnType();
            Class<?>[] types = method.getParameterTypes();
            if (types.length != 0)
                throw new IllegalArgumentException("getterType is null");
        }
        if (getterType == null)
            throw new IllegalArgumentException("getterType is null");

        this.propertyName = propertyName;
        this.propType = getterType;
        this.getter = getter;
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
            } else {
                Method method = (Method) getter;
                method.setAccessible(true);
                return method.invoke(obj);
            }
        } catch (InvocationTargetException e) {
            throw new PropertyValueException(e);
        } catch (IllegalAccessException e) {
            throw new PropertyValueException(e);
        }

    }

    @Override
    public void setVal(Object obj, Object value) throws PropertyValueException {
        throw new PropertyValueException();
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
        return PropertyKind.RO;
    }

    @Override
    public String toString() {
        return "RWReadOnly{" +
                "getter=" + getter +
                ", propType=" + propType +
                ", propertyName='" + propertyName + '\'' +
                ", isPropertyStored=" + isPropertyStored +
                ", isPropertyDefault=" + isPropertyDefault +
                '}';
    }
}
