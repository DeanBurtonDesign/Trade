Stock Market Trading Game Database Structure
============================================

Database Structure

User Profile - Via Login, Twitter, Gmail, Facebook, LinkedIn

Name
Email
Gender
Basic Social Graph Info (Depends on login method above)
Address
Country
City

User Account - This stores all the critical user data

User Stars Points Logic = (User Trade Success /10 ) - (User Risk Avg. /10) + User Activity
User Trade Success - % Percentage of all profit trades = (Profit Trades / Total Trades) X 100  
User Risk Average - % (Average of all Trade Risks)
User Activity = ((Total Trades) / (Last Trade + Last Login)) / Total Trades 
  Total Trades (No. all trades)
  Last Trade (Hours ago)
  Last Login (Hours ago)

User Level
  Bronze = 1-3 star points
  Silver = 3-6 star points
  Gold = 6-9+ stars points

User Rank (Points position comparing all users)
User Bank (Virtual Money Amount $)
User Pips (Number of Pips the market has moved while the user has been active on a trade)
User Status
  Online
  Offline

User Trades - This stores the data for individual user trades

Symbol (AAPL)
Company Name (Apple Computer Inc)
Start Trade Price (!!! Not Current Price)
Start Trade Date
End Trade Price
End Trade Date
Trade Risk ((Trade Cost / User Bank) X 100)
Trade Profit
Trade Loss
Trade Pips
Trade Contracts
Trade Cost $ ( (Trade Contracts X Start Trade Price) + Trade Commission )
Trade Commission ($10 per Trade)
Trade Direction
  Buy (Long)
  Sell (Short)
User Views - Filters the markets shown on the main screen.
Trade - Currently active trades
Watch - Markets marked as Watched
Market Type - Market type identifiyer
Stocks (St) - All standard stocks
Forex (Fx) - All Forex markets
Indicies - (Id) All indicies

Global Market Data - This is contained in the data feed. We will store history of last year (may increase depending on system performance)

Symbol (AAPL)
Company Name (Apple Computer Inc)
Current Price
Open Price
Close Price
Volume
Market Status
  Open (Data flowing during business hours)
  Closed (No data flowing due to error or out of business hours)
