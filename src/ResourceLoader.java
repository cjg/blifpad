/* ResourceLoader.java */

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

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceLoader {
    private static URLClassLoader urlLoader = (URLClassLoader) ResourceLoader.class.getClassLoader();
    private static ResourceBundle myResources =
    ResourceBundle.getBundle("MyResources");

    public static URL getUrl(String filepath) {
        try {
            return urlLoader.findResource(filepath);
        } catch (Exception e) {
            return null;
        }
    }

	public static String _(String text) {
        try {
            return myResources.getString(text);
        } catch (Exception e) {
            System.err.println("String '" + text + "' not localized!");
            return text;
        }
	}
}
