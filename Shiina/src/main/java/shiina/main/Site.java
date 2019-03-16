package shiina.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;
import org.json.simple.parser.ParseException;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import kazukii.me.gg.configs.Config;
import kazukii.me.gg.configs.u;
import shiina.content.mysql;
import shiina.sites.get.ClanLeaderboard;
import shiina.sites.get.Download;
import shiina.sites.get.Error;
import shiina.sites.get.Friend;
import shiina.sites.get.Friends;
import shiina.sites.get.Home;
import shiina.sites.get.Leaderboard;
import shiina.sites.get.Login;
import shiina.sites.get.Logout;
import shiina.sites.get.Profile;
import shiina.sites.get.ScoreLeaderboard;
import shiina.sites.get.Settings;
import shiina.sites.get.Unfriend;
import shiina.sites.post.Extras;
import shiina.sites.post.Userpage;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class Site {
	
	public static Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);
	
	public static mysql msql = null;
	
	public static ArrayList<Route> postroutes = new ArrayList<Route>();
	public static ArrayList<Route> getroutes = new ArrayList<Route>();
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		Config.createConfig();
		Config.loadConfig();
		msql = new mysql(Config.getString("mysqlusername"), Config.getString("mysqlpassword"), Config.getString("mysqldatabase"), Config.getString("mysqlip"), Integer.parseInt(Config.getString("mysqlport") + ""));
		
		Spark.setPort(Integer.parseInt(Config.getString("sparkport")));

		String log4jConfPath ="log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		cfg.setWrapUncheckedExceptions(true);
		
		Spark.externalStaticFileLocation("static/");
		
		File templates = new File("templates/");
		File staticfolder = new File("static/");
		
		final File[] staticfiles = staticfolder.listFiles();
		
		if(!templates.exists())templates.mkdirs();
		if(!staticfolder.exists())staticfolder.mkdirs();
		
		cfg.setDirectoryForTemplateLoading(templates);
		
		
		getroutes.add(new Home("/"));
		getroutes.add(new Home("/home"));
		getroutes.add(new Friends("/home/friends"));
		getroutes.add(new Settings("/home/account/edit"));
		getroutes.add(new Login("/login"));
		getroutes.add(new Login("/register"));
		getroutes.add(new Logout("/logout"));
		getroutes.add(new Unfriend("/unfriend"));
		getroutes.add(new Friend("/friend"));
		getroutes.add(new Download("/home/download"));
		getroutes.add(new Download("/download"));
		
		getroutes.add(new Profile("/users/:id/:mode"));
		getroutes.add(new Profile("/users/:id"));
		
		getroutes.add(new Leaderboard("/rankings/osu/performance", "0"));
		getroutes.add(new Leaderboard("/rankings/fruits/performance", "2"));
		getroutes.add(new Leaderboard("/rankings/taiko/performance", "1"));
		getroutes.add(new Leaderboard("/rankings/mania/performance", "3"));
		
		getroutes.add(new ScoreLeaderboard("/rankings/osu/score", "0"));
		getroutes.add(new ScoreLeaderboard("/rankings/fruits/score", "2"));
		getroutes.add(new ScoreLeaderboard("/rankings/taiko/score", "1"));
		getroutes.add(new ScoreLeaderboard("/rankings/mania/score", "3"));
		
		getroutes.add(new ClanLeaderboard("/rankings/osu/clans", "00000000000000000000"));
		getroutes.add(new ClanLeaderboard("/rankings/fruits/clans", "222222222222222222222"));
		getroutes.add(new ClanLeaderboard("/rankings/taiko/clans", "111111111111111111111"));
		getroutes.add(new ClanLeaderboard("/rankings/mania/clans", "333333333333333333333"));
		
		postroutes.add(new shiina.sites.post.Login("/session"));
		postroutes.add(new Extras("/submit_extras"));
		postroutes.add(new Userpage("/userpage"));
		
		//HANDLE TEMPLATES
		
		for(Route r : getroutes) {
			u.s.println("Registered Route " + r.toString());
			Spark.get(r);
		}
		
		for(Route r : postroutes) {
			u.s.println("Registered Route " + r.toString());
			Spark.post(r);
		}
		
		Spark.get(new Route("*") {
			
			@Override
			public Object handle(Request request, Response response) {
				Boolean s = false;
				for(File f : staticfiles) {
					if(request.pathInfo().startsWith("/"+f.getName())) {
						s = true;
					}
				}
				
				if(s == true) {
					return null;
				}else {
					return new Error().generateError(request, "404", "Not found");
				}
			}
		});
		
		
		
	}
	
	public static String requestAndResponseInfoToString(Request request, Response response) {
	    StringBuilder sb = new StringBuilder();
	    sb.append(request.requestMethod());
	    sb.append(" " + request.url());
	    sb.append(" " + request.body());
	    HttpServletResponse raw = response.raw();
	    sb.append(" Reponse: " + raw.getStatus());
	    sb.append(" " + raw.getHeader("content-type"));
	    try {
	        sb.append(" body size in bytes: " + response.body().getBytes(raw.getCharacterEncoding()).length);
	    } catch (Exception e) {
	        
	    }
	    return sb.toString();
	}
	
	
}
