/* ConsolePane.java */

/* BlifPad -- A simple editor for Blif files
 *
 * Copyright (C) 2006 - 2007
 *     Giuseppe Coviello <cjg@cruxppc.org>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

public class ConsolePane extends JScrollPane {
    protected JEditorPane editorPane;
    protected JPopupMenu popupMenu;
    protected static ImageIcon copyIcon = new ImageIcon(ResourceLoader.getUrl("images/copy.png"));
    protected static ImageIcon selectAllIcon = new ImageIcon(ResourceLoader.getUrl("images/select_all.png"));

    public ConsolePane() {
        super();
        editorPane = new JEditorPane("text/html", 
                                     "<body bgcolor='black'></body>");
        editorPane.setEditable(false);
        getViewport().add(editorPane);
        popupMenu = new JPopupMenu();
        popupMenu.add(new SelectAllAction());
        popupMenu.add(new CopyAction());
        editorPane.addMouseListener(new ConsoleListener());
    }

    public void setText(String text) {
        editorPane.setText(text);
        try {
            editorPane.moveCaretPosition(0);
        } catch(Exception e) {}
    }

    public class ConsoleListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3 ||
                e.getMouseModifiersText(e.getModifiers()).equals("Ctrl+Button1"))
                
                popupMenu.show(e.getComponent() , e.getPoint().x, e.getPoint().y);
        }
    }

    public class SelectAllAction extends ActionClass {
        public SelectAllAction() {
            super(ResourceLoader._("Select All"), KeyEvent.VK_A, selectAllIcon);
        }

        public void actionPerformed(ActionEvent e) {
            editorPane.selectAll();
        }
    }

    public class CopyAction extends ActionClass {
        public CopyAction() {
            super(ResourceLoader._("Copy"), KeyEvent.VK_C, copyIcon);
        }

        public void actionPerformed(ActionEvent e) {
            editorPane.copy();
        }
    }

}
