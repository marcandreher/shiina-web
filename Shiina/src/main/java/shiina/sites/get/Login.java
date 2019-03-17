package shiina.sites.get;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import kazukii.me.gg.configs.u;
import kazukii.me.gg.sites.Permission;
import shiina.main.Site;
import spark.Request;
import spark.Response;
import spark.Route;

public class Login extends Route {

	public Map<String, Object> m = new HashMap<String, Object>();

	public Login(String path) {
		super(path);
	}

	@Override
	public Object handle(Request request, Response response) {
		m.put("titlebar", "Login");
		
		Permission.hasPermissions(request, m, response);
		
		u.s.println(Site.requestAndResponseInfoToString(request, response));
		m.put("fixed", "true");
		try {
			Template template = Site.cfg.getTemplate("login.html");
			Writer out = new StringWriter();
			template.process(m, out);
			String outt = out.toString();
			return outt;
		} catch (IOException | TemplateException e) {
			throw new RuntimeException(e);
		}
	}

}
