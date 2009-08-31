/* CommandsMenu.java */

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

import javax.swing.JMenu;

public class CommandsMenu extends JMenu {
    public CommandsMenu(MainWindow mainWindow) {
        super(ResourceLoader._("Commands"));
        add(new EqnAction(mainWindow));
        add(new SimulateAction(mainWindow));
        add(new StatsAction(mainWindow));
        add(new KissAction(mainWindow));
        addSeparator();
        add(new StateMinimizeAction(mainWindow));
        add(new StateAssignAction(mainWindow));
        add(new BuildNetworkAction(mainWindow));
        add(new SimplifyAction(mainWindow));
        add(new DecompAction(mainWindow));
        add(new MinimizeAction(mainWindow));
    }
}
