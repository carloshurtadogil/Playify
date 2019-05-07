package application.MapReduce;

import java.util.*;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import application.DFS.DFS;
import application.Models.Song;
import application.Models.SongResponse;

public class Mapper implements MapReduceInterface {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void map(JsonObject value, DFS context, String file) throws Exception {
		
		List<JsonObject> listOfJsonObjects = new ArrayList<JsonObject>();
		listOfJsonObjects.add(value);
		Gson gson = new Gson();
		Map<String, JsonObject> unsortMap = new HashMap<String, JsonObject>();

		JsonObject song = value.getAsJsonObject("song");
		// creating new key
		String newKey = song.get("title").getAsString();
		context.emit(newKey, listOfJsonObjects, file);
	}

	@Override
	public void reduce(String key, List<JsonObject> values, DFS context, String file) throws Exception {
		// TODO Auto-generated method stub

		sort(values);
		context.emit(key, values, file);

	}

	public void sort(List<JsonObject> values) throws IOException {

		List<Song> songs= new Gson().fromJson(values.toString(), new TypeToken<List<Song>>(){}.getType());
		Collections.sort(songs, new SortbyAlbum());

		values = new ArrayList<JsonObject>();
		for(Song s: songs) {
			JsonObject releaseDetails = new JsonObject();
			releaseDetails.addProperty("name", s.getRelease().getReleaseName());
			releaseDetails.addProperty("id", s.getRelease().getReleaseID());
			
			JsonObject artistDetails = new JsonObject();
			artistDetails.addProperty("terms", s.getArtistDetails().getTerms());
			artistDetails.addProperty("name", s.getArtistDetails().getName());
			
			JsonObject songDetails = new JsonObject();
			songDetails.addProperty("title", s.getSongDetails().getTitle());
			songDetails.addProperty("id", s.getSongDetails().getSongId());
		
			JsonObject songObject = new JsonObject();
			songObject.add("release", releaseDetails);
			songObject.add("artist", artistDetails);
			songObject.add("song", songDetails);
			
			values.add(songObject);
		}
		

	}

	class SortbyAlbum implements Comparator<Song> {
		// Used for sorting in ascending order of
		// roll number
		public int compare(Song firstSong, Song secondSong) {
			String first = firstSong.getRelease().getReleaseName();
			String second = secondSong.getRelease().getReleaseName();

			return (first.compareTo(second));
		}
	}
}
