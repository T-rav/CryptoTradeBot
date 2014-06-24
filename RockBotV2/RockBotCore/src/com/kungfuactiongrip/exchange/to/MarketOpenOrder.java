/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.to;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Administrator
 */
public class MarketOpenOrder {
    
    @SerializedName("orderid")
    public String OrderID;
    
    @SerializedName("created")
    public String CreatedDate;
    
    @SerializedName("ordertype")
    public String OrderType;
    
    @SerializedName("price")
    public double Price;
    
    @SerializedName("quantity")
    public double Qty;
    
    @SerializedName("orig_quantity")
    public double OrigQty;
    
    @SerializedName("total")
    public double Total;
}
