package ru.steamengine.objecteditor.mainclasses;

import ru.steamengine.objecteditor.forms.JObjectTree;
import ru.steamengine.objecteditor.forms.ObjectTreeListener;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Steam engine corp. in 16.04.2010 23:39:22
 *
 * @author Christopher Marlowe
 */
public class TestClass {


    public static void main(String[] args) {
        JObjectTree tree = new JObjectTree((Frame) null);
        tree.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        tree.addListener(new ObjectTreeListener() {
            @Override
            public void selected(Object o) {
                System.out.println(o);
            }
        });
        tree.setVisible(true);
    }


}
