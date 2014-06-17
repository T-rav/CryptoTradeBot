/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.objects;

import com.google.gson.Gson;

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
}
