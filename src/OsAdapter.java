/* OsAdapter.java */

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

import javax.swing.Action;
import javax.swing.JMenuBar;

public interface OsAdapter {
    public void setMainWindow(MainWindow mainWindow);
    public void mainWindowClosing();
    public String getOpeningFilepath();
    public String getSavingFilepath();
    public JMenuBar getMenuBar(RecentFileMenu recentf);
    public void updateFileMenu(Action[] actions);
    public void updateEditMenu(Action[] actions);
}
