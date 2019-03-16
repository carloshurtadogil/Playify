package application.Models;

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
	//gets the term frequency
	public double getTermsFreq() {
		return terms_freq;
	}
	//gets the terms
	public String getTerms() {
		return terms;
	}
	//gets the name
	public String getName() {
		return name;
	}
	//gets the familiarity 
	public double getFamiliarity() {
		return familiarity;
	}
	//gets the longitude
	public double getLongitude() {
		return longitude;
	}
	//gets the ID
	public String getID() {
		return id;
	}
	//gets the location
	public String getLocation() {
		return location;
	}
	//gets the latitude
	public double getLatitude() {
		return latitude;
	}
	//gets the similar
	public String getSimilar() {
		return similar;
	}
	//gets the hottnesss
	public double getHotttnesss() {
		return hotttnesss;
	}
	
	/*****SETTERS*****/
	
	//sets the term frequency
	public void setTermsFreq(double tf) {
		this.terms_freq = tf;
	}
	//sets the terms
	public void setTerms(String terms) {
		this.terms = terms;
	}
	//sets the name
	public void setName(String name) {
		this.name = name;
	}
	//sets the familiarity
	public void setFamiliarity(double familiarity) {
		this.familiarity = familiarity;
	}
	//sets the longitude
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	//sets the id
	public void setID(String id) {
		this.id = id;
	}
	//sets the location
	public void setLocation(String location) {
		this.location = location;
	}
	//sets the latitude
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	//sets the similar
	public void setSimilar(String similar) {
		this.similar = similar;
	}
	//sets the hottness
	public void setHotttnesss(double hotttnesss) {
		this.hotttnesss = hotttnesss;
	}
	
}
