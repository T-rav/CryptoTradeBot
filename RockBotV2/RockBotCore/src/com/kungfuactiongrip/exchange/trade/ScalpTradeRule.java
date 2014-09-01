package com.kungfuactiongrip.exchange.trade;

import com.kungfuactiongrip.exchange.TransactionType;
import com.kungfuactiongrip.exchange.to.MarketBuySellOrders;
import com.kungfuactiongrip.exchange.to.MarketCreateOrderResult;
import com.kungfuactiongrip.to.TradeOrder;
import com.kungfuactiongrip.to.TradeState;
import com.kungfuactiongrip.to.TradeType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
class ScalpTradeRule extends TradeRule {

    ScalpTradeRule() {
    }    

    @Override
    public void runImpl() {
        _lastRunState = ExecuteState.NOP;
        
        if((_lastRunState = ActionOpenBuyOrders()) == ExecuteState.NOP){
            
        }
    }
    
    private ExecuteState ActionOpenBuyOrders(){
        
        ExecuteState result = ExecuteState.NOP;
        try {
            List<TradeOrder> openBuys = _dbObj.FetchOpenBuyOrdersForMarket(_marketID);
            List<TradeOrder> openSells = _dbObj.FetchOpenSellOrdersForMarket(_marketID);
            
            if(!openBuys.isEmpty()){
          
                // Check to see if we are the best price still ;)
                MarketBuySellOrders buySellOrders = _exchangeObj.FetchMarketOrders(_marketID);

                TradeOrder myOrder = openBuys.get(0);
                double bestBuyPrice = buySellOrders.FetchBestBuyPrice();
                double myBuyPrice = myOrder.PricePer;
                if(myBuyPrice > bestBuyPrice){
                    // I need to cancel my buy order ;(
                    _exchangeObj.CancelTrade(myOrder.TradeID);
                    _dbObj.UpdateOrderState(myOrder.RowID, TradeState.ABORTED);
                    result =  ExecuteState.ABORTED_BUY_ORDER;
                }else{
                    result = ExecuteState.HOLD_BUY_ORDER;
                }
            }
            else if(openSells.isEmpty()){
                // create an order ;)
                
                // TODO : Calculate the buy price, calculate trade fees and see if margin is good enough ;)

                double amt = 0;
                double price = 0;
                double totalValue = amt * price;
                MarketCreateOrderResult tradeID = _exchangeObj.CreateTrade(_marketID, TransactionType.Buy, amt, price);
                _dbObj.InsertOrder(TradeType.BUY, TradeState.OPEN, _marketID, price, totalValue, tradeID.OrderID);
                result = ExecuteState.CREATED_BUY_ORDER;
            }
        } catch (Exception ex) {
            result = ExecuteState.EXCEPTION_BUY_ORDER;
            Logger.getLogger(ScalpTradeRule.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
}
