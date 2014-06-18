/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.io.data;

import com.kungfuactiongrip.exchange.io.IBotIO;
import java.sql.Connection;


public interface IDbDAO extends IBotIO{
    
    public String FetchUser();
    
    public Connection CreateConnection();
    
    /*
    
    public boolean ExecuteInsert();
    
    public boolean ExecuteSelect();
    
    public boolean ExecuteUpdate();
    
    public boolean ExecuteDelete();
    
    */
}
