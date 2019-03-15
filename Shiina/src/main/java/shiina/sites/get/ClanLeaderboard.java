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

public class ClanLeaderboard extends Route {

	public Map<String, Object> m = new HashMap<String, Object>();

	public String mode = "osu";

	public ClanLeaderboard(String path, String mode) {
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
		
		JSONObject jsonObject = new JSONObject(API.request("clans/stats/all?m=" + mode));
		JSONArray arr = (JSONArray) jsonObject.get("clans");
		int loop = 0;
		for (int i = 0; i < arr.length(); i++) {
			loop++;
			if (page >= 2) {
				if (loop <= page * 50 - 50) {
					continue;
				}
			} else {
				if (loop > 50) {
					continue;
				}

			}

			JSONObject jsonObject2 = arr.getJSONObject(i);
			int playcount = jsonObject2.getJSONObject("chosen_mode").getInt("playcount");
			int pp = jsonObject2.getJSONObject("chosen_mode").getInt("pp");
			int id = jsonObject2.getInt("id");
			String username = jsonObject2.getString("name");

			LeaderboardContent n = new LeaderboardContent();
			n.setacc(0);
			n.setCountry("");
			n.setID(id + "");
			n.setname(username);
			n.setplaycount(playcount);
			n.setpp(pp);
			n.setrank(i + 1);

			neww.add(n);
		}

		m.put("leaderboard", neww);
		m.put("which", "Clan");
		m.put("url", "clans");

		if (mode == "00000000000000000000") {
			m.put("fmode", "osu");
			m.put("mode", "0");
		} else if (mode == "111111111111111111111") {
			m.put("mode", "1");
			m.put("fmode", "taiko");
		} else if (mode == "222222222222222222222") {
			m.put("fmode", "fruits");
			m.put("mode", "2");
		} else {
			m.put("fmode", "mania");
			m.put("mode", "3");
		}

		m.put("size", neww.size());

		m.put("page", page);
		m.put("fixed", "false");
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
