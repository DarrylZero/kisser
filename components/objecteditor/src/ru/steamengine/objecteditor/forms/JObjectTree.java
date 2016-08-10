/*
 * Created by JFormDesigner on Fri Apr 16 23:34:34 MSD 2010
 */

package ru.steamengine.objecteditor.forms;

import ru.steamengine.properties.basetypes.ObjectEditor;
import ru.steamengine.properties.basetypes.ObjectEditorItem;
import ru.steamengine.properties.basetypes.ObjectEditorsFactory;
import ru.steamengine.rtti.basetypes.ObjectProcessor;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * @author Christopher Marlowe
 */
public class JObjectTree extends JDialog implements TreeModelListener {

    private ObjectEditorsFactory objectEditorsFactory;

    private ObjectProcessor objectProcessor;

    private Object root;

    private final ArrayList<ObjectTreeListener> listeners = new ArrayList<ObjectTreeListener>();

    private DefaultTreeModel treeModel = new DefaultTreeModel(null, true);

    {
        treeModel.addTreeModelListener(this);
    }

    public JObjectTree(Frame owner) {
        super(owner);
        initComponents();
    }

    public JObjectTree(Dialog owner) {
        super(owner);
        initComponents();
    }

    @Override
    public void dispose() {
        listeners.clear();
        treeModel.removeTreeModelListener(this);
        super.dispose();
    }

    public void addListener(ObjectTreeListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeListener(ObjectTreeListener listener) {
        while (listeners.remove(listener)) ;
    }

    public ObjectProcessor getObjectProcessor() {
        return objectProcessor;
    }

    public void setObjectProcessor(ObjectProcessor objectProcessor) {
        this.objectProcessor = objectProcessor;
        updateModel();
    }

    public ObjectEditorsFactory getObjectEditorsFactory() {
        return objectEditorsFactory;
    }

    public void setObjectEditorsFactory(ObjectEditorsFactory objectEditorsFactory) {
        this.objectEditorsFactory = objectEditorsFactory;
    }

    private void objectSelected(Object o) {
        for (int i = listeners.size(); i >= 0; i--) {
            try {
                listeners.get(i).selected(o);
            } catch (Throwable throwable) {
            }
        }
    }

    private void updateModel() {
        updateForObject(treeModel, root, root, objectProcessor);
        treeModel.reload();
    }

    private void updateForObject(DefaultTreeModel aModel, Object o, Object root, ObjectProcessor op) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(o, true);
        if (o == root)
            aModel.setRoot(rootNode);

        if (op != null)
            for (Object subObj : op.getObjectChildren(o))
                updateForObject(aModel, op, root, op);
    }

    @Override
    public void treeNodesChanged(TreeModelEvent e) {
        updateModel();
    }

    @Override
    public void treeNodesInserted(TreeModelEvent e) {

    }

    @Override
    public void treeNodesRemoved(TreeModelEvent e) {

    }

    @Override
    public void treeStructureChanged(TreeModelEvent e) {

    }

    private void mouseClicked(MouseEvent e) {
        int button = e.getButton();
        switch (button) {
            case 1:
                //  Левая кнопка
                Object o = underlayingObject(e);
                objectSelected(o);
                break;

            case 3:
                //  Правая кнопка
                checkPopup(e);
                break;

            default:
                break;

        }
    }

    private Object underlayingObject(MouseEvent e) {
        Point point = e.getPoint();
        TreePath path = ((JTree) e.getSource()).getClosestPathForLocation(point.x, point.y);
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        return treeNode.getUserObject();
    }

    private void checkPopup(MouseEvent e) {
//        if (e.isPopupTrigger()) {
//        }
        fillMenu(e);
    }

    private void fillMenu(MouseEvent e) {
        Object o = underlayingObject(e);
        ObjectEditor objectEditor = objectEditorsFactory.getObjectEditor(o);
        if (objectEditor == null)
            return;

        JPopupMenu cm = new JPopupMenu("");
        for (final ObjectEditorItem item : objectEditor.getItems()) {
            ObjectMenuItem menuItem = new ObjectMenuItem(o);
            menuItem.setText(item.getCaption());
            menuItem.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //(ObjectMenuItem) e.getSource()
                    item.execute();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });

            cm.add(menuItem);
        }
        cm.show((JTree) e.getSource(), e.getX(), e.getY());
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        scrollPane1 = new JScrollPane();
        tree1 = new JTree();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(new CardLayout());

        //======== scrollPane1 ========
        {

            //---- tree1 ----
            tree1.setShowsRootHandles(true);
            tree1.setModel(new DefaultTreeModel(
                    new DefaultMutableTreeNode("(root)") {
                        {
                            add(new DefaultMutableTreeNode("1"));
                            add(new DefaultMutableTreeNode("2"));
                            add(new DefaultMutableTreeNode("3"));
                            add(new DefaultMutableTreeNode("4"));
                            add(new DefaultMutableTreeNode("5"));
                        }
                    }));
            tree1.setRootVisible(false);
            tree1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JObjectTree.this.mouseClicked(e);
                }
            });
            scrollPane1.setViewportView(tree1);
        }
        contentPane.add(scrollPane1, "card1");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JScrollPane scrollPane1;
    private JTree tree1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
