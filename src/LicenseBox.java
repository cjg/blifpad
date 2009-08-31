/* LicenseBox.java */

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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;

public class LicenseBox extends SFrame implements ActionListener {
    protected static int boxWidth = 500;
    protected static int boxHeight = 330;

    public LicenseBox(Component c) {
        super(ResourceLoader._("BlifPad License"), c);
        SymWindow aSymWindow = new SymWindow();
    
        this.addWindowListener(aSymWindow);    
        JTextPane textPane = new JTextPane();
        textPane.setText(
"BlifPad -- A simple editor for Blif files\n" +
"\n" +
"Copyright (C) 2006 - 2007\n" +
"    Giuseppe Coviello <cjg@cruxppc.org>\n"+
"\n"+
"This program is free software; you can redistribute it and/or modify\n"+
"it under the terms of the GNU General Public License as published by\n"+
"the Free Software Foundation; either version 2 of the License, or\n"+
"(at your option) any later version.\n"+
"\n"+
"This program is distributed in the hope that it will be useful,\n"+
"but WITHOUT ANY WARRANTY; without even the implied warranty of\n"+
"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n"+
"GNU General Public License for more details.\n"+
"\n"+
"You should have received a copy of the GNU General Public License\n"+
"along with this program; if not, write to the Free Software\n"+
"Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.");
        textPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane();
        JViewport viewport = scrollPane.getViewport();
        viewport.add(textPane);
        getContentPane().add(scrollPane);
        this.pack();
        this.setSize(boxWidth, boxHeight);
    }

    class SymWindow extends java.awt.event.WindowAdapter {
        public void windowClosing(java.awt.event.WindowEvent event) {
            setVisible(false);
        }
    }
    
    public void actionPerformed(ActionEvent newEvent) {
        setVisible(false);
    } 
}
