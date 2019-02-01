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
import com.google.gson.JsonElement;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class HomeController {
	
	@FXML
	private Label temporaryLabel;
	
	@FXML
	private AnchorPane rootPane;

	@FXML
	private TextField uField;
	
	@FXML
	private TextField uField2;
	
	private User selectedUser;
	
	//This method executes after the HomeController is loaded, to set the user
	//as a logged in user
	public void setLoggedUser(User theUser) throws FileNotFoundException, IOException, ParseException {
		this.selectedUser = theUser;
		uField.setText(selectedUser.getUsername());
		uField2.setText(selectedUser.getPassword());
		temporaryLabel.setText("This is a temporary home page");
		loadPlaylists(theUser);
		
		if(loadPlaylists(theUser)) {
			System.out.println("Success: User " + theUser.getUsername() + "'s playlists exists");
			for(Playlist p: theUser.getPlaylists()) {
				p.printPlaylistDetails();
			}
		} else {
			System.out.println("Fail: User " + theUser.getUsername() + "'s playlists do not exists");
		}
		
	}

	//Allows a user to add a playlist to the list of playlists created
	public void CreatePlaylist() throws FileNotFoundException, IOException, ParseException {
		Playlist newPlaylist = new Playlist();
		selectedUser.getPlaylists().add(newPlaylist);


		JSONParser parsing = new JSONParser();

		JSONObject mainObject = (JSONObject) parsing.parse(new FileReader("users.json"));
		JSONArray userArray = (JSONArray) mainObject.get("users");

		for(int i=0; i<userArray.size(); i++) {

			JSONObject user = (JSONObject)userArray.get(i);
			if(selectedUser.getUsername().equals(user.get("username"))) {
				JSONArray playlistArray= (JSONArray) user.get("playlists");
				playlistArray.add(newPlaylist);

				BufferedWriter writer = new BufferedWriter(new FileWriter("users.json"));
				writer.write(mainObject.toString());
				writer.flush();
				writer.close();
			}
		}

//		JSONParser parsing = new JSONParser();
//		
//		JSONObject mainObject = (JSONObject) parsing.parse(new FileReader("users.json"));
//		JSONArray userArray = (JSONArray) mainObject.get("users");
//		for(Object jsObj : userArray) {
//			User theUser = (User) jsObj;
//			if(theUser.getUsername().equals(selectedUser.getUsername())) {
//				JSONArray playlistArray = new JSON
//			}
//		}

	}
	
	//Traverse the list of users, identify their playlists, and upload information on said playlists
	public boolean loadPlaylists(User user) throws FileNotFoundException, IOException, ParseException {
		JSONParser parsing = new JSONParser();
		JSONObject mainObject = (JSONObject) parsing.parse(new FileReader("users.json"));
		JSONArray userArray = (JSONArray) mainObject.get("users");
		
		List<Playlist> usersPlaylist = new ArrayList<Playlist>();
		
		for(int i = 0; i < userArray.size(); i++) {
			//Search for the user
			JSONObject traverseUser = (JSONObject) userArray.get(i);
			if(user.getUsername().equals(traverseUser.get("username"))) {
				
				//grab the playlists
				JSONArray playlistArray = (JSONArray) traverseUser.get("playlists");
				
				if(playlistArray.size() > 0) {
					
					for(int j = 0; j < playlistArray.size(); j++) {
						
						JSONObject pl = (JSONObject) playlistArray.get(j);
						
						Playlist playlistObject = new Playlist();
						
						playlistObject.setPlaylistName(pl.get("playlistname").toString());
						
						JSONArray songs = (JSONArray) pl.get("songs");
						
						if(songs.size() > 0) {
							List<Song> songList = new ArrayList<Song>();
							for(int k = 0; k < songs.size(); k++) {
								JSONObject song = (JSONObject) songs.get(k);
								Song songObject = new Song(song.get("songname").toString(), 
														   song.get("songtype").toString());
								songList.add(songObject);
							}
							playlistObject.setSongs(songList);
						}
						usersPlaylist.add(playlistObject);
						user.setPlaylists(usersPlaylist);
					
					}
					return true;
				}
				break;
			}
		}
		
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	


}