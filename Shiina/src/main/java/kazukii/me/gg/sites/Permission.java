package kazukii.me.gg.sites;

import java.sql.ResultSet;
import java.util.Map;

import shiina.content.mysql;
import spark.Request;

public class Permission {
	
	public static boolean hasPermissions(Request request, Map<String, Object> map) {
		if(request.cookie("token") == null) {
			map.put("loggedin", "false");
			return false;
		}else {
			String token = request.cookie("token");
			
			int id = 0;
			try {
				ResultSet rs = mysql.Query("SELECT * FROM `tokens` WHERE `token` = ?", token);
				Boolean tokenexist = false;
				while (rs.next()) {
					tokenexist = true;
					map.put("loggedin", "true");
					id = rs.getInt("user");
				}
				
				if(!tokenexist) { map.put("loggedin", "false"); return false; }
				
			}catch(Exception e) {
				e.printStackTrace();
				return false;
			}
			
			map.put("userid", id + "");
			String name = "";
			int privileges = 0;
			String email = "";
			
			try {
				ResultSet rs = mysql.Query("SELECT * FROM `users` WHERE `id` = ?", id + "");
				while (rs.next()) {
					name = rs.getString("username");
					privileges = rs.getInt("privileges");
					email = rs.getString("email");
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			map.put("user", name);
			map.put("privileges", privileges);
			map.put("email", email);
			
		}
		return true;
		
	}
	
	public static void applySettings(Map<String, Object> map, String id) {
		
		int i = 0;
		try {
			ResultSet rs = mysql.Query("SELECT * FROM `user_extras` WHERE `id` = ?", id);
			while (rs.next()) {
				i++;
				map.put("location", rs.getString("position"));
				map.put("interests", rs.getString("interests"));
				map.put("busyas", rs.getString("busyas"));
				
				map.put("twitter", rs.getString("twitter"));
				map.put("discord", rs.getString("discord"));
				map.put("skype", rs.getString("skype"));
				map.put("site", rs.getString("website"));
				map.put("userpages", rs.getString("new_userpage"));
				if(rs.getString("new_userpage") == null) {
					map.remove("userpages");
					map.put("userpages", "null");
				}
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		if(i == 0) {
			map.put("location", "");
			map.put("interests", "");
			map.put("busyas", "");
			
			map.put("all1", "null");
			
			map.put("twitter", "");
			map.put("discord", "");
			map.put("skype", "");
			map.put("site", "");
			
			map.put("all2", "null");
			
			map.put("userpages", "");
		}else {
			if(!(map.get("location") == "") & !(map.get("interests") == "") & !(map.get("busyas") == "")) {
				map.put("all1", "ss");
			}
			
			if(!(map.get("twitter") == "") & !(map.get("discord") == "") & !(map.get("skype") == "") & !(map.get("site") == "")) {
				map.put("all2", "ss");
			}
		}
		
		
		
	}

}
