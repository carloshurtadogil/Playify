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
	public void map(String key, JsonObject value, DFS context, String file) throws IOException {
		// TODO Auto-generated method stub
		
		Gson gson = new Gson();
		Map unsortMap = new HashMap();
		
		//value is json object which contains one song
		
		//Parsing music.json
		//JsonObject jsonObject = gson.fromJson(new FileReader("music.json"), JsonObject.class);
        //Object obj = new JSONParser().parse(new FileReader("music.json")); 
		
		JsonObject artist = value.getAsJsonObject("artist");
		JsonObject song = value.getAsJsonObject("song");
		JsonObject release = value.getAsJsonObject("release");
		
		//creating new key
		String newKey = song.get("title").getAsString();
		
		//creating a new json object newValue
		String artistName = artist.get("name").getAsString();
		Integer year = song.get("year").getAsInt();
		String albumName = release.get("name").getAsString();
		String songID = song.get("id").getAsString();
			
		//create a rank? 
		JsonObject newValue = new JsonObject();
		newValue.addProperty("artistName", artistName);	
		newValue.addProperty("year", year);	
		newValue.addProperty("albumName", albumName);
		newValue.addProperty("songID", songID);
		
		
		ArrayList<String> unwanted = new ArrayList<String>(); 
		unwanted.add("is");
		unwanted.add("the");
		unwanted.add("and");
		unwanted.add("of");
		unwanted.add("at");
		
		
		String tokenKey;
		//Tokenize string key
		 StringTokenizer st = new StringTokenizer(key);
	     while (st.hasMoreTokens()) {
	    
	    	//setting each token as a key value
	    	 tokenKey = st.nextToken();
	    	 if(unwanted.contains(tokenKey))
	    		 continue;
	    	 else
	    		 //unsorted hashmap to map new tokenized key and value
	   	         unsortMap.put(tokenKey, newValue);
	     }
	    
		context.emit(newKey, newValue, file);
	}

	
	@Override
	public void reduce(Integer key, JsonObject values, DFS context, String file) throws IOException {
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
