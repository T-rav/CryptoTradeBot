/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.objects;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class ObjectConverter {

    private final Gson g = new Gson();
    public ObjectConverter() {
    }
    
    public CryptsyResult MakeResultObject(String payload){
        return g.fromJson(payload, CryptsyResult.class);
    }

    public CryptsyInfo MakeInfoObject(String payload) {
        return g.fromJson(payload, CryptsyInfo.class);
    }
    
    public CryptsyOrder MakeOrderObject(String payload){
        return g.fromJson(payload, CryptsyOrder.class);
    }
    
    public MarketBuySellOrders MakeMaketOrderList(String payload){
        MarketBuySellOrders result = new MarketBuySellOrders();
        Gson json = new Gson();
        
        // process sell orders
        JSONArray obj = JsonPath.read(payload, "$.return.sellorders[*]");
        for(Object e : obj){
            MarketSellOrder mo = json.fromJson(e.toString(), MarketSellOrder.class);
            // Add to collection ;)
            result.SellOrders.add(mo);
        }
        
        // process buy orders
        obj = JsonPath.read(payload, "$.return.buyorders[*]");
        for(Object e : obj){
            MarketBuyOrder mo = json.fromJson(e.toString(), MarketBuyOrder.class);
            // Add to collection ;)
            result.BuyOrders.add(mo);
        }
        
        return result;
    }
}
