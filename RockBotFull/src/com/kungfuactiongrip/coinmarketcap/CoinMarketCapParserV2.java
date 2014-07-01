/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.coinmarketcap;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minidev.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class CoinMarketCapParserV2 {

    public List<CoinCapitalization> Parse(String data) {
        Gson json = new Gson();
        List<CoinCapitalization> result = new ArrayList<>();
        try{
            // process sell orders
            JSONObject obj = JsonPath.read(data, "$.[*]");
            Set<Entry<String, Object>> setData = obj.entrySet();
            Iterator<Entry<String,Object>> itr = setData.iterator();
            while(itr.hasNext()){
                CoinCapitalization coin = new CoinCapitalization();
                Entry<String, Object> entry = itr.next();
                String val = entry.getValue().toString();
                String key = entry.getKey();
                String symbol = JsonPath.read(val, "$.symbol");
                String usdCap = JsonPath.read(val, "$.market_cap[*].usd");
                String btcCap = JsonPath.read(val, "$.market_cap[*].btc");
                String usdPrice = JsonPath.read(val, "$.price[*].usd");
                String btcPrice = JsonPath.read(val, "$.price[*].usd");
                String totalSupply = JsonPath.read(val, "$.supply");
                String usdVol = JsonPath.read(val, "$.volume[*].usd");
                String btcVol = JsonPath.read(val, "$.volume[*].btc");
                String change = JsonPath.read(val, "$.change");
                
                coin.coinName = key;
                coin.coinCode = symbol;
                coin.usdCap = Long.parseLong(usdCap);
                coin.btcCap = Long.parseLong(btcCap);
                coin.usdPrice = Double.parseDouble(usdPrice);
                coin.btcPrice = Double.parseDouble(btcPrice);
                coin.totalSupply = (long)Double.parseDouble(totalSupply);
                coin.usdVolume = (long)Double.parseDouble(usdVol);
                coin.btcVolume = (long)Double.parseDouble(btcVol);
                coin.change = Double.parseDouble(change);
                
                result.add(coin);
            }
            
        }catch(Exception e){
            Logger.getLogger(CoinMarketCapParserV2.class.getName()).log(Level.WARNING, null, e);
        }
        
        return result;
    }
    
}
