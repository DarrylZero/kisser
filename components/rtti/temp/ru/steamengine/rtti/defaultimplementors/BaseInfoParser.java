package ru.steamengine.rtti.defaultimplementors;

import ru.steamengine.rtti.basetypes.*;
import static ru.steamengine.rtti.basetypes.AccessorType.*;
import static ru.steamengine.rtti.basetypes.IsStoredAndDefaultGetter.ALWAYS_TRUE;
import static ru.steamengine.rtti.basetypes.IsStoredAndDefaultGetter.NEVER_TRUE;
import static ru.steamengine.rtti.defaultimplementors.utils.CommonStreamingUtils.isValidIdent;
import static ru.steamengine.rtti.defaultimplementors.utils.CommonStreamingUtils.splitToWords;

import java.lang.reflect.*;

/**
 * Created by Steam engine corp in 31.10.2009 23:06:46
 *
 * @author Christopher Marlowe
 */

/**
 * todo java docs
 */
public class BaseInfoParser {

    private static final RTTIUtilities RTTI_UTILS = RTTIUtils.INSTANCE;

    public RTTIEntry parseInfo(Class clazz, String info) {
        if (info == null)
            throw new IllegalArgumentException("info is null");

        if(info.trim().isEmpty())
            throw new IllegalArgumentException("Wrong property info: " + info);

        String temp = !info.trim().isEmpty() ? info : null;
        String[] items = splitToWords(temp);
        if (items.length < 2)
            throw new IllegalArgumentException("Wrong property info: " + info);
        if (items.length > 5)
            throw new IllegalArgumentException("Wrong property info: " + info);


        String propName = checkPropName(items[0]);
        String propGetterName = checkMemberName(items[1]);
        String propSetterName = items.length >= 3 ? checkMemberName(items[2]) : null;
        IsStoredAndDefaultGetter storedFlag = items.length >= 4 ? checkIsStoredIdent(clazz, items[3]) : ALWAYS_TRUE;
        IsStoredAndDefaultGetter defaultFlag = NEVER_TRUE;


        final Class<?> getType;
        final AccessibleObject getter;
        Field getField;
        Method getMethod;
        AccessorType getterAccessorType = getAccessType(propGetterName);
        switch (getterAccessorType) {

            case field:
                String getterFieldName = getMemberName(propGetterName);
                getField = RTTI_UTILS.findAccessibleField(clazz, getterFieldName);
                getter = getField;
                if (getField == null)
                    throw new IllegalArgumentException("field " + getterFieldName + " does not exist");
                getType = getField.getType();
                break;

            case method:
                String getterMethodName = getMemberName(propGetterName);
                getMethod = RTTI_UTILS.findAccessibleMethod(clazz, getterMethodName);
                getter = getMethod;
                if (getMethod == null)
                    throw new IllegalArgumentException("method " + getterMethodName + "()" + " does not exist");

                Class<?>[] params = getMethod.getParameterTypes();
                if (params.length != 0)
                    throw new IllegalArgumentException("method " +
                            getterMethodName + "()" +
                            " has wrong param list");
                getType = getMethod.getReturnType();
                break;

            case wrong:
            default:
                throw new IllegalArgumentException("Unknown accessor type " + getterAccessorType.name());
        }



        AccessibleObject setter;
        if (propSetterName == null) {
            setter = null;
        } else {
            if (getter instanceof Field && isFinal((Field) getter))
                throw new IllegalArgumentException("field " + propGetterName + " can not be final");

            final Class<?> setType;
            Field setField;
            Method setMethod;
            AccessorType setterAccessorType = getAccessType(propSetterName);
            switch (setterAccessorType) {
                case field:
                    String setterFieldName = getMemberName(propSetterName);
                    setField = RTTI_UTILS.findAccessibleField(clazz, setterFieldName);
                    setter = setField;
                    if (setField == null)
                        throw new IllegalArgumentException("field " + setterFieldName + " does not exist");
                    if (isFinal(setField))
                        throw new IllegalArgumentException("field " + setterFieldName + " can not be final");
                    setType = setField.getType();
                    break;

                case method:
                    String setterMethodName = getMemberName(propSetterName);
                    setMethod = RTTI_UTILS.findAccessibleMethod(clazz, setterMethodName);
                    setter = setMethod;
                    if (setMethod == null)
                        throw new IllegalArgumentException("method " + setterMethodName + "()  does not exist");
                    Class<?>[] params = setMethod.getParameterTypes();
                    if (params.length != 1)
                        throw new IllegalArgumentException("method " +
                                setterMethodName + "()" +
                                " has wrong param list");

                    setType = params[0];
                    break;

                case wrong:
                default:
                    throw new IllegalArgumentException("Unknown accessor type " + setterAccessorType.name());
            }

            if (setType != getType)
                throw new IllegalArgumentException("");
        }


        if (getter != null && setter != null)
            return new RWReadWrite(getter, setter, propName, storedFlag, defaultFlag);

        if (getter != null)
            return new RWReadOnly(getter, propName, storedFlag, defaultFlag);

        throw new IllegalArgumentException("");
    }


    /**
     * check passed RTTI for class 
     *
     * @param clazz class
     * @param info  a string defining ONE item.
     * @return true if passed string is allowed.
     * @throws NullPointerException if one of passed params not defined
     */
    public static boolean checkInfo(Class clazz, String info) {

        if (info.trim().isEmpty())
            return false;

        String[] items = splitToWords(info);
        if (items.length < 4)
            return false;

        if (isValidPropName(items[0]))
            return false;
        String propertyName = items[0];
        fake(propertyName);

        if (!isValidMemberName(items[2]))
            return false;
        String propGetterName = checkMemberName(items[2]);

        if (!isValidMemberName(items[3]))
            return false;
        String propSetterName = checkMemberName(items[3]);

        if (!memberExists(clazz, propGetterName) || !memberExists(clazz, propSetterName))
            return false;

        if (memberExists(clazz, propGetterName) &&
                memberExists(clazz, propSetterName) &&
                getGetterType(clazz, propGetterName) == getSetterType(clazz, propSetterName))
            return false;

        //noinspection RedundantIfStatement
        if (memberExists(clazz, propGetterName) &&
                memberExists(clazz, propSetterName) &&
                getGetterType(clazz, propGetterName) == getSetterType(clazz, propSetterName))
            return false;

        return true;
    }

    private static Object fake(Object o) {
        return o;        
    }

    private static boolean memberExists(Class clazz, String memberName) {
        switch (getAccessType(memberName)) {
            case field:
                return RTTI_UTILS.findAccessibleField(clazz, getMemberName(memberName)) != null;

            case method:
                return RTTI_UTILS.findAccessibleMethod(clazz, memberName) != null;

            case wrong:
            default:
                throw new IllegalArgumentException("");
        }
    }

    /**
     * get getter type.
     *
     * @param clazz      class
     * @param getterName getter name
     * @return getter type.
     * @throws IllegalArgumentException if member with name getterName not found
     */
    private static Class<?> getGetterType(Class clazz, String getterName) {
        String memberName = getMemberName(getterName);

        switch (getAccessType(getterName)) {
            case field:
                Field getField = RTTI_UTILS.findAccessibleField(clazz, memberName);
                if (getField == null)
                    throw new IllegalArgumentException("field " + memberName + " does not exist");
                return getField.getType();

            case method:
                Method getMethod = RTTI_UTILS.findAccessibleMethod(clazz, memberName);
                if (getMethod == null)
                    throw new IllegalArgumentException("method " + memberName + "()" + " does not exist");
                return getMethod.getReturnType();

            case wrong:
            default:
                throw new IllegalArgumentException("");
        }
    }

    private static Class<?> getSetterType(Class clazz, String setterName) {

        String memberName = getMemberName(setterName);
        switch (getAccessType(setterName)) {
            case field:
                Field setField = RTTI_UTILS.findAccessibleField(clazz, memberName);
                if (setField == null)
                    throw new IllegalArgumentException("field " + memberName + " does not exist");
                return setField.getType();

            case method:
                Method setMethod;
                String setterMethodName = getMemberName(memberName);
                setMethod = RTTI_UTILS.findAccessibleMethod(clazz, setterMethodName);
                if (setMethod == null)
                    throw new IllegalArgumentException("method " + setterMethodName + "() does not exist");
                Class<?>[] classes = setMethod.getParameterTypes();
                if (classes.length != 1)
                    throw new IllegalArgumentException("");
                return classes[0];

            case wrong:
            default:
                throw new IllegalArgumentException("");
        }
    }


    private static AccessorType getAccessType(String accessor) {
        if (accessor == null)
            throw new NullPointerException("accessor is null");

        if (accessor.trim().isEmpty())
            return wrong;

        if (isValidIdent(accessor))
            return field;

        int lbi = accessor.indexOf('(');
        int rbi = accessor.indexOf(')');
        if (!(lbi == -1 || rbi == -1 || rbi - lbi != 1 || rbi != accessor.length()))
            return wrong;

        String head = accessor.substring(0, lbi);
        if (!isValidIdent(head))
            return wrong;

        return method;
    }

    private static String getMemberName(String accessor) {
        switch (getAccessType(accessor)) {
            case field:
                return accessor;
            case method: {
                int ind = accessor.indexOf('(');
                return accessor.substring(0, ind);
            }
            case wrong:
            default:
                throw new IllegalArgumentException("");
        }
    }

    /**
     * checks property name
     * @param propName property name
     * @return property name.
     */
    public String checkPropName(String propName) {
        if (!isValidPropName(propName))
            throw new IllegalArgumentException("wrong property name " + propName);
        return propName;
    }


    private static boolean isValidPropName(String propName) {
        return isValidIdent(propName);
    }

    private static IsStoredAndDefaultGetter checkIsStoredIdent(Class clazz, String propName) {
        if (propName == null)
            return ALWAYS_TRUE;

        if (propName.equals("true"))
            return ALWAYS_TRUE;

        if (propName.equals("false"))
            return NEVER_TRUE;

        switch (getAccessType(propName)) {
            case field:
                String getterFieldName = getMemberName(propName);
                final Field getField = RTTI_UTILS.findAccessibleField(clazz, getterFieldName);
                checkStoredAccessor(getterFieldName, getField);

                return new IsStoredAndDefaultGetter() {
                    @Override
                    public boolean getValue(Object object) {
                        try {
                            getField.setAccessible(true);
                            return getField.getBoolean(object);
                        } catch (IllegalAccessException e) {
                            return true;
                        }
                    }
                };

            case method:
                String getterMethodName = getMemberName(propName);
                final Method getMethod = RTTI_UTILS.findAccessibleMethod(clazz, getterMethodName);

                checkStoredAccessor(getterMethodName, getMethod);
                return new IsStoredAndDefaultGetter() {
                    @Override
                    public boolean getValue(Object object) {
                        try {
                            getMethod.setAccessible(true);
                            return (Boolean) getMethod.invoke(object);
                        } catch (IllegalAccessException e) {
                            return true;
                        } catch (InvocationTargetException e) {
                            return true;
                        }
                    }
                };


            case wrong:
            default:
                throw new IllegalArgumentException("");
        }
    }

    private static void checkStoredAccessor(String accessorName, AccessibleObject accessor) {
        if (accessorName == null)
            throw new IllegalArgumentException("propertyName == null");
        if (!(accessor instanceof Method || accessor instanceof Field))
            throw new IllegalArgumentException("accessor has wrong type");

        if (accessor instanceof Method) {
            Method method = (Method) accessor;


            Class<?>[] params = method.getParameterTypes();
            if (params.length != 0)
                throw new IllegalArgumentException("stored method " +
                        accessorName + "()" +
                        " has wrong param list");

            Class type = method.getReturnType();
            if (type == ClassConsts.voidType.clazz)
                throw new IllegalArgumentException("type == voidType");

            if (!type.isAssignableFrom(Boolean.TYPE) && !type.isAssignableFrom(Boolean.class))
                throw new IllegalArgumentException("stored method does not return boolean");


        }

        if (accessor instanceof Field) {
            Field field = (Field) accessor;

            Class type = field.getType();
            if (type == ClassConsts.voidType.clazz)
                throw new IllegalArgumentException("type == voidType");

            if (!type.isAssignableFrom(Boolean.TYPE) && !type.isAssignableFrom(Boolean.class))
                throw new IllegalArgumentException("stored based on not boolean field ");
        }


    }

    private static boolean isValidMemberName(String propName) {
        switch (getAccessType(propName)) {
            case field:
            case method:
                return true;

            case wrong:
            default:
                return false;
        }
    }

    private static String checkMemberName(String propName) {
        if (!isValidMemberName(propName))
            throw new IllegalArgumentException("wrong member name :" + propName);
        return propName;
    }
    
    private static int getModifiers(Field field) {
        return field.getModifiers();
    }

    private static boolean isFinal(Field field) {
        return Modifier.isFinal(getModifiers(field));
    }

}
