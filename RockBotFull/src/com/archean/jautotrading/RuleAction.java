/*
 * jCryptoTrader trading client
 * Copyright (C) 2014 1M4SKfh83ZxsCSDmfaXvfCfMonFxMa5vvh (BTC public key)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 */

package com.archean.jautotrading;

import com.archean.jtradeapi.ApiWorker;
import com.archean.jtradeapi.BaseTradeApi;
import com.archean.jtradeapi.Calculator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;

public class RuleAction {
    public abstract static class BaseAction implements Runnable, Serializable {
        public volatile transient ApiWorker apiWorker = null;
        public volatile transient Object callback = null;

        public void setCallback(Object callback) {
            this.callback = callback;
        }
    }

    public static class TradeAction extends BaseAction {
        public abstract static class Callback {
            abstract public void onSuccess(Object orderId, int tradeType, BigDecimal amount, BigDecimal price);

            abstract public void onError(Exception e);
        }

        public static final int AMOUNT_TYPE_CONSTANT = 0;
        public static final int AMOUNT_TYPE_BALANCE_PERCENT = 1;

        public int tradeType = BaseTradeApi.Constants.ORDER_BUY;
        public BaseTradeApi.PriceType priceType = BaseTradeApi.PriceType.LAST;
        public int amountType = AMOUNT_TYPE_CONSTANT;
        public BigDecimal priceCustom = null;
        public BigDecimal amount = null;

        @Override
        public void run() {
            if (priceCustom == null) {
                priceCustom = BaseTradeApi.getPrice(apiWorker.marketInfo.price, priceType);
            }
            BaseTradeApi.StandartObjects.CurrencyPair pair;
            try {
                pair = apiWorker.tradeApi.getCurrencyPairs().get(apiWorker.getPair());
            } catch (Exception e) {
                e.printStackTrace();
                if (callback != null) {
                    ((Callback) callback).onError(e);
                }
                return;
            }
            if (amountType == AMOUNT_TYPE_BALANCE_PERCENT) {
                try {
                    amount = new BigDecimal(Calculator.balancePercentAmount(apiWorker.accountInfo.balance.getBalance(tradeType == BaseTradeApi.Constants.ORDER_SELL ? pair.firstCurrency : pair.secondCurrency), amount.doubleValue(), tradeType, priceCustom.doubleValue(), apiWorker.tradeApi.getFeePercent(apiWorker.getPair())), MathContext.DECIMAL64);
                    amountType = AMOUNT_TYPE_CONSTANT;
                } catch (Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        ((Callback) callback).onError(e);
                    }
                    return;
                }
            }
            try {
                Object orderId = apiWorker.tradeApi.createOrder(apiWorker.getPair(), tradeType, amount.doubleValue(), priceCustom.doubleValue());
                ((Callback) callback).onSuccess(orderId, tradeType, amount, priceCustom);
            } catch (Exception e) {
                e.printStackTrace();
                if (callback != null) {
                    ((Callback) callback).onError(e);
                }
            }
        }
    }
}
