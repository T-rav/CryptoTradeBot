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

import com.archean.jtradeapi.AccountManager;
import com.archean.jtradeapi.BaseTradeApi;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

public class AccountManagerDlg extends JDialog {
    public volatile String selectedAccount = null;
    public volatile AccountManager.AccountDb accountDb = null;

    public void initAccountManagerDlg() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        Map<String, Integer> exchanges = AccountManager.AccountType.listAccountTypes();
        for (Map.Entry<String, Integer> entry : exchanges.entrySet()) {
            model.addElement(entry.getKey());
        }
        comboBoxType.setModel(model);

        DefaultListModel<String> model1 = new DefaultListModel<>();
        for (String key : accountDb.keySet()) {
            model1.addElement(key);
        }
        listAccounts.setModel(model1);
    }

    public AccountManagerDlg(Frame owner, AccountManager.AccountDb accountDb) {
        super(owner);
        initComponents();
        this.accountDb = accountDb;
        initAccountManagerDlg();
    }

    public AccountManagerDlg(Dialog owner, AccountManager.AccountDb accountDb) {
        super(owner);
        initComponents();
        this.accountDb = accountDb;
        initAccountManagerDlg();
    }

    private void okButtonActionPerformed(ActionEvent e) {
        selectedAccount = (String) listAccounts.getSelectedValue();
        setVisible(false);
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        selectedAccount = null;
        setVisible(false);
    }

    private void buttonAddActionPerformed(ActionEvent e) {
        String label = textFieldLabel.getText(), publicKey = textFieldPublic.getText(), privateKey = textFieldPrivate.getText();
        if (accountDb.containsKey(label)) {
            JOptionPane.showMessageDialog(null, "Account with this label already exists");
        } else if (label.length() > 0 && publicKey.length() > 0 && privateKey.length() > 0) {
            accountDb.addAccount(label, AccountManager.AccountType.listAccountTypes().get(comboBoxType.getSelectedItem()), new BaseTradeApi.ApiKeyPair(publicKey, privateKey));
            DefaultListModel model = (DefaultListModel) listAccounts.getModel();
            model.addElement(label);
            textFieldLabel.setText("");
            textFieldPublic.setText("");
            textFieldPrivate.setText("");
        } else {
            JOptionPane.showMessageDialog(null, "Invalid account settings");
        }
    }

    private void buttonDeleteActionPerformed(ActionEvent e) {
        if (listAccounts.getSelectedIndex() >= 0) {
            DefaultListModel model = (DefaultListModel) listAccounts.getModel();
            accountDb.remove(listAccounts.getSelectedValue());
            model.removeElementAt(listAccounts.getSelectedIndex());
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("com.archean.jtradegui.locale");
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        scrollPane1 = new JScrollPane();
        listAccounts = new JList();
        toolBar1 = new JToolBar();
        buttonAdd = new JButton();
        buttonDelete = new JButton();
        labelLabel = new JLabel();
        textFieldLabel = new JTextField();
        labelPublicKey = new JLabel();
        textFieldPublic = new JTextField();
        labelPrivateKey = new JLabel();
        textFieldPrivate = new JTextField();
        labelType = new JLabel();
        comboBoxType = new JComboBox();
        label1 = new JLabel();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setModal(true);
        setTitle(bundle.getString("AccountManager.this.title"));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(Borders.createEmptyBorder("7dlu, 7dlu, 7dlu, 7dlu"));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new FormLayout(
                        "75dlu, $lcgap, 65dlu, $lcgap, 110dlu, $lcgap, [55dlu,default]:grow",
                        "17dlu, $lgap, 15dlu, 2*($lgap, default), $lgap, 15dlu, $lgap, fill:default:grow"));

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(listAccounts);
                }
                contentPanel.add(scrollPane1, CC.xywh(1, 1, 1, 11));

                //======== toolBar1 ========
                {
                    toolBar1.setFloatable(false);

                    //---- buttonAdd ----
                    buttonAdd.setIcon(new ImageIcon(getClass().getResource("/res/icons/plus.png")));
                    buttonAdd.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            buttonAddActionPerformed(e);
                        }
                    });
                    toolBar1.add(buttonAdd);

                    //---- buttonDelete ----
                    buttonDelete.setIcon(new ImageIcon(getClass().getResource("/res/icons/delete.png")));
                    buttonDelete.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            buttonDeleteActionPerformed(e);
                        }
                    });
                    toolBar1.add(buttonDelete);
                }
                contentPanel.add(toolBar1, CC.xywh(3, 1, 5, 1, CC.LEFT, CC.DEFAULT));

                //---- labelLabel ----
                labelLabel.setText(bundle.getString("AccountManager.labelLabel.text"));
                labelLabel.setLabelFor(textFieldLabel);
                contentPanel.add(labelLabel, CC.xy(3, 3));
                contentPanel.add(textFieldLabel, CC.xywh(5, 3, 3, 1));

                //---- labelPublicKey ----
                labelPublicKey.setText(bundle.getString("AccountManager.labelPublicKey.text"));
                labelPublicKey.setLabelFor(textFieldPublic);
                contentPanel.add(labelPublicKey, CC.xy(3, 5));
                contentPanel.add(textFieldPublic, CC.xywh(5, 5, 3, 1));

                //---- labelPrivateKey ----
                labelPrivateKey.setText(bundle.getString("AccountManager.labelPrivateKey.text"));
                labelPrivateKey.setLabelFor(textFieldPrivate);
                contentPanel.add(labelPrivateKey, CC.xy(3, 7));
                contentPanel.add(textFieldPrivate, CC.xywh(5, 7, 3, 1));

                //---- labelType ----
                labelType.setText(bundle.getString("AccountManager.labelType.text"));
                labelType.setLabelFor(comboBoxType);
                contentPanel.add(labelType, CC.xy(3, 9));
                contentPanel.add(comboBoxType, CC.xywh(5, 9, 3, 1));

                //---- label1 ----
                label1.setText(bundle.getString("AccountManager.label1.text"));
                label1.setFont(label1.getFont().deriveFont(label1.getFont().getStyle() | Font.ITALIC));
                contentPanel.add(label1, CC.xywh(3, 11, 5, 1, CC.CENTER, CC.DEFAULT));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(Borders.createEmptyBorder("5dlu, 0dlu, 0dlu, 0dlu"));
                buttonBar.setLayout(new FormLayout(
                        "$glue, $button, $rgap, $button",
                        "pref"));

                //---- okButton ----
                okButton.setText(bundle.getString("AccountManager.okButton.text"));
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        okButtonActionPerformed(e);
                    }
                });
                buttonBar.add(okButton, CC.xy(2, 1));

                //---- cancelButton ----
                cancelButton.setText(bundle.getString("AccountManager.cancelButton.text"));
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cancelButtonActionPerformed(e);
                    }
                });
                buttonBar.add(cancelButton, CC.xy(4, 1));
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
    private JScrollPane scrollPane1;
    private JList listAccounts;
    private JToolBar toolBar1;
    private JButton buttonAdd;
    private JButton buttonDelete;
    private JLabel labelLabel;
    private JTextField textFieldLabel;
    private JLabel labelPublicKey;
    private JTextField textFieldPublic;
    private JLabel labelPrivateKey;
    private JTextField textFieldPrivate;
    private JLabel labelType;
    private JComboBox comboBoxType;
    private JLabel label1;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
