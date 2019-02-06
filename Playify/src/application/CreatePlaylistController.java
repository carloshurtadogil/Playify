package application;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

public class CreatePlaylistController {
	@FXML
	private Label tempLabel;
	@FXML
	private TextField txtSearch;
	@FXML
	private Button btnEnter;
	@FXML
	private ListView<String> listOfSongs;
	@FXML
	private Button btnAdd;
	@FXML
	private TextField txtPlaylistName;
	@FXML
	private Button btnSet;
	@FXML
	private Button btnCreatePlaylist;
	
	//List to hold songs that match search results
	private List<Song> searchResults = new ArrayList<Song>();
	//User logged in
	private User selectedUser;
	private Playlist newPlaylist = new Playlist();
	
	
	public void setLoggedUser(User theUser) throws FileNotFoundException, IOException, ParseException {
		this.selectedUser = theUser;
		tempLabel.setText("Username: " + selectedUser.getUsername());
		
	}
	
	public void Search(ActionEvent event) { 
		try {
			Gson gson = new Gson();
			
			//List to hold song names
			List<String> songNames = new ArrayList<String>();
		
			//Creates a list that contains every song.
			Playlist myPlaylist = gson.fromJson(new FileReader("music.json"), Playlist.class);
			System.out.println(myPlaylist);
			/*
			
			List<Song> masterSongList = myPlaylist.getSongs();
			
			//Check each song in the master list, if the song matches the search criteria then add it to the searchResults list.
			//Store the name of the song in songNames so that it can be displayed in a list view.
			for(Song x : masterSongList) {
				if(x.getSongName().equals(txtSearch.getText()) || x.getSongType().equals(txtSearch.getText())) {
					searchResults.add(x);
					songNames.add(x.getSongName());
				}
			}
			
			//Display the song names in the list view and set the list view so they can select multiple items.
			listOfSongs.setItems(FXCollections.observableList(songNames));
			listOfSongs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			*/
			
		}catch (Exception e) {
			System.out.println("Search Error");
			e.printStackTrace();
		}
		
	}
	
	public void Search() {
		JsonParser jp = new JsonParser();
	    //JsonElement root = jp.parse(json);
	    //JsonArray rootArr = root.getAsJsonArray();

	    //JsonObject rootObj = rootArr.get(0).getAsJsonObject();
	    //rootObj.entrySet().forEach(entry -> System.out.println(entry.getKey()+": "+entry.getValue().getAsString()));
	}
	
	public void Add(ActionEvent event) {
		try {
			ObservableList<String> selectedSongs;
			selectedSongs = listOfSongs.getSelectionModel().getSelectedItems();
			
			
			for(String songName: selectedSongs) {
				for(Song song: searchResults) {
					if(songName.equals(song.getTitle())) {
						//Add to playlist
					}
				}
			}
		}catch (Exception e) {
			System.out.println("Add Song Error");
			e.printStackTrace();
		}
	}
	
	public void SetPlaylistName(ActionEvent event) {
		this.newPlaylist.setPlaylistName(txtPlaylistName.getText());
	}
	
	public void createPlaylist(ActionEvent event) {
		selectedUser.addPlaylist(this.newPlaylist);
	}
}
