package fpl_server.model.eventapi;

import java.io.Serializable;

public class PlayerDisplay implements Serializable{

	private Integer playerId;
	private String name;
	
	public PlayerDisplay(String name, String team, Integer playerId) {
		this.name = name;
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

	@Override
	public String toString() {
		return "PlayerDisplay [name=" + name + "]";
	}
	
	
	
}
