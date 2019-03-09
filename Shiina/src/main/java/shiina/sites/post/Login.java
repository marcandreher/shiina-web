package shiina.sites.post;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;
import shiina.content.RandomString;
import shiina.content.mysql;
import shiina.sites.get.Error;
import spark.Request;
import spark.Response;
import spark.Route;

public class Login extends Route {

	public Login(String path) {
		super(path);
	}

	private MessageDigest digester;

	{
		try {
			digester = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object handle(Request request, Response response) {
		String username = request.queryParams("username");
		String password = request.queryParams("password");

		String tokensql = "";
		if (username.contains("@")) {
			tokensql = "SELECT * FROM `users` WHERE `email` = ?";
		} else {
			tokensql = "SELECT * FROM `users` WHERE `username` = ?";
		}

		String md5_password = "";
		String id = "";
		try {
			ResultSet rs = mysql.Query(tokensql, username);
			while (rs.next()) {
				id = rs.getString("id");
				md5_password = rs.getString("password_md5");
			}
		} catch (Exception e) {
			return new Error().generateError(request, "java", e.getMessage().toString());
		}

		String en_form_password = crypt(password);
		Boolean resultaftere = false;
		try {
			Result result = BCrypt.verifyer().verify(en_form_password.toCharArray(), md5_password);
			resultaftere = result.verified;
		} catch (Exception e) {
			resultaftere = false;
		}

		if (resultaftere) {
			int privileges = 0;
			String description = request.ip();
			int privatesql = 1;
			int last_updated = 0;
			try {
				ResultSet rs = mysql.Query("SELECT * FROM `users` WHERE `id` = ?", id);
				while (rs.next()) {
					privileges = rs.getInt("privileges");
					last_updated = rs.getInt("latest_activity");
				}
			} catch (Exception e) {
				return new Error().generateError(request, "java", e.getMessage().toString());
			}

			String rdmtoken = new RandomString(32).nextString();
			try {
				mysql.Exec("INSERT INTO `tokens`(`user`, `privileges`, `description`, `token`, `private`, `last_updated`) VALUES (?,?,?,?,?,?)", id, privileges+"",description+"",rdmtoken+"",privatesql +"",last_updated + " ");
			} catch (Exception e) {
				return new Error().generateError(request, "java", e.getMessage().toString());
			}
			
			response.cookie("token", rdmtoken);
			
			response.redirect("/home?l=true");
		} else {
			response.redirect("/login?w=true");
		}
		return null;

	}

	public String crypt(String str) {
		if (str == null || str.length() == 0) {
			throw new IllegalArgumentException("String to encript cannot be null or zero length");
		}

		digester.update(str.getBytes());
		byte[] hash = digester.digest();
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			if ((0xff & hash[i]) < 0x10) {
				hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
			} else {
				hexString.append(Integer.toHexString(0xFF & hash[i]));
			}
		}
		return hexString.toString();
	}

}
