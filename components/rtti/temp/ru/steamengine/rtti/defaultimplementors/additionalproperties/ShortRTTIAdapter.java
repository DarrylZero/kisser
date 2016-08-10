package ru.steamengine.rtti.defaultimplementors.additionalproperties;

import ru.steamengine.rtti.basetypes.PropertyValueException;
import ru.steamengine.rtti.basetypes.additionalproperties.ShortProperty;

/**
 * Created by Steam engine corp. in 17.12.2009 23:30:29
 *
 * @author Christopher Marlowe
 */
public class ShortRTTIAdapter<Obj> extends BaseAdditionalRWProp {

    private final ShortProperty<Obj> property;

    @Override
    public String toString() {
        return "ShortRTTIAdapter{" +
                "property=" + property +
                '}';
    }

    public ShortRTTIAdapter(String propertyName,  ShortProperty<Obj> property) {
        super(propertyName);
        checkObject(property);
        this.property = property;        
    }

    @Override
    public Class getPropType() {
        return Short.TYPE;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Object getVal(Object obj) throws PropertyValueException {
        return property.getValue((Obj) obj);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void setVal(Object obj, Object value) throws PropertyValueException {
        property.setValue((Obj) obj, (Short) value);
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
