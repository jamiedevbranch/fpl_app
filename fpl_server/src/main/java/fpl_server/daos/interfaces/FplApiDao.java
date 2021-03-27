package fpl_server.daos.interfaces;

import fpl_server.objects.PlayerData;
import fpl_server.objects.Subscription;

public interface FplApiDao {

	/**
	 * Get name by player id
	 * @param playerId
	 * @return playerName
	 */
	String getPlayerName(Integer playerId);
	
	/**
	 * Load latest stats from server
	 * @return playerData
	 */
	PlayerData getLatestPlayerData();
	
	/**
	 * Get player ids for this team for the current gameweek
	 * @param teamId
	 * @return subscription
	 */
	Subscription getSubscriptionForTeamId(Integer teamId);
}
