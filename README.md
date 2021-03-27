# fpl_app
standard spring boot server

publish subscription request to /subscriptions/create with team id to subscribe team

updates for all players in each subscribed team published to /topic/subscription/{teamId} 

GET call to /rest/team/{teamId}/events gets all events so far for current gameweek (does not create subscription)
