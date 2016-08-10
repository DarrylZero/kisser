package ru.steamengine.rtti.defaultimplementors.additionalproperties;

import ru.steamengine.rtti.basetypes.PropertyValueException;
import ru.steamengine.rtti.basetypes.additionalproperties.StringProperty;

/**
 * Created by Steam engine corp. in 17.12.2009 23:31:20
 *
 * @author Christopher Marlowe
 */
public class StringRTTIAdapter<Obj> extends BaseAdditionalRWProp {

    private final StringProperty<Obj> property;

    public StringRTTIAdapter(String propertyName, StringProperty<Obj> property) {
        super(propertyName);
        checkObject(property);
        this.property = property;
    }

    @Override
    public Class getPropType() {
        return String.class;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Object getVal(Object obj) throws PropertyValueException {
        return property.getValue((Obj) obj);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void setVal(Object obj, Object value) throws PropertyValueException {
        property.setValue((Obj) obj, (String) value);
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
        return "StringRTTIAdapter{" +
                "property=" + property +
                '}';
    }
}
