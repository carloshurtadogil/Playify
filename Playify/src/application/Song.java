package application;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Song {
	@SerializedName("release")
	@Expose
	ReleaseDetails release;
	@SerializedName("artist")
	@Expose
	ArtistDetails artist;
	@SerializedName("song")
	@Expose
	SongDetails song;
	
	public class ReleaseDetails{
		int id;
		String name;
	}
	
	public class ArtistDetails{
		float terms_freq; 
	    String terms;
	    String name; 
	    float familiarity; 
	    float longitude; 
	    String id; 
	    String location; 
	    float latitude; 
	    String similar; 
	    float hotttnesss;
	}
	
	public class SongDetails{
		float key;
	    float mode_confidence; 
	    float artist_mbtags_count; 
	    float key_confidence; 
	    float tatums_start; 
	    int year; 
	    float duration; 
	    float hotttnesss; 
	    float beats_start; 
	    float time_signature_confidence; 
	    String title; 
	    float bars_confidence; 
	    String id; 
	    float bars_start; 
	    String artist_mbtags; 
	    float start_of_fade_out; 
	    float tempo; 
	    float end_of_fade_in; 
	    float beats_confidence; 
	    float tatums_confidence; 
	    int mode; 
	    float time_signature; 
	    float loudness;
	}

}
