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
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javazoom.jl.player.*;
import javazoom.jl.decoder.JavaLayerException;

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
	private ListView<Song> listOfSongs;
	@FXML
	private Label tempLabel;
	@FXML
	private Button clearButton;
	@FXML
	private Label pLabel;
	@FXML
	private ListView<Song> newPlaylistView;
	@FXML
	private Button removeButton;
	@FXML 
	private Button playButton;
	@FXML 
	private Button pauseButton;
	@FXML 
	private Button stopButton;
	@FXML 
	private Slider volumeSlider;
	
	private List<Song> newSongs;
	private List<Song> mySongs;
	
	private PlayifyPlayer musicPlayer;
	
	//List to hold songs that match search results
	private List<Song> searchResults = new ArrayList<Song>();
	//User logged in
	private User selectedUser;
	private Playlist newPlaylist = new Playlist();
	private int volume;
	
	
	public void setLoggedUser(User theUser) throws FileNotFoundException, IOException, ParseException {
		this.selectedUser = theUser;
		tempLabel.setText("Username: " + selectedUser.getUsername());
		loadAllSongs();
		clearButton.setOnAction((buttonPressed) -> {
			loadAllSongs();
		});
		newSongs = new ArrayList<Song>();
	}
	
	/**
	 * Retrieve the player
	 * @return The music player found in this class
	 */
	public PlayifyPlayer getMusicPlayer() {
		return musicPlayer;
	}
	
	/**
	 * For the transfer of a music player from the previous controller
	 * @param p The player to be transferred
	 */
	public void setMusicPlayer(PlayifyPlayer p) {
		musicPlayer = p;
	}
	
	/**
	 * Transfer the volume from the previous controller
	 * @param v The volume level that the song is playing at
	 */
	public void setVolume(int v) {
		volume = v;
	}
	
	public void Search(ActionEvent event) { 
		try {
			List<Song> songNames = new ArrayList<Song>();
			Gson gson = new Gson();
			List<Song> mySongs = gson.fromJson(new FileReader("music.json"), new TypeToken<List<Song>>(){}.getType());
			Playlist masterPlaylist = new Playlist();
			masterPlaylist.setSongs(mySongs);
			
			for(Song x : masterPlaylist.songs) {
				if(x.getSongDetails().getTitle().equals(txtSearch.getText()) || 
				   x.getArtistDetails().getName().equals(txtSearch.getText()) || 
				   x.getArtistDetails().getTerms().equals(txtSearch.getText())) {
					searchResults.add(x);
					songNames.add(x);
				}
					
			}
			
			listOfSongs.setItems(FXCollections.observableList(songNames));
			listOfSongs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void Play(ActionEvent event) {
		System.out.println("Play clicked");
	}
	
	public void Stop(ActionEvent event) {
		System.out.println("Stop clicked");
	}
	
	public void Pause(ActionEvent event) {
		System.out.println("Pause clicked");
	}
	
	public void Add(ActionEvent event) {
		System.out.println("Clicked");
		try {
			ObservableList<Song> selectedSongs;
			selectedSongs = listOfSongs.getSelectionModel().getSelectedItems();
			List<Song> theSongs = new ArrayList<Song>();
			
			for(Song songName: selectedSongs) {
				System.out.println(songName);
				for(Song song: searchResults) {
					if(songName.getSongDetails().getTitle().equals(song.getSongDetails().getTitle())) {
						theSongs.add(song);
						
					}
				}
				if(mySongs != null) {
					for(Song s : mySongs) {
						if(s.getSongDetails().getTitle().equals(songName.getSongDetails().getTitle())) {
							newSongs.add(s);
						}
					}
					newPlaylistView.setItems(FXCollections.observableList(newSongs));
					newPlaylistView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				}
			}
			
			this.newPlaylist.setSongs(theSongs);
			
		}catch (Exception e) {
			System.out.println("Add Song Error");
			e.printStackTrace();
		}
	}
	
	public void Remove(ActionEvent event) {
		try {
			ObservableList<Song> selectedSongs;
			selectedSongs = newPlaylistView.getSelectionModel().getSelectedItems();
			List<Song> theSongs = new ArrayList<Song>();
			for(Song s: selectedSongs) {
				if(newSongs != null) {
					String songName = s.getSongDetails().getTitle();
					for(int i = 0; i < newSongs.size(); i ++) {
						if(newSongs.get(i).getSongDetails().getTitle().equals(songName)) {
							newSongs.remove(i);
						}
					}
					newPlaylistView.setItems(FXCollections.observableList(newSongs));
				}
			}
			
			this.newPlaylist.setSongs(theSongs);
			
		}catch (Exception e) {
			System.out.println("Remove Song Error");
			e.printStackTrace();
		}
	}
	
	public void SetPlaylistName(ActionEvent event) {
		this.newPlaylist.setPlaylistName(txtPlaylistName.getText());
	}
	
	public void createPlaylist(ActionEvent event) {
		newPlaylist = new Playlist();
		newPlaylist.setPlaylistName(txtPlaylistName.getText());
		
		if(newSongs.size() !=0 && !(newPlaylist.getPlaylistName()).isEmpty()) {
			for(Song s: newSongs) {
				newPlaylist.addSong(s);
			}
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
		else {
			System.out.println("Can't create a new playlist since there are no songs or missing playlistname");
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
			mySongs = gson.fromJson(new FileReader("music.json"), new TypeToken<List<Song>>(){}.getType());
			Playlist masterPlaylist = new Playlist();
			masterPlaylist.setSongs(mySongs);
			List<Song> songNames = new ArrayList<Song> ();
			for(Song s : mySongs) {
				songNames.add(s);
			}
			listOfSongs.setItems(FXCollections.observableList(songNames));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
