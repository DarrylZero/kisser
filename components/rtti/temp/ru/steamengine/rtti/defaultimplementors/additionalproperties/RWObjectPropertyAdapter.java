package ru.steamengine.rtti.defaultimplementors.additionalproperties;

import ru.steamengine.rtti.basetypes.PropertyValueException;
import ru.steamengine.rtti.basetypes.additionalproperties.RWObjectProperty;

/**
 * Created by Steam engine corp. in 20.02.2011 17:40:32
 *
 * @author Christopher Marlowe
 */
public class RWObjectPropertyAdapter<Result, Obj> extends BaseAdditionalRWProp {

    private final RWObjectProperty<Result, Obj> property;
    private final Class<Result> clazz;

    public RWObjectPropertyAdapter(String propertyName,
                                   RWObjectProperty<Result, Obj> property,
                                   Class<Result> clazz) {
        super(propertyName);
        checkObject(property);
        if (clazz == null)
            throw new NullPointerException("clazz is null");
        this.property = property;
        this.clazz = clazz;
    }


    @Override
    public Object getVal(Object obj) throws PropertyValueException {
        return property.getValue((Obj) obj);
    }

    @Override
    public void setVal(Object obj, Object value) throws PropertyValueException {
        property.setValue((Obj) obj, (Result) value);
    }

    @Override
    public Class getPropType() {
        return clazz;
    }

    @Override
    public boolean isDefault(Object obj) {
        return property.isDefault((Obj) obj);
    }

    @Override
    public boolean isStored(Object obj) {
        return property.isStored((Obj) obj);
    }
}
