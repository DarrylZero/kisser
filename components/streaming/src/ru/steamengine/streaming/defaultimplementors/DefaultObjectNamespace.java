package ru.steamengine.streaming.defaultimplementors;

import ru.steamengine.streaming.basetypes.ObjectNamespace;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Steam engine corp. in 23.12.2009 0:49:42
 *
 * @author Christopher Marlowe
 */
public class DefaultObjectNamespace implements ObjectNamespace {
    private final Map<Object, String> objToStr = new HashMap<Object, String>();
    private final Map<String, Object> strToObj = new HashMap<String, Object>();

    @Override
    public Object findObject(String name) {
        return strToObj.get(name);
    }

    @Override
    public boolean isNameUnique(Object obj, String name) {
        if (obj == null || name == null)
            throw new IllegalArgumentException("obj is null or name is null");
        Object o = strToObj.get(name);
        return o == null || o == obj;
    }

    @Override
    public String getObjectName(Object obj, String baseName) {
        if (obj == null)
            throw new IllegalArgumentException("obj is null");
        if (baseName == null)
            throw new IllegalArgumentException("baseName is null");

        String name = objToStr.get(obj);
        if (name == null) {
            name = getUniqueName(baseName);
            objToStr.put(obj, name);
            strToObj.put(name, obj);
        }

        return name;
    }


    @Override
    public String setObjectName(Object obj, String name) {
        if (obj == null || name == null)
            throw new NullPointerException("obj or name is null");

        if (!isNameUnique(obj, name))
            throw new IllegalArgumentException("name  \"" + name + "\"  is not unique");

        if (objToStr.get(obj) != null)
            objToStr.remove(obj);
        objToStr.put(obj, name);

        if (strToObj.get(name) != null)
            strToObj.remove(name);
        strToObj.put(name, obj);

        return name;
    }

    @Override
    public void clear() {
        objToStr.clear();
        strToObj.clear();
    }

    private String getUniqueName(String baseName) {
        int j = 1;
        while (strToObj.get(baseName + j) != null)
            j++;
        return baseName + j;
    }


}