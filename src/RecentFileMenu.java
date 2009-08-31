/* RecentFileMenu.java */

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

import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import java.io.File;

public class RecentFileMenu extends JMenu {
    private Vector items;
    private String recentCache = System.getProperty("user.home") + File.separator + ".BlifPad.recent";
    protected MainWindow mainWindow;
    

    public RecentFileMenu(MainWindow mainWindow) {
        super(ResourceLoader._("Recent Files"));
        this.mainWindow = mainWindow;
        setIcon(new ImageIcon(ResourceLoader.getUrl("images/recent.png")));
        try {
            FileInputStream fis = new FileInputStream(recentCache);
            ObjectInputStream ois = new ObjectInputStream(fis);
            items = (Vector) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            items = new Vector();
        }
        update();
    }

    public void appendFile(String filepath) {
        int index;
        index = items.indexOf(filepath);
        if(index>=0)
            items.remove(index);
        items.add(filepath);
        update();
        save();
    }

    private void update() {
        int i;
        removeAll();
        for(i=0;i<10 && items.size()-i>0;i++)
            add(new RecentAction((String)items.get(items.size()-i-1)));
    }

    private void save() {
        try {
            FileOutputStream fos = new FileOutputStream(recentCache);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(items);
            oos.close();
            fos.close();
        } catch (Exception e) {
        }
    }

    public class RecentAction extends ActionClass {
        private String filepath;

        public RecentAction(String filepath) {
            super(filepath, -1);
            this.filepath = filepath;
        }

        public void actionPerformed(ActionEvent e) {
            mainWindow.openDocument(filepath);
        }
    }

}
