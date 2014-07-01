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

import com.google.gson.reflect.TypeToken;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.*;

public class BtceTradeApi extends BaseTradeApi { // BTC-E trade api
    private StandartObjects.CurrencyPairMapper pairMapper = new StandartObjects.CurrencyPairMapper();

    private void addPairToMapper(String first, String second) {
        StandartObjects.CurrencyPair pair = new StandartObjects.CurrencyPair();
        pair.firstCurrency = first.toUpperCase();
        pair.secondCurrency = second.toUpperCase();
        pair.pairId = (first + "_" + second).toLowerCase();
        pair.pairName = (first + "/" + second).toUpperCase();
        pairMapper.put(pair.pairId, pair);
    }

    public BtceTradeApi() {
        super();
        // Unfortunately i don't know how to retrieve markets list dynamically, so...
        // Crypto/fiat:
        addPairToMapper("BTC", "USD");
        addPairToMapper("BTC", "EUR");
        addPairToMapper("BTC", "RUR");

        addPairToMapper("LTC", "USD");
        addPairToMapper("LTC", "RUR");
        addPairToMapper("LTC", "EUR");

        addPairToMapper("NMC", "USD");
        addPairToMapper("NVC", "USD");
        addPairToMapper("PPC", "USD");

        // Crypto/crypto:
        addPairToMapper("LTC", "BTC");
        addPairToMapper("NMC", "BTC");
        addPairToMapper("NVC", "BTC");
        addPairToMapper("PPC", "BTC");
        addPairToMapper("TRC", "BTC");
        addPairToMapper("FTC", "BTC");
        addPairToMapper("XPM", "BTC");

        // Fiat/fiat:
        addPairToMapper("USD", "RUR");
        addPairToMapper("EUR", "USD");
    }

    public BtceTradeApi(ApiKeyPair pair) {
        this();
        apiKeyPair = pair;
    }

    private static class BtceObjects {
        static class TickerData { // Current market data
            double high;
            double low;
            double avg;
            double vol;
            double vol_cur;
            double last;
            double buy;
            double sell;
            long updated;
            long server_time;
        }

        class Trade { // History
            long date;
            double price;
            double amount;
            long tid;
            String price_currency;
            String item;
            String trade_type; // ask/bid
        }

        static class Depth {
            List<List<Double>> asks = new ArrayList<>(); // [0] - price, [1] - amount
            List<List<Double>> bids = new ArrayList<>();
        }

        static class AccountInfo {
            TreeMap<String, Double> funds = new TreeMap<>();

            private class Rights {
                int info;
                int trade;
            }

            Rights rights = new Rights();
            int transaction_count;
            int open_orders;
            long server_time;
        }

        private static class Order {
            String pair;
            String type; // sell/buy
            double amount;
            double rate;
            long timestamp_created;
            int status;
        }

        private static class AccountTrade extends Order { // History
            long order_id;
            int is_your_order;
            long timestamp;
        }

        private static class OpenOrderStatus {
            double received;
            double remains;
            long order_id;
            TreeMap<String, Double> funds = new TreeMap<>();
        }

        private static class CancelOrderStatus {
            long order_id;
            TreeMap<String, Double> funds = new TreeMap<>();
        }
    }

    // Internal:
    long startNonce = 0;

    @Override
    protected void addNonce(List<NameValuePair> urlParameters) {
        nonce++;
        urlParameters.add(new BasicNameValuePair("nonce", Long.toString(startNonce + nonce)));
    }

    @Override
    protected String executeRequest(boolean needAuth, String url, List<NameValuePair> urlParameters, int httpRequestType) throws IOException {
        String result = super.executeRequest(needAuth, url, urlParameters, httpRequestType);
        while (result.contains("invalid nonce parameter;")) {
            String startStr = "invalid nonce parameter; on key:", endStr = ", you sent:";
            String validNonce = result.substring(result.indexOf(startStr) + startStr.length(), result.indexOf(endStr));
            startNonce = Integer.parseInt(validNonce) + 1;
            result = super.executeRequest(needAuth, url, urlParameters, httpRequestType);
        }
        return result;
    }

    private String publicApiFormatUrl(String pair, String method) {
        return "https://btc-e.com/api/2/" + pair + "/" + method;
    }

    private BtceObjects.TickerData internalGetTicker(String pair) throws IOException {
        String url = publicApiFormatUrl(pair, "ticker");
        String json = executeRequest(false, url, null, Constants.REQUEST_GET);
        TreeMap<String, BtceObjects.TickerData> map = jsonParser.fromJson(json, new TypeToken<TreeMap<String, BtceObjects.TickerData>>() {
        }.getType());
        return map.get("ticker");
    }

    private BtceObjects.Depth internalGetDepth(String pair) throws IOException {
        String url = publicApiFormatUrl(pair, "depth");
        String json = executeRequest(false, url, null, Constants.REQUEST_GET);
        return jsonParser.fromJson(json, new TypeToken<BtceObjects.Depth>() {
        }.getType());
    }

    private List<BtceObjects.Trade> internalGetHistory(String pair) throws IOException {
        String url = publicApiFormatUrl(pair, "trades");
        String json = executeRequest(false, url, null, Constants.REQUEST_GET);
        return jsonParser.fromJson(json, new TypeToken<List<BtceObjects.Trade>>() {
        }.getType());
    }

    private StandartObjects.MarketInfo internalUnifiedGetMarketData(Object pair, boolean retrieveTicker, boolean retrieveOrders, boolean retrieveHistory) throws IOException {


        StandartObjects.MarketInfo marketInfo = new StandartObjects.MarketInfo();
        marketInfo.pairId = pair;

        StandartObjects.CurrencyPair pairInfo = pairMapper.get(pair);
        marketInfo.pairName = pairInfo.pairName;
        marketInfo.firstCurrency = pairInfo.firstCurrency;
        marketInfo.secondCurrency = pairInfo.secondCurrency;

        if (retrieveTicker) {
            BtceObjects.TickerData tickerData = internalGetTicker((String) pair);
            marketInfo.price.average = tickerData.avg;
            marketInfo.price.low = tickerData.low;
            marketInfo.price.high = tickerData.high;
            marketInfo.price.buy = tickerData.buy;
            marketInfo.price.sell = tickerData.sell;
            marketInfo.price.last = tickerData.last;
            marketInfo.price.volume = tickerData.vol_cur;
        }
        if (retrieveOrders) {
            BtceObjects.Depth depth = internalGetDepth((String) pair);
            if (depth.asks != null) for (List<Double> entry : depth.asks) {
                marketInfo.depth.sellOrders.add(new StandartObjects.Order(entry.get(0), entry.get(1)));
            }
            if (depth.bids != null) for (List<Double> entry : depth.bids) {
                marketInfo.depth.buyOrders.add(new StandartObjects.Order(entry.get(0), entry.get(1)));
            }
        }
        if (retrieveHistory) {
            List<BtceObjects.Trade> history = internalGetHistory((String) pair);
            if (history != null) for (BtceObjects.Trade trade : history) {
                StandartObjects.Order order = new StandartObjects.Order();
                order.amount = trade.amount;
                order.id = trade.tid;
                order.pair = pair;
                order.price = trade.price;
                order.time = new Date(trade.date * 1000);
                order.type = trade.trade_type.equals("ask") ? Constants.ORDER_SELL : Constants.ORDER_BUY;
                marketInfo.history.add(order);
            }
        }
        return marketInfo;
    }

    private StandartObjects.AccountInfo internalUnifiedGetAccountInfo(Object pair, boolean retrieveBalance, boolean retrieveOrders, boolean retrieveHistory) throws Exception {
        StandartObjects.AccountInfo accountInfo = new StandartObjects.AccountInfo();
        if (retrieveBalance) {
            ApiStatus<BtceObjects.AccountInfo> accountInfoApiStatus = internalGetAccountInfo();
            if (accountInfoApiStatus.success != 1) {
                throw new TradeApiError("Error retrieving account info (" + accountInfoApiStatus.error + ")");
            }
            accountInfo.balance = new StandartObjects.AccountInfo.AccountBalance();
            for (Map.Entry<String, Double> entry : accountInfoApiStatus.result.funds.entrySet()) { // Fix
                accountInfo.balance.put(entry.getKey().toUpperCase(), entry.getValue());
            }
        }
        if (retrieveOrders) {
            ApiStatus<TreeMap<Long, BtceObjects.Order>> ordersStatus = internalGetOpenOrders((String) pair);
            if (ordersStatus.success != 1 && !ordersStatus.error.contains("no orders")) {
                throw new TradeApiError("Error retrieving orders info (" + ordersStatus.error + ")");
            }
            if (ordersStatus.result != null)
                for (Map.Entry<Long, BtceObjects.Order> entry : ordersStatus.result.entrySet()) {
                    StandartObjects.Order order = new StandartObjects.Order();
                    order.id = entry.getKey();
                    order.amount = entry.getValue().amount;
                    order.pair = entry.getValue().pair;
                    order.price = entry.getValue().rate;
                    order.type = entry.getValue().type.equals("sell") ? Constants.ORDER_SELL : Constants.ORDER_BUY;
                    order.time = new Date(entry.getValue().timestamp_created * 1000);
                    accountInfo.orders.add(order);
                }
        }
        if (retrieveHistory) {
            ApiStatus<TreeMap<Long, BtceObjects.AccountTrade>> accountHistoryStatus = internalGetAccountHistory((String) pair);
            if (accountHistoryStatus.success != 1 && !accountHistoryStatus.error.contains("no trades")) {
                throw new TradeApiError("Error retrieving account history info (" + accountHistoryStatus.error + ")");
            }
            if (accountHistoryStatus.result != null)
                for (Map.Entry<Long, BtceObjects.AccountTrade> tradeEntry : accountHistoryStatus.result.entrySet()) {
                    StandartObjects.Order order = new StandartObjects.Order();
                    order.id = tradeEntry.getKey();
                    order.amount = tradeEntry.getValue().amount;
                    order.pair = tradeEntry.getValue().pair;
                    order.price = tradeEntry.getValue().rate;
                    order.time = new Date(tradeEntry.getValue().timestamp * 1000);
                    order.type = tradeEntry.getValue().type.equals("sell") ? Constants.ORDER_SELL : Constants.ORDER_BUY;
                    accountInfo.history.add(order);
                }
        }
        return accountInfo;
    }

    Map<String, Double> feePercentCache = new HashMap<>();


    @SuppressWarnings("unchecked")
    private double internalGetFeePercent(String pair) throws IOException {
        if (!feePercentCache.containsKey(pair)) {
            String url = publicApiFormatUrl(pair, "fee");
            String json = executeRequest(false, url, null, Constants.REQUEST_GET);
            double percent = ((HashMap<String, Double>) jsonParser.fromJson(json, new TypeToken<HashMap<String, Double>>() {
            }.getType())).get("trade");
            feePercentCache.put(pair, percent);
            return percent;
        } else return feePercentCache.get(pair);
    }

    private final String privateApiUrl = "https://btc-e.com/tapi";

    private ApiStatus<BtceObjects.AccountInfo> internalGetAccountInfo() throws IOException {
        List<NameValuePair> httpParameters = new ArrayList<>(1);
        httpParameters.add(new BasicNameValuePair("method", "getInfo"));
        String json = executeRequest(true, privateApiUrl, httpParameters, Constants.REQUEST_POST);
        return jsonParser.fromJson(json, new TypeToken<ApiStatus<BtceObjects.AccountInfo>>() {
        }.getType());
    }

    private ApiStatus<TreeMap<Long, BtceObjects.Order>> internalGetOpenOrders(String pair) throws IOException {
        List<NameValuePair> httpParameters = new ArrayList<>(1);
        httpParameters.add(new BasicNameValuePair("method", "ActiveOrders"));
        if (pair != null && !pair.isEmpty() && !pair.equals("")) {
            httpParameters.add(new BasicNameValuePair("pair", pair));
        }
        String json = executeRequest(true, privateApiUrl, httpParameters, Constants.REQUEST_POST);
        return jsonParser.fromJson(json, new TypeToken<ApiStatus<TreeMap<Long, BtceObjects.Order>>>() {
        }.getType());
    }

    private ApiStatus<TreeMap<Long, BtceObjects.AccountTrade>> internalGetAccountHistory(String pair) throws IOException {
        List<NameValuePair> httpParameters = new ArrayList<>(1);
        httpParameters.add(new BasicNameValuePair("method", "TradeHistory"));
        if (pair != null && !pair.isEmpty() && !pair.equals("")) {
            httpParameters.add(new BasicNameValuePair("pair", pair));
        }
        String json = executeRequest(true, privateApiUrl, httpParameters, Constants.REQUEST_POST);
        return jsonParser.fromJson(json, new TypeToken<ApiStatus<TreeMap<Long, BtceObjects.AccountTrade>>>() {
        }.getType());
    }

    private ApiStatus<BtceObjects.OpenOrderStatus> internalOpenOrder(String pair, int type, double amount, double price) throws IOException {
        List<NameValuePair> httpParameters = new ArrayList<>(5);
        httpParameters.add(new BasicNameValuePair("method", "Trade"));
        httpParameters.add(new BasicNameValuePair("pair", pair));
        httpParameters.add(new BasicNameValuePair("type", type == Constants.ORDER_SELL ? "sell" : "buy"));
        httpParameters.add(new BasicNameValuePair("rate", Utils.Strings.formatNumber(price)));
        httpParameters.add(new BasicNameValuePair("amount", Utils.Strings.formatNumber(amount)));
        String json = executeRequest(true, privateApiUrl, httpParameters, Constants.REQUEST_POST);
        return jsonParser.fromJson(json, new TypeToken<ApiStatus<BtceObjects.OpenOrderStatus>>() {
        }.getType());
    }

    private ApiStatus<BtceObjects.CancelOrderStatus> internalCancelOrder(long orderId) throws IOException {
        List<NameValuePair> httpParameters = new ArrayList<>(2);
        httpParameters.add(new BasicNameValuePair("method", "CancelOrder"));
        httpParameters.add(new BasicNameValuePair("order_id", Long.toString(orderId)));
        String json = executeRequest(true, privateApiUrl, httpParameters, Constants.REQUEST_POST);
        return jsonParser.fromJson(json, new TypeToken<ApiStatus<BtceObjects.CancelOrderStatus>>() {
        }.getType());
    }

    // Public:
    @Override
    public StandartObjects.CurrencyPairMapper getCurrencyPairs() throws IOException, TradeApiError {
        return pairMapper;
    }

    @Override
    public StandartObjects.Prices getMarketPrices(Object pair) throws Exception {
        return internalUnifiedGetMarketData(pair, true, false, false).price;
    }

    @Override
    public StandartObjects.Depth getMarketDepth(Object pair) throws Exception {
        return internalUnifiedGetMarketData(pair, false, true, false).depth;
    }

    @Override
    public List<StandartObjects.Order> getMarketHistory(Object pair) throws Exception {
        return internalUnifiedGetMarketData(pair, false, false, true).history;
    }

    @Override
    public StandartObjects.AccountInfo.AccountBalance getAccountBalances() throws Exception {
        return internalUnifiedGetAccountInfo(null, true, false, false).balance;
    }

    @Override
    public List<StandartObjects.Order> getAccountOpenOrders(Object pair) throws Exception {
        return internalUnifiedGetAccountInfo(pair, false, true, false).orders;
    }

    @Override
    public List<StandartObjects.Order> getAccountHistory(Object pair) throws Exception {
        return internalUnifiedGetAccountInfo(pair, false, false, true).history;
    }

    @Override
    public StandartObjects.MarketInfo getMarketData(Object pair, boolean retrieveOrders, boolean retrieveHistory) throws Exception {
        return internalUnifiedGetMarketData(pair, true, retrieveOrders, retrieveHistory);
    }

    @Override
    public StandartObjects.AccountInfo getAccountInfo(Object pair, boolean retrieveOrders, boolean retrieveHistory) throws Exception {
        return internalUnifiedGetAccountInfo(pair, true, retrieveOrders, retrieveHistory);
    }

    @Override
    public double getFeePercent(Object pair) throws Exception {
        return internalGetFeePercent((String) pair);
    }

    @Override
    public Object createOrder(Object pair, int orderType, double quantity, double price) throws IOException, TradeApiError {
        ApiStatus<BtceObjects.OpenOrderStatus> orderStatus = internalOpenOrder((String) pair, orderType, quantity, price);
        if (orderStatus.success != 1) {
            throw new TradeApiError("Failed to create order (" + orderStatus.error + ")");
        }
        return orderStatus.result.order_id;
    }

    @Override
    public boolean cancelOrder(Object orderId) throws Exception {
        ApiStatus<BtceObjects.CancelOrderStatus> orderStatus = internalCancelOrder((Long) orderId);
        if (orderStatus.success != 1) {
            throw new TradeApiError("Failed to cancel order (" + orderStatus.error + ")");
        }
        return true;
    }
}
