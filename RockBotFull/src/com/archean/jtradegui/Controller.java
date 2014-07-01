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

import com.archean.coinmarketcap.CoinMarketCapParser;
import com.archean.jautotrading.MarketRule;
import com.archean.jtradeapi.AccountManager;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import org.apache.commons.io.FileUtils;

public class Controller extends JFrame {
    public AccountManager.AccountDb accountDb = new AccountManager.AccountDb();
    protected final static String settingsDir = System.getProperty("user.home") + "/jCryptoTrader/";
    protected final static String accountDbFile = settingsDir + "accounts.json";
    protected final static String tabsDbFile = settingsDir + "tabs.dat";

    public void loadAccountDb() {
        try {
            accountDb.loadFromJson(FileUtils.readFileToString(new File(accountDbFile), "UTF-8"));
        } catch (IOException e) {
            // nothing
        }
    }

    public void saveAccountDb() {
        try {
            FileUtils.writeStringToFile(new File(accountDbFile), accountDb.saveToJson(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class SavedTab implements Serializable {
        String accountLabel;
        double feePercent;
        int timeInterval;
        String pair;
        Map<String, MarketRule.MarketRuleList> ruleListDb = new HashMap<>();

        public SavedTab(String accountLabel, TraderMainForm traderPanel) {
            this.accountLabel = accountLabel;
            Map<String, Object> settings = traderPanel.getSettings();
            feePercent = (Double) settings.get("feePercent");
            timeInterval = (Integer) settings.get("timeInterval");
            pair = (String) settings.get("currentPair");
            ruleListDb = (Map<String, MarketRule.MarketRuleList>) settings.get("marketRuleListDb");
        }
    }

    private void saveTabs() {
        List<SavedTab> savedTabList = new ArrayList<>(tabbedPaneTraders.getTabCount());
        for (int i = 0; i < tabbedPaneTraders.getTabCount(); i++) {
            TraderMainForm traderMainForm = (TraderMainForm) tabbedPaneTraders.getComponentAt(i);
            savedTabList.add(new SavedTab(tabbedPaneTraders.getTitleAt(i), traderMainForm));
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(tabsDbFile);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeObject(savedTabList);
            objectOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTabs() {
        try (FileInputStream fileInputStream = new FileInputStream(tabsDbFile);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)
        ) {
            List<SavedTab> savedTabList = (List<SavedTab>) objectInputStream.readObject();
            for (SavedTab tab : savedTabList) {
                TraderMainForm traderMainForm = new TraderMainForm(accountDb.get(tab.accountLabel));
                tabbedPaneTraders.add(tab.accountLabel, traderMainForm);
                traderMainForm.setSettings(tab.feePercent, tab.timeInterval);
                traderMainForm.setPair(tab.pair);
                traderMainForm.setRuleListDb(tab.ruleListDb);
                traderMainForm.startWorker();
            }
        } catch (IOException e) {
            // nothing
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(SkinDlg.getLookAndFeelClassName("Aluminium"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        new File(settingsDir).mkdirs();

        final Controller controller = new Controller();
        controller.loadAccountDb();
        controller.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        controller.pack();
        controller.setIconImage(new ImageIcon(controller.getClass().getResource("/res/icons/logo.png")).getImage());
        controller.setVisible(true);
        TrayIconController.createTrayIcon(controller.popupMenuTray, "jTrader", new ImageIcon(controller.getClass().getResource("/res/icons/logo.png")).getImage());
        TrayIconController.trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!e.isPopupTrigger()) {
                    if (!controller.isVisible()) {
                        controller.setVisible(true);
                        int state = controller.getExtendedState();
                        state &= ~JFrame.ICONIFIED;
                        controller.setExtendedState(state);
                        controller.setAlwaysOnTop(true);
                        controller.toFront();
                        controller.requestFocus();
                        controller.setAlwaysOnTop(false);
                    } else {
                        controller.setVisible(false);
                    }
                }
            }
        });
        CoinMarketCapParser.getWorker().start();
        controller.loadTabs();
    }

    public Controller() {
        initComponents();
    }

    public void addTab(final String label, final AccountManager.Account account) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TraderMainForm panel = new TraderMainForm(account);
                synchronized (tabbedPaneTraders) {
                    tabbedPaneTraders.addTab(label, panel);
                }
                panel.startWorker();
            }
        }).start();
    }

    private void buttonAddActionPerformed(ActionEvent e) {
        AccountManagerDlg accountManagerDlg = new AccountManagerDlg(this, accountDb);
        accountManagerDlg.setVisible(true); // modal
        if(accountManagerDlg.selectedAccount != null) {
            saveAccountDb();
            addTab(accountManagerDlg.selectedAccount, accountDb.get(accountManagerDlg.selectedAccount));
            saveTabs();
        }
    }

    private void buttonDeleteActionPerformed(ActionEvent e) {
        synchronized (tabbedPaneTraders) {
            if (tabbedPaneTraders.getTabCount() > 0 && tabbedPaneTraders.getSelectedIndex() >= 0) {
                TraderMainForm traderMainForm = (TraderMainForm) tabbedPaneTraders.getSelectedComponent();
                traderMainForm.killThreads();
                tabbedPaneTraders.remove(tabbedPaneTraders.getSelectedIndex());
                tabbedPaneTraders.revalidate();
                saveTabs();
            }
        }
    }

    private void menuItemExitActionPerformed(ActionEvent e) {
        System.exit(0);
    }

    private void thisWindowClosing(WindowEvent e) {
        saveAccountDb();
        saveTabs();
    }

    private void thisWindowStateChanged(WindowEvent e) {
        if (e.getNewState() == ICONIFIED) {
            try {
                setVisible(false);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }

    private void buttonSkinActionPerformed(ActionEvent e) {
        SkinDlg skinDlg = new SkinDlg(this);
        skinDlg.setVisible(true);
    }

    private void buttonAboutActionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "jCryptoTrader trading client\n" +
                "Copyright (C) 2014 1M4SKfh83ZxsCSDmfaXvfCfMonFxMa5vvh (BTC public key)\n" +
                "\n" +
                "This program is free software; you can redistribute it and/or\n" +
                "modify it under the terms of the GNU General Public License\n" +
                "as published by the Free Software Foundation; either version 2\n" +
                "of the License, or (at your option) any later version.");
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("com.archean.jtradegui.locale", new UTF8Control());
        toolBar1 = new JToolBar();
        buttonAdd = new JButton();
        buttonDelete = new JButton();
        buttonSkin = new JButton();
        buttonAbout = new JButton();
        tabbedPaneTraders = new JTabbedPane();
        popupMenuTray = new JPopupMenu();
        menuItemExit = new JButton();

        //======== this ========
        setTitle(bundle.getString("Controller.this.title"));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
        });
        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                thisWindowStateChanged(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
                "[350dlu,default]:grow",
                "top:16dlu, $lgap, fill:[420dlu,default]:grow"));

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

            //---- buttonSkin ----
            buttonSkin.setIcon(new ImageIcon(getClass().getResource("/res/icons/skin.png")));
            buttonSkin.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttonSkinActionPerformed(e);
                }
            });
            toolBar1.add(buttonSkin);

            //---- buttonAbout ----
            buttonAbout.setIcon(new ImageIcon(getClass().getResource("/res/icons/info.png")));
            buttonAbout.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttonAboutActionPerformed(e);
                }
            });
            toolBar1.add(buttonAbout);
        }
        contentPane.add(toolBar1, CC.xy(1, 1, CC.FILL, CC.TOP));
        contentPane.add(tabbedPaneTraders, CC.xy(1, 3));
        pack();
        setLocationRelativeTo(getOwner());

        //======== popupMenuTray ========
        {

            //---- menuItemExit ----
            menuItemExit.setText(bundle.getString("Controller.menuItemExit.text"));
            menuItemExit.setIcon(new ImageIcon(getClass().getResource("/res/icons/delete.png")));
            menuItemExit.setHorizontalAlignment(SwingConstants.LEFT);
            menuItemExit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    menuItemExitActionPerformed(e);
                }
            });
            popupMenuTray.add(menuItemExit);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JToolBar toolBar1;
    private JButton buttonAdd;
    private JButton buttonDelete;
    private JButton buttonSkin;
    private JButton buttonAbout;
    private JTabbedPane tabbedPaneTraders;
    private JPopupMenu popupMenuTray;
    private JButton menuItemExit;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
