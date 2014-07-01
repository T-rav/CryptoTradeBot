/*
 * jCryptoTrader trading client
 * Copyright (C) 2014 1M4SKfh83ZxsCSDmfaXvfCfMonFxMa5vvh (BTC public key)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 */

package com.archean.jtradegui;

import com.archean.jtradeapi.HistoryUtils;
import java.awt.Color;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Builder;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.HighLowItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;

public class CandleChart {
    @Builder
    @Data
    public static class Parameters {
        private final String title = "";
        private final String labelX = ""; // Price
        private final String labelY = ""; // Date
        private final DateFormat dateFormat = new SimpleDateFormat();
        private final NumberFormat numberFormat = new DecimalFormat();
    }

    public static AbstractXYDataset getOHLCDataSet(String title, OHLCDataItem[] dataList) {
        return new DefaultOHLCDataset(title, dataList);
    }

    public static ChartPanel initOHLCChart(Parameters parameters, OHLCDataItem[] dataList) {
        final DateAxis domainAxis = new DateAxis(parameters.getLabelX());
        NumberAxis rangeAxis = new NumberAxis(parameters.getLabelY());
        CandlestickRenderer renderer = new CandlestickRenderer();
        XYPlot mainPlot = new XYPlot(getOHLCDataSet(parameters.getTitle(), dataList), domainAxis, rangeAxis, renderer);
        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setDrawVolume(true);
        rangeAxis.setAutoRangeIncludesZero(false);
        renderer.setBaseToolTipGenerator(new HighLowItemLabelGenerator(parameters.getDateFormat(), parameters.getNumberFormat()));
        JFreeChart chart = new JFreeChart(parameters.getTitle(), null, mainPlot, false);
        mainPlot.setDomainPannable(true);
        mainPlot.setRangePannable(true);
        return new ChartPanel(chart, false);
    }

    public static void updateOHLCChart(ChartPanel chartPanel, String title, OHLCDataItem[] dataList) {
        JFreeChart chart = chartPanel.getChart();
        XYPlot mainPlot = chart.getXYPlot();
        mainPlot.setDataset(getOHLCDataSet(title, dataList));
        chart.setTitle(title);
    }

    public static OHLCDataItem[] updateOHLCDataArray(OHLCDataItem[] chartDataCache, @NonNull List<HistoryUtils.Candle> candles) {
        if (candles == null || candles.size() == 0) {
            chartDataCache = null;
        } else if (chartDataCache == null || chartDataCache.length != candles.size() || !chartDataCache[0].getDate().equals(candles.get(0).start)) { // Full
            int length = candles.size();
            chartDataCache = new OHLCDataItem[length];
            for (int i = 0; i < length; i++) {
                HistoryUtils.Candle candle = candles.get(i);
                chartDataCache[i] = new OHLCDataItem(candle.start, candle.open, candle.high, candle.low, candle.close, candle.volume);
            }
        } else { // Fast
            HistoryUtils.Candle lastCandle = candles.get(candles.size() - 1);
            chartDataCache[chartDataCache.length - 1] = new OHLCDataItem(lastCandle.start, lastCandle.open, lastCandle.high, lastCandle.low, lastCandle.close, lastCandle.volume);
        }
        return chartDataCache;
    }
}
