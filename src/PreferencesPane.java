/* PreferencesPane.java */

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
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class PreferencesPane extends SFrame {
    private MyPreferences myPreferences;
    private BTextField sisPath;
    private JButton okButton;
    private JButton cancelButton;
    //     private JCheckBox autoCloseBox;

    public PreferencesPane(Component c) {
        super(ResourceLoader._("BlifPad Preferences"), c);
        buildLayout();
        myPreferences = new MyPreferences();
        sisPath.setText(myPreferences.getSisPath());
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    cancel();
                }
            });
        okButton.addActionListener (new OKListener());
        cancelButton.addActionListener(new CancelListener());
    }

    private void buildLayout() {
        JPanel labelsPanel;
        JPanel fieldsPanel;
        JPanel preferencesPanel;
        JPanel mainPanel;
        sisPath = new BTextField(20);
        okButton = new JButton(ResourceLoader._("OK"));
        cancelButton = new JButton(ResourceLoader._("Cancel"));

        labelsPanel = new JPanel(new GridLayout(1, 1, 6, 6));
        fieldsPanel = new JPanel(new GridLayout(1, 1, 6, 6));
        preferencesPanel = new JPanel(new BorderLayout(6, 6));//new GridLayout(1, 2, 6, 6));
        labelsPanel.add(new JLabel(ResourceLoader._("Sis Executable Path"), JLabel.RIGHT));
        fieldsPanel.add(sisPath);
        preferencesPanel.add(labelsPanel, BorderLayout.WEST);
        preferencesPanel.add(fieldsPanel, BorderLayout.EAST);
        mainPanel = new JPanel(new BorderLayout(6, 6));
        mainPanel.add(preferencesPanel, BorderLayout.CENTER);

        JPanel buttonsGridPanel = new JPanel(new GridLayout(1, 2, 6, 6));
        buttonsGridPanel.add(okButton);
        buttonsGridPanel.add(cancelButton);
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(buttonsGridPanel);
        JPanel controlsPanel = new JPanel(new BorderLayout());
        controlsPanel.add(new JSeparator(), BorderLayout.NORTH);
        controlsPanel.add(buttonsPanel, BorderLayout.SOUTH);
        mainPanel.add(controlsPanel, BorderLayout.SOUTH);

		mainPanel.setBorder(new EmptyBorder(6, 12, 0, 12)); 

        getContentPane().add(mainPanel);
        pack();
        setResizable(false);
    }

    public void ok() {
        myPreferences.setSisPath(sisPath.getText ());
        setVisible(false);
    }

    public void cancel() {
        sisPath.setText(myPreferences.getSisPath());
        setVisible(false);
    }

    public class OKListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ok();
        }
    }

    public class CancelListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            cancel();
        }
    }
}
