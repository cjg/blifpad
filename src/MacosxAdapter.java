/* MacosxAdapter.java */

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

import com.apple.eawt.Application;
import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;
import java.awt.FileDialog;
import java.io.File;
import java.io.FilenameFilter;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class MacosxAdapter implements OsAdapter {
    private Application fApplication = Application.getApplication();
    protected MainWindow mainWindow;
    private FileDialog openDialog;
    private FileDialog saveDialog;
    private AboutBox aboutBox;
    private JMenu fileMenu;
    private JMenu editMenu;
    private PreferencesPane preferencesPane;

    public MacosxAdapter() {
        try {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        } catch (Exception e) {
            System.setProperty("com.apple.macos.useScreenMenuBar", "true");
        }

        System.setProperty("com.apple.macos.use-file-dialog-packages", "true");

        fApplication.setEnabledPreferencesMenu(true);
        fApplication.addApplicationListener(new
                                            com.apple.eawt.ApplicationAdapter() {
                public void handleOpenFile(ApplicationEvent event) {
                    //while(mainWindow == null);
                    //mainWindow.openDocument(event.getFilename());
                    new DocumentOpener(event.getFilename()).start();
                }

                public void handleAbout(ApplicationEvent e) {
                    about();
                    e.setHandled(true);
                }
                public void handlePreferences(ApplicationEvent e) {
                    preferences();
                }
                public void handleQuit(ApplicationEvent e) {
                    quit();
                }
                public void handleReOpenApplication(ApplicationEvent event) {
                    reshow();
                }
            });
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void mainWindowClosing() {
        mainWindow.setVisible(false);
    }

    public String getOpeningFilepath() {
        if(openDialog == null) {
            openDialog = new FileDialog(mainWindow, "BlifPad", 
                                        FileDialog.LOAD);
            openDialog.setFilenameFilter(new BlifFilter());
        }
        openDialog.show();
        if(openDialog.getFile() == null)
            return null;
        return openDialog.getDirectory() + openDialog.getFile();
    }

    public String getSavingFilepath(){
        if(saveDialog == null) {
            saveDialog = new FileDialog(mainWindow, "BlifPad", 
                                        FileDialog.SAVE);
            saveDialog.setFilenameFilter(new BlifFilter());
        }
        saveDialog.show();
        if(saveDialog.getFile() == null)
            return null;
        return saveDialog.getDirectory() + saveDialog.getFile();
    }

    public JMenuBar getMenuBar(RecentFileMenu recentf) {
        JMenuBar menuBar = new JMenuBar();
        fileMenu = new JMenu(ResourceLoader._("File"));
        fileMenu.add(new NewAction(mainWindow));
        fileMenu.add(new OpenAction(mainWindow));
        fileMenu.add(recentf);
        editMenu = new JMenu(ResourceLoader._("Edit"));
        JMenu helpMenu = new JMenu(ResourceLoader._("Help"));
        helpMenu.add(new LicenseAction(mainWindow));
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(new CommandsMenu(mainWindow));
        menuBar.add(helpMenu);
        return menuBar;
    }

    public void updateFileMenu(Action[] actions) {
        if(fileMenu.getItemCount()>4){
            fileMenu.remove(3);
            fileMenu.remove(3);
            fileMenu.remove(3);
        }
        fileMenu.add(actions[0]);
        fileMenu.add(actions[1]);
        fileMenu.add(actions[2]);
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

    protected void about() {
        if(aboutBox == null)
            aboutBox = new AboutBox(mainWindow);
        aboutBox.setVisible(true);
    }

    protected void preferences() {
        if(preferencesPane == null)
            preferencesPane = new PreferencesPane(mainWindow);
        preferencesPane.setVisible(true);
    }

    protected void quit() {
        mainWindow.quit();
    }

    protected void reshow() {
        mainWindow.setVisible(true);
    }

    public class BlifFilter implements FilenameFilter{
        public boolean accept(File dir, String name) {
            if(name.endsWith(".blif"))
                return true;
            return false;
        }
    }

    public class DocumentOpener implements Runnable {
        private String filepath;
        private Thread thread;
        
        public DocumentOpener(String filepath) {
            this.filepath = filepath;
        }
        
        public void start() {
                thread = new Thread(this);
                thread.start();
        }
        
        public void run() {
            while(mainWindow == null);
            mainWindow.openDocument(filepath);
        }
    }
}
