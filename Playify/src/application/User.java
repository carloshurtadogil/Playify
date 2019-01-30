package application;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class User{
	
	@SerializedName("username")
	@Expose
	private String username;
	
	@SerializedName("password")
	@Expose
	private String password;
	@SerializedName("playlists")
	@Expose
	private List<Playlist> playlists;

	public User(String user, String pass) {
		this.username = user;
		this.password = pass;
		this.playlists = new ArrayList<Playlist>();
	}
	public String getUsername()
	{
		return username;
	}

	public String getPassword() {
		return password;
	}

	public List<Playlist> getPlaylists(){
		return this.playlists;
	}

	public void setUsername(String uname) {
		this.username = uname;
	}

	public void setPassword(String pass) {
		this.password = pass;
	}

	public void setPlaylists(List<Playlist> thePlaylists){
		this.playlists = thePlaylists;
	}
}