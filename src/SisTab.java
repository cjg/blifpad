/* SisTab.java */

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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JPanel;
import java.text.MessageFormat;

public abstract class SisTab extends JPanel {
    private String stdout;
    private String stderr;
    protected Notebook notebook;

    public String getStdOut() {
        return stdout;
    }

    public String getStdErr() {
        return stderr;
    }

    public boolean check() {
        Tab tab = notebook.getSelectedTab();
        if(tab.getTitle().indexOf(File.separator) < 0) {
            Object [] arguments = {
                tab.getTitle()
            };
            String message = MessageFormat.format (ResourceLoader._("{0} must be saved to perform the requested action. Save {0}?"), arguments);
            return tab.requestSave(message);
        }
        if(tab.isModified()) {
            Object [] arguments = {
                tab.getTitle()
            };
            String message = MessageFormat.format (ResourceLoader._("Save {0}?"), arguments);
            tab.requestSave(message);
        }
        return true;
    }

    public String getFilename() {
        Tab tab = notebook.getSelectedTab();
        return tab.getTitle().substring(tab.getTitle().lastIndexOf(File.separator)+1);
    }

    public String getPath() {
        Tab tab = notebook.getSelectedTab();
        return tab.getTitle().substring(0, tab.getTitle().lastIndexOf(File.separator));
    }
    
    protected void exec(String[] cmd, String envp[], String wdir) {
        try {
            Process process = Runtime.getRuntime().exec(cmd, envp, new
                                                        File(wdir));
            process.waitFor();
            InputStream outStream = process.getInputStream();
            InputStream errStream = process.getErrorStream();
            byte out[] = new byte[outStream.available()];
            byte err[] = new byte[errStream.available()];
            outStream.read(out);
            errStream.read(err);
            stdout = new String(out);
            stderr = new String(err);
        } catch (InterruptedException e) {
        } catch (IOException e) {
        }
    }
}
