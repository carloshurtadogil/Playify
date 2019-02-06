package application;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class User{
	
	@SerializedName("username")
	@Expose
	private String username;
	
	@SerializedName("password")
	@Expose
	private String password;
	@SerializedName("playlists")
	@Expose
	private List<Playlist> playlists;

	public User(String user, String pass) {
		this.username = user;
		this.password = pass;
		this.playlists = new ArrayList<Playlist>();
	}
	public String getUsername()
	{
		return username;
	}

	public String getPassword() {
		return password;
	}

	public List<Playlist> getPlaylists(){
		return this.playlists;
	}

	public void setUsername(String uname) {
		this.username = uname;
		try {
			updateUserJSON();
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Unable to update users.json: Users.setUsername()");
		}
	}

	public void setPassword(String pass) {
		this.password = pass;
		try {
			updateUserJSON();
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Unable to update users.json: Users.setPassword()");
		}
	}

	public void setPlaylists(List<Playlist> thePlaylists){
		this.playlists = thePlaylists;
		try {
			updateUserJSON();
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Unable to update users.json: Users.setPlaylists()");
		}
	}
	
	@SuppressWarnings("unchecked")
	public void updateUserJSON() throws FileNotFoundException, IOException, ParseException {

		JSONParser parsing = new JSONParser();
		JSONObject mainObject = (JSONObject) parsing.parse(new FileReader("users.json"));
		
		//TESTING PURPOSES
	/*	Gson gson1 = new Gson();
		String json1 = gson1.toJson(mainObject);
		System.out.println(json1);
		
	    JsonParser jp = new JsonParser();
	    JsonElement root = jp.parse(json1);
	    System.out.println(root.toString());
	    //JsonArray rootArr = root.getAsJsonArray();

	    JsonObject rootObj = rootArr.get(0).getAsJsonObject();
	    rootObj.entrySet().forEach(entry -> System.out.println(entry.getKey()+": "+entry.getValue().getAsString()));

*/
		
		//TESTING PURPOSES
		
		JSONArray userArray = (JSONArray) mainObject.get("users"); //the list of user objects found is users.json
		
		JSONObject userEdit = new JSONObject(); //user JSON object to replace the current user object
		userEdit.put("username", this.username);
		userEdit.put("password", this.password);
		if(this.playlists.size() > 0) {
			
			JSONArray pl = new JSONArray(); //new playlist array 
			for (Playlist tempPL : this.playlists) {
				
		         JSONObject playlistJSON = new JSONObject();
		         JSONArray songs = new JSONArray();//new song array to be inserted into the current playlist
		         
		         //Check if there are any actual songs in the playlists
		         if(tempPL.getSongs()!=null) {
		        	 for(Song song : tempPL.songs) {
		        		 
			        	 JSONObject songJSON = new JSONObject();
			        	 JSONObject releaseJSON = new JSONObject();
			        	 JSONObject artistJSON = new JSONObject();
			        	 
			        	 songJSON.put(	"key"	, song.song.key);
			        	 songJSON.put(	"mode_confidence"	, song.song.mode_confidence);
			        	 songJSON.put(	"artist_mbtags_count"	, song.song.artist_mbtags_count);
			        	 songJSON.put(	"key_confidence"	, song.song.key_confidence);
			        	 songJSON.put(	"tatums_start"	, song.song.tatums_start);
			        	 songJSON.put(	"year"	, song.song.year);
			        	 songJSON.put(	"duration"	, song.song.duration);
			        	 songJSON.put(	"hotttnesss"	, song.song.hotttnesss);
			        	 songJSON.put(	"beats_start"	, song.song.bars_start);
			        	 songJSON.put(	"time_signature_confidence"	, song.song.time_signature_confidence);
			        	 songJSON.put(	"title"	, song.song.title);
			        	 songJSON.put(	"bars_confidence"	, song.song.bars_confidence);
			        	 songJSON.put(	"id"	, song.song.id);
			        	 songJSON.put(	"bars_start"	, song.song.bars_start);
			        	 songJSON.put(	"artist_mbtags"	, song.song.artist_mbtags);
			        	 songJSON.put(	"start_of_fade_out"	, song.song.start_of_fade_out);
			        	 songJSON.put(	"tempo"	, song.song.tempo);
			        	 songJSON.put(	"end_of_fade_in"	, song.song.end_of_fade_in);
			        	 songJSON.put(	"beats_confidence"	, song.song.beats_confidence);
			        	 songJSON.put(	"tatums_confidence"	, song.song.tatums_confidence);
			        	 songJSON.put(	"mode"	, song.song.mode);
			        	 songJSON.put(	"time_signature"	, song.song.time_signature);
			        	 songJSON.put(	"loudness"	, song.song.loudness);
			        	 
			        	 releaseJSON.put( "id", song.release.id);
			        	 releaseJSON.put( "name", song.release.name);
			        	 
			        	 artistJSON.put( "terms_freq", song.artist.terms_freq);
			        	 artistJSON.put( "terms", song.artist.terms);
			        	 artistJSON.put( "name", song.artist.name);
			        	 artistJSON.put( "familiarity", song.artist.familiarity);
			        	 artistJSON.put( "longitude", song.artist.longitude);
			        	 artistJSON.put( "id", song.artist.id);
			        	 artistJSON.put( "location", song.artist.location);
			        	 artistJSON.put( "latitude", song.artist.latitude);
			        	 artistJSON.put( "similar", song.artist.similar);
			        	 artistJSON.put( "hotttnesss", song.artist.hotttnesss);
			        	 
			        	 JSONObject finalizedSongJSON = new JSONObject();
			        	 finalizedSongJSON.put( "release", releaseJSON);
			        	 finalizedSongJSON.put( "artist", artistJSON);
			        	 finalizedSongJSON.put( "song", songJSON);
			        	 
			        	 songs.add(finalizedSongJSON);
			         }
		        	 playlistJSON.put("songs", songs);// add all songs to playlist array
		         }
		         //otherwise in the case of an empty playlist, put a JSONArray in place
		         else {
		        	 playlistJSON.put("songs", new JSONArray());
		         }
		         
		         playlistJSON.put("playlistname", tempPL.getPlaylistName()); //add playlist name to the playlist array
		         pl.add(playlistJSON); 
		         
		    
			}
			
			userEdit.put("playlists", pl);//add playlist array to edited user object
		} else {
			userEdit.put("playlists", new JSONArray());
		}
		
		
		if(userArray.size() > 0) {
			for(int i = 0; i < userArray.size(); i++) { //find the user with the same username as this object
				JSONObject traverseUser = (JSONObject) userArray.get(i);
				if(traverseUser.get("username").equals(this.username)) {
					userArray.remove(i); //remove the information from the main user list
					userArray.add(i, userEdit); //add the new information to the main user list in the same area
					
					//Pretty Print the json string
					GsonBuilder builder = new GsonBuilder();
					builder.disableHtmlEscaping();
					Gson gson = builder.setPrettyPrinting().create();
					String json = gson.toJson(mainObject);
					
					//Writes the new user object into the users.json file
					try {
						BufferedWriter fileWrite  = new BufferedWriter(new FileWriter("users.json"));

						fileWrite.write(json);
						fileWrite.flush();
						fileWrite.close();
					}
					catch(IOException ioException) {
						ioException.printStackTrace();
					}
					catch(Exception e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
	}
	
	public void addPlaylist(Playlist newPlaylist) {
		this.playlists.add(newPlaylist);
		setPlaylists(this.playlists);
	}
}