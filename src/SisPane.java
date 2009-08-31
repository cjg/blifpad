/* SisPane.java */

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

import javax.swing.JTabbedPane;

public class SisPane extends JTabbedPane {
    private EqnTab eqnTab;
    private SimTab simTab;
    private StatsTab statsTab;
    private KissTab kissTab;

    public SisPane(Notebook notebook) {
        super(JTabbedPane.TOP);
        eqnTab = new EqnTab(notebook);
        simTab = new SimTab(notebook);
        statsTab = new StatsTab(notebook);
        kissTab = new KissTab(notebook);
        addTab(ResourceLoader._("Equation"), EqnAction.icon, eqnTab);
        addTab(ResourceLoader._("Simulation"), SimulateAction.icon, simTab);
        addTab(ResourceLoader._("Stats"), StatsAction.icon, statsTab);
        addTab(ResourceLoader._("Transictions"), KissAction.icon, kissTab);
    }

    public SimTab getSimTab() {
        return simTab;
    }

    public EqnTab getEqnTab() {
        return eqnTab;
    }

    public StatsTab getStatsTab() {
        return statsTab;
    }

    public KissTab getKissTab() {
        return kissTab;
    }
}
