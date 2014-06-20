/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.test;

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
    public void CreatePropertyBag_WhenUsingValidPropertyFile_ExpectNotNull(){
        
        PropertyBag bg = CreateGoodPropertyBag();
        
        // Assert
        assertNotNull(bg);
    }
    
    @Test
    public void CreatePropertyBag_WhenUsingValidPropertyFile_ExpectData(){
        
        PropertyBag bg = CreateGoodPropertyBag();
        
        // Assert
        assertNotNull(bg);
    }
    
    @Test
    public void FetchProperty_WhenUsingValidKey_ExpectData(){
        
        PropertyBag bg = CreateGoodPropertyBag();
        
        // Assert
        assertNotNull(bg);
        String result = bg.FetchKey("USER");
        assertEquals("tradebotTest", result);
    }
    
    @Test
    public void FetchProperty_WhenUsingInvalidKey_ExpectNull(){
        
        PropertyBag bg = CreateGoodPropertyBag();
        
        // Assert
        assertNotNull(bg);
        String result = bg.FetchKey("USER_FAKE");
        assertNull(result);
    }
    
    @Test
    public void CreatePropertyBag_WhenUsingInvalidPropertyFile_ExpectNull(){
        
        PropertyBag bg = CreateBadPropertyBag();
        
        // Assert
        assertNull(bg);
    }

    @Test
    public void TotalProperties_WhenPopulated_ExpectNumberGreaterThenZero(){
        PropertyBag bg = CreateGoodPropertyBag();
        
        // Assert
        assertNotNull(bg);
        assertEquals(3, bg.TotalProperties());
    }

    @Test
    public void TotalProperties_WhenNotPopulated_ExpectZero(){
        PropertyBag bg = CreateEmptyPropertyBag();
        
        // Assert
        assertNotNull(bg);
        assertEquals(0, bg.TotalProperties());
    }
    
    // Helpers
    private static PropertyBag CreateEmptyPropertyBag() {
        return PropertyBagFactory.GenerateFromConfig("Test_Empty_DB");
    }
    
    private static PropertyBag CreateBadPropertyBag() {
        return PropertyBagFactory.GenerateFromConfig("Test_DB_FAKE");
    }
    
    private static PropertyBag CreateGoodPropertyBag() {
        return PropertyBagFactory.GenerateFromConfig("Test_DB");
    }
    
}
