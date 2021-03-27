package fpl_server.web;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import fpl_server.model.eventapi.Event;
import fpl_server.model.eventapi.EventsResponse;
import fpl_server.objects.Subscription;
import fpl_server.services.interfaces.EventsService;

@Controller
@EnableScheduling
public class EventsPublisher {

	private final EventsService eventsService;
	
	private final SimpMessagingTemplate template;
		
	@Autowired
	public EventsPublisher(
			final EventsService eventsService,
			final SimpMessagingTemplate template) {
		this.eventsService = eventsService;
		this.template = template;		

	}
	
	@Scheduled(fixedDelay = 60 * 1000)
	public void publishEventsForSubscriptions() {
		
		Map<Subscription, List<Event>> eventsForSubscriptions = eventsService.getEventsForSubscriptions();

		for (Subscription subscription : eventsForSubscriptions.keySet()) {
			
			List<Event> eventsForSub = eventsForSubscriptions.get(subscription);

			EventsResponse response = new EventsResponse(eventsForSub);
			template.convertAndSend("/topic/subscription/" + subscription.getTeamId()  , response);
		}

	}
}
