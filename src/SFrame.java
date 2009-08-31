/* SFrame.java */

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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public abstract class SFrame extends JFrame {
    private ResourceLoader resourceLoader;
    private Component parent;
    private Point location;
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static Random random = new Random();
    private static ImageIcon icon = new ImageIcon(ResourceLoader.getUrl("images/icon48x48.png"));

	public SFrame (String title) {
		super(title);
        resourceLoader = new ResourceLoader();
        setIconImage(icon.getImage());
	}

    public SFrame (String title, Component c) {
        this(title);
        parent = c;
    }

    public SFrame (String title, Point location) {
        this(title);
        this.location = location;
    }

    public void setVisible(boolean visible) {
        if(!visible || visible==isVisible()) {
            super.setVisible(visible);
            return;
        }
        
        if(location == null) {
            location = new Point(0,0);
            if(parent != null){
                Dimension parentSize = parent.getSize();
                Point parentLocation = parent.getLocation();
                location.x = parentLocation.x + (parentSize.width / 3) * 2;
                location.y = parentLocation.y + (parentSize.height / 3) * 2;
            }

            location.x = location.x + (random.nextInt(100) * 
                                       (random.nextBoolean() ? 1 : -1));
            location.y = location.y + (random.nextInt(100) *
                                       (random.nextBoolean() ? 1 : -1));
        }

        Dimension size = getSize();
        if(location.x < 0)
            location.x = 0;
        if(location.y < 0)
            location.y = 0;
        if(location.x + size.width > screenSize.width)
            location.x = screenSize.width - size.width;
        if(location.y + size.height > screenSize.height)
            location.y = screenSize.height - size.height;
        setLocation(location);
        super.setVisible(true);
    }

    protected URL getUrl(String file) {
        return resourceLoader.getUrl(file);
    }
}
