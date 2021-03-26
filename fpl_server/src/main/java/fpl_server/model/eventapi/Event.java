package fpl_server.model.eventapi;

import java.io.Serializable;

public class Event implements Serializable{

	private PlayerDisplay player;
	private int minute;
	private EventType eventType;
	
	public PlayerDisplay getPlayer() {
		return player;
	}
	public void setPlayer(PlayerDisplay player) {
		this.player = player;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public EventType getEventType() {
		return eventType;
	}
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	@Override
	public String toString() {
		return "Event [player=" + player + ", eventType=" + eventType + "]";
	}
	
	

	
}
