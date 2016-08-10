package ru.steamengine.rttiutils.defaultimplementors;

import ru.steamengine.rtti.basetypes.ClassFilter;

/**
 * Created by Steam engine corp. in 27.06.2010 1:26:51
 *
 * @author Christopher Marlowe
 */
public class DefaultClassFilter implements ClassFilter {

    @Override
    public boolean canUse(Class clazz) {
        return false;
    }

    @Override
    public void doClass(Class clazz, Object param) {
        throw new UnsupportedOperationException("void doClass(Class clazz) is not supported");
    }

}
