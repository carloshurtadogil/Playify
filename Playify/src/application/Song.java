package application;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Song {
	@SerializedName("release")
	@Expose
	private ReleaseDetails release;
	@SerializedName("artist")
	@Expose
	private ArtistDetails artist;
	@SerializedName("song")
	@Expose
	private SongDetails song;
	
	
	public ArtistDetails getArtistDetails() {
		return this.artist;
	}
	
	public ReleaseDetails getRelease() {
		return this.release;
	}
	
	public SongDetails getSongDetails() {
		return this.song;
	}
	
	public class ReleaseDetails{
		private int id;
		private String name;
		
		public int getReleaseID() {
			return this.id;
		}
		public void setReleaseID(int id) {
			this.id = id;
		}
		public String getReleaseName() {
			return this.name;
		}
		public void setReleaseName(String name) {
			this.name= name;
		}
	}
	
	public class ArtistDetails{
		private float terms_freq; 
	    private String terms;
	    private String name; 
	    private float familiarity; 
	    private float longitude; 
	    private String id; 
	    private String location; 
	    private float latitude; 
	    private String similar; 
	    private float hotttnesss;
	    
	    public void setTermsFreq(float termsFreq) {
	    	this.terms_freq = termsFreq; 
	    }
	    
	    public float getTermsFreq() {
	    	return this.terms_freq;
	    }
	    
	    public void setTerms(String terms) {
	    	this.terms = terms;
	    }
	    
	    public String getTerms() {
	    	return this.terms;
	    }
	    
	    public void setName(String name) {
	    	this.name = name;
	    }
	    
	    public String getName() {
	    	return this.name;
	    }
	    
	   public void setFamiliarity(float familiarity) {
		   this.familiarity = familiarity;
	   }
	   public float getFamiliarity() {
		   return this.familiarity;
	   }
	   
	   public void setLongitude(float longitude) {
		   this.longitude = longitude;
	   }
	   public float getLongitude() {
		   return this.longitude;
	   }
	   
	   
	   public void setArtistId(String artistId) {
		   this.id = artistId;
	   }
	   public String getArtistId() {
		   return this.id;
	   }
	   
	   public void setLocation(String location) {
		   this.location = location;
	   }
	   public String getLocation() {
		   return this.location;
	   }
	   
	   public void setLatitude(float latitude) {
		   this.latitude = latitude;
	   }
	   public float getLatitude() {
		   return this.latitude;
	   }
			  
	   public void setSimilar(String similar) {
		   this.similar = similar;
	   }
	   public String getSimilar() {
		   return this.similar;
	   }
	   
	   public void setHotness(float hotness) {
		   this.hotttnesss = hotness;
	   }
	   
	   public float getHotness() {
		   return this.hotttnesss;
	   }
	}
	
	public class SongDetails{
		private float key;
		private float mode_confidence; 
		private float artist_mbtags_count; 
		private float key_confidence; 
		private float tatums_start; 
		private int year; 
		private float duration; 
		private float hotttnesss; 
		private float beats_start; 
		private float time_signature_confidence; 
		private String title; 
		private float bars_confidence; 
		private String id; 
		private float bars_start; 
		private String artist_mbtags; 
		private float start_of_fade_out; 
		private float tempo; 
		private float end_of_fade_in; 
		private float beats_confidence; 
		private float tatums_confidence; 
		private int mode; 
		private float time_signature; 
		private float loudness;
		
		public void setKey(float key) {
			this.key = key;
		}
		public float getKey() {
			return this.key;
		}
		
		public void setModeConfidence(float modeConfidence) {
			this.mode_confidence = modeConfidence;
		}
		public float getModeConfidence() {
			return this.mode_confidence;
		}
		public void setArtistMbTagsCount(float tagCount) {
			this.artist_mbtags_count = tagCount;
		}
		public float getArtistMbTagsCount() {
			return this.artist_mbtags_count;
		}
		public void setKeyConfidence(float keyConfidence) {
			this.key_confidence = keyConfidence;
		}
		public float getKeyConfidence() {
			return this.key_confidence;
		}
		public void setTatumsStart(float tatumsStart) {
			this.tatums_start = tatumsStart;
		}
		public float getTatumsStart() {
			return this.tatums_start;
		}
		public void setYear(int year) {
			this.year = year;
		}
		public int getYear() {
			return this.year;
		}
		public void setDuration(float duration) {
			this.duration = duration;
		}
		public float getDuration() {
			return this.duration;
		}
		public void setHotness(float hotness) {
			this.hotttnesss = hotness;
		}
		
		public float getHotness() {
			return this.hotttnesss;
		}
		public void setBeatsStart(float beatsStart) {
			this.beats_start = beatsStart;
		}
		public float getBeatsStart() {
			return this.beats_start;
		}
		
		public void setTimeSignatureConfidence(float timeSignatureConfidence) {
			this.time_signature_confidence = timeSignatureConfidence;
		}
		public float getTimeSignatureConfidence() {
			return this.time_signature_confidence;
		}
		
		public void setTitle(String title) {
			this.title = title;
		}
		public String getTitle() {
			return this.title;
		}
		
		public void setBarsConfidence(float barsConfidence) {
			this.bars_confidence = barsConfidence;
		}
		public float getBarsConfidence() {
			return this.bars_confidence;
		}
		
		public void setSongId(String id) {
			this.id = id;
		}
		public String getSongId() {
			return this.id;
		}
		
		public void setBarsStart(float barsStart) {
			this.bars_start = barsStart;
		}
		public float getBarsStart() {
			return this.bars_start;
		}
		public void setArtistMbTags(String mbTags) {
			this.artist_mbtags = mbTags;
		}
		public String getArtistMbTags() {
			return this.artist_mbtags;
		}
		public void setStartOfFadeOut(float startOfFadeOut) {
			this.start_of_fade_out = startOfFadeOut;
		}
		public float getStartOfFadeOut() {
			return this.start_of_fade_out;
		}
		public void setTempo(float tempo) {
			this.tempo = tempo;
		}
		public float getTempo() {
			return this.tempo;
		}
		public void setEndOfFadeIn(float endOfFadeIn) {
			this.end_of_fade_in = endOfFadeIn;
		}
		public float getEndOfFadeIn() {
			return this.end_of_fade_in;
		}
		public void setBeatsConfidence(float beatsConfidence) {
			this.beats_confidence = beatsConfidence;
		}
		public float getBeatsConfidence() {
			return this.beats_confidence;
		}
		public void setTatumsConfidence(float tatumsConfidence) {
			this.tatums_confidence = tatumsConfidence;
		}
		public float getTatumsConfidence() {
			return this.tatums_confidence;
		}
		public void setMode(int mode) {
			this.mode = mode;
		}
		public int getMode() {
			return this.mode;
		}
		public void setTimeSignature(float timeSignature) {
			this.time_signature = timeSignature;
		}
		public float getTimeSignature() {
			return this.time_signature;
		}
		
		public void setLoudness(float loudness) {
			this.loudness = loudness;
		}
		public float getLoudness() {
			return this.loudness;
		}
	
	}

}
