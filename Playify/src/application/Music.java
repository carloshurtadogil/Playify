package application;

import java.util.*;

import com.google.gson.annotations.SerializedName;

public class Music {
	@SerializedName("songs")
	private List<SongDetails> songDetails;
	
	public List<SongDetails> getSongDetails() {
		return songDetails;
	}
	
	public void setSongDetails(List<SongDetails> songDetails) {
		this.songDetails = songDetails;
	}
	
	public void printSongDetails() {
		for(SongDetails sd: songDetails ) {
			System.out.println(sd.getSong().getTitle());
		}
	}
}
