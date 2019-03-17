package shiina.content;

public class Friend {

    private String name;
    private String UserID;
    private String Country;
    private Boolean Mutalstatus = false;
    private Boolean online = false;
    private String pp;
    
    public String getname() {
    	return name;
    }
    
    public String getpp() {
    	return pp;
    }
    
    public String getid() {
    	return UserID;
    }
    
    public Boolean getonline() {
    	return online;
    }
    
    public void setonline(Boolean on) {
    	online = on;
    }
    
    public String getcountry() {
    	return Country;
    }
    
    public Boolean getstatus() {
    	return Mutalstatus;
    }
    
    public void setPP(String pp) {
    	this.pp = pp;
    }
    
    public void setCountry(String country) {
    	Country = country;
    }
    
    public void setStatus(Boolean status) {
    	Mutalstatus = status;
    }
    public void setID(String id) {
    	UserID = id;
    }
    public void setname(String name) {
    	this.name = name;
    }
    
    
    

}