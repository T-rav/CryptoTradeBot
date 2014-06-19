/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.io.data;

import com.kungfuactiongrip.exchange.ExchangeList;
import com.kungfuactiongrip.to.TradeOrder;
import com.kungfuactiongrip.to.TradeState;
import com.kungfuactiongrip.to.TradeType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Administrator
 */
public class MySQLDBObjectTest {
    
    private static int ValidOrderMarketID = 173;
    private static int InvalidOrderMarketID = 175;
    private static int InsertOrderMarketID = 177;
    
    public MySQLDBObjectTest() {
    }

    @Test
    public void CreateObjectWithoutPropertyFile_ExpectValidObject(){
        IDbDAO obj = DBProviderFactory.GenerateMySQLDBObject();
        
        assertNotNull(obj);
        assertEquals("username", obj.FetchUser());
    }
    
    @Test
    public void CreateObjectWithPropertyFile_ExpectValidObject(){
        IDbDAO obj = GenerateTestDB();
        
        assertNotNull(obj);
        assertEquals("tradebotTest", obj.FetchUser());
    }

    @Test
    public void CreateObjectWithNullPropertyFile_ExpectDefaultObject(){
        IDbDAO obj = DBProviderFactory.GenerateMySQLDBObject(null);
        
        assertNotNull(obj);
        assertEquals("username", obj.FetchUser());
    }
    
    @Test
    public void CreateConnection_ExpectValidConnectionObject(){
        IDbDAO obj = GenerateTestDB();
        
        assertNotNull(obj);
        Connection con = null;
        try{
            con = obj.CreateConnection();
            assertNotNull(con);
        }finally{
            if(con != null){
                try{
                    con.close();
                }catch(SQLException e){}
            }
        }
    }
    
    @Test
    public void FetchOpenBuyOrders_WhenOrdersExist_ExpectOrdersList(){
        IDbDAO obj = GenerateTestDB();
        
        assertNotNull(obj);
        Connection con = null;
        try{
            List<TradeOrder> orders = obj.FetchOpenBuyOrdersForMarket(ValidOrderMarketID, ExchangeList.Cryptsy);
                       
            // Assert
            assertNotNull(orders);
            assertFalse(orders.isEmpty());
            TradeOrder order = orders.get(0);
            assertEquals(1, order.RowID);
            assertEquals(0.1, order.PricePer, 0.00000001);
            assertEquals("CRYPTSY", order.Exchange.name().toUpperCase());
            assertEquals("Dummy-1", order.TradeID);
        }catch(Exception e){
            fail("Exception Thrown [ " + e.getMessage()+ " ]");
        }finally{
            if(con != null){
                try{
                    con.close();
                }catch(SQLException e){}
            }
        }
    }
    
    @Test
    public void FetchOpenSellOrders_WhenOrdersExist_ExpectOrdersList(){
        IDbDAO obj = GenerateTestDB();
        
        assertNotNull(obj);
        Connection con = null;
        try{
            List<TradeOrder> orders = obj.FetchOpenSellOrdersForMarket(ValidOrderMarketID, ExchangeList.Cryptsy);
            
            // Assert
            assertNotNull(orders);
            assertFalse(orders.isEmpty());
            TradeOrder order = orders.get(0);
            assertEquals(2, order.RowID);
            assertEquals(0.1, order.PricePer, 0.00000001);
            assertEquals("CRYPTSY", order.Exchange.name().toUpperCase());
            assertEquals("Dummy-2", order.TradeID);
        }catch(Exception e){
            fail("Exception Thrown [ " + e.getMessage()+ " ]");
        }finally{
            if(con != null){
                try{
                    con.close();
                }catch(SQLException e){}
            }
        }
    }
    
    @Test
    public void FetchOpenBuyOrders_WhenOrdersExist_ExpectEmptyOrdersList(){
        IDbDAO obj = GenerateTestDB();
        
        assertNotNull(obj);
        Connection con = null;
        try{
            List<TradeOrder> orders = obj.FetchOpenBuyOrdersForMarket(InvalidOrderMarketID, ExchangeList.Cryptsy);
                       
             // Assert
            assertNotNull(orders);
            assertTrue(orders.isEmpty());
        }catch(Exception e){
            fail("Exception Thrown [ " + e.getMessage()+ " ]");
        }finally{
            if(con != null){
                try{
                    con.close();
                }catch(SQLException e){}
            }
        }
    }
    
    @Test
    public void FetchOpenSellOrders_WhenOrdersExist_ExpectEmptyOrdersList(){
        IDbDAO obj = GenerateTestDB();
        
        assertNotNull(obj);
        Connection con = null;
        try{
            List<TradeOrder> orders = obj.FetchOpenSellOrdersForMarket(InvalidOrderMarketID, ExchangeList.Cryptsy);
            
            // Assert
            assertNotNull(orders);
            assertTrue(orders.isEmpty());
        }catch(Exception e){
            fail("Exception Thrown [ " + e.getMessage()+ " ]");
        }finally{
            if(con != null){
                try{
                    con.close();
                }catch(SQLException e){}
            }
        }
    }
    
    @Test
    public void InsertSellOrder_WhenOrdersValid_ExpectInserted(){
        IDbDAO obj = GenerateTestDB();
        
        assertNotNull(obj);
        Connection con = null;
        try{
            
            boolean result = obj.InsertOrder(TradeType.SELL, TradeState.OPEN, ExchangeList.Cryptsy, InsertOrderMarketID,0.1, 0.3,"DUMMY-INSERT",null);
            
            // Assert
            assertTrue(result);
        }catch(Exception e){
            fail("Exception Thrown [ " + e.getMessage()+ " ]");
        }finally{
            if(con != null){
                try{
                    con.close();
                }catch(SQLException e){}
            }
        }
    }
   
    private IDbDAO GenerateTestDB() {
        IDbDAO obj = DBProviderFactory.GenerateMySQLDBObject("Test_DB");
        return obj;
    }

}
