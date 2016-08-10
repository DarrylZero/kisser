package ru.steamengine.rtti.defaultimplementors.additionalproperties;

import ru.steamengine.rtti.basetypes.PropertyValueException;
import ru.steamengine.rtti.basetypes.additionalproperties.IntegerProperty;

/**
 * Created by Steam engine corp. in 17.12.2009 23:29:56
 *
 * @author Christopher Marlowe
 */
public class IntegerRTTIAdapter<Obj> extends BaseAdditionalRWProp {

    private final IntegerProperty<Obj> property;

    public IntegerRTTIAdapter(String propertyName, IntegerProperty<Obj> property) {
        super(propertyName);
        checkObject(property);
        this.property = property;
    }

    @Override
    public Class getPropType() {
        return Integer.TYPE;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Object getVal(Object obj) throws PropertyValueException {
        return property.getValue((Obj) obj);
    }

    @SuppressWarnings({"unchecked"})
    @Override                                    
    public void setVal(Object obj, Object value) throws PropertyValueException {
        property.setValue((Obj) obj, (Integer) value);
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
        return "IntegerRTTIAdapter{" +
                "property=" + property +
                '}';
    }
}
