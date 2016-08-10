/*
 * Created by JFormDesigner on Sat Apr 17 13:16:07 MSD 2010
 */

package ru.steamengine.objecteditor.forms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * @author Cristofer Marlowe
 */
public class ObjectInspector extends JFrame {
    public ObjectInspector() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        scrollPane = new JScrollPane();
        table1 = new JTable();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== scrollPane ========
        {
            scrollPane.setToolTipText("tghhtf");
            scrollPane.setEnabled(false);
            scrollPane.setViewportBorder(null);

            //---- table1 ----
            table1.setModel(new DefaultTableModel(
                new Object[][] {
                    {null, null},
                    {null, null},
                },
                new String[] {
                    null, null
                }
            ) {
                Class[] columnTypes = new Class[] {
                    String.class, Object.class
                };
                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    return columnTypes[columnIndex];
                }
            });
            scrollPane.setViewportView(table1);
        }
        contentPane.add(scrollPane);
        scrollPane.setBounds(10, 5, 405, 400);

        { // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JScrollPane scrollPane;
    private JTable table1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
