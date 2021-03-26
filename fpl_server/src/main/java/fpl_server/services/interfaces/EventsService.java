package fpl_server.services.interfaces;

import java.util.List;
import java.util.Map;

import fpl_server.model.eventapi.Event;
import fpl_server.objects.Subscription;

public interface EventsService {
		
	
	public List<Event> getNewEventsForSubscribedPlayers();
	
	public List<Event> getAllEventsForTeamForCurrentGameweek(Integer teamId);

	Map<Subscription, List<Event>> getEventsForSubscriptions();

}
