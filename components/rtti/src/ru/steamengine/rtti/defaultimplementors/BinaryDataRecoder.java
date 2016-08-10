package ru.steamengine.rtti.defaultimplementors;

import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Steam engine corp. in 17.12.2009 22:57:38
 *
 * @author Christopher Marlowe
 */
public class BinaryDataRecoder {

    private static final String ALLOWED_SYMBOLS = "0123456789ABCDEFabcdef";

    private static final Map<Byte, String> byteToString;

    private static final Map<String, Byte> stringToByte;

    static {
        Map<Byte, String> temp1 = new HashMap<Byte, String>();
        Map<String, Byte> temp2 = new HashMap<String, Byte>();
        for (int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++) {
            byte b = (byte) i;
            String byteId = getByteId(b);
            temp1.put(b, byteId);
            temp2.put(byteId, b);
        }
        byteToString = Collections.unmodifiableMap(temp1);
        stringToByte = Collections.unmodifiableMap(temp2);
    }


    public static String dataToString(byte[] data) {
        StringBuilder builder = new StringBuilder();
        for (byte b : data)               
            builder.append(byteToString.get(b));
        return builder.toString();
    }

    public static byte[] dataFromString(String data) {
        checkStringData(data);
        byte[] result = new byte[data.length() / 2];
        int ind = 0;
        int i = 0;
        while (ind < data.length()) {
            String temp = data.substring(ind, ind + 2);
            result[i] = stringToByte.get(temp);
            i++;
            ind = ind + 2;
        }

        return result;
    }

    private static void checkStringData(String data) {
        if (data == null)
            throw new IllegalArgumentException("data is null");
        if (data.length() % 2 != 0)
            throw new IllegalArgumentException("data.length() % 2 != 0");
        for (int i = 0; i < data.length(); i++)
            if (ALLOWED_SYMBOLS.indexOf(data.charAt(i)) == -1)
                throw new IllegalArgumentException("Unexpected char " + "" + data.charAt(i) + " at position " + i);
    }

    private static int getByteVal(byte b) {
        return b >= 0 ? b : 256 + b;
    }

    private static String getByteId(byte b) {
        int val = getByteVal(b);
        String result = new Formatter().format("%x", val).toString();
        while (result.length() < 2)
            result = "0" + result;
        return result.toUpperCase();
    }
}
