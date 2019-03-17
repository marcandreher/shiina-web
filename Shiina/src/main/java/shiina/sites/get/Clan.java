package shiina.sites.get;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import kazukii.me.gg.sites.Permission;
import shiina.content.API;
import shiina.content.mysql;
import shiina.main.Site;
import spark.Request;
import spark.Response;
import spark.Route;

public class Clan extends Route {

	public Map<String, Object> m = new HashMap<String, Object>();

	public Clan(String path) {
		super(path);
	}
	
	@Override
	public Object handle(Request request, Response response) {
		String mode = request.params(":mode");
		String clanid = request.params(":id");
		
		if(mode == null) {
			response.redirect("/clans/"+clanid+"/osu");
			return null;
		}
		Permission.hasPermissions(request, m);
		
		JSONObject jsonObject = new JSONObject(API.request("clans?id="+clanid));
		if(jsonObject.getInt("code") == 400) {
			return null;
		}
		JSONArray arr = (JSONArray) jsonObject.get("clans");
		JSONObject obj = arr.getJSONObject(0);
		String name = obj.getString("name");
		String description = obj.getString("description");
		String tag = obj.getString("tag");
		String icon = obj.getString("icon");
		m.put("name", name);
		m.put("description", Permission.bbcode(description));
		m.put("tag", tag);
		m.put("icon", icon);
		
		String modeint = null;
		if(mode.contains("osu")) {
			modeint = "000000000000";
		}else if(mode.contains("taiko")) {
			modeint = "111111111111";
		}else if(mode.contains("fruits")) {
			modeint = "222222222222";
		}else if(mode.contains("mania")) {
			modeint = "333333333333";
		}
		
		JSONObject jsonObject2 = new JSONObject(API.request("/clans/stats?id="+clanid+"&m="+modeint));
		m.put("rank", jsonObject2.getInt("rank"));
		m.put("ranked_score", jsonObject2.getJSONObject("chosen_mode").getBigInteger("ranked_score"));
		m.put("total_score", jsonObject2.getJSONObject("chosen_mode").getBigInteger("total_score"));
		m.put("playcount", jsonObject2.getJSONObject("chosen_mode").getBigInteger("playcount"));
		m.put("replays_watched", jsonObject2.getJSONObject("chosen_mode").getBigInteger("replays_watched"));
		m.put("pp", jsonObject2.getJSONObject("chosen_mode").getBigInteger("pp"));
		
		ArrayList<shiina.content.Friend> f = new ArrayList<>();
		try {
			int i = 0;
			ResultSet rs = mysql.Query("SELECT * FROM `user_clans` WHERE `clan` = ? AND `perms` = 8", clanid);
			while(rs.next()) {
				i++;
				ResultSet rs2 = mysql.Query("SELECT * FROM `users_stats` WHERE id=?", rs.getInt("user") + "");
				while(rs2.next()) {
					shiina.content.Friend f2 = new shiina.content.Friend();
					f2.setCountry(rs2.getString("country"));
					f2.setPP(rs2.getString("pp_" + mode.replaceAll("fruits", "ctb").replaceAll("osu", "std")));
					f2.setID( rs.getInt("user")+ "");
					f2.setname(rs2.getString("username"));
					f.add(f2);
				}
			}
			m.put("ownercount", i);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		m.put("admins", f);
		
		ArrayList<shiina.content.Friend> f22 = new ArrayList<>();
		try {
			ResultSet rs = mysql.Query("SELECT * FROM `user_clans` WHERE `clan` = ? AND `perms` = 1", clanid);
			while(rs.next()) {
				ResultSet rs2 = mysql.Query("SELECT * FROM `users_stats` WHERE id=?", rs.getInt("user") + "");
				while(rs2.next()) {
					shiina.content.Friend f2 = new shiina.content.Friend();
					f2.setCountry(rs2.getString("country"));
					f2.setPP(rs2.getString("pp_" + mode.replaceAll("fruits", "ctb").replaceAll("osu", "std")));
					f2.setID( rs.getInt("user")+ "");
					f2.setname(rs2.getString("username"));
					f22.add(f2);
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		m.put("members", f22);
		
		
		m.put("fixed", "false");
		m.put("profile", "false");
		m.put("titlebar", name);
		m.put("cid", clanid);
		
		m.put("mode", mode);

		try {
			Template template = Site.cfg.getTemplate("clan.html");
			Writer out = new StringWriter();
			template.process(m, out);
			String outt = out.toString();
			return outt;
		} catch (IOException | TemplateException e) {
			throw new RuntimeException(e);
		}
	}

}
