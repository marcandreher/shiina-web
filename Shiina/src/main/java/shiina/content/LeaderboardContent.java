package shiina.content;

public class LeaderboardContent {

    private String name;
    private String UserID;
    private String Country;
    private String rank;
    
    private String acc;
    private String playcount;
    private String pp;
    
    public String getname() {
    	return name;
    }
    
    public String getrank() {
    	return rank;
    }

    
    public void setrank(int rank) {
    	this.rank = "" + rank;
    }
    
    public String getpp() {
    	return pp;
    }

    
    public void setpp(int pp) {
    	this.pp = "" + pp;
    }
    
    public String getacc() {
    	return acc;
    }

    
    public void setacc(int acc) {
    	this.acc = "" + acc;
    }
    
    public String getplaycount() {
    	return playcount;
    }

    
    public void setplaycount(int playcount) {
    	this.playcount = "" + playcount;
    }
     
    public String getid() {
    	return UserID;
    }
    public String getcountry() {
    	return Country;
    }
    
    
    public void setCountry(String country) {
    	Country = country;
    }
    
    public void setID(String id) {
    	UserID = id;
    }
    public void setname(String name) {
    	this.name = name;
    }
    
    
    

}