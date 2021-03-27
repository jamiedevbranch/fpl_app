# fpl_app
standard spring boot server

publish subscription request to /subscriptions/create with team id to subscribe team

updates published to /topic/subscription/{teamId} for all subscribed teams

GET call to /rest/team/{teamId}/events gets all events so far for current gameweek
