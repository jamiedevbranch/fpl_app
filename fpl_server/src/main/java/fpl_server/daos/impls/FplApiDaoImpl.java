package fpl_server.daos.impls;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
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
	
	@PostConstruct
	private void initialise() {
		initialiseNames();
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
		Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);

		
		GsonBuilder gsonBuilder = new GsonBuilder();

		JsonDeserializer<PlayerData> deserializer = new JsonDeserializer<PlayerData>() {  

			@Override
			public PlayerData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
					throws JsonParseException {
				JsonObject jsonObject = json.getAsJsonObject();
				//JsonObject elements = jsonObject.getAsJsonArray("elements")
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
		PlayerData customObject = customGson.fromJson(reader, PlayerData.class); 
		return customObject;
		
	}

	@Override
	public String getPlayerName(Integer playerId) {
		return names.get(playerId);
	}
	
	
	private Integer getCurrentGameweek() {
		
		//TODO
		return 29;
	}

	@Override
	public Subscription getSubscriptionForTeamId(Integer teamId) {
		//269131 = me
		String webPage = "https://fantasy.premierleague.com/api/entry/" + teamId + "/event/" + getCurrentGameweek() + "/picks/";
		
        InputStream is;
		try {
			is = new URL(webPage).openStream();
			 Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
		        
		        Gson g = new Gson();
		        Picks picks = g.fromJson(reader, Picks.class);
		        
		        reader.close();
		        is.close();
		        
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
