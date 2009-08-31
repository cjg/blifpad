/* DecompTab.java */

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

public class DecompTab extends SisTab {
    private MyPreferences myPreferences;

    public DecompTab(Notebook notebook) {
        super();
        this.notebook = notebook;
    }

    public String decomp(String infile, String outfile, String path) {
        if(myPreferences == null)
            myPreferences = new MyPreferences();
        String cmd[] = {
            myPreferences.getSisPath(),
            "-c", "decomp",
            "-t", "blif",
            "-T", "blif",
            infile
        };
        exec(cmd, null, path);
        if(getStdErr().length() > 0) {
            JOptionPane.showMessageDialog(this, getStdErr(),
                                          "BlifPad",
                                          JOptionPane.ERROR_MESSAGE);
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(path +
                                                        File.separator +
                                                        outfile);


            fos.write(getStdOut().getBytes());
            fos.close();
        } catch (IOException e) {
            return null;
        }
        return path + File.separator + outfile;
    }


    public String decomp() {
        if(!check())
            return null;
        if(myPreferences == null)
            myPreferences = new MyPreferences();

        return decomp(getFilename(), getFilename().replaceAll(".blif",
                                                            "_decomposed.blif"),
                    getPath());
    }
}
