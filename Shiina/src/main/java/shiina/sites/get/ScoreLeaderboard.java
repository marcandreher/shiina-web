package shiina.sites.get;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import kazukii.me.gg.sites.Permission;
import shiina.content.API;
import shiina.content.LeaderboardContent;
import shiina.main.Site;
import spark.Request;
import spark.Response;
import spark.Route;

public class ScoreLeaderboard extends Route {

	public Map<String, Object> m = new HashMap<String, Object>();

	public String mode = "osu";

	public ScoreLeaderboard(String path, String mode) {
		super(path);
		this.mode = mode;
	}

	@Override
	public Object handle(Request request, Response response) {

		int page = 1;
		if (request.queryParams("page") != null) {
			page = Integer.parseInt(request.queryParams("page"));
		}
		int nextpage = page + 1;

		m.put("titlebar", "Leaderboard");
		Permission.hasPermissions(request, m);

		ArrayList<LeaderboardContent> neww = new ArrayList<LeaderboardContent>();
		JSONObject jsonObject = new JSONObject(API.request("scoreleaderboard?p=" + page + "&mode=" + mode));
		JSONArray arr = (JSONArray) jsonObject.get("users");

		for (int i = 0; i < arr.length(); i++) {

			JSONObject jsonObject2 = arr.getJSONObject(i);
			int rank;
			if (page == 1) {
				rank = i + 1;
			} else {
				rank = i + page * 50 + 1 - 50;
			}
			int playcount = jsonObject2.getJSONObject("chosen_mode").getInt("playcount");
			int acc = jsonObject2.getJSONObject("chosen_mode").getInt("accuracy");
			int pp = jsonObject2.getJSONObject("chosen_mode").getInt("ranked_score");
			String country = jsonObject2.getString("country");
			int id = jsonObject2.getInt("id");
			String username = jsonObject2.getString("username");

			LeaderboardContent n = new LeaderboardContent();
			n.setacc(acc);
			n.setCountry(country);
			n.setID(id + "");
			n.setname(username);
			n.setplaycount(playcount);
			n.setpp(pp);
			n.setrank(rank);
			neww.add(n);
		}

		m.put("leaderboard", neww);
		m.put("which", "Score");
		m.put("url", "score");

		m.put("mode", mode);
		if (mode == "0") {
			m.put("fmode", "osu");
		} else if (mode == "1") {
			m.put("fmode", "taiko");
		} else if (mode == "2") {
			m.put("fmode", "fruits");
		} else {
			m.put("fmode", "mania");
		}
		m.put("page", page);
		m.put("fixed", "false");
		m.put("size", neww.size());
		m.put("nextpage", nextpage);
		m.put("lastpage", page - 1);

		try {
			Template template = Site.cfg.getTemplate("leaderboard.html");
			Writer out = new StringWriter();
			template.process(m, out);
			String outt = out.toString();
			return outt;
		} catch (IOException | TemplateException e) {
			throw new RuntimeException(e);
		}
	}

}
