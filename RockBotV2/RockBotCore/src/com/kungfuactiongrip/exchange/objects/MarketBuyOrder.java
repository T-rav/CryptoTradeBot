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
public class MarketBuyOrder {

    @SerializedName("buyprice")
    public double Price;
    
    @SerializedName("quantity")
    public double Qty;
    
    @SerializedName("total")
    public double Total;
}
