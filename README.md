
Match Events Betting

This development  implement a service based on the simple interface provided the funcionality to manage a group of event in a collection.

It is  implementing a client, com.fsbtech.interviews.Client, using the entities supplied under the package com.fsbtech.interviews.entities. The Client interface has comments explaining the functionality required by each method.


Some notes on the data structure:
 + Category corresponds to a type of a sport o E.g. “Football”, “Tennis”
 + Each Category has many SubCategory
o E.g. in Football, “Premier League” or “Champions League” o E.g. in Tennis, “French Open” or “Wimbledon”
 + Each SubCategory has many Event
o E.g. in Football, “Manchester United v Arsenal” o E.g. in Tennis, “Andy Murray v Novak Djokovic”
 + Each Event has many different betting markets, each represented by a MarketRefType
o E.g. in Football, “Home/Draw/Away” for betting on the final outcome of the match o E.g. in Tennis, “Home/Away” for betting on the winner

The client is used for both managing and retrieving the data. For instance;
 +  Adding new Event
 +  Updating an Event as complete
 +  Add / remove MarketRefType to / from an Event
 +  Retrieve a list of Event objects based on filters (e.g. all Events with a “Home/Draw/Away”
market, regardless of the Category)
 +  Return the entire data structure as formatted output
Once an Event has completed, it is no longer of any relevance to the client. Similarly, a SubCategory with no valid events is also of no relevance to the client.
