/**
 * Created by Steam engine corp. in 26.10.2011 23:16:28
 *
 * @author Christopher Marlowe
 */


package ru.steamengine.rtti.defaultimplementors.notifyshutdowntests;

import ru.steamengine.rtti.basetypes.Registry;
import ru.steamengine.rtti.basetypes.ShutDownNotifier;
import ru.steamengine.rtti.defaultimplementors.DefaultObjectProcessor;
import ru.steamengine.rtti.defaultimplementors.DefaultRttiHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SubObjectList {

    private final List<SubObjectList> list = new ArrayList<SubObjectList>();

    public static void reg(Registry r) {
        r.regObjCreator(DefaultRttiHelper.getClassCreator(SubObjectList.class));
        r.regObjProcessor(SubObjectList.class, new DefaultObjectProcessor<SubObjectList>() {

            @Override
            public Iterator getIterator(SubObjectList subObjectList) {
                return subObjectList.list.iterator();
            }

            @Override
            public Object[] getObjectChildren(SubObjectList obj) {
                return obj.list.toArray();
            }

            @Override
            public boolean setChild(Object object, SubObjectList subObjectList, int position) {
                if (object instanceof SubObjectList) {
                    subObjectList.list.add((SubObjectList) object);
                    return true;
                } else {
                    return false;
                }
            }
        });
        r.regShutDownNotifier(SubObjectList.class, new ShutDownNotifier<SubObjectList>() {
            @Override
            public void shutingDown(SubObjectList object) {
                object.shutDown();
            }
        });


    }


    private void shutDown() {

    }

    public void add(SubObjectList subObject) {
        list.add(subObject);
    }
}
