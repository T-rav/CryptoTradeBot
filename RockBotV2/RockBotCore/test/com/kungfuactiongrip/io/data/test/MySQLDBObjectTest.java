/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.io.data.test;

import com.kungfuactiongrip.config.exchange.PropertyBag;
import com.kungfuactiongrip.exchange.ExchangeList;
import com.kungfuactiongrip.exchange.io.data.DBProviderFactory;
import com.kungfuactiongrip.exchange.io.data.IDbDAO;
import com.kungfuactiongrip.exchange.to.MarketTradeVerbose;
import com.kungfuactiongrip.exchange.to.ObjectConverter;
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
    
    public static int ValidOrderMarketID = 173;
    public static int InvalidOrderMarketID = 175;
    public static int InsertOrderMarketID = 177;
    public static int AbortedOrderMarketID = 179;
    public static int OpenIntervalMarketID = 181;
    public static int OpenDayMarketID = 181;
    public static int AbortedDayMarketID = 181;
    public static int AbortedIntervalMarketID = 181;
    
    public MySQLDBObjectTest() {
    }
    
    private IDbDAO GenerateTestDB() {
        IDbDAO obj = DBProviderFactory.GenerateMySQLDBObject("Test_DB");
        return obj;
    }

    private ObjectConverter MakeObjectConverter() {
        ObjectConverter oc = new ObjectConverter();
        return oc;
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
        
        // Pre-Assert
        assertNotNull(obj);
        assertEquals("username", obj.FetchUser());
    }
    
    @Test
    public void CreateConnection_ExpectValidConnectionObject(){
        IDbDAO obj = GenerateTestDB();
        
        // Pre-Assert
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
        
        // Pre-Assert
        assertNotNull(obj);
        List<TradeOrder> orders = obj.FetchOrdersForMarket(ValidOrderMarketID, TradeType.BUY, TradeState.OPEN, ExchangeList.Cryptsy);

        // Assert
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        TradeOrder order = orders.get(0);
        assertEquals(1, order.RowID);
        assertEquals(0.1, order.PricePer, 0.00000001);
        assertEquals("CRYPTSY", order.Exchange.name().toUpperCase());
        assertEquals("Dummy-1", order.TradeID);
    }
    
    @Test
    public void FetchOpenSellOrders_WhenOrdersExist_ExpectOrdersList(){
        IDbDAO obj = GenerateTestDB();
        
        // Pre-Assert
        assertNotNull(obj);
        List<TradeOrder> orders = obj.FetchOrdersForMarket(ValidOrderMarketID, TradeType.SELL, TradeState.OPEN, ExchangeList.Cryptsy);

        // Assert
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        TradeOrder order = orders.get(0);
        assertEquals(2, order.RowID);
        assertEquals(0.1, order.PricePer, 0.00000001);
        assertEquals("CRYPTSY", order.Exchange.name().toUpperCase());
        assertEquals("Dummy-2", order.TradeID);
    }
    
    @Test
    public void FetchOpenBuyOrders_WhenOrdersExist_ExpectEmptyOrdersList(){
        IDbDAO obj = GenerateTestDB();
        
        // Pre-Assert
        assertNotNull(obj);
        List<TradeOrder> orders = obj.FetchOrdersForMarket(InvalidOrderMarketID, TradeType.BUY, TradeState.OPEN, ExchangeList.Cryptsy);

         // Assert
        assertNotNull(orders);
        assertTrue(orders.isEmpty());
    }
    
    @Test
    public void FetchOpenSellOrders_WhenOrdersExist_ExpectEmptyOrdersList(){
        IDbDAO obj = GenerateTestDB();
        
        // Pre-Assert
        assertNotNull(obj);
        List<TradeOrder> orders = obj.FetchOrdersForMarket(InvalidOrderMarketID, TradeType.SELL, TradeState.OPEN, ExchangeList.Cryptsy);

        // Assert
        assertNotNull(orders);
        assertTrue(orders.isEmpty());
    }
    
    @Test
    public void InsertSellOrder_WhenOrdersValid_ExpectInserted(){
        IDbDAO obj = GenerateTestDB();
        
        // Pre-Assert
        assertNotNull(obj);
        int result = obj.InsertOrder(TradeType.SELL, TradeState.OPEN, ExchangeList.Cryptsy, InsertOrderMarketID,0.1, 0.3,"DUMMY-INSERT",null);

        // Assert
        assertTrue(result > 0);
    }
    
    @Test
    public void FetchNumberOfAbortedBuyTrades_WhenArePresent_ExpectNumberGreaterThenZero(){
        IDbDAO obj = GenerateTestDB();
        
        // Pre-Assert
        assertNotNull(obj);
        int result = obj.FetchOrderCountOfTypeForInterval(TradeType.BUY, TradeState.ABORTED, ExchangeList.Cryptsy, AbortedOrderMarketID, 6);

        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void FetchNumberOfAbortedSellTrades_WhenArePresent_ExpectNumberGreaterThenZero(){
        IDbDAO obj = GenerateTestDB();
        
        // Pre-Assert
        assertNotNull(obj);
        int result = obj.FetchOrderCountOfTypeForInterval(TradeType.SELL, TradeState.ABORTED, ExchangeList.Cryptsy, AbortedOrderMarketID, 6);

        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void FetchNumberOfOpenBuyTrades_WhenPresent_ExpectNumberGreaterThenZero(){
        IDbDAO obj = GenerateTestDB();
        
        // Pre-Assert
        assertNotNull(obj);
        int result = obj.FetchOrderCountOfType(TradeType.BUY, TradeState.OPEN, ExchangeList.Cryptsy, ValidOrderMarketID);

        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void FetchNumberOfOpenSellTrades_WhenPresent_ExpectNumberGreaterThenZero(){
        IDbDAO obj = GenerateTestDB();
        
        // Pre-Assert
        assertNotNull(obj);
        int result = obj.FetchOrderCountOfType(TradeType.SELL, TradeState.OPEN, ExchangeList.Cryptsy, ValidOrderMarketID);

        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void FetchEngineConfiguration_WhenOptionsPresent_ExpectConfigurationOptions(){
        IDbDAO obj = GenerateTestDB();
        
        // Pre-Assert
        assertNotNull(obj);
        PropertyBag result = obj.FetchEngineConfiguration();

        // Assert
        assertNotNull(result);
        assertEquals(18, result.TotalProperties());
    }
    
    @Test
    public void InsertTradeHistory_WhenListOfTrades_ExpectTrue(){
        IDbDAO obj = GenerateTestDB();
        ObjectConverter oc = MakeObjectConverter();
        String data = "{\"success\":\"1\",\"return\":[{\"tradeid\":\"48338454\",\"tradetype\":\"Sell\",\"datetime\":\"2014-06-01 15:28:02\",\"tradeprice\":\"0.00000851\",\"quantity\":\"13.85809313\",\"fee\":\"0.000000290\",\"total\":\"0.00011793\",\"initiate_ordertype\":\"Sell\",\"order_id\":\"96872051\"}]}";
    
        List<MarketTradeVerbose> trades = oc.MakeMyTradeList(data);
        
        // Pre-Assert
        assertNotNull(obj);
        boolean result = obj.InsertTradeHistory(trades, ValidOrderMarketID, ExchangeList.Cryptsy);

        // Assert
        assertTrue(result);
    }
        
    @Test
    public void InsertTradeHistory_WhenListOfTradesNull_ExpectFalse(){
        IDbDAO obj = GenerateTestDB();

        // Pre-Assert
        assertNotNull(obj);
        boolean result = obj.InsertTradeHistory(null, ValidOrderMarketID, ExchangeList.Cryptsy);

        // Assert
        assertFalse(result);
    }
    
     @Test
    public void FetchActiveMarkets_WhenMarketsAreActive_ExpectNonEmptyList(){
        IDbDAO obj = GenerateTestDB();

        // Pre-Assert
        assertNotNull(obj);
        List<Integer> result = obj.FetchActiveMarketList(ExchangeList.Cryptsy);

        // Assert
        assertFalse(result.isEmpty());
    }
}
