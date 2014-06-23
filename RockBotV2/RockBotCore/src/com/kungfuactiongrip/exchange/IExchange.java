package com.kungfuactiongrip.exchange;

import com.kungfuactiongrip.exchange.objects.MarketBuySellOrders;
import com.kungfuactiongrip.exchange.objects.MarketTrade;
import java.util.List;

/**
 *
 * @author Administrator
 */
public interface IExchange {

    /**
     *
     * @return JSON Market Data
     * @throws Exception
     */
    public String FetchMarketInfo() throws Exception;
    
    /**
     *
     * @param typeOf
     * @param amt
     * @param price
     * @return JSON String with Fees and Net BTC
     * @throws Exception
     */
    public String CalculateTransactionCost(TransactionType typeOf, double amt, double price) throws Exception;

//    /**
//     *
//     * @param marketID
//     * @return JSON String with Market Trades
//     * @throws Exception
//     */
//    public List<MarketTrade> FetchMarketTrades(int marketID) throws Exception;

    /**
     *
     * @param marketID
     * @return JSON String with Market Orders
     * @throws Exception
     */
    public MarketBuySellOrders FetchMarketOrders(int marketID) throws Exception;

    /**
     *
     * @param marketID
     * @return JSON String with Open Market Orders
     * @throws Exception
     */
    public String FetchMyOpenOrdersForMarket(int marketID) throws Exception;

    /**
     *
     * @param marketID
     * @param transactionType
     * @param amt
     * @param price
     * @return JSON String of Trade Creation Data
     * @throws Exception
     */
    public String CreateTrade(int marketID, TransactionType transactionType, double amt, double price) throws Exception;

    /**
     *
     * @param orderID
     * @return JSON String of Cancel Data
     * @throws Exception
     */
    public String CancelTrade(String orderID) throws Exception;

    /**
     *
     * @return JSON String of Market Data
     * @throws java.lang.Exception
     */
    public String FetchAllMarketData() throws Exception;

    /**
     *
     * @return JSON String of Order Data
     * @throws Exception
     */
    public String FetchAllOrderData() throws Exception;

    /**
     *
     * @return JSON String of Active Market Data
     * @throws Exception
     */
    public String FetchActiveMarketData() throws Exception;
}
