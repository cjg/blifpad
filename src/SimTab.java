/* SimTab.java */

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
import java.io.InputStream;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JViewport;

public class SimTab extends SisTab {
    private ConsolePane simPane;
    protected BinField simField;
    private JButton simButton;
    private MyPreferences myPreferences;

    public SimTab(Notebook notebook) {
        super();
        this.notebook = notebook;
        buildLayout();
        simButton.addActionListener(new SimListener());
    }

    public void simulate() {
        if(!check())
            return;
        if(myPreferences == null)
            myPreferences = new MyPreferences();
        String inputs = simField.getText();
        char inputs_array[] = inputs.toCharArray();
        int i;
        inputs = "";
        for(i =0; i<inputs_array.length;i++)
            inputs = inputs+ " "+inputs_array[i];
        inputs = inputs.trim();
        String cmd[] = {
            myPreferences.getSisPath(),
            "-t", "blif",
            "-T", "none",
            "-c", "simulate " + inputs,
            getFilename()
        };
        exec(cmd, null, getPath());
        String formattedOutput = ("<body bgcolor='black'><b><pre>" + 
                                  "<font color='white'>" + 
                                  getFilename() + "<br>========<br>");
        if(getStdErr().length() > 0)
            formattedOutput += "<font color='red'>" + getStdErr() + "</font><br>";
        formattedOutput += getStdOut() + "</font></pre></b></body>";
        simPane.setText(formattedOutput);
    }

    private void buildLayout() {
        setLayout(new BorderLayout());
        simField = new BinField(10);
        simButton = new JButton(ResourceLoader._("Simulate"));

        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel(ResourceLoader._("Inputs: ")));
        controlPanel.add(simField);
        controlPanel.add(simButton);
        simPane = new ConsolePane();
        add(controlPanel, BorderLayout.NORTH);
        add(simPane, BorderLayout.CENTER);
    }

    public class SimListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            simulate();
        }
    }
}
