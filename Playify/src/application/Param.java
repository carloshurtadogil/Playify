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

	//gets the song id
	public String getSongID() {
		return songID;
	}
	//sets the song id
	public void setSongID(String songID) {
		this.songID = songID;
	}
	//gets the playlist
	public String getPlaylist() {
		return playlist;
	}
	//sets the playlist
	public void setPlaylist(String playlist) {
		this.playlist = playlist;
	}
	//gets the confirmed password
	public String getConfirmPassword() {
		return confirmPassword;
	}
	//sets the confirmed password
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	//gets the search input
	public String getSearchInput() {
		return searchInput;
	}
	//sets the search input
	public void setSearchInput(String searchInput) {
		this.searchInput = searchInput;
	}
	
	//gets the user
	public String getUser() {
		return user;
	}
    //sets the user
	public void setUser(String user) {
		this.user = user;
	}
	//gets the password
	public String getPassword() {
		return password;
	}
	//sets the password
	public void setPassword(String password) {
		this.password = password;
	}
	//gets the song
	public String getSong() {
		return song;
	}
	//sets the song
	public void setSong(String song) {
		this.song = song;
	}
	//gets the fragment
	public String getFragment() {
		return fragment;
	}
	//sets the fragment
	public void setFragment(String fragment) {
		this.fragment = fragment;
	}

}