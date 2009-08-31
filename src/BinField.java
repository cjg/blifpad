/* BinField.java */

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

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class BinField extends JTextField {
 
    public BinField(int cols) {
         super(cols);
    }
 
    protected Document createDefaultModel() {
        return new BinDocument();
    }
 
    static class BinDocument extends PlainDocument {
 
        public void insertString(int offs, String str, AttributeSet a) 
 	          throws BadLocationException {
 
            if (str == null) {
                return;
            }
            
            char[] chars = str.toCharArray();
            char[] good = new char[chars.length];
            int j=0;
            for (int i = 0; i < chars.length; i++)
                if(chars[i] == '0' || chars[i] == '1')
                    good[j++]=chars[i];
            super.insertString(offs, new String(good), a);
        }
    }
}
