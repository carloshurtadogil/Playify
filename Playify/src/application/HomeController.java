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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeController {
	
	@FXML
	private Label temporaryLabel;
	
	@FXML
	private AnchorPane rootPane;

	@FXML
	private ListView<Playlist> playlistView;
	
	private User selectedUser;
	
	//This method executes after the HomeController is loaded, to set the user
	//as a logged in user
	public void setLoggedUser(User theUser) throws FileNotFoundException, IOException, ParseException {
		this.selectedUser = theUser;
		temporaryLabel.setText("This is a temporary home page");
		loadPlaylists(selectedUser);
		
		if(loadPlaylists(selectedUser)) {
			System.out.println("Success: User " + theUser.getUsername() + "'s playlists exists");
			populatePlaylists(theUser);
			for(Playlist p: selectedUser.getPlaylists()) {
				p.printPlaylistDetails();
			}
		} else {
			System.out.println("Fail: User " + theUser.getUsername() + "'s playlists do not exists");
		}
		
	}
	
	//Populates all of the user's playlists into the ListView of Home.fxml
	public void populatePlaylists(User someUser) {

		//Populate the list of playlists into the ListView as an observable list
		playlistView.setItems(FXCollections.observableList(someUser.getPlaylists()));
		
		//Sets a mouse clicked event for each of the Playlists
		playlistView.setOnMouseClicked( event ->{
			
			//assign the selected playlist to a Playlist object/variable
			Playlist selectedPlaylist = playlistView.getSelectionModel().getSelectedItem();
			
			try {
				//Load the Playlist.fxml page for that paricular playlist
				FXMLLoader playlistLoader = new FXMLLoader();
				playlistLoader.setLocation(getClass().getResource("/application/Playlist.fxml"));
				Parent root = playlistLoader.load();
				Scene playlistScene = new Scene(root);
				
				//Obtain the controller to set selected user and playlist
				PlaylistController playlistControl =playlistLoader.getController();
				playlistControl.setUserAndPlaylist(selectedUser, selectedPlaylist);
				
				//Load the current stage to prevent from generating a new window/popup
				Stage playlistStage = (Stage) temporaryLabel.getScene().getWindow();
				playlistStage.setScene(playlistScene);
				playlistStage.show();
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
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
								Song songObject = new Song(song.get("name").toString(), 
														   song.get("terms").toString());
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