-- Update Aborted Trade Timestamp
UPDATE tradeDataTest.BotTrade set rTS = now() where marketID = 179;

-- Update Closed Interval and Day Trades --

-- Update BUY and SELL Trades Day
UPDATE BotTrade set rTS = now() - INTERVAL 8 HOUR where rID in(36,37, 38, 39);

-- Update SELL and BUY Trades Interval
UPDATE BotTrade set rTS = now() - INTERVAL 2 HOUR where rID in(43,44);

-- UPDATE CLOSED BUY and SELL for Day and Interval
UPDATE BotTrade set rTS = now() - INTERVAL 2 HOUR where rID in(41,42);

-- UPDATE ABORTED BUY and SELL for Day 
UPDATE BotTrade set rTS = now() - INTERVAL 8 HOUR where rID in(45,47);

-- UPDATE ABORTED BUY and SELL for Interval 
UPDATE BotTrade set rTS = now() - INTERVAL 2 HOUR where rID in(46,48);

-- CLEAN UP MarketOrderData TABLE
TRUNCATE MarketOrderData;