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
import java.util.Map;

public class RuleCondition {
    public static void makeConditionData(Map<Object, Object> mapData, ApiWorker apiWorker) {
        mapData.put(ApiWorker.ApiDataType.MARKET_PRICES, apiWorker.marketInfo.price);
    }

    public static abstract class BaseCondition implements Serializable {
        public Object compareType = null;
        public Object conditionType = null;
        public Object value = null;

        public BaseCondition(Object conditionType, Object compareType, Object value) {
            this.conditionType = conditionType;
            this.compareType = compareType;
            this.value = value;
        }

        abstract boolean isSatisfied(Map<Object, Object> data) throws Exception;
    }

    public static class PriceCondition extends BaseCondition {
        public PriceCondition(BaseTradeApi.PriceType priceType, Calculator.ArithmeticCompareCondition conditionType, BigDecimal value) {
            super(priceType, conditionType, value);
        }

        @Override
        boolean isSatisfied(Map<Object, Object> data) throws Exception {
            BigDecimal compareValue = (BigDecimal) this.value;
            BigDecimal comparePrice = BaseTradeApi.getPrice((BaseTradeApi.StandartObjects.Prices) data.get(ApiWorker.ApiDataType.MARKET_PRICES), (BaseTradeApi.PriceType) conditionType);
            return Calculator.compare(compareValue, comparePrice, (Calculator.ArithmeticCompareCondition) compareType);
        }
    }
}
