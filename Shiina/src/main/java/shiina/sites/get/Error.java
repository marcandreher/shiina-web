package shiina.sites.get;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import kazukii.me.gg.sites.Permission;
import shiina.main.Site;
import spark.Request;

public class Error {
	public Map<String, Object> m = new HashMap<String, Object>();
	public String generateError(Request request, String lang, String error) {
		
		m.put("loggedin", "true");
		m.put("titlebar", "Error");
		m.put("fixed", "true");
		Permission.hasPermissions(request, m);
		
		m.put("error", error);
		m.put("lang", lang);
		
		try {
			Template template = Site.cfg.getTemplate("error.html");
			Writer out = new StringWriter();
			template.process(m, out);
			String outt = out.toString();
			return outt;
		} catch (IOException | TemplateException e) {
			throw new RuntimeException(e);
		}
	}

}
