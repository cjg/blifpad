/* AboutBox.java */

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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class AboutBox extends SFrame implements ActionListener {
    protected JLabel titleLabel, aboutLabel[];
    protected static int labelCount = 4;
    protected static int aboutWidth = 280;
    protected static int aboutHeight = 230;
    protected Font titleFont, bodyFont;

    public AboutBox(Component c) {
        super(ResourceLoader._("About BlifPad"), c);
        this.setResizable(false);
        SymWindow aSymWindow = new SymWindow();
    
        this.addWindowListener(aSymWindow);    

        aboutLabel = new JLabel[labelCount];
        aboutLabel[0] = new JLabel(new ImageIcon(getUrl("images/icon128x128.png")));
        // Initialize useful fonts
        titleFont = new Font(aboutLabel[0].getFont().getFontName(), Font.BOLD, 
			     aboutLabel[0].getFont().getSize() + 4);
        
        this.getContentPane().setLayout(new BorderLayout());
    
        aboutLabel[1] = new JLabel("BlifPad");
        aboutLabel[1].setFont(titleFont);
        aboutLabel[2] = new JLabel("0.2");
        // aboutLabel[2].setFont(bodyFont);
        aboutLabel[3] = new JLabel("Copyright 2006 Giuseppe Coviello");
	//        aboutLabel[3].setFont(bodyFont);
      
        Panel textPanel2 = new Panel(new GridLayout(labelCount, 1));
        for (int i = 1; i<labelCount; i++) {
            aboutLabel[i].setHorizontalAlignment(JLabel.CENTER);
            textPanel2.add(aboutLabel[i]);
        }
        this.getContentPane().add(aboutLabel[0], BorderLayout.NORTH);
        this.getContentPane().add (textPanel2, BorderLayout.CENTER);
        
        this.pack();
        this.setSize(aboutWidth, aboutHeight);
    }

    class SymWindow extends java.awt.event.WindowAdapter {
        public void windowClosing(java.awt.event.WindowEvent event) {
            setVisible(false);
        }
    }
    
    public void actionPerformed(ActionEvent newEvent) {
        setVisible(false);
    } 
}
