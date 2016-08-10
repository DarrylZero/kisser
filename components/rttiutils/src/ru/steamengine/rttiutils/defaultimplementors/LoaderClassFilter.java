package ru.steamengine.rttiutils.defaultimplementors;

import ru.steamengine.rtti.basetypes.ClassFilter;
import ru.steamengine.rtti.basetypes.LoaderMark;
import ru.steamengine.rtti.basetypes.Registry;
import ru.steamengine.rtti.defaultimplementors.DefaultRttiHelper;

/**
 * Created by Steam engine corp. in 05.08.2011 23:48:52
 * <p/>
 * Public Api
 *
 * @author Christopher Marlowe
 */
public class LoaderClassFilter implements ClassFilter<Registry> {
    @Override
    public boolean canUse(Class clazz) {
        return (LoaderMark.class.isAssignableFrom(clazz) &&
                DefaultRttiHelper.hasNoParamConstructor(clazz)) ||
                (DefaultRttiHelper.hasRegistrationMethods(clazz));
    }

    @Override
    public void doClass(Class clazz, Registry param) {
        if (LoaderMark.class.isAssignableFrom(clazz)) {
            LoaderMark loaderMark = DefaultRttiHelper.getClassCreator(clazz).newTypedInstance();
            loaderMark.perform(param);
        }
        DefaultRttiHelper.callRegistrationMethods(clazz, param);
    }
}
