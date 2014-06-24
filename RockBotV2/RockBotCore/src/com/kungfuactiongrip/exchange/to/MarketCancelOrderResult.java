/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.to;

/**
 *
 * @author Administrator
 */
public class MarketCancelOrderResult {

    public String Message;
    public Boolean IsError;
    
    public MarketCancelOrderResult(String error, boolean isError) {
        Message = error;
        IsError = isError;
    }
    
}
