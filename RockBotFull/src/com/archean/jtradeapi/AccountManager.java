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
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class AccountManager {
    public static class AccountType {
        public static final int ACCOUNT_CRYPTSY = 0;
        public static final int ACCOUNT_BTCE = 1;
        public static final int ACCOUNT_COINEX = 3;

        public static Map<String, Integer> listAccountTypes() {
            Map<String, Integer> map = new TreeMap<>();
            map.put("Cryptsy", ACCOUNT_CRYPTSY);
            map.put("BTC-E", ACCOUNT_BTCE);
            map.put("CoinEX", ACCOUNT_COINEX);
            return map;
        }
    }

    public static class Account implements Serializable {
        public int accountType;
        public BaseTradeApi.ApiKeyPair keyPair = new BaseTradeApi.ApiKeyPair();

        public Account() {
            // do nothing
        }

        public Account(int accountType, BaseTradeApi.ApiKeyPair apiKeyPair) {
            this.accountType = accountType;
            this.keyPair = apiKeyPair;
        }
    }

    public static class AccountDb extends TreeMap<String, Account> {
        public void addAccount(String label, int accountType, BaseTradeApi.ApiKeyPair keyPair) {
            this.put(label, new Account(accountType, keyPair));
        }

        public String saveToJson() {
            return new Gson().toJson(this);
        }

        public void loadFromJson(String json) {
            this.putAll((AccountDb) new Gson().fromJson(json, new TypeToken<AccountDb>() {
            }.getType()));
        }
    }

    public static BaseTradeApi tradeApiInstance(int accountType, BaseTradeApi.ApiKeyPair pair) {
        switch (accountType) {
            case AccountType.ACCOUNT_CRYPTSY:
                return new CryptsyTradeApi(pair);
            case AccountType.ACCOUNT_BTCE:
                return new BtceTradeApi(pair);
            case AccountType.ACCOUNT_COINEX:
                return new CoinExTradeApi(pair);
            default:
                throw new IllegalArgumentException("Unknown trade account type");
        }
    }

    public static BaseTradeApi tradeApiInstance(Account account) {
        return tradeApiInstance(account.accountType, account.keyPair);
    }
}
