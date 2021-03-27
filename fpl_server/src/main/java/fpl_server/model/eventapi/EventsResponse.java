package fpl_server.model.eventapi;

import java.io.Serializable;
import java.util.List;

public class EventsResponse implements Serializable{

	List<Event> events;

	public EventsResponse(List<Event> events) {
		this.events = events;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}
	
	
	
}
