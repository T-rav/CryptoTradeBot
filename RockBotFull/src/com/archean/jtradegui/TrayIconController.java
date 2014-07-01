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

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TrayIconController {
    static public TrayIcon trayIcon;

    public static void showMessage(final String title, final String message, final TrayIcon.MessageType messageType) {
        trayIcon.displayMessage(title,
                message,
                messageType);
    }

    public static boolean createTrayIcon(final JPopupMenu popup, final String hint, final Image image) {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            trayIcon = new TrayIcon(image, hint);

            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        popup.setLocation(e.getX(), e.getY());
                        popup.setInvoker(popup);
                        popup.setVisible(true);
                    }
                }
            });

            try {
                tray.add(trayIcon);
                return true;
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
        }
        return false;
    }
}
