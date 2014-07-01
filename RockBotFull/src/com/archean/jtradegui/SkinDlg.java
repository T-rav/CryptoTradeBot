/*
 * jCryptoTrader trading client
 * Copyright (C) 2014 1M4SKfh83ZxsCSDmfaXvfCfMonFxMa5vvh (BTC public key)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 */

package com.archean.jtradegui;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class SkinDlg extends JDialog {
    public SkinDlg(Frame owner) {
        super(owner);
        initComponents();
    }

    public SkinDlg(Dialog owner) {
        super(owner);
        initComponents();
    }

    public static String getLookAndFeelClassName(String theme) {
        String lfClassName = "";
        switch (theme) {
            case "System":
                lfClassName = UIManager.getSystemLookAndFeelClassName();
                break;
            case "Metal":
                lfClassName = UIManager.getCrossPlatformLookAndFeelClassName();
                break;
            case "Motif":
                lfClassName = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
                break;
            case "GTK":
                lfClassName = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
                break;
            case "Acryl":
                lfClassName = "com.jtattoo.plaf.acryl.AcrylLookAndFeel";
                break;
            case "Aero":
                lfClassName = "com.jtattoo.plaf.aero.AeroLookAndFeel";
                break;
            case "Aluminium":
                lfClassName = "com.jtattoo.plaf.aluminium.AluminiumLookAndFeel";
                break;
            case "Bernstein":
                lfClassName = "com.jtattoo.plaf.bernstein.BernsteinLookAndFeel";
                break;
            case "Fast":
                lfClassName = "com.jtattoo.plaf.fast.FastLookAndFeel";
                break;
            case "HiFi":
                lfClassName = "com.jtattoo.plaf.hifi.HiFiLookAndFeel";
                break;
            case "McWin":
                lfClassName = "com.jtattoo.plaf.mcwin.McWinLookAndFeel";
                break;
            case "Mint":
                lfClassName = "com.jtattoo.plaf.mint.MintLookAndFeel";
                break;
            case "Noire":
                lfClassName = "com.jtattoo.plaf.noire.NoireLookAndFeel";
                break;
            case "Smart":
                lfClassName = "com.jtattoo.plaf.smart.SmartLookAndFeel";
                break;
            case "Luna":
                lfClassName = "com.jtattoo.plaf.luna.LunaLookAndFeel";
                break;
            case "Texture":
                lfClassName = "com.jtattoo.plaf.texture.TextureLookAndFeel";
                break;
            default:
                throw new IllegalArgumentException();
        }
        return lfClassName;
    }

    private void okButtonActionPerformed(ActionEvent e) {
        String theme = (String) comboBox1.getSelectedItem(), lfClassName = getLookAndFeelClassName(theme);

        try {
            UIManager.setLookAndFeel(lfClassName);
            SwingUtilities.updateComponentTreeUI(this.getParent());
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        setVisible(false);
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        setVisible(false);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("com.archean.jtradegui.locale");
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        comboBox1 = new JComboBox<>();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setTitle(bundle.getString("SkinDlg.this.title"));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(Borders.createEmptyBorder("7dlu, 7dlu, 7dlu, 7dlu"));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new FormLayout(
                        "default:grow",
                        "2*(default, $lgap), default"));

                //---- comboBox1 ----
                comboBox1.setModel(new DefaultComboBoxModel<>(new String[]{
                        "System",
                        "Metal",
                        "Motif",
                        "GTK",
                        "Acryl",
                        "Aero",
                        "Aluminium",
                        "Bernstein",
                        "Fast",
                        "HiFi",
                        "McWin",
                        "Mint",
                        "Noire",
                        "Smart",
                        "Luna",
                        "Texture"
                }));
                contentPanel.add(comboBox1, CC.xy(1, 1));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(Borders.createEmptyBorder("5dlu, 0dlu, 0dlu, 0dlu"));
                buttonBar.setLayout(new FormLayout(
                        "[50dlu,pref]:grow, $rgap, [50dlu,pref]:grow",
                        "pref"));

                //---- okButton ----
                okButton.setText(bundle.getString("SkinDlg.okButton.text"));
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        okButtonActionPerformed(e);
                    }
                });
                buttonBar.add(okButton, CC.xy(1, 1));

                //---- cancelButton ----
                cancelButton.setText(bundle.getString("SkinDlg.cancelButton.text"));
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cancelButtonActionPerformed(e);
                    }
                });
                buttonBar.add(cancelButton, CC.xy(3, 1));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JComboBox<String> comboBox1;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
