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
public class MarketTradeFee {
    
    @SerializedName("fee")
    public double Fee;
    
    @SerializedName("net")
    public double Net;
    
    public MarketTradeFee(double fee, double net){
        Fee = fee;
        Net = net;
    }
    
    public double TotalCost(){
        return Fee + Net;
    }
}
