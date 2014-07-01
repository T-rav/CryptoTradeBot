/*
 * jCryptoTrader trading client
 * Copyright (C) 2014 1M4SKfh83ZxsCSDmfaXvfCfMonFxMa5vvh (BTC public key)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 */

package com.archean.jtradeapi;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

public
@NoArgsConstructor
class ApiWorker extends Utils.Threads.UniqueHandlerObserver<ApiWorker.ApiDataUpdateEvent> implements AutoCloseable {
    public interface ApiDataUpdateEvent {
        public void onMarketPricesUpdate(BaseTradeApi.StandartObjects.Prices data);

        public void onMarketDepthUpdate(BaseTradeApi.StandartObjects.Depth data);

        public void onMarketHistoryUpdate(List<BaseTradeApi.StandartObjects.Order> data);

        public void onAccountBalancesUpdate(BaseTradeApi.StandartObjects.AccountInfo.AccountBalance data);

        public void onAccountOrdersUpdate(List<BaseTradeApi.StandartObjects.Order> data);

        public void onAccountHistoryUpdate(List<BaseTradeApi.StandartObjects.Order> data);

        public void onError(Exception e);
    }

    private void onErrorEvent(Exception e) {
        synchronized (eventHandlers) {
            for (ApiDataUpdateEvent event : eventHandlers.values()) {
                event.onError(e);
            }
        }
    }

    private void onUpdateEvent(ApiDataType type, Object data) {
        synchronized (eventHandlers) {
            for (ApiDataUpdateEvent event : eventHandlers.values()) {
                switch (type) {
                    case MARKET_PRICES:
                        event.onMarketPricesUpdate((BaseTradeApi.StandartObjects.Prices) data);
                        break;
                    case MARKET_DEPTH:
                        event.onMarketDepthUpdate((BaseTradeApi.StandartObjects.Depth) data);
                        break;
                    case MARKET_HISTORY:
                        event.onMarketHistoryUpdate((List<BaseTradeApi.StandartObjects.Order>) data);
                        break;
                    case ACCOUNT_BALANCES:
                        event.onAccountBalancesUpdate((BaseTradeApi.StandartObjects.AccountInfo.AccountBalance) data);
                        break;
                    case ACCOUNT_ORDERS:
                        event.onAccountOrdersUpdate((List<BaseTradeApi.StandartObjects.Order>) data);
                        break;
                    case ACCOUNT_HISTORY:
                        event.onAccountHistoryUpdate((List<BaseTradeApi.StandartObjects.Order>) data);
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
        }
    }

    public static enum ApiDataType {
        MARKET_PRICES, MARKET_DEPTH, MARKET_HISTORY,
        ACCOUNT_BALANCES, ACCOUNT_ORDERS, ACCOUNT_HISTORY
    }

    private class ApiWorkerTask extends Utils.Threads.CycledRunnable {
        ApiDataType apiDataType;

        public ApiWorkerTask(ApiDataType dataType) {
            this.apiDataType = dataType;
        }

        private Object retrieveData() throws Exception {
            switch (apiDataType) {
                case MARKET_PRICES:
                    return tradeApi.getMarketPrices(pair);
                case MARKET_DEPTH:
                    return tradeApi.getMarketDepth(pair);
                case MARKET_HISTORY:
                    return tradeApi.getMarketHistory(pair);
                case ACCOUNT_BALANCES:
                    return tradeApi.getAccountBalances();
                case ACCOUNT_ORDERS:
                    return tradeApi.getAccountOpenOrders(pair);
                case ACCOUNT_HISTORY:
                    return tradeApi.getAccountHistory(pair);
                default:
                    throw new IllegalArgumentException();
            }
        }

        private void updateWorkerData(Object data) {
            switch (apiDataType) {
                case MARKET_PRICES:
                    marketInfo.price = (BaseTradeApi.StandartObjects.Prices) data;
                    break;
                case MARKET_DEPTH:
                    marketInfo.depth = (BaseTradeApi.StandartObjects.Depth) data;
                    break;
                case MARKET_HISTORY:
                    marketInfo.history = (List<BaseTradeApi.StandartObjects.Order>) data;
                    break;
                case ACCOUNT_BALANCES:
                    accountInfo.balance = (BaseTradeApi.StandartObjects.AccountInfo.AccountBalance) data;
                    break;
                case ACCOUNT_ORDERS:
                    accountInfo.orders = (List<BaseTradeApi.StandartObjects.Order>) data;
                    break;
                case ACCOUNT_HISTORY:
                    accountInfo.history = (List<BaseTradeApi.StandartObjects.Order>) data;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        @Override
        protected int onError(Exception e) {
            e.printStackTrace();
            onErrorEvent(new Exception("ApiWorker error: " + e.getLocalizedMessage(), e));
            return 10 * 1000; // 10s
        }

        private Object data = null;

        @Override
        protected int cycle() throws Exception {
            data = retrieveData();
            if (data != null && !Thread.currentThread().isInterrupted()) {
                updateWorkerData(data);
                onUpdateEvent(apiDataType, data);
                return timeInterval;
            } else {
                return STOP_CYCLE;
            }
        }
    }

    volatile public BaseTradeApi.StandartObjects.AccountInfo accountInfo = new BaseTradeApi.StandartObjects.AccountInfo();
    volatile public BaseTradeApi.StandartObjects.MarketInfo marketInfo = new BaseTradeApi.StandartObjects.MarketInfo();
    volatile public BaseTradeApi tradeApi = null;
    volatile private
    @Getter
    @Setter
    int timeInterval = 500;
    volatile private
    @Getter
    @Setter
    Object pair = null;
    private Map<ApiDataType, Thread> threadMap = new HashMap<>();

    synchronized public boolean isThreadRunning(final ApiDataType dataType) {
        return threadMap.containsKey(dataType) && threadMap.get(dataType).isAlive();
    }

    synchronized public void stopThread(final ApiDataType dataType) {
        Thread workerThread = threadMap.get(dataType);
        if (workerThread != null) {
            if (workerThread.isAlive()) {
                workerThread.interrupt();
            }
            threadMap.remove(dataType);
        }
    }

    synchronized public void startThread(final ApiDataType dataType) {
        stopThread(dataType);
        Thread workerThread = new Thread(new ApiWorkerTask(dataType));
        threadMap.put(dataType, workerThread);
        workerThread.start();
    }

    synchronized public void setActiveThreads(final List<ApiDataType> activeThreads) {
        Map<ApiDataType, Thread> newMap = new HashMap<>();
        for (Map.Entry<ApiDataType, Thread> entry : threadMap.entrySet()) {
            if (!activeThreads.contains(entry.getKey())) {
                entry.getValue().interrupt();
            } else {
                newMap.put(entry.getKey(), entry.getValue());
            }
        }
        for (ApiDataType dataType : activeThreads) {
            if (!newMap.containsKey(dataType)) {
                Thread thread = new Thread(new ApiWorkerTask(dataType));
                newMap.put(dataType, thread);
                thread.start();
            }
        }
        threadMap = newMap;
    }

    synchronized public void setActiveThreads(final ApiDataType[] activeThreads) {
        setActiveThreads(Arrays.asList(activeThreads));
    }

    synchronized public void stopAllThreads() {
        for (ApiDataType dataType : threadMap.keySet()) {
            threadMap.get(dataType).interrupt();
        }
        threadMap.clear();
    }

    public void close() {
        stopAllThreads();
    }

    public ApiWorker initTradeApiInstance(int accountType, BaseTradeApi.ApiKeyPair apiKeyPair) {
        this.tradeApi = AccountManager.tradeApiInstance(accountType, apiKeyPair);
        return this;
    }

    public ApiWorker initTradeApiInstance(AccountManager.Account account) {
        this.tradeApi = AccountManager.tradeApiInstance(account);
        return this;
    }
}
