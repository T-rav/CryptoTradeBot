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
public class MarketCreateOrderResult{
    
    public static String ErrorOrderID = "ERROR_ID";
    
    @SerializedName("orderid")
    public String OrderID;
    
    @SerializedName("moreinfo")
    public String Message;
    
    public MarketCreateOrderResult(String error){
        Message = error;
        OrderID = ErrorOrderID;
    }
}
