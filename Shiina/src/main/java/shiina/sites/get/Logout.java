package shiina.sites.get;

import java.util.HashMap;
import java.util.Map;

import shiina.content.mysql;
import spark.Request;
import spark.Response;
import spark.Route;

public class Logout extends Route {

	public Map<String, Object> m = new HashMap<String, Object>();

	public Logout(String path) {
		super(path);
	}

	@Override
	public Object handle(Request request, Response response) {
		try {
			mysql.Exec("DELETE FROM `tokens` WHERE `token` = ?", request.cookie("token"));
		}catch (Exception e) {
			e.printStackTrace();
			return new Error().generateError(request, "java", e.getMessage());
		}
		response.removeCookie("token");
		
		response.redirect("/home");
		return null;
	}

}
