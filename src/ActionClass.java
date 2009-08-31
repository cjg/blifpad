/* ActionClass.java */

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

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.KeyStroke;

public abstract class ActionClass extends AbstractAction {
    private ResourceBundle myResources;

    public ActionClass(String text, int keyEvent) {
        super();
        int shortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        
        if(keyEvent != -1){
            KeyStroke shortcut = KeyStroke.getKeyStroke(keyEvent,
                                                        shortcutKeyMask);
            putValue(ACCELERATOR_KEY, shortcut);
        }
        putValue(NAME, text);
	}

    public ActionClass(String text, int keyEvent, Icon icon) {
        super(text, icon);
        int shortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        
        if(keyEvent != -1){
            KeyStroke shortcut = KeyStroke.getKeyStroke(keyEvent,
                                                        shortcutKeyMask);
            putValue(ACCELERATOR_KEY, shortcut);
        }
        putValue(NAME, text);
	}

//     public ActionClass(String text, int keyEvent, Icon icon) {
//         this(text, keyEvent);
//         putValue(
    public abstract void actionPerformed(ActionEvent e);
}
