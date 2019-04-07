package application.Models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
	
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
	
	/**
	 * A overloaded constructor that intializes a username, password, and list of playlists
	 * @param user
	 * @param pass
	 * @param playlists
	 */
	public User(String user, String pass, List<Playlist> playlists) {
		this.username = user;
		this.password = pass;
		this.playlists = playlists; 
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
	
	/**
	 * Retrieves a playlist based off the playlist name
	 * @param name
	 * @return
	 */
	public Playlist getSpecificPlaylist(String name) {
		Playlist found = null;
		for(int i = 0; i < playlists.size(); i++) {
			if(playlists.get(i).getPlaylistName().equals(name)) {
				found = playlists.get(i);
				break;
			}
		}
		
		return found;
	}
	
	/**
	 * <p> Removes a playlist from a user's list of playlists </p>
	 * @return True if successful in removing playlist, false otherwise
	 */
	public boolean removePlaylist(String playlistName) {
		for(int i=0;i<playlists.size();i++) {
			Playlist currentPlaylist = playlists.get(i);
			if(currentPlaylist.getPlaylistName().equals(playlistName)) {
				playlists.remove(i);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * <p> Removes a song from one of the user's playlists </p>
	 * @param playlistName Playlist with song to be removed
	 * @param songID id of the song to be removed
	 * @return True if successful in removing the song, false otherwise
	 */
	public boolean removeSong(String playlistName, String songID) {
		int playlistIndex = 0;
		for(Playlist p : playlists) {
			if(p.getPlaylistName().equals(playlistName)) {
				List<Song> songsinp = p.getSongs();
				for(int i = 0; i < songsinp.size(); i++ ) {
					if(p.getSongs().get(i).getSongDetails().getSongId().equals(songID)) {
						p.getSongs().remove(i);
						p.setSongs(p.getSongs());
						this.getPlaylists().set(playlistIndex, p);
						System.out.println("User Song Found");
						return true;
					}
				}
			}
			playlistIndex++;
		}
		return false;
	}
	
	/**
	 * <p> Retrieve the list of playlistnames that is found in the user's list of playlists</p>
	 * @return List of playlistsnames
	 */
	public List<String> getPlaylistNames() {
		List<String> names = new ArrayList<String>();
		for(Playlist p: this.playlists) {
			names.add(p.getPlaylistName());
		}
		
		return names;
	}

	/**
	 * <p>Set the username that the user has selected</p>
	 * @param uname The user's new username
	 */
	public void setUsername(String uname) {
		this.username = uname;
	}

	/**
	 * <p>Set the users password to a new one once changed/added</p>
	 * @param pass The password selected by the user
	 */
	public void setPassword(String pass) {
		this.password = pass;
	}

	/**
	 * <p>For initialization, set the users playlists</p>
	 * @param thePlaylists Playlist list retrieved from the file
	 */
	public void setPlaylists(List<Playlist> thePlaylists){
		this.playlists = thePlaylists;
	}
	
	/**
	 * <p>Add a playlist to the users current list of playlists </p>
	 * @param newPlaylist New playlist to be added
	 */
	public void addPlaylist(Playlist newPlaylist) {
		this.playlists.add(newPlaylist);
		setPlaylists(this.playlists);
	}
	
	/**
	 * <p>Retrieve the String representation of the current class</p>
	 * @return The String representation of the current class
	 */
	@Override
	public String toString() {
		List<String> playlistsnames = this.getPlaylistNames();
		String result = 
		"User Name: " + username + "\n" + 
		"Password: " + password + "\n" + 
		"Names: {\n";
		for(String name : playlistsnames) {
			result += (name + "\n");
		}
		result += ("}\n");
		return result;
	}
}