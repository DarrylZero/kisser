package ru.steamengine.xmlstreaming3;

import ru.steamengine.rtti.basetypes.LoadNotifier;
import ru.steamengine.rtti.basetypes.Pair;
import ru.steamengine.rtti.basetypes.RegistryUser;
import ru.steamengine.streaming.basetypes.ObjectNamespace;
import ru.steamengine.streaming.basetypes.ReaderExceptionHandler;
import ru.steamengine.streaming.basetypes.PropertyFixup;
import ru.steamengine.streaming.defaultimplementors.DefaultObjectNamespace;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steam engine corp. in 02.06.2010 21:13:14
 *
 * @author Christopher Marlowe
 */
public class ReaderContext {

    public final ObjectNamespace namespace = new DefaultObjectNamespace();

    public final List<PropertyFixup> fixUps = new ArrayList<PropertyFixup>();

    public final List<Object> _loaded = new ArrayList<Object>();

    public final List<Pair<LoadNotifier, Object>> loaded2 = new ArrayList<Pair<LoadNotifier, Object>>();

    public Object rootObj = null;

    public RegistryUser registryUser;

    public ReaderExceptionHandler readerExceptionHandler;

}
