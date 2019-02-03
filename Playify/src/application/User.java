package application;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
	
	public void updateUserJSON() throws FileNotFoundException, IOException, ParseException {
		System.out.println("UPDATING JSON");
		JSONParser parsing = new JSONParser();
		JSONObject mainObject = (JSONObject) parsing.parse(new FileReader("users.json"));
		JSONArray userArray = (JSONArray) mainObject.get("users");
		
		JSONObject userEdit = new JSONObject();
		userEdit.put("username", this.username);
		userEdit.put("password", this.password);
		if(this.playlists.size() > 0) {
			JSONArray pl = new JSONArray();
			for (Playlist tempPL : this.playlists) {
				
		         JSONObject playlistJSON = new JSONObject();
		         JSONArray songs = new JSONArray();
		         for(Song song : tempPL.getSongs()) {
		        	 JSONObject songJSON = new JSONObject();
		        	 songJSON.put("terms", song.getSongType());
		        	 songJSON.put("name", song.getSongName());
		        	 songs.add(songJSON);
		         }
		         playlistJSON.put("songs", songs);
		         playlistJSON.put("playlistname", tempPL.getPlaylistName());
		         pl.add(playlistJSON);
		         userEdit.put("playlists", pl);
		    
			}
		} else {
			userEdit.put("playlists", new JSONArray());
		}
		
		
		if(userArray.size() > 0) {
			for(int i = 0; i < userArray.size(); i++) {
				JSONObject traverseUser = (JSONObject) userArray.get(i);
				if(traverseUser.get("username").equals(this.username)) {
					userArray.remove(i);
					userArray.add(i, userEdit);
					
					
					
					//Writes the new user object into the users.json file
					try {
						BufferedWriter fileWrite  = new BufferedWriter(new FileWriter("users.json"));

						fileWrite.write(mainObject.toString());
						fileWrite.flush();
						fileWrite.close();
					}
					catch(IOException ioException) {
						ioException.printStackTrace();
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}