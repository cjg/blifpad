/* Editor.java */

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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;

public class Editor extends JTextPane {

    private boolean modified = false;
    private Tab tab;
    private Document document;
    private DocumentStateListener stateListener;
    private EditorListener editorListener;    

    public Editor (Tab tab, DocumentStateListener listener, String filepath) {
        super ();
        this.tab = tab;
        stateListener = listener;
        setFont(new Font("monospaced", Font.PLAIN, 14));
		EditorKit editorKit = new StyledEditorKit()
		{
			public Document createDefaultDocument()
			{
				return new SyntaxDocument();
			}
		};

		setEditorKitForContentType("text/blif", editorKit);
		setContentType("text/blif");

        try {
            FileInputStream in = new FileInputStream(filepath);
            read(in, null);
            requestFocus();
        } catch (Exception e) {
        }
        document = getStyledDocument();
        editorListener = new EditorListener();
        document.addDocumentListener(editorListener);

    }
    
    public void setModified(boolean modified) {
        if(isModified() && modified)
            return;
        this.modified = modified;
        stateListener.stateChanged(tab);
    }

    public boolean isModified() {
        return modified;
    }
    
    public class EditorListener implements DocumentListener {
        public void changedUpdate(DocumentEvent e) {
            //setModified(true);
        }

        public void insertUpdate(DocumentEvent e) {
            setModified(true);
        }

        public void removeUpdate(DocumentEvent e) {
            setModified(true);
        }
    }
}
