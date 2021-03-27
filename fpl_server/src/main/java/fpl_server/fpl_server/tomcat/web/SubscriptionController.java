package fpl_server.fpl_server.tomcat.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import fpl_server.model.eventapi.SubscriptionRequest;
import fpl_server.services.interfaces.EventsService;
import fpl_server.services.interfaces.SubscriptionService;

@Controller
public class SubscriptionController {
	
	@Autowired
	SubscriptionService subscriptionService;

	@Autowired
	EventsService monitorService;
	
	@MessageMapping("/create")
	public void saveSubscription(SubscriptionRequest subscriptionRequest) {
				
		System.out.println("subbin': " + subscriptionRequest.getTeamId());

		subscriptionService.saveCurrentGameweekSubscriptionForTeam(subscriptionRequest.getTeamId());
		
		return;
	}
	

	
}
