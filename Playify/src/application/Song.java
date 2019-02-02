package application;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Song {
	@SerializedName("name")
	@Expose
	private String songName;
	@SerializedName("terms")
	@Expose
	private String songType;
	
	public Song(String name, String type) {
		this.songName = name;
		this.songType = type;
	}

	public void setSongName(String sName) {
		this.songName = sName;
	}

	public String getSongName() {
		return this.songName;
	}

	public void setSongType(String sType) {
		this.songType = sType;
	}

	public String getSongType() {
		return this.songType;
	}
	
	public String toString() {
		return this.getSongName();
	}

}