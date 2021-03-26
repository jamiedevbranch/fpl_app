package fpl_server.model.eventapi;

import java.io.Serializable;

public class Event implements Serializable{

	private PlayerDisplay player;
	private EventType eventType;
	private int count;
	
	public PlayerDisplay getPlayer() {
		return player;
	}
	public void setPlayer(PlayerDisplay player) {
		this.player = player;
	}
	public EventType getEventType() {
		return eventType;
	}
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		return "Event [player=" + player + ", eventType=" + eventType + ", count=" + count + "]";
	}
	
	

	
}
