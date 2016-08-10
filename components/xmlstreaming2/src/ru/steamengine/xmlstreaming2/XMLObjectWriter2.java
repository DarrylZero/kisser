/**
 * Created by Steam engine corp in 08.07.2009 23:33:54
 *
 * @author Christopher Marlowe
 */

package ru.steamengine.xmlstreaming2;

import ru.steamengine.easyxml.basetypes.Document;
import ru.steamengine.easyxml.basetypes.Node;
import ru.steamengine.easyxml.basetypes.XMLFormatter;
import ru.steamengine.easyxml.formatters.PrettyFormatter;
import ru.steamengine.rtti.basetypes.*;
import static ru.steamengine.rtti.defaultimplementors.utils.CommonStreamingUtils.*;
import ru.steamengine.streaming.basetypes.*;
import ru.steamengine.streaming.defaultimplementors.DefaultObjectNamespace;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class XMLObjectWriter2 implements ObjectWriter, StreamInit {


    private static final Object[] NO_OBJECTS = new Object[0];

    private boolean inited = false;

    private Node rootElement;

    private Object root;

    private RegistryUser registryUser;

    private ObjectResolver objectResolver;  

    private final ObjectNamespace namespace = new DefaultObjectNamespace();

    public XMLObjectWriter2() {
    }

    public XMLObjectWriter2(RegistryUser registryUser) {
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

    @Override
    public void write(final Object root, OutputStream stream) throws WriteError {
        checkInited();
        try {
            this.root = root;
            final Node localRoot;
            registryUser.doTransaction(new RegistryUser.Transaction() {
                @Override
                public void inTransaction() {
                    startWriting();
                    writeObject(-1, root, "");

                }

            });

            if (rootElement != null) {
                XMLFormatter outputter = new PrettyFormatter();
                Document document = new Document(rootElement);
                outputter.output(document, stream);
            }

        } catch (Exception e) {
            throw new WriteError(e);
        }
    }

    @Override
    public void write(Object root, ObjectResolver objectResolver, OutputStream stream) throws WriteError {
        throw new UnsupportedOperationException();
    }

    private void checkInited() {
        if (!inited)
            throw new IllegalArgumentException("object is not inited");
    }

    public Object getRoot() {
        return root;
    }

    private void writeObjectProps(final Object obj,
                                  final List<Node> propList,
                                  final String propPath) {

        RTTIEntries rtti = registryUser.getRTTI(obj.getClass());
        for (RTTIEntry entry : rtti.getProperties()) {
            PropertyTypeIdent ident = getPropTypeIdent(entry, registryUser);
            Class propClass = entry.getPropType();
            String propertyName = entry.getPropName();
            String newPath = getPropPath(propPath, propertyName);

            switch (ident) {
                case value:
                    boolean valStored = entry.isStored(obj) && !entry.isDefault(obj);
                    if (valStored) {
                        Node valElement = new Node(XMLReadWriteConsts2.PROP_IDENT);
                        try {
                            final Object val = entry.getVal(obj);
                            PropertyRecoder r = registryUser.getSimpleType(propClass);
                            if (r == null)
                                throw new IllegalArgumentException("recoder is null");

                            if (val == null && (r instanceof PropertyRecoderEx) && !((PropertyRecoderEx) r).isNullable())
                                throw new IllegalArgumentException("value for " +
                                        propClass.getName() +
                                        " can not be null");

                            if (val != null) {
                                @SuppressWarnings({"unchecked"})
                                byte[] bytes = r.getValue(val);
                                String valValue = new String(bytes, UsedCharset.CHARSET);
                                valElement.attribs.setItem(XMLReadWriteConsts2.PATH_IDENT, newPath);
                                valElement.attribs.setItem(XMLReadWriteConsts2.VALUE_IDENT, valValue);
                            } else {
                                valElement.attribs.setItem(XMLReadWriteConsts2.PATH_IDENT, newPath);
                            }


                        } finally {
                            propList.add(valElement);
                        }
                    }


                    break;

                case nested:
                    Object nestedObject = entry.getVal(obj);
                    if (nestedObject != null) {
                        writeObjectProps(nestedObject, propList, newPath);
                    }
                    break;

                case reference:
                    boolean refStored = entry.isStored(obj);
                    if (refStored) {
                        final Node refElement = new Node(XMLReadWriteConsts2.PROP_IDENT);
                        propList.add(refElement);
                        Object ref = entry.getVal(obj);
                        if (ref != null) {
                            String classIdent = registryUser.getClassIdent(ref.getClass());
                            String refValue = getObjectName(ref, classIdent);
                            refElement.attribs.setItem(XMLReadWriteConsts2.PATH_IDENT, newPath);
                            refElement.attribs.setItem(XMLReadWriteConsts2.VALUE_IDENT, refValue);
                        } else {
                            refElement.attribs.setItem(XMLReadWriteConsts2.PATH_IDENT, newPath);
                        }
                    }
                    break;

                default:
                    throw new IllegalArgumentException("");

            }


        }


        for (ListSlot slot : rtti.getLists()) {
            String propertyName = slot.getPropertyName();
            String newPath = getPropPath(propPath, propertyName);

            @SuppressWarnings({"unchecked"})
            Node listElements = new Node(XMLReadWriteConsts2.PROP_IDENT);
            listElements.attribs.setItem(XMLReadWriteConsts2.PATH_IDENT, newPath);
            PropertyRecoder st = registryUser.getSimpleType(slot.getItemClass());
            int i = 0;
            if (st != null) {
                for (Object o : getListIterator(slot, obj)) {
                    if ((o == null) && (st instanceof PropertyRecoderEx) &&
                            !((PropertyRecoderEx) st).isNullable())
                        throw new WriteError("value can not be null");

                    Node element = new Node(XMLReadWriteConsts2.ITEM_IDENT);
                    listElements.addNode(element);
                    if (o != null) {
                        //noinspection unchecked
                        byte[] bytes = st.getValue(o);
                        String valAsString = new String(bytes, UsedCharset.CHARSET);
                        element.attribs.setItem(XMLReadWriteConsts2.VALUE_IDENT, valAsString);
                    } else {
                    }
                    i++;
                }

            } else {
                for (Object o : getListIterator(slot, obj)) {
                    Node listedElement = writeListedObject(o, "");
                    listElements.addNode(listedElement);
                    i++;
                }
            }
            if (i > 0)
                propList.add(listElements);
        }
    }

    private Node writeObject(final int position,
                             final Object obj,
                             final String propPath) {
        if (propPath == null)
            throw new NullPointerException("propPath is null");

        Node element = new Node(XMLReadWriteConsts2.OBJECT_IDENT);
        String classIdent = registryUser.getClassIdent(obj.getClass());
        element.attribs.setItem(XMLReadWriteConsts2.CLASS_IDENT, classIdent);
        element.attribs.setItem(XMLReadWriteConsts2.NAME_IDENT, getObjectName(obj, classIdent));
        if (position >= 0)
            element.attribs.setItem(XMLReadWriteConsts2.POSITION_IDENT, String.valueOf(position));
        if (rootElement == null)
            rootElement = element;

        List<Node> elements = new ArrayList<Node>();
        writeObjectProps(obj, elements, propPath);

        ObjectProcessor processor = registryUser.getObjProcessor(obj.getClass());
        CountedIterator iterator = getObjectIterator(processor, obj);
        while (iterator.hasNext()) {
            int index = iterator.nextIndex();
            Object child = iterator.next();
            Node el = writeObject(index, child, propPath);
            elements.add(el);
        }


        for (Node subNode : elements)
            element.addNode(subNode);
        return element;
    }

    /**
     * @param obj    an object
     * @param prefix prefix
     * @return is what is written to the stream
     */
    private String getObjectName(Object obj, String prefix) {
        String name;
        ObjectNameProcessor nameProcessor = registryUser.getNameProcessor(obj.getClass());
        if (objectResolver != null && ((name = objectResolver.getObjectName(obj)) != null)) {
            return namespace.setObjectName(obj, name);
        } else if (nameProcessor == null) {
            return namespace.getObjectName(obj, prefix);
        } else if ((name = nameProcessor.getName(obj)) == null) {
            return namespace.getObjectName(obj, prefix);
        } else if (!namespace.isNameUnique(obj, name)) {
            return namespace.getObjectName(obj, prefix);
        } else {
            return namespace.setObjectName(obj, name);
        }
    }


    private Node writeListedObject(final Object obj,
                                   final String propPath) {
        if (propPath == null)
            throw new NullPointerException("propPath is null");

        Node element = new Node(XMLReadWriteConsts2.ITEM_IDENT);
        List<Node> elements = new ArrayList<Node>();
        writeObjectProps(obj, elements, propPath);
        for (Node subNode : elements)
            element.addNode(subNode);
        return element;
    }

    private void startWriting() {
        rootElement = null;
        namespace.clear();
    }

}