package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javazoom.jl.player.*;
import javazoom.jl.decoder.JavaLayerException;

public class HomeController {
	@FXML
	private Label temporaryLabel;
	@FXML
	private Button deletePlaylistButton;
	@FXML
	private AnchorPane rootPane;
	@FXML
	private ListView<Playlist> playlistView;
	@FXML
	private Button logoutButton;
	@FXML
	private ListView<Song> allSongsView;
	@FXML
	private Label errorLabel;
	@FXML
	private Button playButton;
	@FXML 
	private Button pauseButton;
	@FXML 
	private Button stopButton;
	@FXML
	private Button removeSongButton;
	@FXML
	private Slider volumeSlider;
	
	private User selectedUser;
	private List<Song> mySongs;
	private Playlist master;
	private PlayifyPlayer musicPlayer;
	private int volume;
	

	/**
	 * This method executes after the HomeController is loaded, to set the user 
	 * as a logged in user
	 * @param user The current user
	 */
	public void setLoggedUser(User user) throws FileNotFoundException, IOException, ParseException {
		this.selectedUser = user;
		temporaryLabel.setText(user.getUsername());
		loadPlaylists(selectedUser);
		loadAllSongs();
		populatePlaylists(selectedUser);
		
		/* For Testing purposes
		if(selectedUser.getPlaylists().size() > 0) {
			
			String playlistToRemove = selectedUser.getPlaylists().get(0).getPlaylistName();
			if(removePlaylist(selectedUser, playlistToRemove)) {
				System.out.println("Success in removing playlist: " + playlistToRemove);
				System.out.println("password:" + selectedUser.getPassword());
				System.out.println("username:" + selectedUser.getUsername());
				System.out.println("playlists: ");
				for(Playlist p : selectedUser.getPlaylists()) {
					p.printPlaylistDetails();
				}
			} else {
				System.out.println("Failed to remove playlist: " + playlistToRemove);
			}
		} else {
			System.out.println("No playlists to remove");
		}*/

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

	/**
	 * Populates all of the user's playlists into the ListView of Home.fxml
	 * @param someUser The current user
	 */
	public void populatePlaylists(User someUser) {

		logoutButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				try {
					// Load the Login.fxml page 
					FXMLLoader portalLoader = new FXMLLoader();
				    portalLoader.setLocation(getClass().getResource("/application/Login.fxml"));
					Parent root = portalLoader.load();
					Scene portalScene = new Scene(root);

					// Load the current stage to prevent from generating a new window/popup
					Stage portalStage = (Stage) logoutButton.getScene().getWindow();
					portalStage.setScene(portalScene);
					portalStage.show();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		});
		
		// Populate the list of playlists into the ListView as an observable list
		
		List<Playlist> masterPlaylist = new ArrayList<Playlist>();
		masterPlaylist.add(master);
		
		for(Playlist p : someUser.getPlaylists()) {
			masterPlaylist.add(p);
		}
		playlistView.setItems(FXCollections.observableList(masterPlaylist));

		allSongsView.setOnMouseClicked(event -> {
			if(event.getClickCount() == 1) {
				playButton.setOnAction((buttonPressed) -> {
					String name = allSongsView.getSelectionModel().getSelectedItem().getSongDetails().getTitle();
					String id = allSongsView.getSelectionModel().getSelectedItem().getSongDetails().getSongId();
					System.out.println("Song to be played: " + name + " " + id);
				});
			}
		});
		// Sets a mouse clicked event for each of the Playlists
		playlistView.setOnMouseClicked(event -> {

			// User must click on an item in order to delete it
			if (event.getClickCount() == 1) {
				if(playlistView.getSelectionModel().getSelectedIndex() == 0) {
					allSongsView.setItems(FXCollections.observableList(master.getSongs()));
					removeSongButton.setVisible(false);
				} else {
					String playlistname = playlistView.getSelectionModel().getSelectedItem().getPlaylistName();
					allSongsView.setItems(FXCollections.observableList(someUser.getSpecificPlaylist(playlistname).getSongs()));
					removeSongButton.setVisible(true);
				}
				deletePlaylistButton.setOnAction((buttonPressed) -> {
					//Call method that will delete the playlist
					if(playlistView.getSelectionModel().getSelectedIndex() != 0)
					{
						this.removePlaylist(selectedUser, playlistView.getSelectionModel().getSelectedItem().getPlaylistName());
						System.out.println("This button will delete this playlist...");
						errorLabel.setVisible(false);
					}
					else
					{
						errorLabel.setVisible(true);
					}
					
				});
			}
			

			/*
			// User must click twice on the playlist to go to its page
			if (event.getClickCount() == 2) {
				// assign the selected playlist to a Playlist object/variable
				Playlist selectedPlaylist = playlistView.getSelectionModel().getSelectedItem();

				try {
					// Load the Playlist.fxml page for that particular playlist
					FXMLLoader playlistLoader = new FXMLLoader();
					playlistLoader.setLocation(getClass().getResource("/application/Playlist.fxml"));
					Parent root = playlistLoader.load();
					Scene playlistScene = new Scene(root);

					// Obtain the controller to set selected user and playlist
					PlaylistController playlistControl = playlistLoader.getController();
					
					//Checks if a selected playlist is not null,
					//or in other words if a user clicks on an empty item in the ListView
					if(selectedPlaylist!=null) {
						playlistControl.setUserAndPlaylist(selectedUser, selectedPlaylist);
						// Load the current stage to prevent from generating a new window/popup
						Stage playlistStage = (Stage) temporaryLabel.getScene().getWindow();
						playlistStage.setScene(playlistScene);
						playlistStage.show();
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/

		});
	}

	/**
	 * Allows a user to add a playlist to the list of playlists created
	 */ 
	public void GoToPlaylistPage(ActionEvent event) {

		try {
			//Load the CreatePlaylist.fxml page
			FXMLLoader createPlaylistLoader = new FXMLLoader();
			createPlaylistLoader.setLocation(getClass().getResource("/application/CreatePlaylist.fxml"));
			Parent root = createPlaylistLoader.load();
			Scene createPlaylistScene = new Scene(root);
			
			//Obtain the controller to set the selected user
			CreatePlaylistController createPlaylistControl = createPlaylistLoader.getController();
			createPlaylistControl.setLoggedUser(selectedUser);

			//Load the current stage to prevent from generating a new window/popup
			Stage createPlaylistStage = (Stage) temporaryLabel.getScene().getWindow();
			createPlaylistStage.setScene(createPlaylistScene);
			createPlaylistStage.show();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("An exception has occurred");
		}
	}

	/**
	 * Traverse the list of users, identify their playlists, and upload information
	 * on said play lists
	 * 
	 * @param user User whose playlists are being read
	 */ 
	public boolean loadPlaylists(User user) throws FileNotFoundException, IOException, ParseException {
//		JSONParser parsing = new JSONParser();
//		JSONObject mainObject = (JSONObject) parsing.parse(new FileReader("users.json"));
//		JSONArray userArray = (JSONArray) mainObject.get("users");
//
//		List<Playlist> usersPlaylist = new ArrayList<Playlist>();
//
//		for (int i = 0; i < userArray.size(); i++) {
//			// Search for the user
//			JSONObject traverseUser = (JSONObject) userArray.get(i);
//			if (user.getUsername().equals(traverseUser.get("username"))) {
//
//				// grab the playlists
//				JSONArray playlistArray = (JSONArray) traverseUser.get("playlists");
//
//				if (playlistArray.size() > 0) {
//
//					for (int j = 0; j < playlistArray.size(); j++) {
//
//						JSONObject pl = (JSONObject) playlistArray.get(j);
//
//						Playlist playlistObject = new Playlist();
//
//						playlistObject.setPlaylistName(pl.get("playlistname").toString());
//
//						JSONArray songs = (JSONArray) pl.get("songs");
//
//						if (songs.size() > 0) {
//							List<Song> songList = new ArrayList<Song>();
//							for (int k = 0; k < songs.size(); k++) {
//								JSONObject song = (JSONObject) songs.get(k);
//								Song songObject = new Song();
//								songObject.artist.familiarity = 
//								songObject.setKey(Double.parseDouble(song.get("key").toString()));
//								songObject.setModeConfidence(Double.parseDouble(song.get(	"mode_confidence"	).toString()));
//								songObject.setArtistMBTagsCount(Double.parseDouble(song.get(	"artist_mbtags_count"	).toString()));
//								songObject.setKeyConfidence(Double.parseDouble(song.get(	"key_confidence"	).toString()));
//								songObject.setTatumsStart(Double.parseDouble(song.get(	"tatums_start"	).toString()));
//								songObject.setYear(Integer.parseInt(song.get(	"year"	).toString()));
//								songObject.setDuration(Double.parseDouble(song.get(	"duration"	).toString()));
//								songObject.setHotttnesss(Double.parseDouble(song.get(	"hotttnesss"	).toString()));
//								songObject.setBeatsStart(Double.parseDouble(song.get(	"beats_start"	).toString()));
//								songObject.setTimeSignatureConfidence	(Double.parseDouble(song.get(	"time_signature_confidence"	).toString()));
//								songObject.setTitle	(song.get(	"title"	).toString());
//								songObject.setBarsConfidence(Double.parseDouble(song.get(	"bars_confidence"	).toString()));
//								songObject.setID(song.get(	"id"	).toString());
//								songObject.setBarsStart	(Double.parseDouble(song.get(	"bars_start"	).toString()));
//								songObject.setArtistMBTags(song.get(	"artist_mbtags"	).toString());
//								songObject.setStartOfFadeOut	(Double.parseDouble(song.get(	"start_of_fade_out"	).toString()));
//								songObject.setTempo(Double.parseDouble(song.get(	"tempo"	).toString()));
//								songObject.setEndOfFadeIn(Double.parseDouble(song.get(	"end_of_fade_in"	).toString()));
//								songObject.setBeatsConfidence(Double.parseDouble(song.get(	"beats_confidence"	).toString()));
//								songObject.setTatusmConfidence(Double.parseDouble(song.get(	"tatums_confidence"	).toString()));
//								songObject.setMode(Integer.parseInt(song.get(	"mode"	).toString()));
//								songObject.setTimeSignature(Double.parseDouble(song.get(	"time_signature"	).toString()));
//								songObject.setLoudness(Double.parseDouble(song.get(	"loudness"	).toString()));
//						
//								
//								songList.add(songObject);
//							}
//							playlistObject.setSongs(songList);
//						}
//						usersPlaylist.add(playlistObject);
//						user.setPlaylists(usersPlaylist);
//
//					}
//					return true;
//				}
//				break;
//			}
//		}

		return false;
	}
	
	/**
	 * Remove a playlist from the user's playlist array 
	 * @param user User who's playlists will be updated
	 * @param name The name of the playlist to be removed
	 * @return True if the playlist has been found and subsequently removed, false otherwise
	 */
	public void removePlaylist(User user, String name)  {
		List<Playlist> playlists = user.getPlaylists();
		if(playlists.size() > 0) {
			
			for(int i = 0; i < playlists.size(); i++) {
				if(playlists.get(i).getPlaylistName().equals(name)) {
					playlists.remove(i);
					user.setPlaylists(playlists);
					break;
				}
			}
			
			selectedUser =user;
			populatePlaylists(selectedUser);
		}
		
	}
	
	/**
	 * Load all songs to user's homepage
	 */
	public void loadAllSongs () {
		try {
			Gson gson = new Gson();
			mySongs = gson.fromJson(new FileReader("music.json"), new TypeToken<List<Song>>(){}.getType());
			master = new Playlist();
			master.setPlaylistName("All");
			master.setSongs(mySongs);
			allSongsView.setItems(FXCollections.observableList(master.getSongs()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadPlaylistSongs(ArrayList<Playlist> p) {
		playlistView.setItems(FXCollections.observableList(p));
	}
	
	public void AddActionToSongsView() {
		allSongsView.setOnMouseClicked(event -> {
			if(playlistView.getSelectionModel() != null && playlistView.getSelectionModel().getSelectedIndex() != 0) {
				if(event.getClickCount() == 1) {
					if(removeSongButton.isVisible()) {
						removeSongButton.setOnAction((buttonPressed) -> {
							System.out.println("Song to be removed: " + allSongsView.getSelectionModel().getSelectedItem());
						});
					}
				}
			}
		});
		
	}

}