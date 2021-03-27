package fpl_server.daos.impls;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import fpl_server.daos.interfaces.FplApiDao;
import fpl_server.objects.Bootstrap;
import fpl_server.objects.Picks;
import fpl_server.objects.Player;
import fpl_server.objects.PlayerData;
import fpl_server.objects.Subscription;


@Repository
public class FplApiDaoImpl implements FplApiDao {

	Map<Integer, String> names = null;
		
	private Map<Integer, Date> gameweekDeadlines;
	
	@PostConstruct
	private void initialise() {
		initialiseNames();
		initialiseGameweeks();
	}
	
	private void initialiseGameweeks() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd hh:mm");

		gameweekDeadlines = new HashMap<Integer, Date>();
		try {
			gameweekDeadlines.put(38, dateFormat.parse("20210523 14:30"));
			gameweekDeadlines.put(37, dateFormat.parse("20210515 13:30"));
			gameweekDeadlines.put(36, dateFormat.parse("20210511 18:15"));
			gameweekDeadlines.put(35, dateFormat.parse("20210508 13:30"));
			gameweekDeadlines.put(34, dateFormat.parse("20210501 13:30"));
			gameweekDeadlines.put(33, dateFormat.parse("20210424 13:30"));
			gameweekDeadlines.put(32, dateFormat.parse("20210417 13:30"));
			gameweekDeadlines.put(31, dateFormat.parse("20210409 18:30"));
			gameweekDeadlines.put(30, dateFormat.parse("20210403 11:00"));
			gameweekDeadlines.put(29, dateFormat.parse("20210319 18:30"));
			gameweekDeadlines.put(28, dateFormat.parse("20210319 18:30"));
			gameweekDeadlines.put(27, dateFormat.parse("20210312 18:30"));
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void initialiseNames() {
		
		names = new HashMap<Integer, String>();
		
		InputStream is;
		try {
			is = new URL("https://fantasy.premierleague.com/api/bootstrap-static/").openStream();
			Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);

			Gson g = new Gson();

			Bootstrap bootstrap = g.fromJson(reader, Bootstrap.class);
			bootstrap.getElements().stream().forEach(
					element -> names.put(element.getId(), element.getFirst_name() + " " + element.getSecond_name()));

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public PlayerData getLatestPlayerData() {
		
		System.out.println("Loading played data");

		String webPage = "https://fantasy.premierleague.com/api/event/" + getCurrentGameweek()
		+ "/live/";
		InputStream is;
		try {
			is = new URL(webPage).openStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		
		GsonBuilder gsonBuilder = new GsonBuilder();

		//Rather than deserialising to a flat list that we have to iterate over to find players by id,
		//deserialise to a map of playerId to player for indexed id access
		JsonDeserializer<PlayerData> deserializer = new JsonDeserializer<PlayerData>() {  

			@Override
			public PlayerData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
					throws JsonParseException {
				JsonObject jsonObject = json.getAsJsonObject();
				Gson g = new Gson();
				Map<Integer, Player> players = new HashMap<Integer, Player>();
				for (JsonElement playerJson : jsonObject.getAsJsonArray("elements")) {
					Player p = g.fromJson(playerJson, Player.class);
					players.put(p.getId(), p);
				}
		       

		        return new PlayerData(players);
			}
		};
		gsonBuilder.registerTypeAdapter(PlayerData.class, deserializer);

		Gson customGson = gsonBuilder.create();  
		
		System.out.println("Loading player data");

		Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
		PlayerData customObject = customGson.fromJson(reader, PlayerData.class); 
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Loaded player data");

		return customObject;
		
	}

	@Override
	public String getPlayerName(Integer playerId) {
		return names.get(playerId);
	}
	
	
	private Integer getCurrentGameweek() {
		
		//TODO maybe cache gameweek val and increment on schedule instead of recalculating each time
		Date now = new Date();
		//get the latest gameweek with a past deadline 
		OptionalInt gameweek = gameweekDeadlines.entrySet().stream().filter(entry -> entry.getValue().before(now)).mapToInt(entry->entry.getKey()).max();
		
		if(gameweek.isPresent()) {
			return gameweek.getAsInt();
		}else {
			//TODO we're after gameweek 29 now so won't ever hit this case
			//Should put correct exceptions in place anyway
			return 1;
		}
		
	}
	
	

	@Override
	public Subscription getSubscriptionForTeamId(Integer teamId) {
		//269131 = me
		System.out.println("Loading subscription " + teamId);
		String webPage = "https://fantasy.premierleague.com/api/entry/" + teamId + "/event/" + getCurrentGameweek() + "/picks/";
		
        InputStream is;
		try {
			is = new URL(webPage).openStream();
			 Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
		        
		        Gson g = new Gson();
		        Picks picks = g.fromJson(reader, Picks.class);
		        
		        reader.close();
		        is.close();
		        
		        System.out.println("Loaded subscription " + teamId);

		        Subscription subscription = new Subscription();
		        subscription.setTeamId(teamId);
		        subscription.setPlayerIds(picks.getPicks().stream().map(pick -> pick.getElement()).collect(Collectors.toSet()));
		       		        
		        return subscription;
		        
		} catch (IOException e) {
			// TODO add proper exception handling
			e.printStackTrace();
			return null;
		}
	}

}
