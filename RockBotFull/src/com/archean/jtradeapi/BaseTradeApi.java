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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public abstract class BaseTradeApi {
    public class TradeApiError extends Exception {
        public TradeApiError(String message) {
            super(message);
        }
    }

    public static final class Constants {
        public static final int ORDER_BUY = 0;
        public static final int ORDER_SELL = 1;
        public static final int REQUEST_GET = 0;
        public static final int REQUEST_POST = 1;
        protected static final String JsonDateFormat = "yyyy-MM-dd HH:mm:ss";
    }

    public static class RequestSender {
        String requestEncoding = "UTF-8";

        String formatGetParamString(List<NameValuePair> urlParameters) {
            String url = "";
            boolean firstParam = true;
            for (NameValuePair entry : urlParameters) { // Adding fields
                if (!firstParam) url = url + "&";
                else firstParam = false;
                try {
                    url = url + entry.getName() + "=" + URLEncoder.encode(entry.getValue(), requestEncoding);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return url;
        }

        public HttpResponse getRequest(String url, List<NameValuePair> urlParameters, List<NameValuePair> httpHeaders) throws IOException {
            if (urlParameters.size() > 0) url = url + "?" + formatGetParamString(urlParameters);

            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            for (NameValuePair header : httpHeaders) { // Adding headers
                request.addHeader(header.getName(), header.getValue());
            }

            return client.execute(request);
        }

        public HttpResponse postRequest(String url, List<NameValuePair> urlParameters, List<NameValuePair> httpHeaders) throws IOException {
            return rawPostRequest(url, new UrlEncodedFormEntity(urlParameters), httpHeaders);
        }

        public HttpResponse rawPostRequest(String url, HttpEntity entity, List<NameValuePair> httpHeaders) throws IOException {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(url);
            request.setEntity(entity);

            for (NameValuePair header : httpHeaders) { // Adding headers
                request.addHeader(header.getName(), header.getValue());
            }

            return client.execute(request);
        }

        public String getResponseString(HttpResponse response) throws IOException {
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        }
    }

    public static class ApiKeyPair implements Serializable {
        String publicKey;
        String privateKey;

        public ApiKeyPair() {
            // do nothing
        }

        public ApiKeyPair(String publicKey, String privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

        public ApiKeyPair(ApiKeyPair keyPair) {
            this(keyPair.publicKey, keyPair.privateKey);
        }

        @Override
        public String toString() {
            return "Public=" + publicKey + "; Private=" + privateKey;
        }
    }

    class ApiStatus<ReturnType> {
        int success; // 0 - error, 1 - success
        String error; // Error message
        @SerializedName("return")
        ReturnType result; // Response
    }

    public static class StandartObjects { // Unified, api-independent objects

        public static class Prices {
            public double average;
            public double low;
            public double high;
            public double sell;
            public double buy;
            public double last;
            public double volume;
        }

        public static class Order implements Comparable<Order> {
            public Order() {
                super();
            }

            public Order(double price, double amount) {
                this.price = price;
                this.amount = amount;
            }

            public Object id;
            public Object pair;
            public double price;
            public double amount;
            public Date time;
            public int type; // ORDER_BUY/ORDER_SELL

            @Override
            public int compareTo(Order order) {
                return time.compareTo(order.time);
            }
        }

        public static class Depth {
            public List<Order> sellOrders = new ArrayList<>(); // Ask
            public List<Order> buyOrders = new ArrayList<>(); // Bid
        }

        public static class CurrencyPair {
            public String pairName;
            public Object pairId;
            public String firstCurrency;
            public String secondCurrency;
        }

        public static class MarketInfo extends CurrencyPair {
            public Prices price = new Prices();
            public Depth depth = new Depth();
            public List<Order> history = new ArrayList<>();
        }

        public static class AccountInfo {
            public static class AccountBalance extends TreeMap<String, Double> {
                public AccountBalance() {
                    super();
                }

                public AccountBalance(TreeMap<String, Double> stringDoubleTreeMap) {
                    super(stringDoubleTreeMap);
                }

                public double getBalance(String currencyName) {
                    return this.containsKey(currencyName) ? get(currencyName) : 0.0;
                }
            }

            public AccountBalance balance = new AccountBalance();
            public List<Order> orders = new ArrayList<>();
            public List<Order> history = new ArrayList<>();
        }

        public static class CurrencyPairMapper extends TreeMap<Object, CurrencyPair> {
            public CurrencyPairMapper() {
                super();
            }

            public Map<String, CurrencyPair> makeNameInfoMap() {
                Map<String, CurrencyPair> nameKeyMap = new TreeMap<>();
                for (Map.Entry<Object, CurrencyPair> entry : this.entrySet()) {
                    nameKeyMap.put(entry.getValue().pairName, entry.getValue());
                }
                return nameKeyMap;
            }
        }
    }


    public ApiKeyPair apiKeyPair;
    protected RequestSender requestSender;
    protected Gson jsonParser;
    protected int nonce = 0;

    protected void addNonce(List<NameValuePair> urlParameters) {
        nonce++;
        urlParameters.add(new BasicNameValuePair("nonce", Long.toString(System.currentTimeMillis() / 100 + nonce)));
    }

    public BaseTradeApi() {
        apiKeyPair = new ApiKeyPair();
        requestSender = new RequestSender();
        jsonParser = new GsonBuilder().setDateFormat(Constants.JsonDateFormat).create();
    }

    public BaseTradeApi(ApiKeyPair apiKeyPair) {
        this();
        this.apiKeyPair = apiKeyPair;
    }

    protected String makeSign(List<NameValuePair> urlParameters) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        return Utils.Crypto.Hashing.hmacDigest(requestSender.formatGetParamString(urlParameters), apiKeyPair.privateKey, Utils.Crypto.Hashing.SHA512);
    }

    protected void cleanAuth(List<NameValuePair> urlParameters, List<NameValuePair> httpHeaders) {
        Iterator<NameValuePair> headerIterator = httpHeaders.iterator();
        while (headerIterator.hasNext()) { // Cleaning
            NameValuePair header = headerIterator.next();
            if (header.getName().equals("Key") || header.getName().equals("Sign")) {
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

    protected void writeAuthParams(List<NameValuePair> urlParameters, List<NameValuePair> httpHeaders) {
        if (apiKeyPair == null || apiKeyPair.publicKey.isEmpty() || apiKeyPair.privateKey.isEmpty()) {
            throw new IllegalArgumentException("Invalid API key pair");
        }
        addNonce(urlParameters);
        try {
            httpHeaders.add(new BasicNameValuePair("Sign", makeSign(urlParameters)));
            httpHeaders.add(new BasicNameValuePair("Key", apiKeyPair.publicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String executeRequest(boolean needAuth, String url, List<NameValuePair> urlParameters, int httpRequestType) throws IOException {
        if (urlParameters == null) urlParameters = new ArrayList<>(); // empty list
        List<NameValuePair> httpHeaders = new ArrayList<>();
        cleanAuth(urlParameters, httpHeaders);
        if (needAuth) writeAuthParams(urlParameters, httpHeaders);
        switch (httpRequestType) {
            case Constants.REQUEST_GET:
                return requestSender.getResponseString(requestSender.getRequest(url, urlParameters, httpHeaders));
            case Constants.REQUEST_POST:
                return requestSender.getResponseString(requestSender.postRequest(url, urlParameters, httpHeaders));
            default:
                throw new IllegalArgumentException("Unknown httpRequestType value");
        }
    }

    public enum PriceType {
        LAST, ASK, BID, HIGH, LOW, AVG
    }

    public static BigDecimal getPrice(BaseTradeApi.StandartObjects.Prices prices, PriceType priceType) {
        double price;
        switch (priceType) {
            case LAST:
                price = prices.last;
                break;
            case ASK:
                price = prices.buy;
                break;
            case BID:
                price = prices.sell;
                break;
            case HIGH:
                price = prices.high;
                break;
            case LOW:
                price = prices.low;
                break;
            case AVG:
                price = prices.average;
                break;
            default:
                throw new IllegalArgumentException("Unknown price type");
        }
        return new BigDecimal(price, MathContext.DECIMAL64);
    }


    public abstract StandartObjects.CurrencyPairMapper getCurrencyPairs() throws Exception;

    // Basic info:
    public abstract StandartObjects.Prices getMarketPrices(Object pair) throws Exception;

    public abstract StandartObjects.Depth getMarketDepth(Object pair) throws Exception;

    public abstract List<StandartObjects.Order> getMarketHistory(Object pair) throws Exception;

    public abstract StandartObjects.AccountInfo.AccountBalance getAccountBalances() throws Exception;

    public abstract List<StandartObjects.Order> getAccountOpenOrders(Object pair) throws Exception;

    public abstract List<StandartObjects.Order> getAccountHistory(Object pair) throws Exception;

    // Consolidated info:
    public StandartObjects.MarketInfo getMarketData(Object pair, boolean retrieveOrders, boolean retrieveHistory) throws Exception {
        StandartObjects.MarketInfo marketInfo = new StandartObjects.MarketInfo();
        marketInfo.price = getMarketPrices(pair);
        if (retrieveOrders) {
            marketInfo.depth = getMarketDepth(pair);
        }
        if (retrieveHistory) {
            marketInfo.history = getMarketHistory(pair);
        }
        return marketInfo;
    }

    public StandartObjects.AccountInfo getAccountInfo(Object pair, boolean retrieveOrders, boolean retrieveHistory) throws Exception {
        StandartObjects.AccountInfo accountInfo = new StandartObjects.AccountInfo();
        accountInfo.balance = getAccountBalances();
        if (retrieveOrders) {
            accountInfo.orders = getAccountOpenOrders(pair);
        }
        if (retrieveHistory) {
            accountInfo.history = getAccountHistory(pair);
        }
        return accountInfo;
    }

    // Misc
    public abstract double getFeePercent(Object pair) throws Exception;

    // Trading api:
    public abstract Object createOrder(Object pair, int orderType, double quantity, double price) throws Exception;

    public abstract boolean cancelOrder(Object orderId) throws Exception;
}
