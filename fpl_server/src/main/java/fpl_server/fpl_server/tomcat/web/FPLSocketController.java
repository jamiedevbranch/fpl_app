package fpl_server.fpl_server.tomcat.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import fpl_server.fpl_server.api.requests.SubscriptionRequest;
import fpl_server.services.interfaces.EventsService;
import fpl_server.services.interfaces.SubscriptionService;

@Controller
public class FPLSocketController {
	
	@Autowired
	SubscriptionService subscriptionService;

	@Autowired
	EventsService monitorService;
	
	@MessageMapping("/chat")
	@SendTo("/topic/messages")
	public String backAtcha(SubscriptionRequest subscriptionRequest) {
				
		System.out.println("subbin': " + subscriptionRequest.getTeamId());

		subscriptionService.saveCurrentGameweekSubscriptionForTeam(subscriptionRequest.getTeamId());
		
		//monitorService.processSubscriptions();
		
		return subscriptionRequest + ": that's cool i guess";
	}
	

	
}
