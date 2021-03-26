package fpl_server.objects;

import java.util.HashSet;
import java.util.Set;

public class Subscription {
	
	private int teamId;
	private Set<Integer> playerIds = new HashSet<Integer>();
	
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	public Set<Integer> getPlayerIds() {
		return playerIds;
	}
	public void setPlayerIds(Set<Integer> playerIds) {
		this.playerIds = playerIds;
	}
	public void addPlayerId(Integer playerId) {
		this.getPlayerIds().add(playerId);
	}
	

}
