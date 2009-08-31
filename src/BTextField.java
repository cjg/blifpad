/* BTextField.java */

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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

public class BTextField extends JTextField {
    private JPopupMenu popupMenu;

    public BTextField(int cols) {
        super(cols);
        popupMenu = new JPopupMenu();
        popupMenu.add(new BTextFieldCutAction(this));
        popupMenu.add(new BTextFieldCopyAction(this));
        popupMenu.add(new BTextFieldPasteAction(this));
        addMouseListener(new BTextFieldMouseListener());
    }

    public class BTextFieldMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3 ||
                e.getMouseModifiersText(e.getModifiers()).equals("Ctrl+Button1")) {
                popupMenu.show(e.getComponent() , e.getX(), e.getY());
            }
        }
    }

    public class BTextFieldCutAction extends ActionClass {
        private BTextField textField;

        public BTextFieldCutAction(BTextField textField) {
            super(ResourceLoader._("Cut"), KeyEvent.VK_X);
            this.textField = textField;
        }

        public void actionPerformed(ActionEvent e) {
            cut();
        }
    }

    public class BTextFieldCopyAction extends ActionClass {
        private BTextField textField;

        public BTextFieldCopyAction(BTextField textField) {
            super(ResourceLoader._("Copy"), KeyEvent.VK_C);
            this.textField = textField;
        }

        public void actionPerformed(ActionEvent e) {
            copy();
        }
    }

    public class BTextFieldPasteAction extends ActionClass {
        private BTextField textField;

        public BTextFieldPasteAction(BTextField textField) {
            super(ResourceLoader._("Paste"), KeyEvent.VK_V);
            this.textField = textField;
        }

        public void actionPerformed(ActionEvent e) {
            paste();
        }
    }
}
