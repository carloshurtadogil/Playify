package application;


public class Release {
	//The id associated with the release
	private int id;
	//The name of the release
	private String name;
	
	/*****GETTERS*****/
	
	/**
	 * Return the id associated with the release
	 * @return The release's ID
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Return the name associated with the release
	 * @return The release's name
	 */
	public String getName() {
		return name;
	}
	
	/*****SETTERS*****/
	
	/**
	 * Set the id of the release
	 * @param id The ID of the release
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * Set the name of the release
	 * @param name The release's name
	 */
	public void setName(String name) {
		this.name = name;
	}

	
}
