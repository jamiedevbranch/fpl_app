package fpl_server.services.interfaces;

import java.util.Set;

import fpl_server.objects.Subscription;

public interface SubscriptionService {

	public void saveCurrentGameweekSubscriptionForTeam(int teamId);
	
	public Set<Subscription> getSubscriptions();

	Set<Integer> getSubscribedPlayerIds();

	public Subscription generateCurrentGameweekSubscriptionForTeam(int teamId);
}
