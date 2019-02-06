package application;

public class Song {
	
	private double key;
	private double mode_confidence;
	private double artist_mbtags_count;
	private double key_confidence;
	private double tatums_start;
	private int year;
	private double duration;
	private double hotttnesss;
	private double beats_start;
	private double time_signature_confidence;
	private String title;
	private double bars_confidence;
	private String id;
	private double bars_start;
	private String artist_mbtags;
	private double start_of_fade_out;
	private double tempo;
	private double end_of_fade_in;
	private double beats_confidence;
	private double tatums_confidence;
	private int mode;
	private double time_signature;
	private double loudness;
	
	/*****GETTERS*****/
	
	public double getKey() {
		return key;
	}
	
	public double getModeConfidence() {
		return mode_confidence;
	}
	
	public double getArtistMBTagsCount() {
		return artist_mbtags_count;
	}
	
	public double getKeyConfidence() {
		return key_confidence;
	}
	
	public double getTatumsStart() {
		return tatums_start;
	}
	
	public int getYear() {
		return year;
	}
	
	public double getDuration() {
		return duration;
	}
	
	public double getHotttnesss() {
		return hotttnesss;
	}
	
	public double getBeatsStart() {
		return beats_start;
	}
	
	public double getTimeSignatureConfidence() {
		return time_signature_confidence;
	}
	
	public String getTitle() {
		return title;
	}
	
	public double getBarsConfidence() {
		return bars_confidence;
	}
	
	public String getID() {
		return id;
	}
	
	public double getBarsStart() {
		return bars_start;
	}
	
	public String getArtistMBTags() {
		return artist_mbtags;
	}
	
	public double getStartOfFadeOut() {
		return start_of_fade_out;
	}
	
	public double getTempo() {
		return tempo;
	}
	
	public double getEndOfFadeIn() {
		return end_of_fade_in;
	}
	
	public double getBeatsConfidence() {
		return beats_confidence;
	}
	
	public double getTatumsConfidence() {
		return tatums_confidence;
	}
	
	public int getMode() {
		return mode;
	}
	
	public double getTimeSignature() {
		return time_signature;
	}
	
	public double getLoudness() {
		return loudness;
	}
	
	/*****SETTERS*****/
	
	public void setKey(double key) {
		this.key = key;
	}
	
	public void setModeConfidence(double mode_confidence) {
		this.mode_confidence = mode_confidence;
	}
	
	public void setArtistMBTagsCount(double artist_mbtags_count) {
		this.artist_mbtags_count = artist_mbtags_count;
	}
	
	public void setKeyConfidence(double key_confidence) {
		this.key_confidence = key_confidence;
	}
	
	public void setTatumsStart(double tatums_start) {
		this.tatums_start = tatums_start;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public void setDuration(double duration) {
		this.duration = duration;
	}
	
	public void setHotttnesss(double hotttnesss) {
		this.hotttnesss = hotttnesss;
	}
	
	public void setBeatsStart(double beats_start) {
		this.beats_start = beats_start;
	}
	
	public void setTimeSignatureConfidence(double time_signature_confidence) {
		this.time_signature_confidence = time_signature_confidence;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setBarsConfidence(double bars_confidence) {
		this.bars_confidence = bars_confidence;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	public void setBarsStart(double bars_start) {
		this.bars_start = bars_start;
	}
	
	public void setArtistMBTags(String artist_mbtags) {
		this.artist_mbtags = artist_mbtags;
	}
	
	public void setStartOfFadeOut (double start_of_fade_out) {
		this.start_of_fade_out = start_of_fade_out;
	}
	
	public void setTempo(double tempo) {
		this.tempo = tempo;
	}
	
	public void setEndOfFadeIn(double end_of_fade_in) {
		this.end_of_fade_in = end_of_fade_in;
	}
	
	public void setBeatsConfidence(double beats_confidence) {
		this.beats_confidence = beats_confidence;
	}
	
	public void setTatusmConfidence(double tatums_confidence) {
		this.tatums_confidence = tatums_confidence;
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public void setTimeSignature(double time_signature) {
		this.time_signature = time_signature;
	}
	
	public void setLoudness(double loudness) {
		this.loudness = loudness;
	}
	
	/******METHODS******/
	@Override
	public String toString() {
		return this.title;
	}
	 
}