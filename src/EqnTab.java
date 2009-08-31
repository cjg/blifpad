/* EqnTab.java */

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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class EqnTab extends FullOptionalTab {
    private JCheckBox decompBox;
    private JButton eqnButton;
    public final static ImageIcon sequentialIcon = new ImageIcon(ResourceLoader.getUrl("images/sequential.png"));
    public final static ImageIcon combinatoryIcon = new ImageIcon(ResourceLoader.getUrl("images/combinatory.png"));

    public EqnTab(Notebook notebook) {
        super();
        myPreferences = new MyPreferences();
        this.notebook = notebook;
        buildLayout();
        eqnButton.addActionListener(new EqnListener());
    }

    public void writeEquation() {
        if(!check())
            return;

        String filename = getFilename();

        File stateMinimized = null;
       
        if(stateMinimizeBox.isSelected()) {
            try {
                stateMinimized = getStateMinimizedFile(filename);
                filename = stateMinimized.getName();
            } catch (IOException e) {
                return;
            }
        }

        File stateAssigned = null;
       
        if(stateAssignBox.isSelected()) {
            try {
                stateAssigned = getStateAssignedFile(filename);
                filename = stateAssigned.getName();
            } catch (IOException e) {
                return;
            }
        }

        File netBuilt = null;
       
        if(buildNetworkBox.isSelected()) {
            try {
                netBuilt = getNetBuiltFile(filename);
                filename = netBuilt.getName();
            } catch (IOException e) {
                return;
            }
        }

        File simplified = null;
       
        if(fullSimplifyBox.isSelected()) {
            try {
                simplified = getSimplifiedFile(filename);
                filename = simplified.getName();
            } catch (IOException e) {
                return;
            }
        }

        File decomposed = null;

        if(decompBox.isSelected() && !minimizeBox.isSelected()) {
            try {
                decomposed = getDecomposedFile(filename);
                filename = decomposed.getName();
            } catch (IOException e) {
                return;
            }
        }

        File minimized = null;

        if(minimizeBox.isSelected()) {
            try {
                minimized = getMinimizedFile(filename);
                filename = minimized.getName();
            } catch (IOException e) {
                return;
            }
        }

        String cmd[] = {
            myPreferences.getSisPath(),
            "-t", "blif",
            "-T", "eqn",
            filename
        };

        exec(cmd, null, getPath());

        if(stateMinimized != null)
            stateMinimized.delete();
        if(stateAssigned != null)
            stateAssigned.delete();
        if(netBuilt != null)
            netBuilt.delete();
        if(simplified != null)
            simplified.delete();
        if(decomposed != null)
            decomposed.delete();
        if(minimized != null)
            minimized.delete();

        String formattedOutput = ("<body bgcolor='black'><b><pre>" + 
                                  "<font color='white'>" + 
                                  getFilename() + "<br>========<br>");
        if(minimizeBox.isSelected())
            formattedOutput = (formattedOutput + "Applied script.rugged " +
                               minimize.getCounter() + " time(s)!<br>");
        if(getStdErr().length() > 0)
            formattedOutput += "<font color='red'>" + getStdErr() + "</font><br>";
        formattedOutput += getStdOut() + "</font></pre></b></body>";
        consolePane.setText(formattedOutput);
    }

    private void buildLayout() {
        setLayout(new BorderLayout());
        makeCommonControl("EQN");
        decompBox = new JCheckBox(ResourceLoader._("Decompose"));
        eqnButton = new JButton(ResourceLoader._("Write Equation"));
        sequentialPanel.add(stateMinimizeBox);
        sequentialPanel.add(stateAssignBox);
        sequentialPanel.add(buildNetworkBox);

        combinatoryPanel.add(fullSimplifyBox);
        combinatoryPanel.add(decompBox);
        combinatoryPanel.add(minimizeBox);

        JPanel controlPanel = new JPanel();
        controlPanel.add(sequentialButton);
        controlPanel.add(sequentialPanel);
        controlPanel.add(combinatoryButton);
        controlPanel.add(combinatoryPanel);
        controlPanel.add(eqnButton);
        consolePane = new ConsolePane();
        add(new JScrollPane(controlPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
            BorderLayout.NORTH);
        add(consolePane, BorderLayout.CENTER);
    }

    public class EqnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            writeEquation();
        }
    }
}
