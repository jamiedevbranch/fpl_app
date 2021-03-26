package fpl_server.daos.interfaces;

import fpl_server.objects.PlayerData;
import fpl_server.objects.Subscription;

public interface FplApiDao {

	String getPlayerName(Integer playerId);
	
	PlayerData getLatestPlayerData();
	
	Subscription getSubscriptionForTeamId(Integer teamId);
}
