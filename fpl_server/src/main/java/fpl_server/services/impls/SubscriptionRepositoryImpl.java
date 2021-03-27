package fpl_server.services.impls;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import fpl_server.daos.interfaces.FplApiDao;
import fpl_server.objects.Subscription;
import fpl_server.services.interfaces.SubscriptionRepository;


@Repository
public class SubscriptionRepositoryImpl implements SubscriptionRepository {
	
	private Set<Subscription> subscriptions ;

	private final FplApiDao fplApiDao;
	
	@Autowired
	public SubscriptionRepositoryImpl(final FplApiDao fplApiDao) {
		this.fplApiDao = fplApiDao;
		subscriptions = new HashSet<Subscription>();
	}
	
	@Override
	public void saveCurrentGameweekSubscriptionForTeam(int teamId) {
		subscriptions.add(generateCurrentGameweekSubscriptionForTeam(teamId));
		return;
       
	}
	
	@Override 
	public Subscription generateCurrentGameweekSubscriptionForTeam(int teamId)
	{
		Optional<Subscription> existing = subscriptions.stream().filter(sub -> sub.getTeamId() == teamId).findFirst()  ;
		if(existing.isPresent()) {
			//Team already subscribed
			return existing.get();
		}
		
		return fplApiDao.getSubscriptionForTeamId(teamId);
		
	}
	 
	
	

	@Override
	public Set<Subscription> getSubscriptions() {
		return subscriptions;
	}
	
	@Override
	public Set<Integer> getSubscribedPlayerIds() {
		return subscriptions.stream().flatMap(sub -> sub.getPlayerIds().stream()).collect(Collectors.toSet());
	}

}
