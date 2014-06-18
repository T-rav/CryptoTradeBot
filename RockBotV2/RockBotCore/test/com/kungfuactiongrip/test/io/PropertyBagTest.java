/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.test.io;

import com.kungfuactiongrip.config.exchange.PropertyBag;
import com.kungfuactiongrip.config.exchange.PropertyBagFactory;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
public class PropertyBagTest {
    
    public PropertyBagTest() {
    }


    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void CanCreatePropertyBag_ExpectNotNull(){
        
        PropertyBag bg = PropertyBagFactory.GenerateFromConfig("Test_DB");
        
        assertNotNull(bg);
    }
    
    @Test
    public void CanCreatePropertyBag_WhenUsingValidPropertyFile_ExpectData(){
        
        PropertyBag bg = PropertyBagFactory.GenerateFromConfig("Test_DB");
        
        assertNotNull(bg);
    }
    
    @Test
    public void CanFetchProperty_WhenUsingValidKey_ExpectData(){
        
        PropertyBag bg = PropertyBagFactory.GenerateFromConfig("Test_DB");
        
        assertNotNull(bg);
        String result = bg.FetchKey("USER");
        assertEquals("tradebotTest", result);
    }
    
    @Test
    public void CannotFetchProperty_WhenUsingInvalidKey_ExpectNull(){
        
        PropertyBag bg = PropertyBagFactory.GenerateFromConfig("Test_DB");
        
        assertNotNull(bg);
        String result = bg.FetchKey("USER_FAKE");
        assertNull(result);
    }
    
    @Test
    public void CanCreatePropertyBag_WhenUsingInvalidPropertyFile_ExpectNull(){
        
        PropertyBag bg = PropertyBagFactory.GenerateFromConfig("Test_DB_FAKE");
        
        assertNull(bg);
    }
}
