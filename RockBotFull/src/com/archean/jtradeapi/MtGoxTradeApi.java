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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MtGoxTradeApi extends BaseTradeApi {
    public MtGoxTradeApi(ApiKeyPair pair) {
        super(pair);
    }

    // MtGox compatibility:
    class ApiStatus<ReturnType> {
        String result; // "success" / ???
        String error; // Error message
        ReturnType data; // Response
    }

    @Override
    protected void cleanAuth(List<NameValuePair> urlParameters, List<NameValuePair> httpHeaders) {
        Iterator<NameValuePair> headerIterator = httpHeaders.iterator();
        while (headerIterator.hasNext()) { // Cleaning
            NameValuePair header = headerIterator.next();
            if (header.getName().equals("Rest-Key") || header.getName().equals("Rest-Sign")) {
                headerIterator.remove();
            }
        }
        Iterator<NameValuePair> paramsIterator = urlParameters.iterator();
        while (paramsIterator.hasNext()) { // Cleaning
            NameValuePair header = paramsIterator.next();
            if (header.getName().equals("nonce")) {
                paramsIterator.remove();
            }
        }
    }

    @Override
    protected void writeAuthParams(List<NameValuePair> urlParameters, List<NameValuePair> httpHeaders) {
        if (apiKeyPair == null || apiKeyPair.publicKey.isEmpty() || apiKeyPair.privateKey.isEmpty()) {
            throw new IllegalArgumentException("Invalid API key pair");
        }
        addNonce(urlParameters);
        try {
            httpHeaders.add(new BasicNameValuePair("Rest-Sign", makeSign(urlParameters)));
            httpHeaders.add(new BasicNameValuePair("Rest-Key", apiKeyPair.publicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Internal:
    private static class MtGoxObjects {
        class TickerFast {
            class TickerData {
                double value;
                String currency; // USD, EUR, JPY
            }

            TickerData last_local; // ???
            TickerData last_orig; // ???
            TickerData last_all; // ???
            TickerData last;
            TickerData buy;
            TickerData sell;
            long now; // Timestamp
        }

        class Ticker extends TickerFast {
            TickerData high;
            TickerData low;
            TickerData avg;
            TickerData vwap; // ???
            TickerData vol; // Volume
            String item; // "BTC"
        }

        class Depth {
            class DepthEntry {
                double price;
                double amount;
                long stamp;
            }

            long now; // Timestamp
            long cached; // Timestamp
            List<DepthEntry> asks = new ArrayList<>();
            List<DepthEntry> bids = new ArrayList<>();
            Ticker.TickerData filter_min_price;
            Ticker.TickerData filter_max_price;
        }

        class Trade {
            long date; // Timestamp
            double price;
            double amount;
            long tid; // trade id
            String price_currency; // USD
            String item; // BTC
            String trade_type; // "bid"/"ask"
        }
    }

    private String formatPublicMtGoxApiUrl(String pair, String method) {
        if (pair != null && !pair.equals(""))
            return "http://data.mtgox.com/api/2/" + pair + "/money/" + method;
        else
            return "http://data.mtgox.com/api/2/money/" + method;
    }

    // Ticker (prices):
    @Deprecated
    private ApiStatus<MtGoxObjects.TickerFast> internalGetFastTicker(String pair) throws Exception {
        String url = formatPublicMtGoxApiUrl(pair, "ticker_fast");
        return jsonParser.fromJson(executeRequest(false, url, null, Constants.REQUEST_GET), new TypeToken<ApiStatus<MtGoxObjects.TickerFast>>() {
        }.getType());
    }

    private ApiStatus<MtGoxObjects.Ticker> internalGetTicker(String pair) throws Exception {
        String url = formatPublicMtGoxApiUrl(pair, "ticker");
        return jsonParser.fromJson(executeRequest(false, url, null, Constants.REQUEST_GET), new TypeToken<ApiStatus<MtGoxObjects.Ticker>>() {
        }.getType());
    }

    // Depth:
    private ApiStatus<MtGoxObjects.Depth> internalGetDepth(String pair) throws Exception {
        String url = formatPublicMtGoxApiUrl(pair, "depth");
        return jsonParser.fromJson(executeRequest(false, url, null, Constants.REQUEST_GET), new TypeToken<ApiStatus<MtGoxObjects.Depth>>() {
        }.getType());
    }

    // History:
    private ApiStatus<List<MtGoxObjects.Trade>> internalGetTrades(String pair) throws Exception {
        String url = formatPublicMtGoxApiUrl(pair, "trades");
        return jsonParser.fromJson(executeRequest(false, url, null, Constants.REQUEST_GET), new TypeToken<ApiStatus<List<MtGoxObjects.Trade>>>() {
        }.getType());
    }


    // Public:
    public BaseTradeApi.StandartObjects.CurrencyPairMapper getCurrencyPairs() throws Exception {
        BaseTradeApi.StandartObjects.CurrencyPairMapper mapper = new BaseTradeApi.StandartObjects.CurrencyPairMapper();
        BaseTradeApi.StandartObjects.CurrencyPair btcUsd = new BaseTradeApi.StandartObjects.CurrencyPair();

        // Only BTC/USD :(
        btcUsd.firstCurrency = "BTC";
        btcUsd.secondCurrency = "USD";
        btcUsd.pairId = "BTCUSD";
        btcUsd.pairName = "BTC/USD";

        mapper.put(btcUsd.pairId, btcUsd);
        return mapper;
    }

    // Basic info:
    public BaseTradeApi.StandartObjects.Prices getMarketPrices(Object pair) throws Exception {
        ApiStatus<MtGoxObjects.Ticker> tickerApiStatus = internalGetTicker((String) pair);
        StandartObjects.Prices prices = new StandartObjects.Prices();
        if (!tickerApiStatus.result.equals("success")) {
            throw new TradeApiError("Error retrieving prices data (" + tickerApiStatus.error + ")");
        } else if (tickerApiStatus.data != null) {
            prices.average = tickerApiStatus.data.avg.value;
            prices.last = tickerApiStatus.data.last.value;
            prices.high = tickerApiStatus.data.high.value;
            prices.low = tickerApiStatus.data.low.value;
            prices.buy = tickerApiStatus.data.buy.value;
            prices.sell = tickerApiStatus.data.sell.value;
            prices.volume = tickerApiStatus.data.vol.value;
        }
        return prices;
    }

    public BaseTradeApi.StandartObjects.Depth getMarketDepth(Object pair) throws Exception {
        ApiStatus<MtGoxObjects.Depth> depthApiStatus = internalGetDepth((String) pair);
        StandartObjects.Depth depth = new StandartObjects.Depth();
        if (!depthApiStatus.result.equals("success")) {
            throw new TradeApiError("Error retrieving depth data (" + depthApiStatus.error + ")");
        } else {
            if (depthApiStatus.data.asks != null) for (MtGoxObjects.Depth.DepthEntry entry : depthApiStatus.data.asks) {
                StandartObjects.Order order = new StandartObjects.Order();
                order.amount = entry.amount;
                order.price = entry.price;
                order.pair = pair;
                order.time = new Date(entry.stamp);
                order.type = Constants.ORDER_SELL;
                depth.sellOrders.add(order);
            }
            if (depthApiStatus.data.bids != null) for (MtGoxObjects.Depth.DepthEntry entry : depthApiStatus.data.bids) {
                StandartObjects.Order order = new StandartObjects.Order();
                order.amount = entry.amount;
                order.price = entry.price;
                order.pair = pair;
                order.time = new Date(entry.stamp);
                order.type = Constants.ORDER_BUY;
                depth.buyOrders.add(order);
            }
        }
        return depth;
    }

    public List<BaseTradeApi.StandartObjects.Order> getMarketHistory(Object pair) throws Exception {
        ApiStatus<List<MtGoxObjects.Trade>> historyApiStatus = internalGetTrades((String) pair);
        if (historyApiStatus.data == null) return null;
        final List<StandartObjects.Order> history = new ArrayList<>(historyApiStatus.data.size());
        if (!historyApiStatus.result.equals("success")) {
            throw new TradeApiError("Error retrieving history data (" + historyApiStatus.error + ")");
        } else historyApiStatus.data.forEach(entry -> {
            StandartObjects.Order trade = new StandartObjects.Order();
            trade.amount = entry.amount;
            trade.price = entry.price;
            trade.id = entry.tid;
            trade.pair = pair;
            trade.type = entry.trade_type.equals("ask") ? Constants.ORDER_SELL : Constants.ORDER_BUY;
            trade.time = new Date(entry.date * 1000);
            history.add(trade);
        });
        return history;
    }

    public BaseTradeApi.StandartObjects.AccountInfo.AccountBalance getAccountBalances() throws Exception {
        return null;
    }

    public List<BaseTradeApi.StandartObjects.Order> getAccountOpenOrders(Object pair) throws Exception {
        return null;
    }

    public List<BaseTradeApi.StandartObjects.Order> getAccountHistory(Object pair) throws Exception {
        return null;
    }

    // Consolidated info:
    public BaseTradeApi.StandartObjects.MarketInfo getMarketData(Object pair, boolean retrieveOrders, boolean retrieveHistory) throws Exception {
        return null;
    }

    public BaseTradeApi.StandartObjects.AccountInfo getAccountInfo(Object pair, boolean retrieveOrders, boolean retrieveHistory) throws Exception {
        return null;
    }

    // Misc
    public double getFeePercent(Object pair) throws Exception {
        return 0.60;
    }

    // Trading api:
    public Object createOrder(Object pair, int orderType, double quantity, double price) throws IOException, TradeApiError {
        throw new NotImplementedException();
    }

    public boolean cancelOrder(Object orderId) throws Exception {
        throw new NotImplementedException();
    }
}
