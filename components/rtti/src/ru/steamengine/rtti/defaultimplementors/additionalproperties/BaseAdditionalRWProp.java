package ru.steamengine.rtti.defaultimplementors.additionalproperties;

import ru.steamengine.rtti.basetypes.PropertyKind;
import ru.steamengine.rtti.defaultimplementors.BaseRTTIEntry;

/**
 * Created by Steam engine corp. in 16.12.2009 22:33:03
 *
 * @author Christopher Marlowe
 */
public abstract class BaseAdditionalRWProp extends BaseRTTIEntry {

    private final String propertyName;

    protected BaseAdditionalRWProp(String propertyName) {
        if (propertyName == null)
            throw new IllegalArgumentException("propertyName is null");
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
        return PropertyKind.RW;
    }

}
