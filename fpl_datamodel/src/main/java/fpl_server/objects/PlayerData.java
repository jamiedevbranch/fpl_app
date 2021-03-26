package fpl_server.objects;

import java.util.Map;

public class PlayerData {

	//The actual fpl api model is List<Player> elements, but we'll deserialise to a map (with clearer name 'players'),
	//to allow indexed players by id, rather than constantly iterating the list
	Map<Integer, Player> players;
	
	public PlayerData(Map<Integer, Player> players) {
		this.players = players;
	}

	public Map<Integer, Player> getPlayers() {
		return players;
	}

	public void setPlayers(Map<Integer, Player> players) {
		this.players = players;
	}
	
	
	
	
}
