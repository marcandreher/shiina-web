package shiina.sites.get;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import kazukii.me.gg.configs.Config;
import kazukii.me.gg.configs.u;
import kazukii.me.gg.sites.Permission;
import shiina.content.API;
import shiina.content.mysql;
import shiina.main.Site;
import spark.Request;
import spark.Response;
import spark.Route;

public class Profile extends Route {

	public Map<String, Object> m = new HashMap<String, Object>();

	public Profile(String path) {
		super(path);
	}

	static double decimal(double d){
		int dd = (int)d;
		d -=dd;
		return d;
	}
	
	@Override
	public Object handle(Request request, Response response) {

		Permission.hasPermissions(request, m);
		
		String name = null;
		int privileges = 0;
		try {
			ResultSet userdata = mysql.Query("SELECT * FROM `users` WHERE `id` = ?", request.params(":id"));

			Boolean exist = false;
			while (userdata.next()) {
				name = userdata.getString("username");
				privileges = userdata.getInt("privileges");
				exist = true;
			}

			if (exist == false) {
				return new Error().generateError(request, "404", "Not found");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String aka = null;
		String country = null;
		int favo = 0;
		try {
			ResultSet userstats = mysql.Query("SELECT * FROM `users_stats` WHERE `id` = ?", request.params(":id"));

			while (userstats.next()) {
				favo = userstats.getInt("favourite_mode");
				aka = userstats.getString("username_aka");
				country = userstats.getString("country");
				if (aka.length() <= 1) {
					continue;
				}
				m.put("paka", aka);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(request.params("mode") == null) {
			String mode = null;
			if(favo == 0) {
				mode = "osu";
			}else if(favo == 1) {
				mode = "taiko";
			}else if(favo == 2) {
				mode = "ctb";
			}else if(favo == 3) {
				mode = "mania";
			}
			response.redirect("/users/"+request.params(":id") + "/"+mode);
			return null;
		}
		
		try {
			ResultSet ranks = mysql.Query("SELECT * FROM `beatmap_ranks` WHERE `id` = ?", request.params(":id"));
			Boolean working = false;
			while (ranks.next()) {
				m.put("pSS", ranks.getInt("SS"));
				m.put("pSSH", ranks.getInt("SSH"));
				m.put("pS", ranks.getInt("S"));
				m.put("pSH", ranks.getInt("SH"));
				m.put("pA", ranks.getInt("A"));
			}
			if(working == false) {
				m.put("pSS", "0");
				m.put("pSSH", "0");
				m.put("pS", "0");
				m.put("pSH", "0");
				m.put("pA", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Permission.applySettings(m, request.params(":id"));
		m.put("pid", request.params(":id"));
		m.put("puser", name);
		m.put("pcountry", country);
		m.put("pprivileges", privileges);
		if(m.get("loggedin") == "true") {
			if (m.get("userid").toString().contains(m.get("pid").toString())) {
				m.put("yourself", "true");
			} else {
				m.put("yourself", "false");
			}
		}else {
			m.put("yourself", "false");
		}
		

		JSONObject jsonObject = new JSONObject(API.request("users/full?id=" + request.params(":id")));
		JSONObject mode = jsonObject.getJSONObject(request.params("mode").replaceAll("osu", "std").replaceAll("fruits", "ctb"));
		try {
			m.put("prank", mode.getInt("global_leaderboard_rank"));
		} catch (Exception e) {
			m.put("prank", "Unknown");
		}
		
		
		m.put("p_rscore", mode.getInt("ranked_score"));
		m.put("pscore", mode.getInt("total_score"));
		m.put("ppc", mode.getInt("playcount"));
		m.put("preplays", mode.getInt("replays_watched"));
		m.put("phits", mode.getInt("total_hits"));
		m.put("latest_activity", jsonObject.getString("latest_activity"));
		m.put("registered_on", jsonObject.getString("registered_on"));
		m.put("plvf", mode.getLong("level"));
		String lv = mode.getDouble("level") + "";
		try {
			m.put("plv", lv.substring((int)Math.log10(mode.getLong("level"))+2, lv.length()).substring(0,2));
		}catch(Exception e) {
			m.put("plv", "0");
			e.printStackTrace();
		}
		m.put("pacc", mode.getLong("accuracy"));
		m.put("ppp", mode.getInt("pp"));
		try {
			ResultSet getMedalCount = mysql.Query("SELECT COUNT(*) AS count FROM users_achievements WHERE user_id = ?", request.params("id"));
			while (getMedalCount.next()) {
				m.put("medals", getMedalCount.getInt("count"));
			}
		} catch (Exception e) {
			m.put("medals", 0);
		}
		
		try {
			ResultSet getMutalCount = mysql.Query("SELECT COUNT(*) AS count FROM users_relationships WHERE user2 = ?", request.params("id"));
			while (getMutalCount.next()) {
				m.put("subs", getMutalCount.getInt("count"));
			}
		} catch (Exception e) {
			m.put("subs", 0);
		}
		
		if(m.get("loggedin") == "true") {
			try {
				int i = 0;
				
					PreparedStatement stmt = mysql.getCon().prepareStatement("SELECT * FROM `users_relationships` WHERE user1= ? AND user2 = ?");
					stmt.setInt(1, Integer.parseInt(m.get("userid").toString()));
					stmt.setInt(2, Integer.parseInt(request.params(":id")));
					if (Config.getString("debug").contains("true")) {
						u.s.println(stmt.toString());
					}
					
					ResultSet getFriendStatus = stmt.executeQuery();
				while (getFriendStatus.next()) {
					 i = 1;
					m.put("isFriend", "true");
				}
				
				if(i==0) {
					m.put("isFriend", "false");
				}
			} catch (Exception e) {
				e.printStackTrace();
				m.put("isFriend", "false");
			}
		}

		m.put("titlebar", "Profile of " + name);
		m.put("fixed", "false");
		m.put("profile", "true");
		
		m.put("mode", request.params(":mode"));

		try {
			Template template = Site.cfg.getTemplate("profile.html");
			Writer out = new StringWriter();
			template.process(m, out);
			String outt = out.toString();
			return outt;
		} catch (IOException | TemplateException e) {
			throw new RuntimeException(e);
		}
	}

}
