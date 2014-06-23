/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kungfuactiongrip.exchange.io;

import com.kungfuactiongrip.exchange.ExchangeList;

/**
 *
 * @author Administrator
 */
public class IOFactory {

    public static IBotIO CreateIOObject(String propertyFile, ExchangeList exchange) {
        // Use the properties file passed in to create the db stuff against ;)
        if (propertyFile == null) {
            return new BotIOImpl();
        }

        return new BotIOImpl(propertyFile, exchange);
    }
}
