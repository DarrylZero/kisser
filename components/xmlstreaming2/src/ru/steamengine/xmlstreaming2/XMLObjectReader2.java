package ru.steamengine.xmlstreaming2;


import ru.steamengine.easyxml.basetypes.Attribute;
import ru.steamengine.easyxml.basetypes.Document;
import ru.steamengine.easyxml.basetypes.Node;
import ru.steamengine.easyxml.reader.QuickXMLReader;
import ru.steamengine.rtti.basetypes.*;
import ru.steamengine.rtti.defaultimplementors.DefaultPair;
import static ru.steamengine.rtti.defaultimplementors.utils.CommonStreamingUtils.*;
import ru.steamengine.rtti.defaultimplementors.utils.PropertyEntry;
import ru.steamengine.streaming.basetypes.*;
import ru.steamengine.streaming.defaultimplementors.DefaultObjectNamespace;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Steam engine corp in 10.07.2009 21:50:36
 *
 * @author Christopher Marlowe
 */
public class XMLObjectReader2 implements ObjectReader, StreamInit {

    /**
     * object first read
     */
    private Object rootObj = null;

    private RegistryUser registryUser;

    private boolean inited = false;

    private final List<PropertyFixup> fixUps = new ArrayList<PropertyFixup>();

    private final ObjectNamespace namespace = new DefaultObjectNamespace();

    private final List<Pair<LoadNotifier, Object>> loaded = new ArrayList<Pair<LoadNotifier, Object>>();

    private ReaderExceptionHandler readerExceptionHandler;

    public XMLObjectReader2() {
    }

    public XMLObjectReader2(RegistryUser registryUser) {
        this();
        initialize(registryUser);
    }

    public <T> T initialize(RegistryUser registryUser) {
        if (inited)
            throw new IllegalArgumentException("object is already inited");
        if (registryUser == null)
            throw new NullPointerException("registryUser is null");

        this.registryUser = registryUser;
        inited = true;

        //noinspection unchecked
        return (T) this;
    }


    public <T> T read(InputStream stream, final ReaderExceptionHandler readerExceptHandler)
            throws ReadError {
        checkInited();

        try {
            Document document = QuickXMLReader.read(stream);
            final Node node = document.rootNode();


            registryUser.doTransaction(new RegistryUser.Transaction() {
                @Override
                public void inTransaction() {
                    readerExceptionHandler = readerExceptHandler;
                    beginReading();
                    try {
                        readNode(rootObj, null, node);
                    } finally {
                        endReading();
                    }
                }
            });

        } catch (Exception e) {
            throw new ReadError(e);
        }

        //noinspection unchecked
        return (T) rootObj;
    }

    @Override
    public <T> T read(InputStream stream, ObjectResolver objectResolver, ReaderExceptionHandler readerExceptionHandler) throws ReadError {
        throw new UnsupportedOperationException();
    }

    private void readNode(Object rootObject, Object parentObject, Node node) {


        final String nodeName = node.getName();
        if (nodeName.equalsIgnoreCase(XMLReadWriteConsts2.OBJECT_IDENT)) {
            String classIdent = node.attribs.getValue(XMLReadWriteConsts2.CLASS_IDENT);

            Class instanceClass = registryUser.findInstanceClass(classIdent);
            if (instanceClass == null && rootObj == null)
                throw new ReadError("class ident for root object not found");
            if (!(instanceClass == null && processException(new ReadError("class ident not found")))) {
                String objectName = node.attribs.getValue(XMLReadWriteConsts2.NAME_IDENT);

                Integer position = getPosition(node);
                int pos = position != null ? position : -1;

                Object obj = rootObject != null && rootObj == null ? rootObject : registryUser.getInstance(classIdent);
                if (obj == null) throw new IllegalArgumentException("objRegUser.getInstance(classIdent)");

                callReadNotification(obj, ReadNotifier.ReadMode.runtime);

                //  adding to loaded
                addToLoaded(obj);

                // processing a name
                processObjectName(objectName, obj);


                if (rootObj == null) rootObj = obj;

                if (parentObject != null) {
                    ObjectProcessor processor = registryUser.getObjProcessor(parentObject.getClass());
                    processor.setChild(obj, parentObject, pos);
                }

                for (Node subNode : node.nodes())
                    readNode(rootObj, obj, subNode);
            }

        } else if (nodeName.equalsIgnoreCase(XMLReadWriteConsts2.PROP_IDENT)) {

            final String strPropPath = node.attribs.getValue(XMLReadWriteConsts2.PATH_IDENT);

            if (strPropPath == null)
                return;

            final PropertyEntry propPair = findPropertyEntry(registryUser, parentObject, strPropPath);

            if (propPair == null) {

                Pair<Object, ListSlot> listedItemSlotPair = findListedEntry(registryUser, parentObject, strPropPath);
                if (listedItemSlotPair == null)
                    //noinspection ThrowableInstanceNeverThrown
                    processException(new ReadError("property path is not found " + strPropPath));

                if (listedItemSlotPair != null) {
                    Object aValue = listedItemSlotPair.getObjOne();
                    ListSlot slot = listedItemSlotPair.getObjTwo();


                    if (slot == null)
                        //noinspection ThrowableInstanceNeverThrown
                        processException(new ReadError("class " + parentObject.getClass().getName() +
                                " do not have listed items"));

                    if (slot != null) {
                        //noinspection unchecked
                        slot.beginUpdate(aValue);
                        try {
                            //noinspection unchecked
                            slot.clear(aValue);

                            int curInd = 0;
                            for (Node subnode : node.nodes()) {
                                if (subnode.getName().equals(XMLReadWriteConsts2.ITEM_IDENT)) {
                                    @SuppressWarnings({"unchecked"})
                                    Class<?> itemClass = slot.getItemClass();
                                    Object listItem = slot.newItem(aValue);
                                    PropertyRecoder st = registryUser.getSimpleType(itemClass);
                                    if (st != null) {
                                        Attribute valAttr = subnode.attribs.getItem(XMLReadWriteConsts2.VALUE_IDENT);
                                        try {
                                            final Object o;
                                            if (valAttr != null) {
                                                byte[] bytes = valAttr.getValue().getBytes();
                                                o = st.getObject(bytes, itemClass);
                                            } else {
                                                o = null;
                                            }

                                            //noinspection unchecked
                                            slot.setItemValue(aValue, o, curInd);
                                        } catch (Throwable e) {
                                            //noinspection ThrowableInstanceNeverThrown
                                            if (processException(new ReadError(e)))
                                                continue;
                                        }

                                    } else {
                                        try {
                                            if (listItem == null) {
                                                //noinspection ThrowableInstanceNeverThrown
                                                processException(new ReadError("list item is not defined"));
                                            } else if (!(itemClass.isAssignableFrom(listItem.getClass()))) {
                                                //noinspection ThrowableInstanceNeverThrown
                                                processException(new ReadError("list item class does not conform to defined"));
                                            }
                                            readNode(rootObj, listItem, subnode);
                                            //noinspection unchecked
                                            slot.setItemValue(aValue, listItem, curInd);
                                        } catch (Throwable e) {
                                            //noinspection ThrowableInstanceNeverThrown
                                            if (processException(new ReadError(e)))
                                                continue;
                                        }
                                    }
                                    curInd++;

                                }
                            }

                        } finally {
                            //noinspection unchecked
                            slot.endUpdate(aValue);
                        }
                    }
                }


            } else {


                try {
                    final RTTIEntry pe = propPair.getEntry();
                    final Object anObject = propPair.getObject();
                    final String strPropValue = node.attribs.getValue(XMLReadWriteConsts2.VALUE_IDENT);

                    PropertyTypeIdent propTypeIdent = getPropTypeIdent(pe, registryUser);
                    switch (propTypeIdent) {
                        case value:
                            final Object aValue;
                            if (strPropValue != null) {
                                PropertyRecoder recoder = registryUser.getSimpleType(pe.getPropType());
                                aValue = recoder.getObject(strPropValue.getBytes(), pe.getPropType());
                            } else {
                                aValue = null;
                            }

                            pe.setVal(anObject, aValue);
                            break;

                        case nested:
                            //noinspection ThrowableInstanceNeverThrown
                            processException(new IllegalArgumentException(""));
                            break;

                        case reference:
                            addFixUp(anObject, strPropPath, strPropValue, pe);
                            break;

                        default:
                            throw new IllegalArgumentException("Unknown type " + propTypeIdent.name());

                    }

                } catch (Exception e) {
                    //noinspection ThrowableInstanceNeverThrown
                    processException(new ReadError(e));
                }
            }
        } else if (nodeName.equalsIgnoreCase(XMLReadWriteConsts2.ITEM_IDENT)) {
            for (Node subNode : node)
                readNode(rootObj, parentObject, subNode);

        }
    }

    private void callReadNotification(Object obj,  ReadNotifier.ReadMode readMode) {
        ReadNotifier readNotifier = registryUser.getReadNotfier(obj.getClass());
        try {
            //noinspection unchecked
            readNotifier.onObjectRead(obj, readMode);
        } catch (Throwable ignored) {
            processException(new ReadError(ignored));
        }
    }

    private void addToLoaded(Object obj) {
        LoadNotifier notifier = registryUser.getLoadNotfier(obj.getClass());
        loaded.add(new DefaultPair<LoadNotifier, Object>(notifier, obj));
    }

    private void checkInited() {
        if (!inited)
            throw new IllegalArgumentException("object is not inited");
    }

    private Integer getPosition(Node node) {
        if (node == null)
            throw new NullPointerException("node is null");
        String attribute = node.attribs.getValue(XMLReadWriteConsts2.POSITION_IDENT);
        try {
            return Integer.valueOf(attribute);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }
    
    private void addFixUp(final Object anObject,
                          final String propPath,
                          final String propertyValue,
                          final RTTIEntry pe) {
        PropertyFixup fixUp = new PropertyFixup() {
            @Override
            public Object getObject() {
                return anObject;
            }

            @Override
            public String getPropPath() {
                return propPath;
            }

            @Override
            public void resolve() {
                Object ref;
                if (propertyValue != null) {
                    ref = namespace.findObject(propertyValue);
//                    if (ref == null && objectResolver != null)
//                        //noinspection UnusedAssignment
//                        ref = objectResolver.findObject(propertyValue);
                } else {
                    ref = null;
                }

                pe.setVal(anObject, ref);
            }
        };
        fixUps.add(fixUp);
    }


    private void beginReading() {
        if (readerExceptionHandler != null)
            readerExceptionHandler.readingStarted();
        loaded.clear();
        fixUps.clear();
        namespace.clear();
        rootObj = null;
    }

    private void resolveFixUps() {
        for (PropertyFixup fixUp : fixUps) {
            try {
                fixUp.resolve();
            } catch (Exception e) {
                String val = "unable to set reference for property path " + fixUp.getPropPath() +
                        " . Class : " + fixUp.getObject().getClass().getName();
                //noinspection ThrowableInstanceNeverThrown
                processException(new IllegalArgumentException(val));
            }
        }
    }

    private void cleanUpFixUps() {
        fixUps.clear();
    }

    private void cleanUpNames() {
        namespace.clear();
    }

    private void cleanUpLoaded() {
        loaded.clear();
    }

    private void processObjectName(
            String objectName,
            Object obj) {

        if (objectName == null) {
            objectName = namespace.getObjectName(obj, "UNSET");
        } else if (namespace.isNameUnique(obj, objectName)) {
            namespace.setObjectName(obj, objectName);
        } else {
            //noinspection ThrowableInstanceNeverThrown
            processException(new ReadError("name  \"" + objectName + "\"  is not unique"));
        }

        ObjectNameProcessor objectNameProcessor = registryUser.getNameProcessor(obj.getClass());
        if (objectNameProcessor != null)
            //noinspection unchecked
            objectNameProcessor.setName(obj, objectName);
    }

    private void endReading() {
        resolveFixUps();
        cleanUpFixUps();
        cleanUpNames();
        callLoaded();
        cleanUpLoaded();
        if (readerExceptionHandler != null)
            readerExceptionHandler.readingFinished();
    }

    private void callLoaded() {
        for (Pair<LoadNotifier, Object> pair : loaded)
            //noinspection unchecked
            pair.getObjOne().objectLoaded(pair.getObjTwo());
    }

    private boolean processException(RuntimeException e) throws RuntimeException {
        if (readerExceptionHandler == null)
            throw e;
        if (!readerExceptionHandler.isHandled(e))
            throw e;
        return true;
    }


}