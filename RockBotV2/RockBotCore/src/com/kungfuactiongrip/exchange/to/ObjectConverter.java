/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.to;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import java.util.ArrayList;
import java.util.List;
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
        try{
            return g.fromJson(payload, CryptsyResult.class);
        }catch(Exception e){
            return null;
        }
    }

    public CryptsyInfo MakeInfoObject(String payload) {
        try{
            return g.fromJson(payload, CryptsyInfo.class);
        }catch(Exception e){
            return null;
        }
    }
    
    public CryptsyOrder MakeOrderObject(String payload){
        try{
            return g.fromJson(payload, CryptsyOrder.class);
        }catch(Exception e){
            return null;
        }
    }
    
    public MarketTradeFee MakeTradeFeeObject(String payload){
        MarketTradeFee result = null;

        // process sell orders
        try{
            JSONObject obj = JsonPath.read(payload, "$.return[*]");

            Object fee = obj.get("fee");
            Object net = obj.get("net");

            result = new MarketTradeFee(Double.parseDouble(fee.toString()), Double.parseDouble(net.toString()));
        }catch(Exception e){
            return null;
        }
        
        return result;
    }
    
    public MarketBuySellOrders MakeMarketOrderList(String payload){
        MarketBuySellOrders result = new MarketBuySellOrders();
        Gson json = new Gson();
        
        try{
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
        }catch(Exception e){
            return null;
        }
        
        return result;
    }

    public List<MarketTrade> MakeMarketTradeList(String payload) {
        List<MarketTrade> result = new ArrayList<>();
        Gson json = new Gson();
        try{
            JSONArray obj = JsonPath.read(payload, "$.return[*]");
            for(Object e : obj){
                MarketTrade mt = json.fromJson(e.toString(), MarketTrade.class);
                // Add to collection ;)
                result.add(mt);
            }
        }catch(Exception e){
            return null;
        }
        
        return result;
    }
}
