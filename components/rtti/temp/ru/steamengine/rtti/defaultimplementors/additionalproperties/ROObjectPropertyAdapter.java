package ru.steamengine.rtti.defaultimplementors.additionalproperties;

import ru.steamengine.rtti.basetypes.PropertyValueException;
import ru.steamengine.rtti.basetypes.additionalproperties.ROObjectProperty;

/**
 * Created by Steam engine corp. in 20.02.2011 17:40:20
 *
 * @author Christopher Marlowe
 */
public class ROObjectPropertyAdapter<Result, Obj> extends BaseAdditionalROProp {

    private final ROObjectProperty<Result, Obj> property;
    private final Class<Result> clazz;

    public ROObjectPropertyAdapter(String propertyName,
                                   ROObjectProperty<Result, Obj> property,
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
        throw new UnsupportedOperationException("method is not supported");
    }

    @Override
    public Class getPropType() {
        return clazz;
    }
}
