package shiina.sites.get;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import kazukii.me.gg.sites.Permission;
import shiina.content.Beatmap;
import shiina.content.mysql;
import shiina.main.Site;
import spark.Request;
import spark.Response;
import spark.Route;

public class Home extends Route {

	public Map<String, Object> m = new HashMap<String, Object>();

	public Home(String path) {
		super(path);
	}

	@Override
	public Object handle(Request request, Response response) {
		m.put("titlebar", "Home");
		m.put("fixed", "false");
		
		Permission.hasPermissions(request, m);
		
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

		ArrayList<Beatmap> rdmbeatmaps = new ArrayList<Beatmap>();
		try {
			ResultSet rdmbeatmaps_rs = mysql.Query("SELECT * FROM beatmaps ORDER BY RAND() LIMIT 6");
			while (rdmbeatmaps_rs.next()) {
				Beatmap n = new Beatmap();
				
				String title = rdmbeatmaps_rs.getString(5);
				String[] separated = title.split("\\-");
				n.setID(rdmbeatmaps_rs.getString("beatmap_id"));
				n.setsetID(rdmbeatmaps_rs.getString("beatmapset_id"));
				try {
					n.setName(title.replaceAll(separated[0], "").replaceAll("-", ""));
				}catch(PatternSyntaxException e) {
					continue;
				}
				
				
				n.setArtist(separated[0]);
				rdmbeatmaps.add(n);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		m.put("latest_beatmaps", rdmbeatmaps);

		try {
			Template template = Site.cfg.getTemplate("home.html");
			Writer out = new StringWriter();
			template.process(m, out);
			String outt = out.toString();
			return outt;
		} catch (IOException | TemplateException e) {
			throw new RuntimeException(e);
		}
	}

}
