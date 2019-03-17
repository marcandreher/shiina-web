package shiina.sites.get;

import java.util.HashMap;
import java.util.Map;

import kazukii.me.gg.sites.Permission;
import shiina.content.mysql;
import spark.Request;
import spark.Response;
import spark.Route;

public class Unfriend extends Route {

	public Map<String, Object> m = new HashMap<String, Object>();

	public Unfriend(String path) {
		super(path);
	}

	@Override
	public Object handle(Request request, Response response) {
		
		Permission.hasPermissions(request, m, response);
		
		
		try {
			mysql.Exec("DELETE FROM `users_relationships` WHERE `user1`=? AND `user2`=?", (String) m.get("userid"), request.queryParams("id"));
		} catch (Exception e) {
			return new Error().generateError(request, "java", e.getMessage().toString());
		}
		
		if(request.queryParams("action").contains("friend")) {
			response.redirect("/home/friends");
		}else if(request.queryParams("action").contains("user")) {
			response.redirect("/users/"+request.queryParams("id"));
		}
		
		return null;

}
}
