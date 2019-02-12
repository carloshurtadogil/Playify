package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.parser.ParseException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreatePlaylistController {
	@FXML
	private Button backButton;
	@FXML
	private Button btnEnter;
	@FXML
	private Button btnAdd;
	@FXML
	private Button btnSet;
	@FXML
	private Button btnCreatePlaylist;
	@FXML
	private TextField txtSearch;
	@FXML
	private TextField txtPlaylistName;
	@FXML
	private ListView<String> listOfSongs;
	@FXML
	private Label tempLabel;
	@FXML
	private Button clearButton;
	
	//List to hold songs that match search results
	private List<Song> searchResults = new ArrayList<Song>();
	//User logged in
	private User selectedUser;
	private Playlist newPlaylist = new Playlist();
	
	
	public void setLoggedUser(User theUser) throws FileNotFoundException, IOException, ParseException {
		this.selectedUser = theUser;
		tempLabel.setText("Username: " + selectedUser.getUsername());
		loadAllSongs();
		clearButton.setOnAction((buttonPressed) -> {
			loadAllSongs();
		});
	}
	
	public void Search(ActionEvent event) { 
		try {
			List<String> songNames = new ArrayList<String>();
			Gson gson = new Gson();
			List<Song> mySongs = gson.fromJson(new FileReader("music.json"), new TypeToken<List<Song>>(){}.getType());
			Playlist masterPlaylist = new Playlist();
			masterPlaylist.setSongs(mySongs);
			
			for(Song x : masterPlaylist.songs) {
				if(x.getSongDetails().getTitle().equals(txtSearch.getText()) || 
				   x.getArtistDetails().getName().equals(txtSearch.getText()) || 
				   x.getArtistDetails().getTerms().equals(txtSearch.getText())) {
					searchResults.add(x);
					songNames.add(x.getSongDetails().getTitle());
				}
					
			}
			
			listOfSongs.setItems(FXCollections.observableList(songNames));
			listOfSongs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void Add(ActionEvent event) {
		try {
			ObservableList<String> selectedSongs;
			selectedSongs = listOfSongs.getSelectionModel().getSelectedItems();
			List<Song> theSongs = new ArrayList<Song>();
			
			for(String songName: selectedSongs) {
				for(Song song: searchResults) {
					if(songName.equals(song.getSongDetails().getTitle())) {
						theSongs.add(song);
					}
				}
			}
			
			this.newPlaylist.setSongs(theSongs);
			
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
		try {
			FXMLLoader homeControllerLoader = new FXMLLoader();
			homeControllerLoader.setLocation(getClass().getResource("/application/Home.fxml"));
			Parent root = homeControllerLoader.load();
			
			HomeController homeController = homeControllerLoader.getController();
			homeController.setLoggedUser(selectedUser);
			
			Scene homeScene = new Scene(root);
			Stage homeStage = (Stage) tempLabel.getScene().getWindow();
			homeStage.setScene(homeScene);
			homeStage.show();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void goBackToHomePage(ActionEvent event) {
		try {
			FXMLLoader homeControllerLoader = new FXMLLoader();
			homeControllerLoader.setLocation(getClass().getResource("/application/Home.fxml"));
			Parent root = homeControllerLoader.load();
			
			
			HomeController homeController = homeControllerLoader.getController();
			homeController.setLoggedUser(selectedUser);
			
			Scene homeScene = new Scene(root);
			Stage homeStage = (Stage) tempLabel.getScene().getWindow();
			homeStage.setScene(homeScene);
			homeStage.show();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadAllSongs () {
		try {
			Gson gson = new Gson();
			List<Song> mySongs = gson.fromJson(new FileReader("music.json"), new TypeToken<List<Song>>(){}.getType());
			Playlist masterPlaylist = new Playlist();
			masterPlaylist.setSongs(mySongs);
			List<String> songNames = new ArrayList<String> ();
			for(Song s : mySongs) {
				songNames.add(s.getSongDetails().getTitle());
			}
			listOfSongs.setItems(FXCollections.observableList(songNames));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
