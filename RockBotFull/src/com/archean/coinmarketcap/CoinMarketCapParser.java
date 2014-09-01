/*
 * jCryptoTrader trading client
 * Copyright (C) 2014 1M4SKfh83ZxsCSDmfaXvfCfMonFxMa5vvh (BTC public key)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 */

package com.archean.coinmarketcap;

import com.archean.jtradeapi.BaseTradeApi;
import com.archean.jtradeapi.Utils;
import com.kungfuactiongrip.coinmarketcap.CoinCapitalization;
import com.kungfuactiongrip.coinmarketcap.CoinMarketCapParserV2;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CoinMarketCapParser {
    static ResourceBundle settings = ResourceBundle.getBundle("com.archean.jtradegui.settings");
    private final static String coinMarketCapJsonUrl = settings.getString("coinmarketcap.url");
    
    private String requestPage() throws IOException {
        BaseTradeApi.RequestSender requestSender = new BaseTradeApi.RequestSender();
        return requestSender.getResponseString(requestSender.getRequest(coinMarketCapJsonUrl, new ArrayList<>(), new ArrayList<>()));
    }

    public List<CoinCapitalization> getData() throws IOException {
        String response = requestPage();
        CoinMarketCapParserV2 cmcpv2 = new CoinMarketCapParserV2();
        return cmcpv2.Parse(response,true);
    }

    // Worker:
    public interface CoinMarketCapEvent {
        public void onDataUpdate(final List<CoinCapitalization> data);
        public void onError(final Exception e);
    }

    public static class CoinMarketCapWorker extends Utils.Threads.UniqueHandlerObserver<CoinMarketCapEvent> implements AutoCloseable {
        private void onErrorEvent(final Exception e) {
            synchronized (eventHandlers) {
                eventHandlers.values().stream().forEach((event) -> {
                    event.onError(e);
                });
            }
        }
        private void onDataUpdateEvent(final List<CoinCapitalization> data) {
            synchronized (eventHandlers) {
                eventHandlers.values().stream().forEach((event) -> {
                    event.onDataUpdate(data);
                });
            }
        }
        private final Runnable marketCapUpdateTask = new Utils.Threads.CycledRunnable() {
            private final static int UPDATE_INTERVAL = 10 * 1000; // 10 sec
            private final CoinMarketCapParser parser = new CoinMarketCapParser();

            @Override
            protected int onError(Exception e) {
                onErrorEvent(new Exception("Error retrieving market cap data", e));
                return UPDATE_INTERVAL;
            }

            @Override
            protected int cycle() throws Exception {
                onDataUpdateEvent(parser.getData());
                return UPDATE_INTERVAL;
            }
        };
        private Thread marketCapUpdateThread;
        public void start() {
            stop();
            marketCapUpdateThread = new Thread(marketCapUpdateTask);
            marketCapUpdateThread.start();
        }
        public void stop() {
            if(marketCapUpdateThread != null && marketCapUpdateThread.isAlive()) {
                marketCapUpdateThread.interrupt();
            }
        }
        @Override
        public void close() {
            stop();
        }
    }

    private static CoinMarketCapWorker worker = null;

    public static CoinMarketCapWorker getWorker() {
        if (worker == null) {
            worker = new CoinMarketCapWorker();
        }
        return worker;
    }
}
