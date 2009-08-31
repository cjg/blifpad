/* DefaultAdapter.java */

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
import java.io.File;
import java.io.FilenameFilter;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

public class DefaultAdapter implements OsAdapter {
    private MainWindow mainWindow;
    private JFileChooser openDialog;
    private JFileChooser saveDialog;
    private JMenu fileMenu;
    private JMenu editMenu;
    private QuitAction quitAction;

    public DefaultAdapter() {
        System.setProperty("java.awt.Window.locationByPlatform", "true");
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
        }
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void mainWindowClosing() {
        mainWindow.quit();
    }

    public String getOpeningFilepath() {
        if(openDialog == null) {
            openDialog = new JFileChooser();
            openDialog.setFileFilter(new BlifFilter());
        }
        int returnVal = openDialog.showOpenDialog(mainWindow);
        if(returnVal != JFileChooser.APPROVE_OPTION)
            return null;
        return openDialog.getSelectedFile().getPath();
    }

    public String getSavingFilepath(){
        if(saveDialog == null) {
            saveDialog = new JFileChooser();
            saveDialog.setFileFilter(new BlifFilter());
        }
        int returnVal = saveDialog.showSaveDialog(mainWindow);
        if(returnVal != JFileChooser.APPROVE_OPTION)
            return null;
        return saveDialog.getSelectedFile().getPath();
    }

    public JMenuBar getMenuBar(RecentFileMenu recentf) {
        JMenuBar menuBar = new JMenuBar();
        fileMenu = new JMenu(ResourceLoader._("File"));
        fileMenu.add(new NewAction(mainWindow));
        fileMenu.add(new OpenAction(mainWindow));
        fileMenu.add(recentf);
        quitAction = new QuitAction(mainWindow);
        fileMenu.add(quitAction);
        editMenu = new JMenu(ResourceLoader._("Edit"));
        JMenu optionsMenu = new JMenu(ResourceLoader._("Options"));
        optionsMenu.add(new PreferencesAction(mainWindow));
        JMenu helpMenu = new JMenu(ResourceLoader._("Help"));
        helpMenu.add(new AboutAction(mainWindow));
        helpMenu.add(new LicenseAction(mainWindow));
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(new CommandsMenu(mainWindow));
        menuBar.add(optionsMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }

    public void updateFileMenu(Action[] actions) {
        if(fileMenu.getItemCount()>4){
            fileMenu.remove(3);
            fileMenu.remove(3);
            fileMenu.remove(3);
        }
        fileMenu.insert(actions[0], 3);
        fileMenu.insert(actions[1], 4);
        fileMenu.insert(actions[2], 5);
    }

    public void updateEditMenu(Action[] actions) {
        if(editMenu.getItemCount ()>0) {
            editMenu.remove(0);
            editMenu.remove(0);
            editMenu.remove(0);
            editMenu.remove(0);
            editMenu.remove(0);
            editMenu.remove(0);
        }
        editMenu.add(actions[0]);
        editMenu.add(actions[1]);
        editMenu.addSeparator();
        editMenu.add(actions[2]);
        editMenu.add(actions[3]);
        editMenu.add(actions[4]);
    }

    public class BlifFilter extends FileFilter {
        public boolean accept(File f) {
            if(f != null) {
                if(f.isDirectory()) {
                    return true;
                }
                String extension = getExtension(f);
                if(extension != null && extension.equals("blif")) {
                    return true;
                }
            }
            return false;
        }

        public String getExtension(File f) {
            if(f != null) {
                String filename = f.getName();
                int i = filename.lastIndexOf('.');
                if(i>0 && i<filename.length()-1) {
                    return filename.substring(i+1).toLowerCase();
                }
            }
            return null;
        }

        public String getDescription() {
            return "Berkeley Logic Interchange Format";
        }
    }
}
