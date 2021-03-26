package fpl_server.model.eventapi;

import java.io.Serializable;

public class PlayerDisplay implements Serializable{

	private Integer playerId;
	private String name;
	private String team;
	
	public PlayerDisplay(String name, String team, Integer playerId) {
		this.name = name;
		this.team = team;
		this.playerId = playerId;
	}
	
	public Integer getPlayerId() {
		return playerId;
	}
	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}

	@Override
	public String toString() {
		return "PlayerDisplay [name=" + name + "]";
	}
	
	
	
}
