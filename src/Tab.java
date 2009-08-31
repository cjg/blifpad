/* Tab.java */

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

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class Tab extends JScrollPane {
    private String filepath;
    protected Editor editor;
    private Notebook notebook;
    private OsAdapter osAdapter;
    private SaveAction saveAction;
    private SaveAsAction saveAsAction;
    private CloseAction closeAction;
    private CutAction cutAction;
    private CopyAction copyAction;
    private PasteAction pasteAction;
    private Action[] fileActions;
    private Action[] editActions;
    protected JPopupMenu popupMenu;
    protected UndoManager undo = new UndoManager();
    protected UndoAction undoAction;
    protected RedoAction redoAction;
    protected static ResourceLoader resourceLoader = new ResourceLoader();
    protected static ImageIcon saveIcon = new ImageIcon(resourceLoader.getUrl("images/save.png"));
    protected static ImageIcon saveAsIcon = new ImageIcon(resourceLoader.getUrl("images/save_as.png"));
    protected static ImageIcon closeIcon = new ImageIcon(resourceLoader.getUrl("images/close.png"));
    protected static ImageIcon undoIcon = new ImageIcon(resourceLoader.getUrl("images/undo.png"));
    protected static ImageIcon redoIcon = new ImageIcon(resourceLoader.getUrl("images/redo.png"));
    protected static ImageIcon cutIcon = new ImageIcon(resourceLoader.getUrl("images/cut.png"));
    protected static ImageIcon copyIcon = new ImageIcon(resourceLoader.getUrl("images/copy.png"));
    protected static ImageIcon pasteIcon = new ImageIcon(resourceLoader.getUrl("images/paste.png"));

    public Tab(Notebook notebook, String filepath, 
               DocumentStateListener listener, OsAdapter osAdapter) {
        super(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
              JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.notebook = notebook;
        this.filepath = filepath;
        this.osAdapter = osAdapter;
        editor = new Editor(this, listener, filepath);
        saveAction = new SaveAction();
        saveAsAction = new SaveAsAction();
        closeAction = new CloseAction();
        undoAction = new UndoAction();
        redoAction = new RedoAction();
        cutAction = new CutAction();
        copyAction = new CopyAction();
        pasteAction = new PasteAction();
        popupMenu = new JPopupMenu();
        popupMenu.add(undoAction);
        popupMenu.add(redoAction);
        popupMenu.add(cutAction);
        popupMenu.add(copyAction);
        popupMenu.add(pasteAction);
        fileActions = new Action[3];
        fileActions[0] = saveAction;
        fileActions[1] = saveAsAction;
        fileActions[2] = closeAction;
        editActions = new Action[5];
        editActions[0] = undoAction;
        editActions[1] = redoAction;
        editActions[2] = cutAction;
        editActions[3] = copyAction;
        editActions[4] = pasteAction;
        
        editor.getDocument().addUndoableEditListener(new MyUndoableEditListener());
        JViewport viewport = getViewport();
        viewport.add(editor);
        editor.addMouseListener(new MiceListener());
        editor.requestFocus();
    }

    public String getTitle() {
        if(filepath == null)
            return ResourceLoader._("No Name");
        return filepath;
    }

    public boolean isModified() {
        return editor.isModified();
    }

    public String getText() {
        return editor.getText();
    }

    public boolean requestSave(String message) {
        int response = JOptionPane.showConfirmDialog(null, message, 
                                                     "BlifPad", 
                                                     JOptionPane.YES_NO_OPTION);
        if(response == 0)
            save();
        else
            return false;
        return !isModified();
    }

    private void save() {
        if(filepath == null) {
            saveAs();
            return;
        }
        String text = editor.getText();
        if(!text.endsWith("\n"))
            text = text + "\n";
        try {
            FileOutputStream fos = new FileOutputStream(filepath);
            fos.write(text.getBytes());
            fos.close();
            editor.setModified(true);
            editor.setModified(false);
        } catch(Exception e) {}
    }

    private void saveAs() {
        filepath = osAdapter.getSavingFilepath();
        if(filepath != null){
            if(!filepath.endsWith(".blif"))
                filepath = filepath + ".blif";
            save();
        }
    }

    public boolean close() {
        if(editor.isModified()) {
            Object [] arguments = {
                getTitle()
            };
            String message = MessageFormat.format (ResourceLoader._("Close {0} without saving?"), arguments);
        
            int response = JOptionPane.showConfirmDialog(null, message, 
                                                         "BlifPad", 
                                                         JOptionPane.YES_NO_OPTION);
            if(response == 0)
                notebook.removeTab(this);
            else
                return false;
        } else 
            notebook.removeTab(this);
        return true;
    }
        
    public Action[] getFileActions() {
        return fileActions;
    }

    public Action[] getEditActions() {
        return editActions;
    }
    
    public Action[] getActions() {
        Action[] actions = new Action[9];
        actions[0] = new SaveAction();
        actions[1] = new SaveAsAction();
        actions[2] = new CloseAction();
        actions[3] = undoAction;
        actions[4] = redoAction;
        actions[5] = new CutAction();
        actions[6] = new CopyAction();
        actions[7] = new PasteAction();

        return actions;
    }

    public class MiceListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3 ||
                e.getMouseModifiersText(e.getModifiers()).equals("Ctrl+Button1"))
                
                popupMenu.show(e.getComponent() , e.getPoint().x, e.getPoint().y);
        }
    }

    public class SaveAction extends ActionClass {
        public SaveAction() {
            super(ResourceLoader._("Save"), KeyEvent.VK_S, saveIcon);
        }

        public void actionPerformed(ActionEvent e) {
            save();
        }
    }

    public class SaveAsAction extends ActionClass {
        public SaveAsAction() {
            super(ResourceLoader._("Save as ..."), -1, saveAsIcon);
        }

        public void actionPerformed(ActionEvent e) {
            saveAs();
        }
    }

    public class CloseAction extends ActionClass {
        public CloseAction() {
            super(ResourceLoader._("Close"), -1, closeIcon);
        }

        public void actionPerformed(ActionEvent e) {
            close();
        }
    }

    public class UndoAction extends ActionClass {
        public UndoAction() {
            super(ResourceLoader._("Undo"), KeyEvent.VK_Z, undoIcon);
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undo.undo();
                editor.requestFocus();
            } catch (CannotUndoException ex) {
                System.out.println("Unable to undo: " + ex);
                ex.printStackTrace();
            }
            updateUndoState();
            redoAction.updateRedoState();
        }

        protected void updateUndoState() {
            if (undo.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, undo.getUndoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Undo");
            }
        }
    }

    public class RedoAction extends ActionClass {
        public RedoAction() {
            super(ResourceLoader._("Redo"), KeyEvent.VK_Y, redoIcon);
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undo.redo();
                editor.requestFocus();
            } catch (CannotRedoException ex) {
                System.out.println("Unable to redo: " + ex);
                ex.printStackTrace();
            }
            updateRedoState();
            undoAction.updateUndoState();
        }

        protected void updateRedoState() {
            if (undo.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, undo.getRedoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Redo");
            }
        }
    }

    public class CutAction extends ActionClass {
        public CutAction() {
            super(ResourceLoader._("Cut"), KeyEvent.VK_X, cutIcon);
        }

        public void actionPerformed(ActionEvent e) {
            editor.cut();
            editor.requestFocus();
        }
    }

    public class CopyAction extends ActionClass {
        public CopyAction() {
            super(ResourceLoader._("Copy"), KeyEvent.VK_C, copyIcon);
        }

        public void actionPerformed(ActionEvent e) {
            editor.copy();
            editor.requestFocus();
        }
    }

    public class PasteAction extends ActionClass {
        public PasteAction() {
            super(ResourceLoader._("Paste"), KeyEvent.VK_V, pasteIcon);
        }

        public void actionPerformed(ActionEvent e) {
            editor.paste();
            editor.requestFocus();
        }
    }

    protected class MyUndoableEditListener
                    implements UndoableEditListener {
        public void undoableEditHappened(UndoableEditEvent e) {
            //Remember the edit and update the menus.
            if(e.getEdit().getPresentationName().equals("aggiunta")) {
                undo.addEdit(e.getEdit());
                undoAction.updateUndoState();
                redoAction.updateRedoState();
            }
        }
    }

}

