package fi.smaa.jsmaa;


public class Version {
	
	public Version(String version) {
		this.version = version;
	}
	
	public Version() {
		version = "";
	}
	
	private String version;
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String ver) {
		this.version = ver;
	}

}
