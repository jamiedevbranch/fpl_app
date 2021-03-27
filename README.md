# fpl_app

Spring boot server polling the FPL website to get the lastest player info, parsing player stat changes as Events, for all players in FPL teams which are subscribed for updates.
Publishes Events over websocket topics for each subscribed team as they are detected
e.g.:

{"events":[{"player":{"playerId":388,"name":"Harry Kane"},"eventType":"GOAL","count":1},{"player":{"playerId":388,"name":"Harry Kane"},"eventType":"CLEAN_SHEET_GAINED","count":1},{"player":{"playerId":42,"name":"Matt Targett"},"eventType":"YELLOW","count":1},{"player":{"playerId":202,"name":"Patrick Bamford"},"eventType":"GOAL","count":1},{"player":{"playerId":202,"name":"Patrick Bamford"},"eventType":"ASSIST","count":1},{"player":{"playerId":202,"name":"Patrick Bamford"},"eventType":"YELLOW","count":1},{"player":{"playerId":558,"name":"Vladimir Coufal"},"eventType":"YELLOW","count":1},{"player":{"playerId":570,"name":"Raphael Dias Belloli"},"eventType":"GOAL","count":1}]}

publish subscription request to /subscriptions/create with team id to subscribe team

updates for all players in each subscribed team published to /topic/subscription/{teamId} 

GET call to /rest/team/{teamId}/events gets all events so far for current gameweek (does not create subscription)
