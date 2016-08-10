package ru.steamengine.xmlstreaming3;


import ru.steamengine.easyxml.basetypes.Attribute;
import ru.steamengine.easyxml.basetypes.Document;
import ru.steamengine.easyxml.basetypes.Node;
import ru.steamengine.easyxml.reader.QuickXMLReader;
import ru.steamengine.rtti.basetypes.*;
import ru.steamengine.rtti.defaultimplementors.DefaultPair;
import static ru.steamengine.rtti.defaultimplementors.utils.CommonStreamingUtils.*;
import ru.steamengine.rtti.defaultimplementors.utils.PropertyEntry;
import ru.steamengine.streaming.basetypes.*;

import java.io.InputStream;
import java.lang.reflect.Method;


/**
 * Created by Steam engine corp in 10.07.2009 21:50:36
 *
 * @author Christopher Marlowe
 */
public class XMLObjectReader3 implements ObjectReader, StreamInit {

    private RegistryUser registryUser;

    private boolean inited = false;

    public XMLObjectReader3() {
    }

    public XMLObjectReader3(RegistryUser registryUser) {
        this();
        initialize(registryUser);
    }

    @Override
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

    private static Class<?> getTypes(ListedItem listedItem) {
        Class<?> childType = null;

        Method[] methods = listedItem.getClass().getMethods();
        for (Method method : methods) {

            if (!method.getReturnType().isArray())
                continue;

            if (!method.getName().equals("getItems"))
                continue;

            Class<?>[] paramTypes = method.getParameterTypes();
            if (paramTypes.length != 1)
                continue;

            Class<?> tempChild = method.getReturnType().getComponentType();
            if (tempChild == null)
                continue;

            if (childType == null || childType.isAssignableFrom(tempChild)) {
                childType = tempChild;
            }
        }

        if (childType == null)
            throw new IllegalStateException();

        return childType;
    }

    public <T> T read(InputStream stream, final ReaderExceptionHandler readerExceptHandler)
            throws ReadError {
        checkInited();

        final ReaderContext context = new ReaderContext();
        context.registryUser = registryUser;
        context.readerExceptionHandler = readerExceptHandler;
        try {
            Document document = QuickXMLReader.read(stream);
            final Node node = document.rootNode();


            context.registryUser.doTransaction(new RegistryUser.Transaction() {
                @Override
                public void inTransaction() {
                    context.readerExceptionHandler = readerExceptHandler;
                    if (context.readerExceptionHandler != null)
                        context.readerExceptionHandler.readingStarted();
                    context.readerExceptionHandler = readerExceptHandler;
                    context.registryUser = registryUser;
                    try {
                        readNode(context.rootObj, null, node, context);
                    } finally {
                        endReading(context);
                    }
                }
            });

        } catch (Exception e) {
            throw new ReadError(e);
        }

        //noinspection unchecked
        return (T) context.rootObj;
    }

    @Override
    public <T> T read(InputStream stream, ObjectResolver objectResolver, ReaderExceptionHandler readerExceptionHandler) throws ReadError {
        throw new UnsupportedOperationException();
    }

    private static void readNode(Object rootObject,
                          Object parentObject,
                          Node node,
                          final ReaderContext context) {


        final String nodeName = node.getName();
        if (nodeName.equalsIgnoreCase(XMLReadWriteConsts3.OBJECT_IDENT)) {
            String classIdent = node.attribs.getValue(XMLReadWriteConsts3.CLASS_IDENT);

            Class instanceClass = context.registryUser.findInstanceClass(classIdent);
            if (instanceClass == null && context.rootObj == null)
                throw new ReadError("class ident for root object not found");
            if (!(instanceClass == null && processException(new ReadError("class ident not found"), context))) {
                String objectName = node.attribs.getValue(XMLReadWriteConsts3.NAME_IDENT);

                Integer position = getPosition(node);
                int pos = position != null ? position : -1;

                Object obj = rootObject != null && context.rootObj == null ?
                        rootObject :
                        context.registryUser.getInstance(classIdent);

                if (obj == null) throw new IllegalArgumentException("objRegUser.getInstance(classIdent)");

                callReadNotification(context, obj, ReadNotifier.ReadMode.runtime);
                LoadNotifier loadNotfier = context.registryUser.getLoadNotfier(obj.getClass());
                addToLoaded(loadNotfier, context, obj);

                processObjectName(context, objectName, obj);

                if (context.rootObj == null) context.rootObj = obj;

                if (parentObject != null) {
                    ObjectProcessor processor = context.registryUser.getObjProcessor(parentObject.getClass());
                    processor.setChild(obj, parentObject, pos);
                }

                for (Node subNode : node.nodes())
                    readNode(context.rootObj, obj, subNode, context);
            }



        } else if (nodeName.equalsIgnoreCase(XMLReadWriteConsts3.PROP_IDENT)) {
            //  reading.
            final String strPropPath = node.attribs.getValue(XMLReadWriteConsts3.PATH_IDENT);

            if (strPropPath == null)
                return;

            final PropertyEntry propPair = findPropertyEntry(context.registryUser, parentObject, strPropPath);

            if (propPair == null) {
                //  parsing a list
                Pair<Object, ListSlot> listedItemSlotPair = findListedEntry(context.registryUser, parentObject, strPropPath);
                if (listedItemSlotPair == null)
                    //noinspection ThrowableInstanceNeverThrown
                    processException(new ReadError("property path is not found " + strPropPath), context);

                if (listedItemSlotPair != null) {
                    Object aValue = listedItemSlotPair.getObjOne();
                    ListSlot slot = listedItemSlotPair.getObjTwo();


                    if (slot == null)
                        //noinspection ThrowableInstanceNeverThrown
                        processException(new ReadError("class " + parentObject.getClass().getName() +
                                " do not have listed items"), context);

                    if (slot != null) {
                        //noinspection unchecked
                        slot.beginUpdate(aValue);
                        try {
                            //noinspection unchecked
                            slot.clear(aValue);

                            int curInd = 0;
                            for (Node subnode : node.nodes()) {
                                if (subnode.getName().equals(XMLReadWriteConsts3.ITEM_IDENT)) {
                                    @SuppressWarnings({"unchecked"})
                                    Object listItem = slot.newItem(aValue);
                                    PropertyRecoder st = context.registryUser.getSimpleType(slot.getItemClass());
                                    if (st != null) {
                                        Attribute valAttr = subnode.attribs.getItem(XMLReadWriteConsts3.VALUE_IDENT);
                                        try {
                                            final Object o;
                                            if (valAttr != null) {
                                                byte[] bytes = valAttr.getValue().getBytes();
                                                o = st.getObject(bytes, slot.getItemClass());
                                            } else {
                                                // value is null
                                                o = null;
                                            }
                                            //noinspection unchecked
                                            slot.setItemValue(aValue, o, curInd);
                                        } catch (Throwable e) {
                                            //noinspection ThrowableInstanceNeverThrown
                                            if (processException(new ReadError(e), context))
                                                continue;
                                        }

                                    } else {

                                        try {
                                            if (listItem == null) {
                                                //noinspection ThrowableInstanceNeverThrown
                                                processException(new ReadError("list item is not defined"), context);
                                            } else if (!(slot.getItemClass().isAssignableFrom(listItem.getClass()))) {
                                                //noinspection ThrowableInstanceNeverThrown
                                                processException(new ReadError("list item class does not conform to defined"), context);
                                            }
                                            readNode(context.rootObj, listItem, subnode, context);

                                            //noinspection unchecked
                                            slot.setItemValue(aValue, listItem, curInd);
                                        } catch (Throwable e) {
                                            //noinspection ThrowableInstanceNeverThrown
                                            if (processException(new ReadError(e), context))
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

                    PropertyTypeIdent propTypeIdent = getPropTypeIdent(pe, context.registryUser);
                    final String strPropValue = node.attribs.getValue(XMLReadWriteConsts3.VALUE_IDENT);
                    switch (propTypeIdent) {
                        case value:
                            final Object aValue;

                            if (strPropValue != null) {
                                PropertyRecoder recoder = context.registryUser.getSimpleType(pe.getPropType());
                                aValue = recoder.getObject(strPropValue.getBytes(), pe.getPropType());
                            } else {
                                aValue = null;
                            }

                            pe.setVal(anObject, aValue);
                            break;

                        case nested:
                            //noinspection ThrowableInstanceNeverThrown
                            processException(new IllegalArgumentException(""), context);
                            break;

                        case reference:
                            addFixUp(context, anObject, strPropPath, strPropValue, pe);
                            break;

                        default:
                            throw new IllegalArgumentException("Unknown type " + propTypeIdent.name());

                    }

                } catch (Exception e) {
                    //noinspection ThrowableInstanceNeverThrown
                    processException(new ReadError(e), context);
                }
            }
        } else if (nodeName.equalsIgnoreCase(XMLReadWriteConsts3.ITEM_IDENT)) {
            for (Node subNode : node)
                readNode(context.rootObj, parentObject, subNode, context);

        }
    }

    private static void callReadNotification(ReaderContext context, Object obj,  ReadNotifier.ReadMode readMode) {
        ReadNotifier readNotifier = context.registryUser.getReadNotfier(obj.getClass());
        try {
            //noinspection unchecked
            readNotifier.onObjectRead(obj, readMode);
        } catch (Throwable ignored) {
            processException(new ReadError(ignored), context);
        }
    }

    private static void addToLoaded(LoadNotifier notifier, ReaderContext context, Object obj) {
        context.loaded2.add(new DefaultPair<LoadNotifier, Object>(notifier, obj));
    }


    private static void processObjectName(
            ReaderContext context,
            String objectName,
            Object obj) {

        if (objectName == null) {
            objectName = context.namespace.getObjectName(obj, "UNSET");
        } else if (context.namespace.isNameUnique(obj, objectName)) {
            context.namespace.setObjectName(obj, objectName);
        } else {
            //noinspection ThrowableInstanceNeverThrown
            processException(new ReadError("name  \"" + objectName + "\"  is not unique"), context);
        }

        ObjectNameProcessor objectNameProcessor = context.registryUser.getNameProcessor(obj.getClass());
        if (objectNameProcessor != null)
            //noinspection unchecked
            objectNameProcessor.setName(obj, objectName);
    }

    private void checkInited() {
        if (!inited)
            throw new IllegalArgumentException("object is not inited");
    }

    private static Integer getPosition(Node node) {
        if (node == null)
            throw new NullPointerException("node is null");
        String attribute = node.attribs.getValue(XMLReadWriteConsts3.POSITION_IDENT);
        try {
            return Integer.valueOf(attribute);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    private static void addFixUp(ReaderContext context, PropertyFixup fixUp) {
        if (fixUp == null)
            throw new IllegalArgumentException("fixUp is null");
        context.fixUps.add(fixUp);
    }

    private static void addFixUp(final ReaderContext context,
                          final Object anObject,
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
                    ref = context.namespace.findObject(propertyValue);
//                    if (ref == null && objectResolver != null)
//                        //noinspection UnusedAssignment
//                        ref = objectResolver.findObject(propertyValue);
                } else {
                    ref = null;
                }
                pe.setVal(anObject, ref);
            }
        };
        context.fixUps.add(fixUp);
    }



    private static void resolveFixUps(ReaderContext context) {
        for (PropertyFixup fixUp : context.fixUps) {
            try {
                fixUp.resolve();
            } catch (Exception e) {
                String val = "unable to set reference for property path " + fixUp.getPropPath() +
                        " . Class : " + fixUp.getObject().getClass().getName();
                //noinspection ThrowableInstanceNeverThrown                       
                processException(new ReadError(val), context);
            }
        }
    }

    private static void cleanUpFixUps(ReaderContext context) {
        context.fixUps.clear();
    }

    private static void cleanUpNames(ReaderContext context) {
        context.namespace.clear();
    }

    private static void cleanUpLoaded(ReaderContext context) {
        context.loaded2.clear();
    }

    private static void endReading(ReaderContext context) {
        resolveFixUps(context);
        cleanUpFixUps(context);
        cleanUpNames(context);
        callLoaded(context);
        cleanUpLoaded(context);
        if (context.readerExceptionHandler != null)
            context.readerExceptionHandler.readingFinished();
    }

    private static void callLoaded(ReaderContext context) {
        for (Pair<LoadNotifier, Object> pair : context.loaded2)
            //noinspection unchecked
            pair.getObjOne().objectLoaded(pair.getObjTwo());
    }

    private static boolean processException(RuntimeException e, ReaderContext context) throws
            RuntimeException {
        if (context.readerExceptionHandler == null)
            throw e;
        if (!context.readerExceptionHandler.isHandled(e))
            throw e;
        return true;
    }


}