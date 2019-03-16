package application.Models;

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
	//sets the songs
	public void setSongs(List<Song> theSongs) {
		this.songs = theSongs;
	}
	//gets the songs
	public List<Song> getSongs() {
		return this.songs;
	}
	//adds song to the playlist
	public void addSong(Song s) {
		if(songs == null) {
			songs = new ArrayList<Song>();
		}
		songs.add(s);
	}
	//gets the song names
	public List<String> getSongNames() {
		List<String> names = new ArrayList<String>();
		for(Song s: songs)
		{
			names.add(s.getSongDetails().getTitle());
		}
		return names;
	}
	//get the playlist name
	public String getPlaylistName () {
		return this.playlistname;
	}
	//sets the playlist name
	public void setPlaylistName (String name) {
		this.playlistname = name;
	}
	//searches though the playlist and gets the song
	public Song getSong(String name) {
		for(int i = 0; i < songs.size(); i++) {
			if(songs.get(i).getSongDetails().getTitle().equals(name)) {
				return songs.get(i);
			}
		}
		return null;
	}
	//to string method that gets the playlist name
	public String toString() {
		return this.getPlaylistName();
	}

}