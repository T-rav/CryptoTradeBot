/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.config.exchange;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 *
 * @author Administrator
 */
public class PropertyBag {

    private final Dictionary<String,String> _grid;

    public PropertyBag() {
        this._grid = new Hashtable<>();
    }
    
    public void AddPair(String key, String value) {
        _grid.put(key, value);
    }
    
    public String FetchKey(String key){
        return _grid.get(key).replace("\"", "");
    }
    
}
