package ru.steamengine.xmlstreaming;


import org.w3c.dom.*;
import ru.steamengine.rtti.basetypes.*;
import ru.steamengine.rtti.defaultimplementors.DefaultPair;
import static ru.steamengine.rtti.defaultimplementors.utils.CommonStreamingUtils.*;
import ru.steamengine.rtti.defaultimplementors.utils.PropertyEntry;
import ru.steamengine.streaming.basetypes.*;
import ru.steamengine.streaming.defaultimplementors.DefaultObjectNamespace;
import static ru.steamengine.xmlstreaming.XMLReadWriteConsts.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Steam engine corp in 10.07.2009 21:50:36
 * <p/>
 * <p/>
 * [kisser]xmlstreamer
 *
 * @author Christopher Marlowe
 */
public class XMLObjectReader implements ObjectReader, StreamInit {

    private Object rootObj = null;

    private boolean inited = false;

    private RegistryUser registryUser;

    private final List<PropertyFixup> fixUps = new ArrayList<PropertyFixup>();

    private final ObjectNamespace namespace = new DefaultObjectNamespace();

    private final List<Pair<Object, LoadNotifier>> loaded = new ArrayList<Pair<Object, LoadNotifier>>();

    private ReaderExceptionHandler readerExceptionHandler;

    private ObjectResolver objectResolver;

    public XMLObjectReader(RegistryUser registryUser) {
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

    @Override
    public <T> T read(InputStream stream, ReaderExceptionHandler readerExceptionHandler)
            throws ReadError {
        checkInited();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(stream);
            final Element root = doc.getDocumentElement();
            this.readerExceptionHandler = readerExceptionHandler;

            registryUser.doTransaction(new RegistryUser.Transaction() {
                @Override
                public void inTransaction() {
                    beginReading();
                    try {
                        readNode(rootObj, null, root);
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
    public <T> T read(InputStream stream,
                      ObjectResolver objectResolver,
                      ReaderExceptionHandler readerExceptionHandler) throws ReadError {

        checkInited();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(stream);
            final Element root = doc.getDocumentElement();
            this.readerExceptionHandler = readerExceptionHandler;
            this.objectResolver = objectResolver;

            registryUser.doTransaction(new RegistryUser.Transaction() {
                @Override
                public void inTransaction() {
                    beginReading();
                    try {
                        readNode(rootObj, null, root);
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

    private void checkInited() {
        if (!inited)
            throw new IllegalArgumentException("object is not inited");
    }


    private void readNode(Object rootObject, Object parentObject, Node node_node) {

        if (!(node_node instanceof Element))
            return;
        Element element = (Element) node_node;


        final String nodeName = element.getNodeName();
        if (nodeName.equalsIgnoreCase(OBJECT_IDENT)) {
            String classIdent = element.getAttribute(CLASS_IDENT);
            Class instanceClass = registryUser.findInstanceClass(classIdent);
            if (instanceClass == null && rootObj == null)
                throw new ReadError("class ident for root object not found");
            if (!(instanceClass == null && processException(new ReadError("class ident not found")))) {
                String objectName = element.getAttribute(NAME_IDENT);
                int pos = getPosition(element) != null ? getPosition(element) : -1;

                //registryUser.
                Object obj = rootObject != null && rootObj == null ? rootObject : registryUser.getInstance(classIdent);
                if (obj == null)
                    throw new IllegalArgumentException("objRegUser.getInstance(classIdent)");

                //  calling a read notification
                callReadNotification(obj, ReadNotifier.ReadMode.runtime);

                // adding an object to loaded list
                addToLoaded(obj);

                // processing a name
                processObjectName(objectName, obj);


                if (rootObj == null)
                    rootObj = obj;

                if (parentObject != null) {
                    ObjectProcessor processor = registryUser.getObjProcessor(parentObject.getClass());
                    //noinspection unchecked
                    processor.setChild(obj, parentObject, pos);
                }


                NodeList childNodes = element.getChildNodes();
                int length = childNodes.getLength();
                for (int j = 0; j < length; j++)
                    readNode(rootObj, obj, childNodes.item(j));
            }
        } else if (nodeName.equalsIgnoreCase(PROP_IDENT)) {
            //  reading properties.
            final String strPropPath = element.getAttribute(PATH_IDENT);

            if (strPropPath == null)
                return;

            PropertyEntry propPair = findPropertyEntry(registryUser, parentObject, strPropPath);
            if (propPair == null) {
                //  dealing with a list.
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

                            NodeList childNodes = node_node.getChildNodes();
                            int length = childNodes.getLength();
                            int curInd = 0;
                            for (int j = 0; j < length; j++) {
                                Node node = childNodes.item(j);
                                if (node.getNodeName().equals(ITEM_IDENT) && node instanceof Element) {
                                    Element elem = (Element) node;


                                    @SuppressWarnings({"unchecked"})
                                    Object listItem = slot.newItem(aValue);
//                                    Class itemClass = listedItem.getItemClass();
                                    Class itemClass = slot.getItemClass();
                                    PropertyRecoder st = registryUser.getSimpleType(itemClass);
                                    if (st != null) {
                                        Attr valAttr = findAttribute(elem, VALUE_IDENT);
                                        try {
                                            final Object o;
                                            if (valAttr != null) {
                                                byte[] bytes = valAttr.getNodeValue().getBytes(UsedCharset.CHARSET);
                                                //noinspection unchecked
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
                                            readNode(rootObj, listItem, node);
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

                    final String strPropValue;
                    if (findAttribute(element, VALUE_IDENT) != null) {
                        strPropValue = element.getAttribute(VALUE_IDENT);
                    } else {
                        strPropValue = null;
                    }

                    PropertyTypeIdent propTypeIdent = getPropTypeIdent(pe, registryUser);
                    switch (propTypeIdent) {
                        case value:

                            final Object aValue;
                            if (strPropValue != null) {
                                PropertyRecoder recoder = registryUser.getSimpleType(pe.getPropType());
                                byte[] value = strPropValue.getBytes(UsedCharset.CHARSET);
                                //noinspection unchecked
                                aValue = recoder.getObject(value, pe.getPropType());
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
        } else if (nodeName.equalsIgnoreCase(ITEM_IDENT)) {
            NodeList childNodes = element.getChildNodes();
            int length = childNodes.getLength();
            for (int j = 0; j < length; j++)
                readNode(rootObj, parentObject, childNodes.item(j));
        }
    }

    private void addToLoaded(Object obj) {
        if (obj == null)
            throw new NullPointerException("notifier is null or obj is null");

        LoadNotifier loadNotfier = registryUser.getLoadNotfier(obj.getClass());
        loaded.add(new DefaultPair<Object, LoadNotifier>(obj, loadNotfier));
    }

    private void callReadNotification(Object obj, ReadNotifier.ReadMode readMode) {
        ReadNotifier readNotifier = registryUser.getReadNotfier(obj.getClass());
        try {
            //noinspection unchecked
            readNotifier.onObjectRead(obj, readMode);
        } catch (Throwable ignored) {
            processException(new ReadError(ignored));
        }
    }

    private Integer getPosition(Element element) {
        if (element == null)
            throw new NullPointerException("element is null");
        String attribute = element.getAttribute(POSITION_IDENT);
        try {
            return Integer.valueOf(attribute);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    private Attr findAttribute(Element element, String attribName) {
        if (element == null || attribName == null)
            return null;
        return element.getAttributeNode(attribName);
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
                    if (ref == null && objectResolver != null)
                        //noinspection UnusedAssignment
                        ref = objectResolver.findObject(propertyValue);
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

    private void cleanUpFixUps() {
        fixUps.clear();
    }

    private void cleanUpNames() {
        namespace.clear();
    }

    private void cleanUpLoaded() {
        loaded.clear();
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
        for (Pair<Object, LoadNotifier> pair : loaded)
            //noinspection unchecked
            pair.getObjTwo().objectLoaded(pair.getObjOne());
    }

    private boolean processException(RuntimeException e) throws RuntimeException {
        if (readerExceptionHandler == null)
            throw e;
        if (!readerExceptionHandler.isHandled(e))
            throw e;
        return true;
    }


}
