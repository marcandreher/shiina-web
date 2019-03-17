package kazukii.me.gg.sites;

import java.sql.ResultSet;
import java.util.HashMap;
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
	
	public static String bbcode(String text) {
        String html = text;

        Map<String,String> bbMap = new HashMap<String , String>();

        bbMap.put("(\r\n|\r|\n|\n\r)", "<br/>");
        bbMap.put("\\[b\\](.+?)\\[/b\\]", "<strong>$1</strong>");
        bbMap.put("\\[i\\](.+?)\\[/i\\]", "<span style='font-style:italic;'>$1</span>");
        bbMap.put("\\[u\\](.+?)\\[/u\\]", "<span style='text-decoration:underline;'>$1</span>");
        bbMap.put("\\[h1\\](.+?)\\[/h1\\]", "<h1>$1</h1>");
        bbMap.put("\\[h2\\](.+?)\\[/h2\\]", "<h2>$1</h2>");
        bbMap.put("\\[h3\\](.+?)\\[/h3\\]", "<h3>$1</h3>");
        bbMap.put("\\[h4\\](.+?)\\[/h4\\]", "<h4>$1</h4>");
        bbMap.put("\\[h5\\](.+?)\\[/h5\\]", "<h5>$1</h5>");
        bbMap.put("\\[h6\\](.+?)\\[/h6\\]", "<h6>$1</h6>");
        bbMap.put("\\[quote\\](.+?)\\[/quote\\]", "<blockquote>$1</blockquote>");
        bbMap.put("\\[p\\](.+?)\\[/p\\]", "<p>$1</p>");
        bbMap.put("\\[p=(.+?),(.+?)\\](.+?)\\[/p\\]", "<p style='text-indent:$1px;line-height:$2%;'>$3</p>");
        bbMap.put("\\[center\\](.+?)\\[/center\\]", "<div align='center'>$1");
        bbMap.put("\\[align=(.+?)\\](.+?)\\[/align\\]", "<div align='$1'>$2");
        bbMap.put("\\[color=(.+?)\\](.+?)\\[/color\\]", "<span style='color:$1;'>$2</span>");
        bbMap.put("\\[size=(.+?)\\](.+?)\\[/size\\]", "<span style='font-size:$1;'>$2</span>");
        bbMap.put("\\[img\\](.+?)\\[/img\\]", "<img src='$1' />");
        bbMap.put("\\[img=(.+?),(.+?)\\](.+?)\\[/img\\]", "<img width='$1' height='$2' src='$3' />");
        bbMap.put("\\[email\\](.+?)\\[/email\\]", "<a href='mailto:$1'>$1</a>");
        bbMap.put("\\[email=(.+?)\\](.+?)\\[/email\\]", "<a href='mailto:$1'>$2</a>");
        bbMap.put("\\[url\\](.+?)\\[/url\\]", "<a href='$1'>$1</a>");
        bbMap.put("\\[url=(.+?)\\](.+?)\\[/url\\]", "<a href='$1'>$2</a>");
        bbMap.put("\\[youtube\\](.+?)\\[/youtube\\]", "<object width='640' height='380'><param name='movie' value='http://www.youtube.com/v/$1'></param><embed src='http://www.youtube.com/v/$1' type='application/x-shockwave-flash' width='640' height='380'></embed></object>");
        bbMap.put("\\[video\\](.+?)\\[/video\\]", "<video src='$1' />");
        
        bbMap.put("\\[B\\](.+?)\\[/B\\]", "<strong>$1</strong>");
        bbMap.put("\\[I\\](.+?)\\[/I\\]", "<span style='font-style:italic;'>$1</span>");
        bbMap.put("\\[U\\](.+?)\\[/U\\]", "<span style='text-decoration:underline;'>$1</span>");
        bbMap.put("\\[H1\\](.+?)\\[/H1\\]", "<h1>$1</h1>");
        bbMap.put("\\[H2\\](.+?)\\[/H2\\]", "<h2>$1</h2>");
        bbMap.put("\\[H3\\](.+?)\\[/H3\\]", "<h3>$1</h3>");
        bbMap.put("\\[H4\\](.+?)\\[/H4\\]", "<h4>$1</h4>");
        bbMap.put("\\[H5\\](.+?)\\[/H5\\]", "<h5>$1</h5>");
        bbMap.put("\\[H6\\](.+?)\\[/H6\\]", "<h6>$1</h6>");
        bbMap.put("\\[QUOTE\\](.+?)\\[/QUOTE\\]", "<blockquote>$1</blockquote>");
        bbMap.put("\\[P\\](.+?)\\[/P\\]", "<p>$1</p>");
        bbMap.put("\\[P=(.+?),(.+?)\\](.+?)\\[/P\\]", "<p style='text-indent:$1px;line-height:$2%;'>$3</p>");
        bbMap.put("\\[CENTER\\](.+?)\\[/CENTER\\]", "<div align='center'>$1");
        bbMap.put("\\[ALIGN=(.+?)\\](.+?)\\[/ALIGN\\]", "<div align='$1'>$2");
        bbMap.put("\\[COLOR=(.+?)\\](.+?)\\[/COLOR\\]", "<span style='color:$1;'>$2</span>");
        bbMap.put("\\[SIZE=(.+?)\\](.+?)\\[/SIZE\\]", "<span style='font-size:$1;'>$2</span>");
        bbMap.put("\\[IMG\\](.+?)\\[/IMG\\]", "<img src='$1' />");
        bbMap.put("\\[IMG=(.+?),(.+?)\\](.+?)\\[/IMG\\]", "<img width='$1' height='$2' src='$3' />");
        bbMap.put("\\[EMAIL\\](.+?)\\[/EMAIL\\]", "<a href='mailto:$1'>$1</a>");
        bbMap.put("\\[EMAIL=(.+?)\\](.+?)\\[/EMAIL\\]", "<a href='mailto:$1'>$2</a>");
        bbMap.put("\\[URL\\](.+?)\\[/URL\\]", "<a href='$1'>$1</a>");
        bbMap.put("\\[URL=(.+?)\\](.+?)\\[/URL\\]", "<a href='$1'>$2</a>");
        bbMap.put("\\[YOUTUBE\\](.+?)\\[/YOUTUBE\\]", "<object width='640' height='380'><param name='movie' value='http://www.youtube.com/v/$1'></param><embed src='http://www.youtube.com/v/$1' type='application/x-shockwave-flash' width='640' height='380'></embed></object>");
        bbMap.put("\\[YOUTUBE\\](.+?)\\[/YOUTUBE\\]", "<video src='$1' />");

        for (@SuppressWarnings("rawtypes") Map.Entry entry: bbMap.entrySet()) {
            html = html.replaceAll(entry.getKey().toString(), entry.getValue().toString());
        }

        return html;
    }

}
