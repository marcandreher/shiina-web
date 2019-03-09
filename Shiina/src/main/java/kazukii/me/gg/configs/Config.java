package kazukii.me.gg.configs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Config {

	
	public static String createJSON(HashMap<String, Object> map) {
		
		String end = "{\n";
		int i = 0;
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			i++;
			if(i == map.size()) {
				end = end + "	\""+ entry.getKey() + "\": \"" + entry.getValue() + "\"\n";
			}else {
				end = end + "	\""+ entry.getKey() + "\": \"" + entry.getValue() + "\",\n";
			}
			
		    
		}
		end = end +"}";
		
		return end;
	}
	
	//OLD CONFIG API
	
	public static Object config = null;
	public static JSONObject objconfig = null;
	
	public static void loadConfig() throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
    	Object obj = null;
		obj = parser.parse(new FileReader("config.json"));
		config = obj;
        JSONObject jsonObject = (JSONObject) obj;
        objconfig = jsonObject;
        System.out.println(u.info +"Successfully loaded Config.json");
	}
	
	public static void createConfig()  {
		File f = new File("config.json");
		if(f.exists()) {
			System.out.println(u.info +"Found Config! No one must be created");
		}else{
			System.out.println(u.error +"No Config Found! Create one...");
			HashMap<String, Object> obj = new HashMap<>();
			
			obj.put("sparkport", "88");
			obj.put("mysqlip", "localhost");
			obj.put("mysqlport", "3306");
			obj.put("mysqldatabase", "giveway");
			obj.put("mysqlusername", "root");
			obj.put("mysqlpassword", "");
			obj.put("discordtoken", "");
			obj.put("debug", "false");
			
			try (FileWriter file = new FileWriter("config.json")) {
				file.write(createJSON(obj));
				System.out.println(u.info +"Successfully created Config...");
				
			} catch (IOException e) {
				System.err.println(u.error +"Can't create Config...");
				e.printStackTrace();
			}
			System.exit(0);
		}
		
    
	}
	
	public static String getString(String string) {
			String result = (String) objconfig.get(string);
			return result;
	}
	
	public static Boolean getBool(String string) {
			Boolean result = (Boolean) objconfig.get(string);
			return result;
	}
	
	public static long getLong(String string) {
			long result = (Long) objconfig.get(string);
			return result;
	}
}
