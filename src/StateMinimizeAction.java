/* StateMinimizeAction.java */

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
import javax.swing.ImageIcon;

public class StateMinimizeAction extends ActionClass {
    private MainWindow mainWindow;
    private static ImageIcon icon = new ImageIcon(ResourceLoader.getUrl("images/state_minimize.png"));

    public StateMinimizeAction(MainWindow mainWindow) {
        super(ResourceLoader._("State Minimize"), -1, icon); 
        this.mainWindow = mainWindow;
    }

    public void actionPerformed(ActionEvent e) {
        mainWindow.stateMinimizeCurrentDocument();
    }
}
