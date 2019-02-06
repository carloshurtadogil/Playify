package application;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Playlist {

	@SerializedName("songs")
	@Expose
	private List<Song> songs;
	
	@SerializedName("playlistname")
	@Expose
	private String playlistname;
	
	public void setSongs(List<Song> theSongs) {
		this.songs = theSongs;
	}

	public List<Song> getSongs() {
		return this.songs;
	}
	
	public String getPlaylistName () {
		return this.playlistname;
	}
	
	public void setPlaylistName (String name) {
		this.playlistname = name;
	}
	
	public void printPlaylistDetails() {
		System.out.println("Playlist Name: " + playlistname);
		if(songs.size() > 0) {
			for (Song s : songs) {
				System.out.println("	Song Name: " + s.getTitle());
			}
		}
	}
	
	public String toString() {
		return this.getPlaylistName();
	}

}