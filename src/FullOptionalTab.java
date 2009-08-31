/* FullOptionalTab.java */

/* <project_name> -- <project_description>
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
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public abstract class FullOptionalTab extends SisTab {
    protected ConsolePane consolePane;
    protected JCheckBox stateAssignBox;
    protected JCheckBox stateMinimizeBox;
    protected JCheckBox buildNetworkBox;
    protected JCheckBox fullSimplifyBox;
    protected JCheckBox decompBox;
    protected JCheckBox minimizeBox;
    protected JButton combinatoryButton;
    protected JButton sequentialButton;
    protected JPanel combinatoryPanel;
    protected JPanel sequentialPanel;
    protected MyPreferences myPreferences;
    protected SimplifyTab simplify;
    protected DecompTab decomp;
    protected MinimizeTab minimize;
    protected StateAssignTab stateAssign;
    protected StateMinimizeTab stateMinimize;
    protected BuildNetworkTab buildNetwork;
    public final static ImageIcon sequentialIcon = new ImageIcon(ResourceLoader.getUrl("images/sequential.png"));
    public final static ImageIcon combinatoryIcon = new ImageIcon(ResourceLoader.getUrl("images/combinatory.png"));

    protected File getStateAssignedFile(String filename) throws IOException {
        if(stateAssign == null)
            stateAssign = new StateAssignTab(notebook);
        File stateAssigned = File.createTempFile(getFilename(), ".blif", new
                                                 File(getPath()));
        stateAssign.stateAssign(filename, stateAssigned.getName(),
                                getPath());
        return stateAssigned;
    }

    protected File getStateMinimizedFile(String filename) throws IOException {
        if(stateMinimize == null)
            stateMinimize = new StateMinimizeTab(notebook);
        File stateMinimized = File.createTempFile(getFilename(), ".blif", new
                                                 File(getPath()));
        stateMinimize.stateMinimize(filename, stateMinimized.getName(),
                                    getPath());
        return stateMinimized;
    }

    protected File getNetBuiltFile(String filename) throws IOException {
        if(buildNetwork == null)
            buildNetwork = new BuildNetworkTab(notebook);
        File netBuilt = File.createTempFile(getFilename(), ".blif", new
                                            File(getPath()));
        buildNetwork.buildNetwork(filename, netBuilt.getName(),
                                  getPath());
        return netBuilt;
    }

    protected File getSimplifiedFile(String filename) throws IOException {
        if(simplify == null)
            simplify = new SimplifyTab(notebook);
        File simplified = File.createTempFile(getFilename(), ".blif", new
                                              File(getPath()));
        simplify.simplify(filename, simplified.getName(), getPath());
        return simplified;
    }

    protected File getDecomposedFile(String filename) throws IOException {
        if(decomp == null)
            decomp = new DecompTab(notebook);
        File decomposed = File.createTempFile(filename, ".blif", new
                                              File(getPath()));
        decomp.decomp(filename, decomposed.getName(), getPath());
        return decomposed;
    }

    protected File getMinimizedFile(String filename) throws IOException {
        if(minimize == null)
            minimize = new MinimizeTab(notebook);
        File minimized = File.createTempFile(filename, ".blif", new
                                              File(getPath()));
        minimize.minimize(filename, minimized.getName(), getPath());
        return minimized;
    }
    
    protected void makeCommonControl(String tabName) {
        stateAssignBox = new JCheckBox(ResourceLoader._("State Assign"));
        stateMinimizeBox = new JCheckBox(ResourceLoader._("State Minimize"));
        buildNetworkBox = new JCheckBox(ResourceLoader._("Build Network"));
        fullSimplifyBox = new JCheckBox(ResourceLoader._("Full Simplify"));
        minimizeBox = new JCheckBox(ResourceLoader._("Minimize"));
        sequentialButton = new JButton(new SequentialAction(tabName));
        sequentialButton.setToolTipText(ResourceLoader._("Show/Hide options for sequential circuit."));
        combinatoryButton = new JButton(new CombinatoryAction(tabName));
        combinatoryButton.setToolTipText(ResourceLoader._("Show/Hide options for combinatory circuit."));
        sequentialPanel = new JPanel();
        sequentialPanel.setVisible(myPreferences.getPanelVisible(tabName +
                                                                 "_SEQUENTIAL"));
        combinatoryPanel = new JPanel();
        combinatoryPanel.setVisible(myPreferences.getPanelVisible(tabName +
                                                                  "_COMBINATORY"));
    }
            
    public class SequentialAction extends ActionClass {
        private String tabName;
        public SequentialAction(String tabName) {
            super("", -1, sequentialIcon); 
            this.tabName = tabName;
        }

        public void actionPerformed(ActionEvent e) {
            sequentialPanel.setVisible (!sequentialPanel.isVisible ());
            myPreferences.setPanelVisible(tabName+"_SEQUENTIAL",
                                          combinatoryPanel.isVisible());
        }
    }

    public class CombinatoryAction extends ActionClass {
        private String tabName;
        public CombinatoryAction(String tabName) {
            super("", -1, combinatoryIcon); 
            this.tabName = tabName;
        }

        public void actionPerformed(ActionEvent e) {
            combinatoryPanel.setVisible (!combinatoryPanel.isVisible ());
            myPreferences.setPanelVisible(tabName+"_COMBINATORY",
                                          combinatoryPanel.isVisible());
        }
    }
}
