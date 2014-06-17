/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.objects;

import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Administrator
 */

public class CryptsyInfo {
    
    @SerializedName("balances_available")
    public HashMap<String, String> Balances;

    @SerializedName("servertimestamp")
    public long ServerTimestamp;
    
    @SerializedName("servertimezone")
    public String ServerTimezone;
    
    @SerializedName("serverdatetime")
    public String ServerDateTime;
    
    @SerializedName("openordercount")
    public int OpenOrders;
    
}
