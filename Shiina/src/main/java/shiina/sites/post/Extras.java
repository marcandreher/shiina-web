package shiina.sites.post;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import kazukii.me.gg.sites.Permission;
import shiina.content.mysql;
import spark.Request;
import spark.Response;
import spark.Route;

public class Extras extends Route{

	public Extras(String path) {
		super(path);
	}
	
		
	public Map<String, Object> m = new HashMap<String, Object>();

	@Override
	public Object handle(Request request, Response response) {
		String interests = request.queryParams("interests");
		String position = request.queryParams("position");
		String busyas = request.queryParams("busyas");
		
		String twitter = request.queryParams("twitter");
		String discord = request.queryParams("discord");
		String skype = request.queryParams("skype");
		String site = request.queryParams("website");
		
		Permission.hasPermissions(request, m);
		
		int i = 0;
		
		try {
			ResultSet rs3 = mysql.Query("SELECT * FROM `user_extras` WHERE `id` = ?", m.get("userid").toString());
			while (rs3.next()) {
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(i == 0) {
			mysql.Exec("INSERT INTO user_extras(id,interests,position,busyas,skype,discord,website,twitter) VALUES(?,?,?,?,?,?,?,?)", m.get("userid").toString(), interests, position, busyas, skype,discord,site,twitter);
		}else {
			 mysql.Exec("UPDATE `user_extras` SET `interests`=?,`position`=?,`busyas`=?,`twitter`=?,`discord`=?,`skype`=?,`website`=? WHERE id = ?", 
					 interests, position,busyas,twitter,discord,skype,site, (String) m.get("userid"));
		}
		response.redirect("/home/account/edit");
		return null;
	}
		

}
