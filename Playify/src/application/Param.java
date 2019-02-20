package application;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Param {

	@SerializedName("user")
	@Expose
	private String user;
	@SerializedName("password")
	@Expose
	private String password;
	@SerializedName("song")
	@Expose
	private String song;
	@SerializedName("fragment")
	@Expose
	private String fragment;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSong() {
		return song;
	}

	public void setSong(String song) {
		this.song = song;
	}

	public String getFragment() {
		return fragment;
	}

	public void setFragment(String fragment) {
		this.fragment = fragment;
	}

}