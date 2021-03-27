package fpl_server.services.interfaces;

import java.util.Set;

import fpl_server.objects.Subscription;

public interface SubscriptionRepository {

	
	/**
	 * Load a subscription object for the given team id, without persisting it
	 * @param teamId
	 * @return
	 */
	Subscription generateCurrentGameweekSubscriptionForTeam(int teamId);
	
	/**
	 * Load and persist a subscription for the current gameweek for the given team id
	 * @param teamId
	 */
	void saveCurrentGameweekSubscriptionForTeam(int teamId);

	/**
	 * Get persisted subscriptions
	 * @return subscriptions
	 */
	Set<Subscription> getSubscriptions();

	/**
	 * Get the set of player ids across all persisted subscriptions
	 * @return playerIds
	 */
	Set<Integer> getSubscribedPlayerIds();

	
}
