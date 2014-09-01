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


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class HistoryUtils {
    public static final long PERIOD_1M = 60 * 1000;
    public static final long PERIOD_5M = PERIOD_1M * 5;
    public static final long PERIOD_15M = PERIOD_1M * 15;
    public static final long PERIOD_30M = PERIOD_15M * 2;
    public static final long PERIOD_1H = PERIOD_30M * 2;

    public static long getPeriod(String periodLabel){
        
        switch(periodLabel){
            case "1M":
                return PERIOD_1M;
            case "5M":
                return PERIOD_5M;
            case "15M":
                return PERIOD_15M;
            case "30M":
                return PERIOD_30M;
            case "1H":
                return PERIOD_1H;
            default : 
                return PERIOD_15M;
        }
    }
    
    public static class Candle implements Comparable<Candle> {
        public enum CandleType {
            BULL, BEAR
        }

        public CandleType getType() {
            return close - open >= 0 ? Candle.CandleType.BULL : Candle.CandleType.BEAR;
        }

        public Date start;
        public Date end;
        public Date update;
        public double open;
        public double close;
        public double low;
        public double high;
        public double volume = 0;

        @Override
        public int compareTo(Candle candle) {
            return start.compareTo(candle.start);
        }
    }

    private final static Calendar calendar = Calendar.getInstance();

    public static Date timeDelta(Date date, int field, int amount) {
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    public static Date timeDelta(int field, int amount) { // Date = now
        return timeDelta(new Date(), field, amount);
    }

    public static BaseTradeApi.StandartObjects.Order getNearestTrade(List<BaseTradeApi.StandartObjects.Order> history, Date targetDate) {
        Collections.sort(history);
        BaseTradeApi.StandartObjects.Order result = history.get(0);
        for (BaseTradeApi.StandartObjects.Order order : history) {
            // if the current iteration's date is "before" the target date
            if (order.time.compareTo(targetDate) <= 0) {
                // if the current iteration's date is "after" the current return date
                if (order.time.compareTo(result.time) > 0) {
                    result = order;
                }
            }
        }
        return result;
    }

    public static Candle getNearestCandle(final List<Candle> candles, Date targetDate) {
        if(candles.size() < 1) return null;
        Candle result = candles.get(0);
        for (Candle candle : candles) {
            // if the current iteration's date is "before" the target date
            if (candle.start.compareTo(targetDate) <= 0) {
                // if the current iteration's date is "after" the current return date
                if (candle.start.compareTo(result.start) > 0) {
                    result = candle;
                }
            }
        }
        return result;
    }

    public static List<Candle> buildCandles(List<BaseTradeApi.StandartObjects.Order> history, Date limit, long period) {
        List<Candle> candles = new ArrayList<>(history.size());
        Collections.sort(history);

        int i = 0;
        if (limit != null) while (i < history.size()) {
            if (history.get(i).time.before(limit)) {
                i++;
            } else {
                break;
            }
        }

        if(i >= history.size()) return candles; // empty
        Candle candle = new Candle();
        candle.start = candle.update = history.get(i).time; // first
        candle.open = candle.close = candle.high = candle.low = history.get(i).price;
        i++;
        while (i < history.size()) {
            BaseTradeApi.StandartObjects.Order order = history.get(i);
            candle.update = order.time;
            candle.volume += order.amount;
            candle.close = order.price;
            if (order.price < candle.low) {
                candle.low = order.price;
            }
            if (order.price > candle.high) {
                candle.high = order.price;
            }
            if (order.time.getTime() - candle.start.getTime() > period) { // Next candle
                candle.end = order.time;
                candles.add(candle);
                candle = new Candle();
                candle.start = candle.update = order.time;
                candle.open = candle.close = candle.high = candle.low = order.price;
            }
            i++;
        }
        candles.add(candle); // last
        return candles;
    }

    public static void refreshCandles(List<Candle> candles, List<BaseTradeApi.StandartObjects.Order> history, Date limit, long period) { // fast update

        // Remove old:
        if (limit != null) {
            Iterator<Candle> candleIterator = candles.listIterator();
            while (candleIterator.hasNext()) {
                Candle candle = candleIterator.next();
                if (candle.start.before(limit)) {
                    candleIterator.remove();
                }
            }
        }

        if(candles.size() < 1) return;
        // Refresh/add new:
        Candle candle = candles.get(candles.size() - 1);
        for (BaseTradeApi.StandartObjects.Order order : history) {
            if (order.time.before(candle.update) || order.time.equals(candle.update)) continue;
            candle.update = order.time;
            candle.volume += order.amount;
            candle.close = order.price;
            if (order.price < candle.low) {
                candle.low = order.price;
            }
            if (order.price > candle.high) {
                candle.high = order.price;
            }
            if (order.time.getTime() - candle.start.getTime() > period) { // Next candle
                candle.end = order.time;
                candles.add(candle);
                candle = new Candle();
                candle.start = candle.update = order.time;
                candle.open = candle.close = candle.high = candle.low = order.price;
            }
        }
        candles.add(candle); // last
    }
}
