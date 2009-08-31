/* BlifPadPreferences.java */

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

import java.awt.Dimension;
import java.awt.Point;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JSplitPane;

public class MyPreferences {
    private Preferences preferences;
	
    public MyPreferences() {
        preferences = Preferences.userNodeForPackage(BlifPad.class);
    }

    public boolean keyExists(String key) {
        int i;
        String[] keys;
        try {
            keys = preferences.keys();
        } catch (BackingStoreException e) {
            return false;
        }
        for(i=0;i<keys.length;i++)
            if(key.equals(keys[i]))
                return true;
        
        return false;
    }

    public String getSisPath() {
        return preferences.get("SIS_PATH", "sis");
    }

    public void setSisPath(String sisPath) {
        preferences.put("SIS_PATH", sisPath);
    }

    public Point getLocation() {
        int x;
        int y;
        x = preferences.getInt("LOCATION_X", 0);
        y = preferences.getInt("LOCATION_Y", 0);
        return new Point(x, y);
    }

    public void setLocation(Point p) {
        preferences.putInt("LOCATION_X", p.x);
        preferences.putInt("LOCATION_Y", p.y);
    }

    public Dimension getSize() {
        int width = preferences.getInt("SIZE_WIDTH", 600);
        int height = preferences.getInt("SIZE_HEIGTH", 600);
        return new Dimension(width, height);
    }

    public void setSize(Dimension d) {
        preferences.putInt("SIZE_WIDTH", d.width);
        preferences.putInt("SIZE_HEIGTH", d.height);
    }

    public int getSisSelectedTab() {
        return preferences.getInt("SIS_SELECTED_TAB", 0);
    }

    public void setSisSelectedTab(int i) {
        preferences.putInt("SIS_SELECTED_TAB", i);
    }

    public int getDividerLocation(JSplitPane splitPane) {
        return preferences.getInt("DIVIDER_LOCATION",
                                  splitPane.getDividerLocation());

    }

    public void setDividerLocation(int dl) {
        preferences.putInt("DIVIDER_LOCATION", dl);
    }

    public boolean getPanelVisible(String panel) {
        return preferences.getBoolean (panel, false);
    }

    public void setPanelVisible(String panel, boolean visible) {
        preferences.putBoolean(panel, visible);
    }

//     public String getDownloadDir() {
//         return preferences.get("DOWNLOAD_DIR", System.getProperty("user.home"));
//     }

//     public void setDownloadDir(String path) {
//         preferences.put("DOWNLOAD_DIR", path);
//     }

//     public boolean getAutoCloseDisplay() {
//         return preferences.getBoolean("AUTOCLOSE_DISPLAY", false);
//     }

//     public void setAutoCloseDisplay(boolean autoClose) {
//         preferences.putBoolean("AUTOCLOSE_DISPLAY", autoClose);
//     }
}
