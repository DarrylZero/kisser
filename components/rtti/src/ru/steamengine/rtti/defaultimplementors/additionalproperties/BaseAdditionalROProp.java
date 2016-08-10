package ru.steamengine.rtti.defaultimplementors.additionalproperties;

import ru.steamengine.rtti.basetypes.PropertyKind;
import ru.steamengine.rtti.defaultimplementors.BaseRTTIEntry;

/**
 * Created by Steam engine corp. in 20.02.2011 17:43:01
 *
 * @author Christopher Marlowe
 */
public abstract class BaseAdditionalROProp extends BaseRTTIEntry {


    private final String propertyName;

    protected BaseAdditionalROProp(String propertyName) {
        if (propertyName == null)
            throw new NullPointerException("propertyName is null");
        this.propertyName = propertyName;
    }

    /**
     * Checks passed object
     *
     * @param obj an object.
     */
    protected void checkObject(Object obj) {
        if (obj == null)
            throw new IllegalArgumentException("param is not defined");
    }

    @Override
    public String getPropName() {
        return propertyName;
    }


    @Override
    public final PropertyKind getPropertyKind() {
        return PropertyKind.RO;
    }


    @Override
    public boolean isDefault(Object obj) {
        return false;
    }


    @Override
    public boolean isStored(Object obj) {
        return true;
    }

}
