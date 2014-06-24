package com.kungfuactiongrip.exchange.test;

import com.kungfuactiongrip.exchange.ExchangeList;
import com.kungfuactiongrip.exchange.IExchange;
import com.kungfuactiongrip.exchange.TransactionType;
import com.kungfuactiongrip.exchange.to.MarketBalanceList;
import com.kungfuactiongrip.exchange.to.MarketCreateOrderResult;
import com.kungfuactiongrip.exchange.to.MarketBuySellOrders;
import com.kungfuactiongrip.exchange.to.MarketCancelOrderResult;
import com.kungfuactiongrip.exchange.to.MarketOpenOrder;
import com.kungfuactiongrip.exchange.to.MarketTrade;
import com.kungfuactiongrip.exchange.to.MarketTradeFee;
import com.kungfuactiongrip.exchange.to.MarketTradeVerbose;
import com.kungfuactiongrip.exchange.to.ObjectConverter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
        MarketBalanceList amt = null;
        try {
            amt = exchange.FetchBalances();
        } catch (Exception ex) {
            Logger.getLogger(CryptsyTest.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        // Assert
        Assert.assertNotNull(amt);
        Double btcBalance = amt.FetchBalance("BTC");
        Assert.assertTrue(btcBalance > 0);
   }
   
   @Test
   public void GetMarketFees_ExpectFeesString(){
        // Setup
        IExchange exchange = ExchangeList.Cryptsy.GenerateExchangeObject();
       
        // Execute
        MarketTradeFee amt = null;
        try {
            amt = exchange.CalculateTransactionCost(TransactionType.Sell, 2.0, 0.0025);
        } catch (Exception ex) {
            Logger.getLogger(CryptsyTest.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        // Assert
        Assert.assertNotNull(amt);
        Assert.assertEquals(0.00001500, amt.Fee,0.000000001);
        Assert.assertEquals(0.00498500, amt.Net,0.000000001);
   }
   
   @Test
   public void FetchMarketTrades_ExpectValidMarketTradesList(){
        // Setup
        IExchange exchange = ExchangeList.Cryptsy.GenerateExchangeObject();
       
        // Execute
        List<MarketTrade> trades = null;
        try {
            trades = exchange.FetchMarketTrades(173);
        } catch (Exception ex) {
            Logger.getLogger(CryptsyTest.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        // Assert
        Assert.assertNotNull(trades);
        Assert.assertFalse(trades.isEmpty());
   }
   
   @Test
   public void FetchMarketOrders_ExpectValidMarketOrdersString(){
        // Setup
        IExchange exchange = ExchangeList.Cryptsy.GenerateExchangeObject();

        // Execute
        MarketBuySellOrders result = null;
        try {
            result = exchange.FetchMarketOrders(173);
        } catch (Exception ex) {
            Logger.getLogger(CryptsyTest.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        // Assert
        Assert.assertNotNull(result);
        Assert.assertFalse(result.BuyOrders.isEmpty());
        Assert.assertFalse(result.SellOrders.isEmpty());
   }
   
   @Test
   public void FetchMyOpenOrders_ExpectValidOpenOrdersString(){
       // Setup
       IExchange exchange = ExchangeList.Cryptsy.GenerateExchangeObject();
       
        // Execute
        List<MarketOpenOrder> orders = null;
        try {
            orders = exchange.FetchMyOpenOrdersForMarket(173);
        } catch (Exception ex) {
            Logger.getLogger(CryptsyTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Assert
        Assert.assertNotNull(orders);
        Assert.assertTrue(orders.isEmpty());
   }
   
   @Test
   public void CreateMarketTradeLowerThenMin_ExpectError(){
        // Setup
        IExchange exchange = ExchangeList.Cryptsy.GenerateExchangeObject();
       
        // Execute
        MarketCreateOrderResult result = null;
        try {
            result = exchange.CreateTrade(173, TransactionType.Buy, 0.1, 0.000001);
        } catch (Exception ex) {
            Logger.getLogger(CryptsyTest.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals("Total value of an order cannot be less than 0.00000010.  Please try again.", result.Message);
   }
   
   @Test
   public void CancelMarketTradeThatDoesNotExist_ExpectErrorString(){
        // Setup
        IExchange exchange = ExchangeList.Cryptsy.GenerateExchangeObject();
       
        // Execute
        MarketCancelOrderResult result = null;
        try {
            result = exchange.CancelTrade("97332445");
        } catch (Exception ex) {
            Logger.getLogger(CryptsyTest.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        // Assert
        Assert.assertNotNull(result);
        Assert.assertTrue(result.IsError);
        Assert.assertTrue(result.Message.contains("Order not found."));
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
       MarketCreateOrderResult tradeCreate = null;
       MarketCancelOrderResult cancelTrade = null;
        try {
            tradeCreate = exchange.CreateTrade(173, TransactionType.Buy, 20, 0.000001);
           
            // Cancel trade ;)
            cancelTrade = exchange.CancelTrade(tradeCreate.OrderID);
        } catch (Exception ex) {
            Logger.getLogger(CryptsyTest.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       // Assert
       Assert.assertNotNull(tradeCreate);
       Assert.assertNotNull(cancelTrade);
       // Check create string
       Assert.assertTrue(!tradeCreate.OrderID.equals(MarketCreateOrderResult.ErrorOrderID));
       Assert.assertTrue(tradeCreate.Message.contains("Your Buy order has been placed for"));
       // Check cancel string
       Assert.assertFalse(cancelTrade.IsError);
       Assert.assertTrue(cancelTrade.Message.contains("has been cancelled."));
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
   
   @Test
   public void FetchMyTradesForMarket_ExpectTradeData(){
        IExchange exchange = ExchangeList.Cryptsy.GenerateExchangeObject();
       
        List<MarketTradeVerbose> trades = null;
        try {
            trades = exchange.FetchMyTradesForMarket(173);
        } catch (Exception ex) {
            Logger.getLogger(CryptsyTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        Assert.assertNotNull(trades);
        Assert.assertFalse(trades.isEmpty());
         
   }
   
}
