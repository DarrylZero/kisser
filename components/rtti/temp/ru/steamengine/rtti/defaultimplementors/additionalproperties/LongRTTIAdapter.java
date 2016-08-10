package ru.steamengine.rtti.defaultimplementors.additionalproperties;

import ru.steamengine.rtti.basetypes.PropertyValueException;
import ru.steamengine.rtti.basetypes.additionalproperties.LongProperty;

/**
 * Created by Steam engine corp. in 16.12.2009 22:29:50
 *
 * @author Christopher Marlowe
 */
public class LongRTTIAdapter<Obj> extends BaseAdditionalRWProp {

    private final LongProperty<Obj> longProperty;

    public LongRTTIAdapter(String propertyName, LongProperty<Obj> longProperty) {
        super(propertyName);
        this.longProperty = longProperty;        
    }

    @Override
    public Class getPropType() {
        return Long.TYPE;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Object getVal(Object obj) throws PropertyValueException {
        return longProperty.getValue((Obj) obj);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void setVal(Object obj, Object value) throws PropertyValueException {
        longProperty.setValue((Obj) obj, (Long) value);
    }


    @SuppressWarnings({"unchecked"})
    @Override
    public boolean isDefault(Object obj) {
        return longProperty.isDefault((Obj) obj);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean isStored(Object obj) {
        return longProperty.isStored((Obj) obj);
    }

    @Override
    public String toString() {
        return "LongRTTIAdapter{" +
                "longProperty=" + longProperty +
                '}';
    }
}
