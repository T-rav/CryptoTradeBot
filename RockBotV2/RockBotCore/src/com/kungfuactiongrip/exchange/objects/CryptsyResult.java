/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.objects;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Administrator
 */
public class CryptsyResult extends CryptsyStatus{
    @SerializedName("return")
    Object payload;
    
    // these both are error fields ;)
    String error;
    String result;
    
    public String FetchPayload(){
        Gson g = new Gson();
        return g.toJson(payload);
    }
    
    public String FetchRawPayload(){
        return payload.toString();
    }
    
    public String FetchError(){
        if(error != null){
            return error;
        }
        
        return result;
    }
    
    public boolean HasError(){
        return (error != null || result != null);
    }
}
