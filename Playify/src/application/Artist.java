package application;

public class Artist {
	
	private double terms_freq;
	private String terms;
	private String name;
	private double familiarity;
	private double longitude;
	private String id;
	private String location;
	private double latitude;
	private String similar;
	private double hotttnesss;
	
	
	/*****GETTERS*****/
	
	public double getTermsFreq() {
		return terms_freq;
	}
	
	public String getTerms() {
		return terms;
	}
	
	public String getName() {
		return name;
	}
	
	public double getFamiliarity() {
		return familiarity;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public String getID() {
		return id;
	}
	
	public String getLocation() {
		return location;
	}
	
	public double getLatitude() {
		return latitude;
	}
	public String getSimilar() {
		return similar;
	}
	
	public double getHotttnesss() {
		return hotttnesss;
	}
	
	/*****SETTERS*****/
	
	public void setTermsFreq(double tf) {
		this.terms_freq = tf;
	}
	
	public void setTerms(String terms) {
		this.terms = terms;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setFamiliarity(double familiarity) {
		this.familiarity = familiarity;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public void setSimilar(String similar) {
		this.similar = similar;
	}
	
	public void setHotttnesss(double hotttnesss) {
		this.hotttnesss = hotttnesss;
	}
	
}
