package fpl_server.services.interfaces;

import java.util.List;
import java.util.Map;

import fpl_server.model.eventapi.Event;
import fpl_server.objects.Subscription;

public interface EventsService {
	
	/**
	 * Retrieve all the events for the players in this team so far 
	 * in the current gameweek. Does not create a subscription for that team.
	 * @param teamId
	 * @return allEventsForTeam
	 */
	List<Event> getAllEventsForTeamForCurrentGameweek(Integer teamId);

	/**
	 * Map from each subscription to the new events for that subscription's players
	 * since the last time getEventsForSubscriptions was called
	 * @return eventsPerSubscription
	 */
	Map<Subscription, List<Event>> getEventsForSubscriptions();

}
