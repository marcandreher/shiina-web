package shiina.content;

public class Medal {
	
	public String name;
	public String description;
	public String icon;
	
	public String getName() { return name; }
	public String getDescription() {return description;}
	public String getIcon() {return icon;}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String des) {
		description = des;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
}
