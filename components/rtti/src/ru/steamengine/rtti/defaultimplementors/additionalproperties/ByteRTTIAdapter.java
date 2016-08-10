package ru.steamengine.rtti.defaultimplementors.additionalproperties;

import ru.steamengine.rtti.basetypes.PropertyValueException;
import ru.steamengine.rtti.basetypes.additionalproperties.ByteProperty;

/**
 * Created by Steam engine corp. in 17.12.2009 23:30:08
 *
 * @author Christopher Marlowe
 */
public class ByteRTTIAdapter<Obj> extends BaseAdditionalRWProp {

    private final ByteProperty<Obj> property;

    public ByteRTTIAdapter(String propertyName, ByteProperty<Obj> property) {
        super(propertyName);
        checkObject(property);
        this.property = property;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Object getVal(Object obj) throws PropertyValueException {
        return property.getValue((Obj) obj);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void setVal(Object obj, Object value) throws PropertyValueException {
        property.setValue((Obj) obj, (Byte) value);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Class getPropType() {
        return Byte.TYPE;
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

    @Override
    public String toString() {
        return "ByteRTTIAdapter{" +
                "property=" + property +
                '}';
    }
}
