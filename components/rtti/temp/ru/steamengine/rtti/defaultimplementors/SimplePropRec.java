package ru.steamengine.rtti.defaultimplementors;

import ru.steamengine.rtti.basetypes.ClassConsts;
import ru.steamengine.rtti.basetypes.PropertyRecoderEx;
import ru.steamengine.rtti.basetypes.UsedCharset;

import java.math.BigDecimal;

/**
 * Recoder for all primitive types and their objected analogs
 * <p/>
 * <p/>
 * Created by Steam engine corp in 02.12.2009 21:52:36
 *           
 * @author Christopher Marlowe
 */
public class SimplePropRec implements PropertyRecoderEx {

    private final Class propClass;

    public SimplePropRec(Class propClass) {
        if (propClass == null)
            throw new IllegalArgumentException();
        this.propClass = propClass;
    }

    @Override
    public byte[] getValue(Object value) {
        if (value == null)
            throw new NullPointerException("value mustn't be null");

        ClassConsts simpleType = ClassConsts.getSimpleType(getPropClass());
        if (simpleType == null)
            throw new IllegalArgumentException("simpleType is null");

        switch (simpleType) {
            case voidType:
                throw new IllegalArgumentException("type void is not processed");


            case binaryData:
                return BinaryDataRecoder.dataToString((byte[]) value).getBytes(UsedCharset.CHARSET);


            case enumeration:
                Enum anEnum = (Enum) value;
                return anEnum.name().getBytes(UsedCharset.CHARSET);


            case longPrim:
            case longObj:
                Long aLong = (Long) value;
                return String.valueOf(aLong).getBytes(UsedCharset.CHARSET);


            case intPrim:
            case intClass:
                Integer aInt = (Integer) value;
                return String.valueOf(aInt).getBytes(UsedCharset.CHARSET);


            case shortPrim:
            case shortClass:
                Short aShort = (Short) value;
                return String.valueOf(aShort).getBytes(UsedCharset.CHARSET);


            case bytePrim:
            case byteClass:
                Byte aByte = (Byte) value;
                return String.valueOf(aByte).getBytes(UsedCharset.CHARSET);


            case doublePrim:
            case doubleClass:
                Double aDouble = (Double) value;
                return String.valueOf(aDouble).getBytes(UsedCharset.CHARSET);


            case floatPrim:
            case floatClass:
                Float aFloat = (Float) value;
                return String.valueOf(aFloat).getBytes(UsedCharset.CHARSET);


            case booleanPrim:
            case booleanClass:
                Boolean bool = (Boolean) value;
                return Boolean.toString(bool).getBytes(UsedCharset.CHARSET);


            case charPrim:
            case charClass:
                Character character = (Character) value;
                return character.toString().getBytes(UsedCharset.CHARSET);


            case string:
                String string = (String) value;
                return string.getBytes(UsedCharset.CHARSET);


            case bigDecimalClass:
                BigDecimal bigDecimal = (BigDecimal) value;
                return bigDecimal.toPlainString().getBytes(UsedCharset.CHARSET);


            default:
                throw new IllegalArgumentException("type is not served :" + simpleType.name());
        }
    }

    @Override
    public Object getObject(byte[] bytes, Class actualType) {


        boolean b = getPropClass().isAssignableFrom(actualType);
        if (!b)
            throw new IllegalArgumentException("");/*   */

        String oValStr = new String(bytes, UsedCharset.CHARSET);
        ClassConsts simpleType = ClassConsts.getSimpleType(actualType);
        if (simpleType == null)
            throw new IllegalArgumentException("simpleType is null");

        switch (simpleType) {
            case voidType:
                throw new IllegalArgumentException("type void is not served");

            case binaryData:
                return BinaryDataRecoder.dataFromString(oValStr);

            case enumeration:
                //noinspection unchecked
                return Enum.valueOf(actualType, oValStr);


            case longPrim:
            case longObj:
                return Long.valueOf(oValStr);


            case intPrim:
            case intClass:
                return Integer.valueOf(oValStr);


            case shortPrim:
            case shortClass:
                return Short.valueOf(oValStr);


            case bytePrim:
            case byteClass:
                return Byte.valueOf(oValStr);


            case doublePrim:
            case doubleClass:
                return Double.valueOf(oValStr);


            case floatPrim:
            case floatClass:
                return Float.valueOf(oValStr);


            case booleanPrim:
            case booleanClass:
                return stringToBoolean(oValStr);


            case charPrim:
            case charClass:
                return oValStr.charAt(0);


            case string:
                return oValStr;

            case bigDecimalClass:
                return new BigDecimal(oValStr);

            default:
                throw new IllegalArgumentException("type is not served " + simpleType.name());
        }
    }

    private boolean stringToBoolean(String oValStr) {
        if (oValStr == null)
            throw new NullPointerException("oValStr == null");
        if (oValStr.equalsIgnoreCase("true")) {
            return true;
        } else if (oValStr.equalsIgnoreCase("false")) {
            return false;
        } else
            throw new IllegalArgumentException(oValStr + " is not valid boolean value");
    }

    @Override
    public boolean isNullable() {
        ClassConsts simpleType = ClassConsts.getSimpleType(getPropClass());
        if (simpleType == null)
            throw new IllegalArgumentException("simpleType == null");
        return simpleType.isNullable;
    }

    private Class getPropClass() {
        return propClass;
    }

}
