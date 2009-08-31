/* Simulation.java */

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

import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;

public class Simulation {
    private String output;
    private String inputs;
    
    public Simulation() {
        reset();
    }

    public void reset() {
        output = "";
        inputs = "";
    }

    public void addInputs(String input) {
        inputs += "echo <font color='green'>Inputs: " + input + "</font>\nsimulate " + input + "\n";
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getOutput() {
        return output;
    }

    public File getScript() {
        try {
            File script = File.createTempFile("simulation", ".script");
            FileOutputStream fos = new FileOutputStream(script);
            fos.write(inputs.getBytes());
            fos.close();
            return script;
        } catch (Exception e) {}
        return null;
    }
}
