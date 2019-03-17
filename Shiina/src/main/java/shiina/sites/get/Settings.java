package shiina.sites.get;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import kazukii.me.gg.sites.Permission;
import shiina.content.mysql;
import shiina.main.Site;
import spark.Request;
import spark.Response;
import spark.Route;

public class Settings extends Route {

	public Map<String, Object> m = new HashMap<String, Object>();

	public Settings(String path) {
		super(path);
	}

	@Override
	public Object handle(Request request, Response response) {
		m.put("titlebar", "Settings");
		m.put("fixed", "false");

		if (!Permission.hasPermissions(request, m, response)) {
			response.redirect("/home");
			return null;
		}
		Permission.applySettings(m, m.get("userid").toString());

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
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Template template = Site.cfg.getTemplate("settings.html");
			Writer out = new StringWriter();
			template.process(m, out);
			String outt = out.toString();
			return outt;
		} catch (IOException | TemplateException e) {
			throw new RuntimeException(e);
		}
	}

}
