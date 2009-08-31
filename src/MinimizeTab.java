/* MinimizeTab.java */

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
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JOptionPane;

public class MinimizeTab extends SisTab {
    private MyPreferences myPreferences;
    private String ruggedPath;
    private int ruggedCounter; 

    public MinimizeTab(Notebook notebook) {
        super();
        this.notebook = notebook;
    }

    public String minimize(String infile, String outfile, String path) {
        if(myPreferences == null)
            myPreferences = new MyPreferences();
        if(ruggedPath == null) {
            try {
                File rugged = File.createTempFile("rugged", ".script");
                FileOutputStream fos = new FileOutputStream(rugged);
                fos.write(("sweep; eliminate -1\n" + 
                           "simplify -m nocomp\n" +
                           "eliminate -1\n" +
                           "sweep; eliminate 5\n" +
                           "simplify -m nocomp\n" +
                           "resub -a\n" +
                           "fx\n" +
                           "resub -a; sweep\n" +
                           "eliminate -1; sweep\n" +
                           "full_simplify -m nocomp\n").getBytes());
                fos.close();
                ruggedPath = rugged.getAbsolutePath ();
            } catch (IOException e) {
                return null;
            }
        }

        File minimized = null;
        File oldMinimized = null;
        String minimizedName = infile;
        String oldStdOut = null;
        ruggedCounter = 0;

        while (true) {
            if((minimized = getMinimized(path, minimizedName))==null)
                return null;
            if(oldMinimized != null)
                oldMinimized.delete();
            if(getStdOut().equals(oldStdOut))
                break;
            oldStdOut = getStdOut();
            oldMinimized = minimized;
            minimizedName = minimized.getName();
            ruggedCounter++;
        }
        
        if(minimized != null)
            minimized.delete();

        try {
            FileOutputStream fos = new FileOutputStream(path +
                                                        File.separator +
                                                        outfile);


            fos.write(("# Applied script.rugged " + (--ruggedCounter) + 
                       " time(s)!\n" +  getStdOut()).getBytes());
            fos.close();
        } catch (IOException e) {
            return null;
        }
        return path + File.separator + outfile;
    }


    public String minimize() {
        if(!check())
            return null;
        if(myPreferences == null)
            myPreferences = new MyPreferences();

        return minimize(getFilename(), getFilename().replaceAll(".blif",
                                                                "_minimized.blif"),
                        getPath());
    }

    public int getCounter() {
        return ruggedCounter;
    }

    private File getMinimized(String path, String filename) {
        String cmd[] = {
            myPreferences.getSisPath(),
            "-f", ruggedPath,
            "-t", "blif",
            "-T", "blif",
            filename
        };
        exec(cmd, null, path);
        if(getStdErr().length() > 0) {
            JOptionPane.showMessageDialog(this, getStdErr(),
                                          "BlifPad",
                                          JOptionPane.ERROR_MESSAGE);
            return null;
        }
        try {
            File minimized = File.createTempFile(filename.replaceAll(".blif", "") ,
                                                 ".blif", new File(path));
            FileOutputStream fos = new FileOutputStream(minimized);
            fos.write(getStdOut().getBytes());
            fos.close();
            return minimized;
        } catch(IOException e) {
            return null;
        }
    }
}
