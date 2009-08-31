/* KissTab.java */

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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class KissTab extends FullOptionalTab {
    private JButton kissButton;

    public KissTab(Notebook notebook) {
        super();
        this.notebook = notebook;
        buildLayout();
        kissButton.addActionListener(new KissListener());
    }

    public void writeKiss() {
        if(!check())
            return;

        if(myPreferences == null)
            myPreferences = new MyPreferences();

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

        String cmd[] = {
            myPreferences.getSisPath(),
            "-t", "blif",
            "-T", "none",
            "-c", "write_kiss",
            filename
        };

        exec(cmd, null, getPath());

        if(stateMinimized != null)
            stateMinimized.delete();

        String formattedOutput = ("<body bgcolor='black'><b><pre>" + 
                                  "<font color='white'>" + 
                                  getFilename() + "<br>========<br>");
        if(getStdErr().length() > 0)
            formattedOutput += "<font color='red'>" + getStdErr() + "</font><br>";
        formattedOutput += getStdOut() + "</font></pre></b></body>";
        consolePane.setText(formattedOutput);
    }

    private void buildLayout() {
        setLayout(new BorderLayout());
        stateMinimizeBox = new JCheckBox(ResourceLoader._("State Minimize"));
        kissButton = new JButton(ResourceLoader._("Write Transictions Table"));
        
        JPanel controlPanel = new JPanel();
        controlPanel.add(stateMinimizeBox);
        controlPanel.add(kissButton);
        consolePane = new ConsolePane();
        add(controlPanel, BorderLayout.NORTH);
        add(consolePane, BorderLayout.CENTER);
    }

    public class KissListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            writeKiss();
        }
    }
}
