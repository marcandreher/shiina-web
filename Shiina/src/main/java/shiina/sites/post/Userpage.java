package shiina.sites.post;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import kazukii.me.gg.configs.u;
import kazukii.me.gg.sites.Permission;
import shiina.content.mysql;
import shiina.sites.get.Error;
import spark.Request;
import spark.Response;
import spark.Route;

public class Userpage extends Route {

	public Userpage(String path) {
		super(path);
	}

	public Map<String, Object> m = new HashMap<String, Object>();

	@Override
	public Object handle(Request request, Response response) {
		String userpage = request.queryParams("usepage");

		u.s.println(userpage);

		Permission.hasPermissions(request, m);

		int i = 0;

		try {
			ResultSet rs3 = mysql.Query("SELECT * FROM `user_extras` WHERE `id` = ?", m.get("userid").toString());
			while (rs3.next()) {
				i++;
			}
		} catch (Exception e) {
			return new Error().generateError(request, "java", e.getMessage().toString());
		}

		if (i == 0) {
			mysql.Exec(
					"INSERT INTO user_extras(id,interests,position,busyas,skype,discord,website,twitter,new_userpage) VALUES(?,?,?,?,?,?,?,?,?)",
					(String) m.get("userid"), "", "", "", "", "", "", "", userpage);
		} else {
			mysql.Exec("UPDATE `user_extras` SET `new_userpage`=? WHERE id = ?", userpage, m.get("userid").toString());
		}
		response.redirect("/home/account/edit");
		return null;
	}

}
