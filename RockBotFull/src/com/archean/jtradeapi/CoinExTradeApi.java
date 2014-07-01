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

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class CoinExTradeApi extends BaseTradeApi {
    public CoinExTradeApi(ApiKeyPair keyPair) {
        super(keyPair);
    }

    // Fixes:

    protected String makeSign(String signString) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        return Utils.Crypto.Hashing.hmacDigest(signString, apiKeyPair.privateKey, Utils.Crypto.Hashing.SHA512);
    }

    protected String executePostJsonRequest(String url, String jsonEntity) {
        List<NameValuePair> httpHeaders = new ArrayList<>(1);
        httpHeaders.add(new BasicNameValuePair("Content-Type", "application/json"));
        writeAuthParams(jsonEntity, httpHeaders);
        return requestSender.getResponseString(requestSender.rawPostRequest(url, new StringEntity(jsonEntity), httpHeaders));
    }
    protected String executeRequest(boolean needAuth, String url, List<NameValuePair> urlParameters, int httpRequestType) throws IOException {
        if (urlParameters == null) urlParameters = new ArrayList<>(); // empty list
        List<NameValuePair> httpHeaders = new ArrayList<>();
        cleanAuth(urlParameters, httpHeaders);
        if (needAuth) writeAuthParams("", httpHeaders);
        switch (httpRequestType) {
            case Constants.REQUEST_GET:
                return requestSender.getResponseString(requestSender.getRequest(url, urlParameters, httpHeaders));
            case Constants.REQUEST_POST:
                throw new NotImplementedException();
            default:
                throw new IllegalArgumentException("Unknown httpRequestType value");
        }
    }

    protected void cleanAuth(List<NameValuePair> urlParameters, List<NameValuePair> httpHeaders) {
        Iterator<NameValuePair> headerIterator = httpHeaders.iterator();
        while (headerIterator.hasNext()) { // Cleaning
            NameValuePair header = headerIterator.next();
            if (header.getName().equals("API-Key") || header.getName().equals("API-Sign")) {
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

    protected void writeAuthParams(String signString, List<NameValuePair> httpHeaders) {
        if (apiKeyPair == null || apiKeyPair.publicKey.isEmpty() || apiKeyPair.privateKey.isEmpty()) {
            throw new IllegalArgumentException("Invalid API key pair");
        }
        try {
            httpHeaders.add(new BasicNameValuePair("API-Sign", makeSign(signString)));
            httpHeaders.add(new BasicNameValuePair("API-Key", apiKeyPair.publicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Internal:
    private static class CoinExObjects {
        public static class CurrencyPair {
            int id;
            // double buy_fee;
            // double sell_fee;
            long last_price;
            // int currency_id;
            // int market_id;
            String url_slug;
            long rate_min;
            long rate_max;
            // long currency_volume;
            long market_volume;
            // Date updated_at;
        }
        public static class Trade {
            long id;
            String created_at;
            boolean bid;
            long rate;
            long amount;
            // int trade_pair_id;
        }
        public static class Order extends Trade {
            // long filled;
            boolean cancelled;
            boolean complete;
            // Date updated_at;
        }
        public static class Balance {
            // int id;
            // int currency_id;
            String currency_name;
            long amount;
            // long held;
            // String deposit_address;
            // Date updated_at;
        }
    }
    private static final String COINEX_BASE_API_URL = "https://coinex.pw/api/v2/";
    private static String formatCoinExApiUrl(String method) {
        return COINEX_BASE_API_URL + method;
    }
    private static final DateFormat dateShitNormalizer = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static Date getNormalDateNotShit(String shit) {
        try {
            return dateShitNormalizer.parse(shit);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }
    private static final BigDecimal shitNormalizer = new BigDecimal(0.00000001, MathContext.DECIMAL64);
    private double getNormalNumberNotShit(long shit) {
        return new BigDecimal(shit, MathContext.DECIMAL64).multiply(shitNormalizer).doubleValue();
    }

    private List<StandartObjects.Order> internalConvertOrders(List<CoinExObjects.Order> orderList, int marketId) {
        final List<StandartObjects.Order> orders = new ArrayList<>(orderList.size());
        orderList.forEach(order -> {
            StandartObjects.Order stdOrder = new StandartObjects.Order();
            stdOrder.amount = getNormalNumberNotShit(order.amount);
            stdOrder.price = getNormalNumberNotShit(order.rate);
            stdOrder.type = order.bid ? Constants.ORDER_BUY : Constants.ORDER_SELL;
            stdOrder.pair = marketId;
            stdOrder.time = getNormalDateNotShit(order.created_at);
            orders.add(stdOrder);
        });
        return orders;
    }

    @SuppressWarnings("unchecked")
    private List<CoinExObjects.CurrencyPair> internalGetCurrencyPairs() throws IOException, TradeApiError {
        String url = formatCoinExApiUrl("trade_pairs");
        String response = executeRequest(false, url, null, Constants.REQUEST_GET);
        try{return (((HashMap<String, List<CoinExObjects.CurrencyPair>>)jsonParser.fromJson(response, new TypeToken<HashMap<String, List<CoinExObjects.CurrencyPair>>>(){}.getType())).get("trade_pairs")); }
        catch(JsonSyntaxException e) {
            throw new TradeApiError(response);
        }
    }

    @SuppressWarnings("unchecked")
    private List<CoinExObjects.Order> internalGetOrders(String url, List<NameValuePair> urlParameters, boolean authNeeded) throws IOException, TradeApiError {
        String response = executeRequest(authNeeded, url, urlParameters, Constants.REQUEST_GET);
        try {return (((HashMap<String, List<CoinExObjects.Order>>)jsonParser.fromJson(response, new TypeToken<HashMap<String, List<CoinExObjects.Order>>>(){}.getType())).get("orders"));}
        catch(JsonSyntaxException e) {
            throw new TradeApiError(response);
        }
    }
    private List<CoinExObjects.Order> internalGetMarketOrders(int marketId) throws IOException, TradeApiError {
        String url = formatCoinExApiUrl("orders");
        List<NameValuePair> urlParameters = new ArrayList<>(1);
        urlParameters.add(new BasicNameValuePair("tradePair", Integer.toString(marketId)));
        return internalGetOrders(url, urlParameters, false);
    }

    @SuppressWarnings("unchecked")
    private List<CoinExObjects.Trade> internalGetMarketHistory(int marketId) throws IOException, TradeApiError {
        String url = formatCoinExApiUrl("trades");
        List<NameValuePair> urlParameters = new ArrayList<>(1);
        urlParameters.add(new BasicNameValuePair("tradePair", Integer.toString(marketId)));
        String response = executeRequest(false, url, urlParameters, Constants.REQUEST_GET);
        try {return (((HashMap<String, List<CoinExObjects.Trade>>)jsonParser.fromJson(response, new TypeToken<HashMap<String, List<CoinExObjects.Trade>>>(){}.getType())).get("trades")); }
        catch(JsonSyntaxException e) {
            throw new TradeApiError(response);
        }
    }

    @SuppressWarnings("unchecked")
    private List<CoinExObjects.Balance> internalGetAccountBalances() throws IOException, TradeApiError {
        String url = formatCoinExApiUrl("balances");
        String response = executeRequest(true, url, null, Constants.REQUEST_GET);
        try{return (((HashMap<String, List<CoinExObjects.Balance>>)jsonParser.fromJson(response, new TypeToken<HashMap<String, List<CoinExObjects.Balance>>>(){}.getType())).get("balances"));}
        catch(JsonSyntaxException e) {
            throw new TradeApiError(response);
        }
    }

    private List<CoinExObjects.Order> internalGetAccountOrders(int marketId) throws IOException, TradeApiError {
        String url = formatCoinExApiUrl(String.format("orders/own?tradePair=%d", marketId));
        return internalGetOrders(url, null, true);
    }

    private static String formatSubmitOrderRequest(int marketId, int orderType, double amount, double price) {
        final String template = "{\"order\":{\"trade_pair_id\": %d, \"amount\": %d, \"bid\": %s, \"rate\": %d}}";
        return String.format(template, marketId, new BigDecimal(amount).divide(new BigDecimal(Calculator.MINIMAL_AMOUNT), 8, RoundingMode.FLOOR).intValue(), orderType == Constants.ORDER_BUY ? "true" : "false", new BigDecimal(price).divide(new BigDecimal(Calculator.MINIMAL_AMOUNT), 8, RoundingMode.FLOOR).intValue());
    }

    @SuppressWarnings("unchecked")
    private List<CoinExObjects.Order> internalSubmitOrder(int marketId, int orderType, double amount, double price) throws TradeApiError {
        String response = executePostJsonRequest(formatCoinExApiUrl("orders"), formatSubmitOrderRequest(marketId, orderType, amount, price));
        try{return (((HashMap<String, List<CoinExObjects.Order>>)jsonParser.fromJson(response, new TypeToken<HashMap<String, List<CoinExObjects.Order>>>(){}.getType())).get("orders"));}
        catch(Exception e) {
            throw new TradeApiError(response);
        }
    }

    private List<CoinExObjects.Order> internalCancelOrder(int orderId) throws IOException, TradeApiError {
        String url = formatCoinExApiUrl(String.format("orders/%d/cancel", orderId));
        return internalGetOrders(url, null, true);
    }


    // Public:
    private StandartObjects.CurrencyPairMapper pairMapper = null;
    public StandartObjects.CurrencyPairMapper getCurrencyPairs() throws Exception {
        if(pairMapper == null) {
            pairMapper = new StandartObjects.CurrencyPairMapper();
            List<CoinExObjects.CurrencyPair> currencyPairs = internalGetCurrencyPairs();
            for(CoinExObjects.CurrencyPair currencyPair : currencyPairs) {
                StandartObjects.CurrencyPair stdCurrencyPair = new StandartObjects.CurrencyPair();
                String[] urlSlugSplit = currencyPair.url_slug.split("_");
                stdCurrencyPair.firstCurrency = urlSlugSplit[0].toUpperCase();
                stdCurrencyPair.secondCurrency = urlSlugSplit[1].toUpperCase();
                stdCurrencyPair.pairId = currencyPair.id;
                stdCurrencyPair.pairName = String.format("%s/%s", stdCurrencyPair.firstCurrency, stdCurrencyPair.secondCurrency);
                pairMapper.put(stdCurrencyPair.pairId, stdCurrencyPair);
            }
        }
        return pairMapper;
    }

    // Basic info:
    public StandartObjects.Prices getMarketPrices(Object pair) throws Exception {
        StandartObjects.Prices prices = new StandartObjects.Prices();
        List<CoinExObjects.CurrencyPair> currencyPairs = internalGetCurrencyPairs();
        for(CoinExObjects.CurrencyPair currencyPair : currencyPairs) if(pair.equals(currencyPair.id)) {
            prices.average = getNormalNumberNotShit((currencyPair.rate_max + currencyPair.rate_min) / 2);
            prices.buy = prices.sell = prices.last = getNormalNumberNotShit(currencyPair.last_price);
            prices.low = getNormalNumberNotShit(currencyPair.rate_min);
            prices.high = getNormalNumberNotShit(currencyPair.rate_max);
            prices.volume = currencyPair.market_volume;
            break;
        }
        return prices;
    }

    public StandartObjects.Depth getMarketDepth(Object pair) throws Exception {
        StandartObjects.Depth depth = new StandartObjects.Depth();
        List<StandartObjects.Order> orderList = internalConvertOrders(internalGetMarketOrders((Integer) pair), (Integer) pair);
        for(StandartObjects.Order order : orderList) {
            if(order.type == Constants.ORDER_SELL) {
                depth.sellOrders.add(order);
            } else {
                depth.buyOrders.add(order);
            }
        }
        return depth;
    }

    public List<StandartObjects.Order> getMarketHistory(Object pair) throws Exception {
        List<CoinExObjects.Trade> trades = internalGetMarketHistory((Integer) pair);
        List<StandartObjects.Order> history = new ArrayList<>(trades.size());
        for(CoinExObjects.Trade trade : trades) {
            StandartObjects.Order order = new StandartObjects.Order();
            order.amount = getNormalNumberNotShit(trade.amount);
            order.price = getNormalNumberNotShit(trade.rate);
            order.id = trade.id;
            order.pair = pair;
            order.type = trade.bid ? Constants.ORDER_BUY : Constants.ORDER_SELL;
            order.time = getNormalDateNotShit(trade.created_at);
            history.add(order);
        }
        return history;
    }

    public StandartObjects.AccountInfo.AccountBalance getAccountBalances() throws Exception {
        StandartObjects.AccountInfo.AccountBalance stdBalanceList = new StandartObjects.AccountInfo.AccountBalance();
        List<CoinExObjects.Balance> balances = internalGetAccountBalances();
        for(CoinExObjects.Balance balance : balances) {
            stdBalanceList.put(balance.currency_name, getNormalNumberNotShit(balance.amount));
        }
        return stdBalanceList;
    }

    public List<StandartObjects.Order> getAccountOpenOrders(Object pair) throws Exception {
        return internalConvertOrders(internalGetAccountOrders((Integer) pair), (Integer) pair);
    }

    public List<StandartObjects.Order> getAccountHistory(Object pair) throws Exception {
        return null;
    }

    // Misc
    public double getFeePercent(Object pair) throws Exception {
        return 0.2;
    }

    // Trading api:
    public Object createOrder(Object pair, int orderType, double quantity, double price) throws Exception {
        List<CoinExObjects.Order> orderList = internalSubmitOrder((Integer) pair, orderType, quantity, price);
        if(orderList == null || orderList.isEmpty()) {
            return 0;
        } else return orderList.get(0).id;
    }

    public boolean cancelOrder(Object orderId) throws Exception {
        List<CoinExObjects.Order> orderList = internalCancelOrder((Integer) orderId);
        return true; // How the fuck i know that query is executed?! Who written this retarded api?
    }
}
