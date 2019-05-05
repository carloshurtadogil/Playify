package application.MapReduce;

import java.util.*;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import application.DFS.DFS;
import application.Models.Song;
import application.Models.SongResponse;

public class Mapper implements MapReduceInterface{
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void map(JsonObject value, DFS context, String file) throws Exception {
		Gson gson = new Gson();
		Map<String, JsonObject> unsortMap = new HashMap<String, JsonObject>();
		
		JsonObject song = value.getAsJsonObject("song");
		//creating new key
		String newKey = song.get("title").getAsString();
		
		//create an arraylist of unwanted keywords that should be ignored
		ArrayList<String> unwanted = new ArrayList<String>(); 
		unwanted.add("is");
		unwanted.add("the");
		unwanted.add("and");
		unwanted.add("of");
		unwanted.add("at");
		
		String tokenKey;
		//Tokenize string key
		StringTokenizer st = new StringTokenizer(newKey);
	    while (st.hasMoreTokens()) {
	    
	    	//setting each token as a key value
	    	 tokenKey = st.nextToken();
	    	 if(unwanted.contains(tokenKey)) {
	    		 continue;
	    	 }
	    	 else {
	    		 //unsorted hashmap to map new tokenized key and value
	   	         unsortMap.put(tokenKey, value);
	    	 }
	     }
	    

	    context.emit(newKey, value, file);
	 }

	
	@Override
	public void reduce(String key, JsonObject values, DFS context, String file) throws Exception {
		// TODO Auto-generated method stub
		
		sort(values);
		context.emit(key, values, file);
		
	} 
	
	public void sort (JsonObject values) throws IOException {
		
		//sort by the ArtistName
		//SortedMap<String, JsonObject> sortedMap = new TreeMap<>();
		
		//values is one JsonObject which contains a bunch of values
		SongResponse songResponse = new Gson().fromJson(values, SongResponse.class);
		List<Song> songs = songResponse.getSongsInPage();
		
	//	List songValues = new ArrayList();
	//	songValues.add(values);
		
		Collections.sort(songs, new SortbyAlbum()); 
	
	}
	
	class SortbyAlbum implements Comparator<Song> 
	{ 
	    // Used for sorting in ascending order of
	    // roll number 
	    public int compare(Song firstSong, Song secondSong) 
	    { 
	    	String first = firstSong.getRelease().getReleaseName();
	    	String second = secondSong.getRelease().getReleaseName();
	    	
	        return (first.compareTo(second));
	    } 
	}
}
