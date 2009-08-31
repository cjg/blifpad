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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;

public class EqnTab extends SisTab {
    protected ConsolePane eqnPane;
    protected JCheckBox fullSimplifyBox;
    protected JCheckBox decompBox;
    protected JCheckBox minimizeBox;
    private JButton eqnButton;
    private MyPreferences myPreferences;
    protected SimplifyTab simplify;
    protected DecompTab decomp;
    protected MinimizeTab minimize;

    public EqnTab(Notebook notebook) {
        super();
        this.notebook = notebook;
        buildLayout();
        eqnButton.addActionListener(new EqnListener());
    }

    public void writeEquation() {
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
        eqnPane.setText(formattedOutput);
    }

    protected File getSimplifiedFile() throws IOException {
        if(simplify == null)
            simplify = new SimplifyTab(notebook);
        File simplified = File.createTempFile(getFilename(), ".blif", new
                                              File(getPath()));
        simplify.simplify(getFilename(), simplified.getName(), getPath());
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
            
    private void buildLayout() {
        setLayout(new BorderLayout());
        fullSimplifyBox = new JCheckBox(ResourceLoader._("Full Simplify"));
        decompBox = new JCheckBox(ResourceLoader._("Decompose"));
        minimizeBox = new JCheckBox(ResourceLoader._("Minimize"));
        eqnButton = new JButton(ResourceLoader._("Write Equation"));
        
        JPanel controlPanel = new JPanel();
        controlPanel.add(fullSimplifyBox);
        controlPanel.add(decompBox);
        controlPanel.add(minimizeBox);
        controlPanel.add(eqnButton);
        eqnPane = new ConsolePane();
        add(controlPanel, BorderLayout.NORTH);
        add(eqnPane, BorderLayout.CENTER);
    }

    public class EqnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            writeEquation();
        }
    }
}
