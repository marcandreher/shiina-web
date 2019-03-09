package shiina.content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import kazukii.me.gg.configs.Config;
import kazukii.me.gg.configs.u;

public class API {

	public static String request(String route) {
		String url = "https://enjuu.click/api/v1/" + route;
		StringBuffer response2 = null;
		try {
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Shiina/Java");
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			response2 = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response2.append(inputLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (Config.getString("debug").contains("true")) {
			u.s.println("shiina.api.request: "+"/api/v1/"+route);
		}
		return response2.toString();
	}
	
	public static String banchorequest(String route) {
		String url = "https://c.enjuu.click/api/v1/" + route;
		StringBuffer response2 = null;
		try {
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Shiina/Java");
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			response2 = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response2.append(inputLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (Config.getString("debug").contains("true")) {
			u.s.println("shiina.api.bancho.request: "+"/api/v1/"+route);
		}
		return response2.toString();
	}

}
