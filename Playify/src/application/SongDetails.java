package application;

import com.google.gson.annotations.SerializedName;

public class SongDetails {
	
	@SerializedName("release")
	private Release release;
	@SerializedName("artist")
	private Artist artist;
	@SerializedName("song")
	private Song song;
	
	/****GETTERS****/
	
	public Release getRelease() {
		return release;
	}
	
	public Artist getArtist() {
		return artist;
	}
	
	public Song getSong() {
		return song;
	}
	
	/****SETTERS****/
	
	public void setRelease(Release release) {
		this.release = release;
	}
	
	public void setArtist(Artist artist) {
		this.artist = artist;
	}
	
	public void setSong(Song song) {
		this.song = song;
	}
	
	
}
