package shiina.content;

public class Beatmap {

    private String Image;
    private String Name;
    private String Artist;
    private String id;
    private String set_id;
    private int pp;
    private double acc;
    private int Playcount;
    
    private String Timestamp;
    
    public String getTimestamp() {
    	return Timestamp;
    }
    
    public String getID() {
    	return id;
    }
    public String getsetid() {
    	return set_id;
    }
    
    public void setID(String id) {
    	this.id = id;
    }
    
    public void setsetID(String setid) {
    	this.set_id = setid;
    }
    
    
    public void setTimestamp(String time) {
    	Timestamp = time;
    }
    
    public String getImage() {
        return Image;
    }
    
    public void setPlaycount(int playcount) {
    	Playcount = playcount;
    }
    
    public void setPP(int ppp) {
    	pp = ppp;
    }
    
    public void setAcc(double accc) {
    	acc = accc;
    }
    
    public int getPlaycount() {
        return Playcount;
    }

    public void setImage(String imgurl) {
    	Image = imgurl;
    }
    
    

    public String getName() {
        return Name;
    }
    
    public double getacc() {
        return acc;
    }
    
    public int getpp() {
        return pp;
    }

    public void setName(String name) {
    	Name = name;
    }
    
    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
    	Artist = artist;
    }
    

}