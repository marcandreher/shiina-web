package shiina.sites.get;

import java.util.HashMap;
import java.util.Map;

import kazukii.me.gg.sites.Permission;
import shiina.content.mysql;
import spark.Request;
import spark.Response;
import spark.Route;

public class Friend extends Route {

	public Map<String, Object> m = new HashMap<String, Object>();

	public Friend(String path) {
		super(path);
	}

	@Override
	public Object handle(Request request, Response response) {
		
		Permission.hasPermissions(request, m);
		
		if(request.queryParams("action") == null || request.queryParams("user") == null) {
			return new Error().generateError(request, "null", "null");
		}
		
		if(!request.queryParams("action").contains("user")) {
			return new Error().generateError(request, "null", "null");
		}
		
		try {
			mysql.Exec("INSERT INTO `users_relationships`(`user1`, `user2`) VALUES (?,?)", (String) m.get("userid"), request.queryParams("user"));
		} catch (Exception e) {
			return new Error().generateError(request, "java", e.getMessage().toString());
		}
		
		if(request.queryParams("action").contains("friend")) {
			response.redirect("/home/friends");
			return null;
			
		}else if(request.queryParams("action").contains("user")) {
			response.redirect("/users/"+request.queryParams("user"));
			return null;
		}
		
		return null;

}
}
