package ru.steamengine.rtti.defaultimplementors.additionalproperties;

import ru.steamengine.rtti.basetypes.PropertyValueException;
import ru.steamengine.rtti.basetypes.additionalproperties.CharProperty;

/**
 * Created by Steam engine corp. in 17.12.2009 23:31:46
 *
 * @author Christopher Marlowe
 */
public class CharRTTIAdapter<Obj> extends BaseAdditionalRWProp {

    private final CharProperty<Obj> property;

    public CharRTTIAdapter(String propertyName, CharProperty<Obj> property) {
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
        property.setValue((Obj) obj, (Character) value);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Class getPropType() {
        return Character.TYPE;

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
        return "CharRTTIAdapter{" +
                "property=" + property +
                '}';
    }
}
