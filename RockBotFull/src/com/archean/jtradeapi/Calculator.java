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

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Calculator {
    public static final double MINIMAL_AMOUNT = 0.00000001; // 1 Satoshi
    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100.0, MathContext.DECIMAL64);
    public static final RoundingMode ROUNDING_MODE = RoundingMode.FLOOR;
    public static final int ROUNDING_PRECISION = 8;

    public static double totalWithFee(int orderType, double price, double amount, double feePercent) {
        BigDecimal bigDecimalPrice = new BigDecimal(price, MathContext.DECIMAL64), bigDecimalAmount = new BigDecimal(amount, MathContext.DECIMAL64), bigDecimalFeePercent = new BigDecimal(feePercent, MathContext.DECIMAL64);
        return orderType == BaseTradeApi.Constants.ORDER_BUY ? bigDecimalAmount.multiply(bigDecimalPrice).multiply(ONE_HUNDRED.add(bigDecimalFeePercent).divide(ONE_HUNDRED, ROUNDING_PRECISION, ROUNDING_MODE)).doubleValue() : bigDecimalAmount.multiply(bigDecimalPrice).divide(ONE_HUNDRED.add(bigDecimalFeePercent).divide(ONE_HUNDRED, ROUNDING_PRECISION, ROUNDING_MODE), ROUNDING_PRECISION, ROUNDING_MODE).doubleValue();
    }

    public static double balancePercentAmount(double balance, double balancePercent, int orderType, double price, double feePercent) {
        BigDecimal bigDecimalBalance = new BigDecimal(balance, MathContext.DECIMAL64), bigDecimalPrice = new BigDecimal(price, MathContext.DECIMAL64), bigDecimalBalancePercent = new BigDecimal(balancePercent, MathContext.DECIMAL64), bigDecimalFeePercent = new BigDecimal(feePercent, MathContext.DECIMAL64);
        return orderType == BaseTradeApi.Constants.ORDER_BUY ? bigDecimalBalance.divide(bigDecimalPrice, ROUNDING_PRECISION, ROUNDING_MODE).multiply(bigDecimalBalancePercent.divide(ONE_HUNDRED, ROUNDING_PRECISION, ROUNDING_MODE)).divide(ONE_HUNDRED.add(bigDecimalFeePercent).divide(ONE_HUNDRED, ROUNDING_PRECISION, ROUNDING_MODE), ROUNDING_PRECISION, ROUNDING_MODE).doubleValue() : bigDecimalBalance.multiply(bigDecimalBalancePercent).divide(ONE_HUNDRED, ROUNDING_PRECISION, ROUNDING_MODE).doubleValue();
    }

    public static double priceChangePercent(double p1, double p2) {
        return ((p2 - p1) / p1) * 100.0;
    }

    public enum ArithmeticCompareCondition {
        EQUAL, GREATER, LESS, GREATER_OR_EQUAL, LESS_OR_EQUAL
    }

    public static boolean compare(BigDecimal compareWant, BigDecimal compareActual, ArithmeticCompareCondition conditionType) {
        int compareResult = compareActual.compareTo(compareWant);
        switch (conditionType) {
            case EQUAL:
                return compareResult == 0;
            case GREATER:
                return compareResult == 1;
            case GREATER_OR_EQUAL:
                return compareResult == 0 || compareResult == 1;
            case LESS:
                return compareResult == -1;
            case LESS_OR_EQUAL:
                return compareResult == -1 || compareResult == 0;
            default:
                throw new UnknownError();
        }
    }
}
