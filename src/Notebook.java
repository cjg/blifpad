/* Notebook.java */

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

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Notebook extends JTabbedPane {
    private MainWindow mainWindow;
    private DocumentStateListener documentStateListener;

    public Notebook(MainWindow mainWindow) {
        super();
        this.mainWindow = mainWindow;
        addChangeListener(new NotebookListener());
        documentStateListener = new DocumentStateListener(this);
        newDocument();
    }

    public void newDocument() {
        Tab tab = new Tab(this, null, documentStateListener, 
                          mainWindow.getOsAdapter());
        addTab(makeTitle(tab, true), null, tab);
        setSelectedComponent(tab);
    }

    public void openDocument(String filepath) {
        Tab tab = new Tab(this, filepath, documentStateListener,
                          mainWindow.getOsAdapter());
        addTab(makeTitle(tab, true), null, tab);
        setSelectedComponent(tab);
    }

    public void removeTab(Tab tab) {
        remove(tab);
        if(getTabCount()==0)
            newDocument();
        else
            updateMainWindow();
    }

    public Tab getSelectedTab() {
        return (Tab) getSelectedComponent();
    }

    public String getSelectedTitle() {
        return getSelectedTab().getTitle();
    }
    
    public void updateState(Tab tab) {
        setTitleAt(indexOfComponent(tab), makeTitle(tab, true));
        mainWindow.updateTitle(makeTitle(tab, false));
    }

    public boolean requestQuit() {
        int i = getTabCount();
        while(i-->=0)
            if(!getSelectedTab().close())
                return false;
        return true;
    }

    private String makeTitle(Tab tab, boolean cut) {
        String title = tab.getTitle();
        if(cut && title.length()>23)
            title = title.substring(0, 10) + "..." + title.substring(title.length()-10);
        title = (tab.isModified() ? "* " : "") + title;
        return title;
    }

    private void updateMainWindow() {
        try {
            mainWindow.updateToolbar((Tab)getSelectedComponent());
            mainWindow.updateTitle(makeTitle((Tab)getSelectedComponent(),
                                             false));
            mainWindow.updateSimulationTab();
        } catch(Exception e){}
    }

    public class NotebookListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            updateMainWindow();
        }
    }
}
