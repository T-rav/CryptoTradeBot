/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.test.io;

import com.kungfuactiongrip.config.exchange.PropertyBag;
import com.kungfuactiongrip.config.exchange.PropertyBagFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
        String result = bg.FetchKey("USER");
        assertEquals("tradebot", result);
    }
}
