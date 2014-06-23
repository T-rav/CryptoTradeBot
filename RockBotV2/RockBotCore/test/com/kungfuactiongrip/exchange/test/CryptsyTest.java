package com.kungfuactiongrip.exchange.test;

import com.kungfuactiongrip.exchange.ExchangeList;
import com.kungfuactiongrip.exchange.IExchange;
import com.kungfuactiongrip.exchange.TransactionType;
import com.kungfuactiongrip.exchange.objects.CryptsyOrder;
import com.kungfuactiongrip.exchange.objects.ObjectConverter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Administrator
 */
public class CryptsyTest {
    
    public CryptsyTest() {
        
        
    }

   @Test
   public void GeneratCryptsyObject(){
       // Setup
       
       // Execute
       IExchange exchange = ExchangeList.Cryptsy.GenerateExchangeObject();
       String classOf = exchange.getClass().getSimpleName();
       
       // Assert
       Assert.assertEquals("Cryptsy", classOf);
   }
   
   @Test
   public void GetMarketInfo_ExpectValidMarketInfoString(){
       // Setup
       IExchange exchange = ExchangeList.Cryptsy.GenerateExchangeObject();
       
       // Execute
       String amt = null;
        try {
            amt = exchange.FetchMarketInfo();
        } catch (Exception ex) {
            Logger.getLogger(CryptsyTest.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       // Assert
       Assert.assertNotNull(amt);
       Assert.assertTrue(!"{\"success\":\"0\",\"error\":\"Unable to Authorize Request - Check Your Post Data\"}".equals(amt));
   }
   
   @Test
   public void GetMarketFees_ExpectFeesString(){
       // Setup
       IExchange exchange = ExchangeList.Cryptsy.GenerateExchangeObject();
       
       // Execute
       String amt = null;
        try {
            amt = exchange.CalculateTransactionCost(TransactionType.Sell, 2.0, 0.0025);
        } catch (Exception ex) {
            Logger.getLogger(CryptsyTest.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       // Assert
       Assert.assertNotNull(amt);
       Assert.assertTrue("{\"success\":\"1\",\"return\":{\"fee\":\"0.00001500\",\"net\":\"0.00498500\"}}".equals(amt));
   }
   
   @Test
   public void FetchMarketTrades_ExpectValidMarketTradesString(){
       // Setup
       IExchange exchange = ExchangeList.Cryptsy.GenerateExchangeObject();
       
       // Execute
       String amt = null;
        try {
            //amt = exchange.FetchMarketTrades(173);
        } catch (Exception ex) {
            Logger.getLogger(CryptsyTest.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       // Assert
       Assert.assertNotNull(amt);
       Assert.assertTrue(amt.contains("{\"success\":\"1\",\"return\":[{\"tradeid\""));
   }
   
   @Test
   public void FetchMarketOrders_ExpectValidMarketOrdersString(){
        // Setup
        IExchange exchange = ExchangeList.Cryptsy.GenerateExchangeObject();

        // Execute
        String amt = null;
        try {
            //amt = exchange.FetchMarketOrders(173);
        } catch (Exception ex) {
            Logger.getLogger(CryptsyTest.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        // Assert
        Assert.assertNotNull(amt);
        Assert.assertTrue(amt.contains("{\"success\":\"1\",\"return\":{\"sellorders\":"));
   }
   
   @Test
   public void FetchMyOpenOrders_ExpectValidOpenOrdersString(){
       // Setup
       IExchange exchange = ExchangeList.Cryptsy.GenerateExchangeObject();
       
       // Execute
       String amt = null;
        try {
            amt = exchange.FetchMyOpenOrdersForMarket(173);
        } catch (Exception ex) {
            Logger.getLogger(CryptsyTest.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       // Assert
       Assert.assertNotNull(amt);
       Assert.assertTrue(amt.contains("{\"success\":\"1\",\"return\":["));
   }
   
   @Test
   public void CreateMarketTradeLowerThenMin_ExpectError(){
       // Setup
       IExchange exchange = ExchangeList.Cryptsy.GenerateExchangeObject();
       
       // Execute
       String amt = null;
        try {
            amt = exchange.CreateTrade(173, TransactionType.Buy, 0.1, 0.000001);
        } catch (Exception ex) {
            Logger.getLogger(CryptsyTest.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       // Assert
       Assert.assertNotNull(amt);
       Assert.assertTrue(amt.equals("{\"success\":\"0\",\"error\":\"Total value of an order cannot be less than 0.00000010.  Please try again.\"}"));
   }
   
   @Test
   public void CancelMarketTradeThatDoesNotExist_ExpectErrorString(){
       // Setup
       IExchange exchange = ExchangeList.Cryptsy.GenerateExchangeObject();
       
       // Execute
       String amt = null;
        try {
            amt = exchange.CancelTrade("97332445");
        } catch (Exception ex) {
            Logger.getLogger(CryptsyTest.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       // Assert
       Assert.assertNotNull(amt);
       Assert.assertTrue(amt.equals("{\"success\":\"0\",\"error\":\"Order not found.\"}"));
   }
   
   @Test
   public void FetchAllActiveMarketData_ExpectValidMarketDataString(){
        // Setup
       IExchange exchange = ExchangeList.Cryptsy.GenerateExchangeObject();
       
       // Execute
       String activeMarketData = null;
        try {
            activeMarketData = exchange.FetchActiveMarketData();
        } catch (Exception ex) {
            Logger.getLogger(CryptsyTest.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       // Assert
       Assert.assertNotNull(activeMarketData);
       
       Assert.assertTrue(activeMarketData.contains("{\"success\":\"1\",\"return\":[{\"marketid\""));
       
   }
   
   // ** Integration Test ** //
   
   @Test
   public void CreateMarketTradeAboveMinAndCancelIt_ExpectTransactionWorks(){
       // Setup
       IExchange exchange = ExchangeList.Cryptsy.GenerateExchangeObject();
       
       // Execute
       String tradeCreate = null;
       String cancelTrade = null;
        try {
            tradeCreate = exchange.CreateTrade(173, TransactionType.Buy, 20, 0.000001);
            // Extract ID
            ObjectConverter oc = new ObjectConverter();
            CryptsyOrder orderObj = oc.MakeOrderObject(tradeCreate);
            Assert.assertNotNull(orderObj);
            // Cancel trade ;)
            cancelTrade = exchange.CancelTrade(orderObj.orderid);
        } catch (Exception ex) {
            Logger.getLogger(CryptsyTest.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       // Assert
       Assert.assertNotNull(tradeCreate);
       Assert.assertNotNull(cancelTrade);
       // Check create string
       Assert.assertTrue(tradeCreate.contains("{\"success\":\"1\",\"orderid\":\""));
       Assert.assertTrue(tradeCreate.contains("Your Buy order has been placed for"));
       // Check cancel string
       Assert.assertTrue(cancelTrade.contains("{\"success\":\"1\""));
       Assert.assertTrue(cancelTrade.contains("has been cancelled."));
   }
   
   @Test
   public void FetchMarketData_ExpectMarketData(){
       IExchange exchange = ExchangeList.Cryptsy.GenerateExchangeObject();
       
       String marketData = "";
        try {
            marketData = exchange.FetchAllMarketData();
        } catch (Exception ex) {
            Logger.getLogger(CryptsyTest.class.getName()).log(Level.SEVERE, null, ex);
        }

       Assert.assertTrue(marketData.contains("{\"success\":1,\"return\":{\"markets\":"));
   }
   
   @Test
   public void FetchOrderData_ExpectOrderData(){
       IExchange exchange = ExchangeList.Cryptsy.GenerateExchangeObject();
       
       String marketData = "";
        try {
            marketData = exchange.FetchAllOrderData();
        } catch (Exception ex) {
            Logger.getLogger(CryptsyTest.class.getName()).log(Level.SEVERE, null, ex);
        }

       Assert.assertTrue(marketData.contains("{\"success\":1,\"return\":{"));
   }
   
}
