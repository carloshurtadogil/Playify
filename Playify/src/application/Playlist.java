package application;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Playlist {

	@SerializedName("songs")
	@Expose
	private List<Song> songs;

	public void setSongs(List<Song> theSongs) {
		this.songs = theSongs;
	}

	public List<Song> getSongs() {
		return this.songs;
	}

}