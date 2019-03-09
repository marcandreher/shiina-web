package shiina.sites.get;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import kazukii.me.gg.sites.Permission;
import shiina.content.API;
import shiina.content.Friend;
import shiina.content.mysql;
import shiina.main.Site;
import spark.Request;
import spark.Response;
import spark.Route;

public class Friends extends Route {

	public Map<String, Object> m = new HashMap<String, Object>();

	public Friends(String path) {
		super(path);
	}

	@Override
	public Object handle(Request request, Response response) {
		m.put("titlebar", "Friends");

		if (!Permission.hasPermissions(request, m)) {
			response.redirect("/home");
			return null;
		}

		try {

			ResultSet usercount = mysql.Query("SELECT COUNT(*) AS count FROM users");
			while (usercount.next()) {
				m.put("users", usercount.getInt("count"));
			}
			ResultSet beatmaps = mysql.Query("SELECT COUNT(*) AS count FROM beatmaps");
			while (beatmaps.next()) {
				m.put("beatmaps", beatmaps.getInt("count"));
			}
			ResultSet scores = mysql.Query("SELECT COUNT(*) AS count FROM scores");
			while (scores.next()) {
				m.put("scores", scores.getInt("count"));
			}


			ArrayList<Friend> friends = new ArrayList<Friend>();
			ResultSet getallfriends = mysql.Query("SELECT * FROM users_relationships WHERE user1 = ?",
					m.get("userid").toString());
			while (getallfriends.next()) {
				Friend n = new Friend();

				String id = getallfriends.getString("user2");
				n.setID(id);

				if (API.banchorequest("isOnline?id=" + id).contains("true")) {
					n.setonline(true);
				}

				ResultSet getuserinfos = mysql.Query("SELECT * FROM users WHERE id = ?", id);
				while (getuserinfos.next()) {
					n.setname(getuserinfos.getString("username"));
					ResultSet getmutalstatus = mysql.Query("SELECT * FROM users_relationships WHERE user1 = ?", id);
					while (getmutalstatus.next()) {
						n.setStatus(true);
					}

				}
				friends.add(n);
			}

			m.put("friends", friends);
			m.put("fixed", "true");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			Template template = Site.cfg.getTemplate("friends.html");
			Writer out = new StringWriter();
			template.process(m, out);
			String outt = out.toString();
			return outt;
		} catch (IOException | TemplateException e) {
			throw new RuntimeException(e);
		}
	}

}
