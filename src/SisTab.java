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

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import javax.swing.JPanel;

public abstract class SisTab extends JPanel {
    private String stdout;
    private String stderr;
    private String warnings;
    private String errors;
    protected Notebook notebook;
    private static boolean usingWindows = System.getProperty("file.separator").equals("\\");

    public String getStdOut() {
        return stdout;
    }

    public String getStdErr() {
        return stderr;
    }

    public String getErrors() {
        return errors;
    }

    public String getWarnings() {
        return warnings;
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
            Process process;
            if(usingWindows){
                process = Runtime.getRuntime().exec(formatWindowsCmd(cmd), envp,
                                                    new File(wdir));
            } else
                process = Runtime.getRuntime().exec(cmd, makeUnixEnvp(cmd, envp), 
                                                    new File(wdir));
            final DataInputStream dos = new DataInputStream(process.getInputStream());
            final DataInputStream des = new DataInputStream(process.getErrorStream());

            stdout = "";
            Thread thread = new Thread("input stream") {
                    public void run() {
                        try {
                            String line=null;
                            while ((line = dos.readLine()) != null) 
                                stdout += line + "\n";
                        }
                        catch (IOException e) {
                            System.err.println(e);
                        }
                    }
                };
            thread.start();

            stderr = "";
            warnings = "";
            errors = "";
            Thread thread2 = new Thread("error stream") {
                    public void run() {
                        try {
                            String line=null;
                            while ((line = des.readLine()) != null) {
                                stderr += line + "\n";
                                if(line.startsWith("Warning"))
                                    warnings += line + "\n";
                                else
                                    errors += line + "\n";
                            }
                        }
                        catch (IOException e) {
                            System.err.println(e);
                        }
                    }
                };
            thread2.start();

            process.waitFor();
        } catch (InterruptedException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] makeUnixEnvp(String[] cmd, String[] envp) {
        String[] unixEnvp;
        int i=0;
        if(envp == null)
            unixEnvp = new String[1];
        else {
            unixEnvp = new String[envp.length + 1];
            for(i=0;i<envp.length;i++)
                unixEnvp[i] = envp[i];
        }
        if(cmd[0].lastIndexOf("/")>0) {
            String sisPath = cmd[0].substring(0, cmd[0].lastIndexOf("/"));
            unixEnvp[i] = "PATH=$PATH:" + sisPath; 
        }
        return unixEnvp;
    }

    private String formatWindowsCmd(String[] cmd) {
        int i;
        String _cmd = "cmd /C \""; 
        if(cmd[0].indexOf("\\")>0) {
            String sisPath = cmd[0].substring(0, cmd[0].lastIndexOf("\\"));
            _cmd += "path %PATH%;" + sisPath + " && "; 
        }
        for(i=0;i<cmd.length;i++) {
            _cmd += cmd[i] + " ";
        }
        _cmd += "\"";
        return _cmd;
    }
}
