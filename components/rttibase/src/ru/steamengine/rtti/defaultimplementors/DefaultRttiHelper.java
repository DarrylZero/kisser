package ru.steamengine.rtti.defaultimplementors;

import ru.steamengine.rtti.basetypes.*;
import ru.steamengine.rtti.development.Example;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;


/**
 * Assistance class used for forming instance creator with behavoir like this:
 * <p/>
 * <p/>
 * 1. instance is created by no - arg constructor  if there are not such constructor exception is thrown
 * <p/>
 * 2. class ident can be clazz.getSimpleName() or taken from second param
 * <p/>
 * 3. Instance class is a ref to passed class.
 * Created by Steam engine corp. in 11.12.2009 23:55:18
 * <p/>
 * Public API
 *
 * @author Christopher Marlowe
 */

public class DefaultRttiHelper {
    private static final String UNKNOWN_ACCESSIBLE_OBJECT = "unknown accessible object";

    private static class TypeConsts {

        public static final Class<?> voidType = Void.TYPE;

        public static final Class<?> booleanPrim = Boolean.TYPE;

    }

    private static class InstPlant implements InstanceCreator {

        private final Class clazz;

        private final Constructor constructor;

        private final String ident;

        @Override
        public String classIdent() {
            return ident;
        }

        @Override
        public Class instanceClass() {
            return clazz;
        }

        @Override
        public Object newInstance()
                throws IllegalArgumentException {
            try {
                return constructor.newInstance();
            } catch (InstantiationException e) {
                throw new IllegalArgumentException(e);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            } catch (InvocationTargetException e) {
                throw new IllegalArgumentException(e);
            }
        }

        @Override
        public <T> T newTypedInstance() throws IllegalArgumentException {
            //noinspection unchecked
            return (T) newInstance();
        }

        private InstPlant(Class clazz, String ident) {
            if (clazz == null)
                throw new NullPointerException("clazz is null");
            this.clazz = clazz;

            Constructor ctr = getDefaultConstructor(clazz);
            if (ctr == null)
                throw new IllegalArgumentException("class " +
                        clazz.getName() +
                        " has no no-param constructor");

            constructor = ctr;
            this.ident = ident != null ? ident : clazz.getSimpleName();
        }

        private InstPlant(Class clazz) {
            this(clazz, null);
        }

    }


    private static Constructor getDefaultConstructor(Class clazz) {
        final Constructor[] constructors = clazz.getConstructors();
        Constructor ctr = null;
        for (Constructor c : constructors) {
            final Class[] params = c.getParameterTypes();
            if (params.length == 0) {
                ctr = c;
                break;
            }
        }
        return ctr;
    }


    /**
     * Get a new class instance creator
     *
     * @param clazz class for which creator is got
     * @param ident class string ident
     * @return new object instance of the class clazz
     */
    public static InstanceCreator getClassCreator(Class clazz, String ident) {
        return new InstPlant(clazz, ident);
    }

    /**
     * Get a new class instance creator.
     *
     * @param clazz class
     * @return new object instance of the class clazz
     */
    public static InstanceCreator getClassCreator(Class clazz) {
        return new InstPlant(clazz);
    }


    public static boolean hasNoParamConstructor(Class clazz) {
        return getDefaultConstructor(clazz) != null;
    }

    public static String classProps(Class clazz) {
        if (clazz == null)
            throw new NullPointerException("clazz is null");

        List<Method> getters = new ArrayList<Method>();
        List<Method> setters = new ArrayList<Method>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (isGetterCandidate(method))
                getters.add(method);

            if (isSetterCandidate(method))
                setters.add(method);
        }


        List<Pair<Method, List<Method>>> pairCands = new ArrayList<Pair<Method, List<Method>>>();
        for (int i = getters.size() - 1; i >= 0; i--) {

            List<Method> setCands = new ArrayList<Method>();
            Method getter = getters.get(i);

            pairCands.add(0, new DefaultPair<Method, List<Method>>(getter, setCands));

            final String getterTail;
            if (isBoolGetter(getter)) {
                getterTail = getter.getName().substring("is".length());
            } else {
                getterTail = getter.getName().substring("get".length());
            }

            for (int j = setters.size() - 1; j >= 0; j--) {
                String setterTail = setters.get(j).getName().substring("set".length());
                if (setterTail.equals(getterTail))
                    setCands.add(setters.get(j));
            }
            getters.remove(i);
        }

        StringBuilder sb = new StringBuilder();
        for (Pair<Method, List<Method>> cand : pairCands) {
            Method getter = cand.getObjOne();
            for (Method setter : cand.getObjTwo())
                //  here we check that types and param count are appropriate.
                if (isRightParamCountAndTypes(getter, setter)) {
                    final String getterTail;
                    if (isBoolGetter(getter)) {
                        getterTail = getter.getName().substring("is".length());
                    } else {
                        getterTail = getter.getName().substring("get".length());
                    }

                    String setterTail = setter.getName().substring("set".length());
                    if (!getterTail.equals(setterTail))
                        throw new IllegalStateException();

                    String methodTail =
                            getterTail.substring(0, 1).toLowerCase() +
                                    getterTail.substring(1);
                    sb.append("").
                            append(methodTail).
                            append("  ").
                            append(getter.getName()).append("()").
                            append("  ").
                            append(setter.getName()).append("()").
                            append("\n");
                }
        }
        return sb.toString();
    }


    public static List<Class> getClassLadder(final Class startClass) {
        if (startClass == null)
            throw new IllegalArgumentException("startClass is null");

        List<Class> tempList = new ArrayList<Class>();
        Class temp = startClass;
        while (temp != null) {
            tempList.add(temp);
            temp = temp.getSuperclass();
        }

        List<Class> result = new ArrayList<Class>();
        for (int i = tempList.size() - 1; i >= 0; i--)
            result.add(tempList.get(i));
        return result;
    }

    public static boolean hasRegistrationMethods(Class<?> clazz) {
        return getRegMethods(clazz).length > 0;
    }

    public static void callRegistrationMethods(Class<?> clazz, Registry registry) {
        for (Method method : getRegMethods(clazz)) {
            method.setAccessible(true);
            try {
                method.invoke(null, registry);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            } catch (InvocationTargetException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    private static String classProps2(Class clazz) {
        if (clazz == null)
            throw new NullPointerException("passed clazz is null");

        Map<String, Pair<List<AccessibleObject>, List<AccessibleObject>>> items =
                new HashMap<String, Pair<List<AccessibleObject>, List<AccessibleObject>>>();

        boolean isAnector = false;
        List<Class> ladder = getClassLadder(clazz);

        for (Class temp : ladder) {
            Field[] declaredFields = temp.getDeclaredFields();
            for (Field field : declaredFields) {
                Annotation[] annotations = field.getAnnotations();

                for (Annotation annotation : annotations) {
                    if (annotation instanceof Getter) {
                        Getter getter = (Getter) annotation;
                        for (String propName : getter.properties())
                            getPair(propName, items).getObjOne().add(field);
                    }

                    if (annotation instanceof Setter) {
                        Setter setter = (Setter) annotation;
                        for (String propName : setter.properties())
                            getPair(propName, items).getObjTwo().add(field);

                    }
                }
            }

            Method[] methods = temp.getDeclaredMethods();
            for (Method method : methods) {
                Annotation[] annotations = method.getAnnotations();

                for (Annotation annotation : annotations) {
                    if (annotation instanceof Getter) {
                        Getter getter = (Getter) annotation;
                        for (String propName : getter.properties())
                            getPair(propName, items).getObjOne().add(method);

                    }


                    if (annotation instanceof Setter) {
                        Setter setter = (Setter) annotation;
                        for (String propName : setter.properties())
                            getPair(propName, items).getObjTwo().add(method);
                    }
                }
            }
            isAnector = true;
        }


        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Pair<List<AccessibleObject>, List<AccessibleObject>>> entry : items.entrySet()) {
            String propertyName = entry.getKey();
            Pair<List<AccessibleObject>, List<AccessibleObject>> value = entry.getValue();
            List<AccessibleObject> getters = value.getObjOne();
            List<AccessibleObject> setters = value.getObjTwo();

            for (AccessibleObject getter : getters)
                for (AccessibleObject setter : setters)
                    if (isRightParamCountAndTypes2(getter, setter)) {
                        sb.append("").
                                append(propertyName).
                                append("  ").
                                append(accessorName(getter)).
                                append("  ").
                                append(accessorName(setter)).
                                append("\n");
                    }
        }

        return sb.toString();
    }

        


    private static boolean isRightParamCountAndTypes(Method getter, Method setter) {
        Class<?>[] setterParameterTypes = setter.getParameterTypes();
        return setterParameterTypes.length == 1 && getter.getReturnType() == setterParameterTypes[0];
    }


    private static boolean isRightParamCountAndTypes2(AccessibleObject getter, AccessibleObject setter) {
        if (getter instanceof Method && setter instanceof Method) {
            // they are both methods
            Method getterM = (Method) getter;
            Method setterM = (Method) setter;

            return isValidGetter(getter) &&
                    isValidSetter(setter) &&
                    getterM.getReturnType() == setterM.getParameterTypes()[0];

        } else if (getter instanceof Field && setter instanceof Method) {
            // getter is Field and setter is Method
            Field getterF = (Field) getter;
            Method setterM = (Method) setter;

            return isValidGetter(getter) &&
                    isValidSetter(setter) &&
                    getterF.getType() == setterM.getParameterTypes()[0];

        } else if (getter instanceof Method && setter instanceof Field) {
            // getter is Field and setter is Method
            Method getterM = (Method) getter;
            Field setterF = (Field) setter;

            return isValidGetter(getter) &&
                    isValidSetter(setter) &&
                    getterM.getReturnType() == setterF.getType();

        } else if (getter instanceof Field && setter instanceof Field) {
            // getter is Field and setter is Field
            Field getterF = (Field) getter;
            Field setterF = (Field) setter;

            return isValidGetter(getter) &&
                    isValidSetter(setter) &&
                    getterF.getType() == setterF.getType();


        } else
            throw new IllegalArgumentException(UNKNOWN_ACCESSIBLE_OBJECT);

    }


    private static boolean isGetterCandidate(Method method) {
        Class<?> type = method.getReturnType();
        if (!((type != TypeConsts.voidType &&
                (method.getParameterTypes() == null ||
                        method.getParameterTypes().length == 0))))
            return false;

        if (isBoolGetter(method)) {
            return method.getName().toLowerCase().startsWith("is");
        } else {
            return method.getName().toLowerCase().startsWith("get");
        }
    }

    private static boolean isBoolGetter(Method method) {
        Class<?> type = method.getReturnType();
        return type.isAssignableFrom(TypeConsts.booleanPrim);
    }

    private static boolean isSetterCandidate(Method method) {
        if (!(method.getName().toLowerCase().startsWith("set")))
            return false;

        Class<?>[] paramTypes = method.getParameterTypes();
        return (method.getReturnType() == TypeConsts.voidType &&
                paramTypes != null &&
                paramTypes.length == 1 &&
                paramTypes[0] != TypeConsts.voidType);
    }

    private static String accessorName(AccessibleObject accessibleObject) {
        if (accessibleObject instanceof Method) {
            Method method = (Method) accessibleObject;
            return method.getName() + "()";
        }
        if (accessibleObject instanceof Field) {
            Field field = (Field) accessibleObject;
            return field.getName();
        } else
            throw new IllegalArgumentException(UNKNOWN_ACCESSIBLE_OBJECT);
    }

    private static Pair<List<AccessibleObject>, List<AccessibleObject>> getPair(
            String propName,
            Map<String, Pair<List<AccessibleObject>, List<AccessibleObject>>> items) {

        Pair<List<AccessibleObject>, List<AccessibleObject>> result = items.get(propName);
        if (result == null) {
            List<AccessibleObject> getters = new ArrayList<AccessibleObject>();
            List<AccessibleObject> setters = new ArrayList<AccessibleObject>();
            result = new DefaultPair<List<AccessibleObject>, List<AccessibleObject>>(getters, setters);
            items.put(propName, result);
        }
        return result;
    }

    private static boolean isValidGetter(AccessibleObject getter) {
        if (getter == null)
            return false;

        if (getter instanceof Method) {
            Method getterM = (Method) getter;
            return getterM.getReturnType() != TypeConsts.voidType &&
                    getterM.getParameterTypes().length == 0;

        }
        if (getter instanceof Field) {
            Field getterF = (Field) getter;
            return getterF.getType() != TypeConsts.voidType;

        } else
            throw new IllegalArgumentException(UNKNOWN_ACCESSIBLE_OBJECT);

    }

    private static boolean isValidSetter(AccessibleObject setter) {
        if (setter == null)
            return false;

        if (setter instanceof Method) {
            Method setterM = (Method) setter;
            return setterM.getReturnType() == TypeConsts.voidType &&
                    setterM.getParameterTypes().length == 1 &&
                    setterM.getParameterTypes()[0] != TypeConsts.voidType;

        }
        if (setter instanceof Field) {
            Field setterF = (Field) setter;
            return setterF.getType() != TypeConsts.voidType;

        } else
            throw new IllegalArgumentException(UNKNOWN_ACCESSIBLE_OBJECT);

    }

    /**
     * Returns all methods that can be called for registration
     * theese methods are :
     * 1. static
     * 2. annotated with Registered
     * 3. must return nothing
     * 4. must support only one param of Registry type
     *
     * @param clazz a class
     * @return
     */
    private static Method[] getRegMethods(Class<?> clazz) {
        List<Method> methods = new ArrayList<Method>(Arrays.asList(clazz.getDeclaredMethods()));
        for (int i = methods.size() - 1; i >= 0; i--) {
            Method method = methods.get(i);
            if (!isRegMethod(method))
                methods.remove(i);
        }
        return methods.toArray(new Method[]{});
    }

    private static boolean isRegMethod(Method method) {
        if ((method.getModifiers() & Modifier.STATIC) == 0)
            return false;
        if (method.getReturnType() != Void.TYPE)
            return false;
        if (method.getAnnotation(Registered.class) == null)
            return false;
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 1)
            return false;
        if (!Registry.class.isAssignableFrom(parameterTypes[0]))
            return false;
        return true;
    }


}
