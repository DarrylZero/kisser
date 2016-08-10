package ru.steamengine.rtti.basetypes;

import java.math.BigDecimal;

/**
 *
 * Enumeration of buit-in simple types.
 *
 *
 * Public API
 * Created by Steam engine corp. in 13.12.2009 17:01:25
 * @author Christopher Marlowe
 */

public enum ClassConsts {

    /**
     * an empty type
     */
    voidType(Fake.class.getDeclaredMethods()[0].getReturnType(), false),

    /**
     * binary data
     *
     */
    binaryData(byte[].class, true),

    /**
     * enumeration
     */
    enumeration(Enum.class, true),

    /**
     *  long prim type
     */
    longPrim(Long.TYPE, false),

    /**
     * Long type
     */
    longObj(Long.class, true),

    /**
     * int prim type
     */
    intPrim(Integer.TYPE, false),

    /**
     * Integer type
     */
    intClass(Integer.class, true),

    /**
     *
     * short prim type
     *
     */
    shortPrim(Short.TYPE, false),


    /**
     *
     * Short type
     *
     */
    shortClass(Short.class, true),

    /**
     * byte prim type
     *
     */
    bytePrim(Byte.TYPE, false),

    /**
     * Byte type
     */
    byteClass(Byte.class, true),

    /**
     * double prim type
     */
    doublePrim(Double.TYPE, false),

    /**
     * Double type
     */
    doubleClass(Double.class, true),

    /**
     * float prim type
     */
    floatPrim(Float.TYPE, false),

    /**
     * Float type
     */
    floatClass(Float.class, true),

    /**
     * boolean prim type
     */
    booleanPrim(Boolean.TYPE, false),

    /**
     * Boolean type
     */
    booleanClass(Boolean.class, true),

    /**
     *
     * char prim type
     */
    charPrim(Character.TYPE, false),

    /**
     * Character type
     */
    charClass(Character.class, true),

    /**
     * String type
     */
    string(String.class, true),

    /**
     * BigDecimal type
     */
    bigDecimalClass(BigDecimal.class, true);

    /**
     * reference to class
     */
    public final Class clazz;

    /**
     * wether the type can be Nullable
     */
    public final boolean isNullable;

    private ClassConsts(Class clazz,
                        boolean nullable) {
        if (clazz == null)
            throw new IllegalArgumentException("clazz is null");
        this.clazz = clazz;
        isNullable = nullable;
    }


    @SuppressWarnings({"UnusedDeclaration"})
    private static boolean isSimple(Class clazz) {
        return getSimpleType(clazz) != null;
    }


    /**
     * get enueration for type
     * @param clazz type
     * @return emuneration item.
     */
    public static ClassConsts getSimpleType(Class clazz) {
        for (ClassConsts aConst : ClassConsts.values())
            if (aConst.clazz.isAssignableFrom(clazz))
                return aConst;
        return null;
    }


    private static class Fake {
        public void fakeMethod() {
        }
    }

}
