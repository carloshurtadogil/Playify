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
	@SerializedName("confirmPassword")
	@Expose
	private String confirmPassword;
	@SerializedName("song")
	@Expose
	private String song;
	@SerializedName("fragment")
	@Expose
	private String fragment;
	@SerializedName("songID")
	@Expose
	private String songID;
	@SerializedName("playlist")
	@Expose
	private String playlist;
	@SerializedName("searchInput")
	@Expose
	private String searchInput;

	public String getSongID() {
		return songID;
	}
	
	public void setSongID(String songID) {
		this.songID = songID;
	}
	
	public String getPlaylist() {
		return playlist;
	}
	
	public void setPlaylist(String playlist) {
		this.playlist = playlist;
	}
	
	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	public String getSearchInput() {
		return searchInput;
	}
	
	public void setSearchInput(String searchInput) {
		this.searchInput = searchInput;
	}
	
	
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