package application;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Playlist {

	@SerializedName("songs")
	@Expose
	List<Song> songs;
	
	@SerializedName("playlistname")
	@Expose
	private String playlistname;
	
	public void setSongs(List<Song> theSongs) {
		this.songs = theSongs;
	}

	public List<Song> getSongs() {
		return this.songs;
	}
	
	public void addSong(Song s) {
		if(songs == null) {
			songs = new ArrayList<Song>();
		}
		songs.add(s);
	}
	
	
	public String getPlaylistName () {
		return this.playlistname;
	}
	
	public void setPlaylistName (String name) {
		this.playlistname = name;
	}
	
	
	public String toString() {
		return this.getPlaylistName();
	}

}