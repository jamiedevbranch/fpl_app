package fpl_server.services.impls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fpl_server.daos.interfaces.FplApiDao;
import fpl_server.model.eventapi.Event;
import fpl_server.model.eventapi.EventType;
import fpl_server.model.eventapi.PlayerDisplay;
import fpl_server.objects.Player;
import fpl_server.objects.PlayerData;
import fpl_server.objects.Stats;
import fpl_server.objects.Subscription;
import fpl_server.services.interfaces.EventsService;
import fpl_server.services.interfaces.SubscriptionService;

@Service
public class EventsServiceImpl implements EventsService {

	private final SubscriptionService subscriptionService;
	
	private final FplApiDao fplApiDao;

	

	Map<Integer, Player> previousState;

	Set<Integer> subscribedPlayers;
	
	@Autowired
	public EventsServiceImpl(
			final SubscriptionService subscriptionService,
			final FplApiDao fplApiDao
		) {
		
		this.subscriptionService = subscriptionService;
		this.fplApiDao = fplApiDao;
		
		previousState = new HashMap<Integer, Player>();
		subscribedPlayers = new HashSet<Integer>();
	}
	
	//Pulls in newest subscription data from subscription service into this.subscribedPlayers,
	//In case there are new players we need to track, or players we no longer need to track
	private void refreshSubscriptions() {

		Set<Integer> newSubscribedPlayers = subscriptionService.getSubscribedPlayerIds();
		if(newSubscribedPlayers.equals(subscribedPlayers)) {
			return;
		}
		subscribedPlayers = newSubscribedPlayers;
		
		//Removes state data for all players to which we're no longer subscribed
		previousState.keySet().retainAll(newSubscribedPlayers);		

	}

	@Override
	public List<Event> getNewEventsForSubscribedPlayers() {

		
		refreshSubscriptions();
		
		//Exit if no subscribed players
		if(subscribedPlayers.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<Event> newEvents = new ArrayList<Event>();
	
		PlayerData latestPlayerData = fplApiDao.getLatestPlayerData();

		for (Integer subscribedPlayerId : subscribedPlayers) {

			//If we're getting a player for whom we haven't gotten a state yet (newly subscribebd),
			//Default to a player object with empty stats for correct diffing
			Player oldPlayerData = previousState.getOrDefault(subscribedPlayerId, new Player(subscribedPlayerId));

			Player newPlayerData = latestPlayerData.getPlayers().get(subscribedPlayerId);
			newEvents.addAll(diffStates(oldPlayerData, newPlayerData));
			previousState.put(subscribedPlayerId, newPlayerData);


		}
		return newEvents;

	}

	private List<Event> diffStates(Player oldState, Player newState) {

		List<Event> newEvents = new ArrayList<Event>();

		int playerId = newState.getId();
		
		PlayerDisplay playerDisplay = new PlayerDisplay(fplApiDao.getPlayerName(playerId), "", playerId);

		Stats oldStats = oldState.getStats();
		Stats newStats = newState.getStats();
		
		if (oldStats.getGoals_scored() != newStats.getGoals_scored()) {
			Event event = new Event();
			event.setEventType(EventType.GOAL);
			event.setPlayer(playerDisplay);
			event.setCount(newStats.getGoals_scored() - oldStats.getGoals_scored());
			
			newEvents.add(event);
		}

		if (oldStats.getAssists() != newStats.getAssists()) {
			Event event = new Event();
			event.setEventType(EventType.ASSIST);
			event.setPlayer(playerDisplay);
			event.setCount(newStats.getAssists() - oldStats.getAssists());
			
			newEvents.add(event);
		}

		if (oldStats.getYellow_cards() != newStats.getYellow_cards()) {
			Event event = new Event();
			event.setEventType(EventType.YELLOW);
			event.setPlayer(playerDisplay);
			event.setCount(newStats.getYellow_cards() - oldStats.getYellow_cards());

			newEvents.add(event);
		}

		if (oldStats.getRed_cards() != newStats.getRed_cards()) {
			Event event = new Event();
			event.setEventType(EventType.RED);
			event.setPlayer(playerDisplay);
			event.setCount(newStats.getRed_cards() - oldStats.getRed_cards());

			newEvents.add(event);
		}

		if (oldStats.getPenalties_missed() != newStats.getPenalties_missed()) {
			Event event = new Event();
			event.setEventType(EventType.PENALTY_MISS);
			event.setPlayer(playerDisplay);
			event.setCount(newStats.getPenalties_missed() - oldStats.getPenalties_missed());

			newEvents.add(event);
		}

		if (oldStats.getPenalties_saved() != newStats.getPenalties_saved()) {
			Event event = new Event();
			event.setEventType(EventType.PENALTY_SAVE);
			event.setPlayer(playerDisplay);
			event.setCount(newStats.getPenalties_saved() - oldStats.getPenalties_saved());

			newEvents.add(event);
		}

		//TODO - filter these out for strikers
		if (oldStats.getClean_sheets() < newStats.getClean_sheets()) {
			
			Event event = new Event();
			event.setEventType(EventType.CLEAN_SHEET_GAINED);
			event.setPlayer(playerDisplay);
			event.setCount(newStats.getClean_sheets() - oldStats.getClean_sheets());

			newEvents.add(event);
		} 
		else if (oldStats.getClean_sheets() > newStats.getClean_sheets()) {
			Event event = new Event();
			event.setEventType(EventType.CLEAN_SHEET_LOST);
			event.setPlayer(playerDisplay);
			event.setCount(newStats.getClean_sheets() - oldStats.getClean_sheets());

			newEvents.add(event);
		}

		return newEvents;

	}
	
	@Override
	public Map<Subscription, List<Event>> getEventsForSubscriptions(){
		Set<Subscription> subscriptions = subscriptionService.getSubscriptions();

		if(subscriptions.isEmpty()) {
			return Collections.emptyMap();
		}
		
		//First, get all the new events for subscribed players
		List<Event> newEvents = getNewEventsForSubscribedPlayers();

		//Then populate map, each sub mapping to the relevant subset of newEvents
		Map<Subscription, List<Event>> eventsForSubscriptions = new HashMap<Subscription, List<Event>>();
		for (Subscription subscription : subscriptions) {
			List<Event> eventsForSub = getEventsForSubscription(subscription, newEvents);
			eventsForSubscriptions.put(subscription, eventsForSub);
		}
		
		return eventsForSubscriptions;
	}

	private List<Event> getEventsForSubscription(Subscription sub, List<Event> events) {
		return events.stream().filter(event -> sub.getPlayerIds().contains(event.getPlayer().getPlayerId()))
				.collect(Collectors.toList());
	}



	@Override
	public List<Event> getAllEventsForTeamForCurrentGameweek(Integer teamId) {
		Subscription tmpSub = subscriptionService.generateCurrentGameweekSubscriptionForTeam(teamId);
		Set<Integer> tmpSubscribedPlayerIds = tmpSub.getPlayerIds();
		
		List<Event> newEvents = new ArrayList<Event>();
		
		Map<Integer, Player> playerStates;
		//If we already have a state for all the players in team, just derive events from that state 
		//instead of pulling down and parsing new events 
		//- trade off of data being as old as the last time it was polled,
		//But removes an additional FPL API call and reparsing of the player data
		if(previousState.keySet().containsAll(tmpSub.getPlayerIds())) {			
			playerStates = previousState;
		}else {
			playerStates = fplApiDao.getLatestPlayerData().getPlayers();
		}
		
		for(Integer tmpSubscribedPlayerId : tmpSubscribedPlayerIds) {			
			newEvents.addAll(diffStates(new Player(tmpSubscribedPlayerId), playerStates.get(tmpSubscribedPlayerId)));
		}
		
		List<Event> eventsForSub = getEventsForSubscription(tmpSub, newEvents);
		return eventsForSub;
	}
	
}
