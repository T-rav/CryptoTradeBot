-- Update Aborted Trade Timestamp
UPDATE tradeDataTest.BotTrade set rTS = now() where marketID = 179;