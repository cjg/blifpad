/* StatsTab.java */

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class StatsTab extends EqnTab {
    protected ConsolePane statsPane;
    protected JCheckBox fullSimplifyBox;
    private JCheckBox minimizeBox;
    private JButton statsButton;
    private MyPreferences myPreferences;

    public StatsTab(Notebook notebook) {
        super(notebook);
        buildLayout();
        statsButton.addActionListener(new StatsListener());
    }

    public void printStats() {
        if(!check())
            return;
        if(myPreferences == null)
            myPreferences = new MyPreferences();

        String filename = getFilename();

        File simplified = null;
       
        if(fullSimplifyBox.isSelected()) {
            try {
                simplified = getSimplifiedFile();
                filename = simplified.getName();
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
            "-T", "none",
            "-c", "print_stats",
            filename
        };

        exec(cmd, null, getPath());

        if(simplified != null)
            simplified.delete();
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
        statsPane.setText(formattedOutput);
    }

    private void buildLayout() {
        setLayout(new BorderLayout());
        fullSimplifyBox = new JCheckBox(ResourceLoader._("Full Simplify"));
        minimizeBox = new JCheckBox(ResourceLoader._("Minimize"));
        statsButton = new JButton(ResourceLoader._("Print Stats"));
        
        JPanel controlPanel = new JPanel();
        controlPanel.add(fullSimplifyBox);
        controlPanel.add(minimizeBox);
        controlPanel.add(statsButton);
        statsPane = new ConsolePane();
        add(controlPanel, BorderLayout.NORTH);
        add(statsPane, BorderLayout.CENTER);
    }

    public class StatsListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            printStats();
        }
    }
}
