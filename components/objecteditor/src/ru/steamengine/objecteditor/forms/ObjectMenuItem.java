package ru.steamengine.objecteditor.forms;

import javax.swing.*;

/**
 * Created by Steam engine corp. in 18.04.2010 15:38:14
 *
 * @author Christopher Marlowe
 */
public class ObjectMenuItem extends JMenuItem {

    private final Object obj;

    public ObjectMenuItem(Object obj) {
        this.obj = obj;
    }
}
