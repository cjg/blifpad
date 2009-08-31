/* MainWindow.java */

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
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar.Separator;
import javax.swing.JToolBar;

public class MainWindow extends SFrame {
    private Notebook notebook;
    private SisPane sisPane;
    private JToolBar toolbar;
    private JSplitPane splitPane;
    private SimplifyTab simplify;
    private DecompTab decomp;
    private MinimizeTab minimize;
    private StateAssignTab stateAssign;
    private StateMinimizeTab stateMinimize;
    private BuildNetworkTab buildNetwork;
    private RecentFileMenu recentf;
    final protected OsAdapter osAdapter;
    private static MyPreferences myPreferences = new MyPreferences();

    public MainWindow(final OsAdapter osAdapter) {
        super("", myPreferences.getLocation());
        this.osAdapter = osAdapter;
        this.osAdapter.setMainWindow(this);
        recentf = new RecentFileMenu(this);
        setJMenuBar(osAdapter.getMenuBar(recentf));
        setDefaultCloseOperation(0);
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    osAdapter.mainWindowClosing();
                }
            });
        buildLayout();
        sisPane.setSelectedIndex(myPreferences.getSisSelectedTab());
        splitPane.setDividerLocation(myPreferences.getDividerLocation(splitPane));
        setSize(myPreferences.getSize());
        setVisible(true);
    }

    public OsAdapter getOsAdapter() {
        return osAdapter;
    }

    public void newDocument() {
        notebook.newDocument();
    }

    public void openDocument() {
        String filepath = osAdapter.getOpeningFilepath();
        if(filepath != null)
            notebook.openDocument(filepath);
        recentf.appendFile(filepath);
    }

    public void openDocument(String filepath) {
        while(notebook == null){}
        notebook.openDocument(filepath);
        recentf.appendFile(filepath);
    }

    public void updateToolbar(Tab tab) {
        try {
            toolbar.remove(2);
            toolbar.remove(2);
            toolbar.remove(2);
            toolbar.remove(2);
            toolbar.remove(2);
            toolbar.remove(2);
            toolbar.remove(2);
            toolbar.remove(2);
            toolbar.remove(2);
            toolbar.remove(2);
        } catch(Exception e) {
            //             e.printStackTrace();
        }
        Action[] actions = tab.getFileActions();
        toolbar.add(actions[0]);
        toolbar.add(actions[1]);
        toolbar.add(actions[2]);
        osAdapter.updateFileMenu(actions);
        toolbar.add(new JToolBar.Separator());
        actions = tab.getEditActions();
        toolbar.add(actions[0]);
        toolbar.add(actions[1]);
        toolbar.add(new JToolBar.Separator());
        toolbar.add(actions[2]);
        toolbar.add(actions[3]);
        toolbar.add(actions[4]);
        osAdapter.updateEditMenu(actions);
    }
    
    public void updateTitle(String title) {
        setTitle("BlifPad - " + title);
    }

    public void updateSimulationTab() {
        sisPane.getSimTab().setText(notebook.getSelectedTab().getSimulation().getOutput());
    }

    public void simulate() {
        sisPane.setSelectedComponent(sisPane.getSimTab());
        sisPane.getSimTab().simulate();
    }

    public void writeEquation() {
        sisPane.setSelectedComponent(sisPane.getEqnTab());
        sisPane.getEqnTab().writeEquation();
    }

    public void printStats() {
        sisPane.setSelectedComponent(sisPane.getStatsTab());
        sisPane.getStatsTab().printStats();
    }

    public void writeKiss() {
        sisPane.setSelectedComponent(sisPane.getKissTab());
        sisPane.getKissTab().writeKiss();
    }

    public void simplifyCurrentDocument() {
        if(simplify == null)
            simplify = new SimplifyTab(notebook);
        String filepath = simplify.simplify();
        if(filepath != null)
            openDocument(filepath);
    }

    public void decompCurrentDocument() {
        if(decomp == null)
            decomp = new DecompTab(notebook);
        String filepath = decomp.decomp();
        if(filepath != null)
            openDocument(filepath);
    }

    public void minimizeCurrentDocument() {
        if(minimize == null)
            minimize = new MinimizeTab(notebook);
        String filepath = minimize.minimize();
        if(filepath != null)
            openDocument(filepath);
    }

    public void stateAssignCurrentDocument() {
        if(stateAssign == null)
            stateAssign = new StateAssignTab(notebook);
        String filepath = stateAssign.stateAssign();
        if(filepath != null)
            openDocument(filepath);
    }

    public void stateMinimizeCurrentDocument() {
        if(stateMinimize == null)
            stateMinimize = new StateMinimizeTab(notebook);
        String filepath = stateMinimize.stateMinimize();
        if(filepath != null)
            openDocument(filepath);
    }

    public void buildNetworkCurrentDocument() {
        if(buildNetwork == null)
            buildNetwork = new BuildNetworkTab(notebook);
        String filepath = buildNetwork.buildNetwork();
        if(filepath != null)
            openDocument(filepath);
    }

    public void quit() {
        if(notebook.requestQuit()) {
            myPreferences.setLocation(getLocation());
            myPreferences.setSize(getSize());
            myPreferences.setSisSelectedTab(sisPane.getSelectedIndex());
            myPreferences.setDividerLocation(splitPane.getDividerLocation());
            System.exit(0);
        }
    }

    private void buildLayout() {
        JPanel mainPanel;
        toolbar = new JToolBar();
        toolbar.add(new NewAction(this));
        toolbar.add(new OpenAction(this));
        notebook = new Notebook(this);
        sisPane = new SisPane(notebook);
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, notebook,
                                   sisPane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight (0.7);
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(toolbar, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 3, 6, 3));
        getContentPane().add(mainPanel);
        pack();
    }

    public static void main(String[] argv) {
        new MainWindow(new MacosxAdapter());
    }
}
