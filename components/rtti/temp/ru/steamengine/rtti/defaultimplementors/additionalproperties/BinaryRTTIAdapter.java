package ru.steamengine.rtti.defaultimplementors.additionalproperties;

import ru.steamengine.rtti.basetypes.PropertyValueException;
import ru.steamengine.rtti.basetypes.additionalproperties.BinaryProperty;

/**
 * Created by Steam engine corp. in 16.12.2009 23:43:12
 *
 * @author Christopher Marlowe
 */
public class BinaryRTTIAdapter<Obj> extends BaseAdditionalRWProp {

    private final BinaryProperty<Obj> property;

    public BinaryRTTIAdapter(String propertyName, BinaryProperty<Obj> property) {
        super(propertyName);
        checkObject(property);
        this.property = property;
    }

    @Override
    public Class getPropType() {
        return byte[].class;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Object getVal(Object obj) throws PropertyValueException {
        return property.getValue((Obj) obj);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void setVal(Object obj, Object value) throws PropertyValueException {
        property.setValue((Obj) obj, (byte[]) value);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean isDefault(Object obj) {
        return property.isDefault((Obj) obj);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean isStored(Object obj) {
        return property.isStored((Obj) obj);
    }
}
