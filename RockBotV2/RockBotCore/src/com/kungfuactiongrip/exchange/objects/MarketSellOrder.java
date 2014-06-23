/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.objects;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Administrator
 */
public class MarketSellOrder implements Comparable<MarketSellOrder> {
    
    @SerializedName("sellprice")
    public Double Price;
    
    @SerializedName("quantity")
    public Double Qty;
    
    @SerializedName("total")
    public Double Total;
    
    public MarketSellOrder(double price, double qty, double total){
        Price = price;
        Qty = qty;
        Total = total;
    }
    
    @Override
    public int compareTo(MarketSellOrder o) {
          return o.Price.compareTo(Price);
    }
}
