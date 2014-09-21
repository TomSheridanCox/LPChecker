import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;


public class QueryHandler 
{
	private String apiKey = ""; //DELETE THIS BEFORE COMMIT
	private String line;
	private String response = "";
	
	public String getSummID(String summName, String region){
		String summID = "";
		URL summURL;
		try {
			summURL = new URL("https://"+region+".api.pvp.net/api/lol/"+region+"/v1.4/summoner/by-name/"+summName+"?api_key="+apiKey);
			URLConnection connection = summURL.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while((line = rd.readLine()) != null){
				response = line;
			}
			JSONObject obj = new JSONObject(response);
			summID = obj.getJSONObject(summName.toLowerCase()).get("id").toString();
			rd.close();
		} catch (IOException e1) {
			return "failed";
		}
		return summID;
	}
	
	public long getRankedMatchTime(String summID, String region){
		long matchTime;
		try {
			URL url = new URL("https://"+region+".api.pvp.net/api/lol/"+region+"/v2.2/matchhistory/"+summID+"?rankedQueues=RANKED_SOLO_5x5&api_key="+apiKey);
			URLConnection connection = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while((line = rd.readLine()) != null){
				response = line;
			}
			JSONObject obj = new JSONObject(response);
			JSONArray arr = obj.getJSONArray("matches");	
			matchTime = arr.getJSONObject(arr.length()-1).getLong("matchCreation");	
			rd.close();
		} catch (IOException e) {
			return 0;
		}
		return matchTime;
	}
}
