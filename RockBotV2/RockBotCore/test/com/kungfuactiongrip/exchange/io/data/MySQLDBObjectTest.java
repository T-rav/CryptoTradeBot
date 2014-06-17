/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.io.data;

import com.kungfuactiongrip.to.BuyOrder;
import com.kungfuactiongrip.to.SellOrder;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Administrator
 */
public class MySQLDBObjectTest {
    
    public MySQLDBObjectTest() {
    }


    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void CanCreateObjectWithoutPropertyFile_ExpectValidObject(){
        MySQLDBObject2 obj = new MySQLDBObject2();
        
        assertNotNull(obj);
        assertEquals("username", obj.FetchUser());
    }
    
    @Test
    public void CanCreateObjectWithPropertyFile_ExpectValidObject(){
        MySQLDBObject2 obj = new MySQLDBObject2("Test_DB");
        
        assertNotNull(obj);
        assertEquals("tradebot", obj.FetchUser());
    }
    
    @Test
    public void CanCreateObjectWithNullPropertyFile_ExpectDefaultObject(){
        MySQLDBObject2 obj = new MySQLDBObject2(null);
        
        assertNotNull(obj);
        assertEquals("username", obj.FetchUser());
    }
    
}
