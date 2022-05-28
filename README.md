# GroceryPriceMatch

This is the final project in ICS4UE

# Description: 
The program aims to create a platform for both customers and sellers to find the best deal. The program minimizes the handling fee and serves as a communication tool between customers and sellers. The customers can request a deal with the seller at a reasonable price and the numbers they are willing to buy, usually a large amount. In this way, the seller can gain a good amount of money and lower their storage load. 

The program has a database of the current prices from different grocery stores and the price over a long period of time. The user types in the item they are looking for and the program compares prices from different grocery stores. It outputs the best deal at the top of the list with detailed information and it also provides a price analysis over a period. The user decides if the current price is a good deal to buy.

The program also supports group order, users can send a request about what item and how many they are willing to buy which can be separate in packs or per kg for some item. The program stream has all the requests and different users can join the group. The users will need to input a date and location for further arrangement. 

Upgrade: When the price is too high, the user can place a deal with the information of how many and at what price they are willing to buy. In this way, the seller can find out the market demand and adjust their price. 

# Data Sources: 
Price analysis over time: Statistics Canada: Monthly average retail prices 
Using a webScraper: https://blog.hubspot.com/website/how-to-inspect  https://youtu.be/BEvRZUEQ3Dc  
**Targeted grocery stores: **
- Costco https://www.costcobusinesscentre.ca/grocery.html 
- Walmart https://www.walmart.ca/browse/grocery/10019	
- NoFills https://www.nofrills.ca	
- Sobeys https://voila.ca
- (more can be added as the project goes on)
Possible approach - using Java Persistence API (JPA) to manage data between Java objects and a database. 

# Challenges: 
How get the sources and maintain the database in good condition: 
- Keep track of the data and constantly updating them from online sources
- How to process the data from various databases with different formats
Group order, communication between multiple users
- How to store large amount of information of the user’s requests and display them onto the stream
Backup plan: if the web scraper doesn’t work, we will create a simulator app that the comparison app can call to compare the prices

# Description on a high-level of major classes/objects:
**PriceMatchManagement:** Program starting place, create a HomePage and contains the user list, ItemsDatabase, and PriceDatabase
**User** Object with username, password, and the group orders that the user committed and requested
**HomePage:** GUI, inner class includes, SearchItemPage, PriceAnalysisPage, GroupOrderPage
**ItemsDatabase:** A large database of the prices from different grocery stores
**PriceDatabase:** A database of the prices of the items since 1995, updated every month ← for price analyze use
**WebScraper:** a tool to collect data by extracting data from the HTML source code in the website

