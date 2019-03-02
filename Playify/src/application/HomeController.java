package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

//import javazoom.jl.player.*;
//import javazoom.jl.decoder.JavaLayerException;

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
	 * This method executes after the HomeController is loaded, to set the user as a
	 * logged in user
	 * 
	 * @param user
	 *            The current user
	 */
	public void setLoggedUser(User user) throws FileNotFoundException, IOException, ParseException {
		this.selectedUser = user;
		temporaryLabel.setText(user.getUsername());
		loadAllSongs();
		populatePlaylists(selectedUser);

	}

	/**
	 * Retrieve the player
	 * 
	 * @return The music player found in this class
	 */
	public PlayifyPlayer getMusicPlayer() {
		return musicPlayer;
	}

	/**
	 * For the transfer of a music player from the previous controller
	 * 
	 * @param p
	 *            The player to be transferred
	 */
	public void setMusicPlayer(PlayifyPlayer p) {
		musicPlayer = p;
	}

	/**
	 * Transfer the volume from the previous controller
	 * 
	 * @param v
	 *            The volume level that the song is playing at
	 */
	public void setVolume(int v) {
		volume = v;
	}

	/**
	 * Populates all of the user's playlists into the ListView of Home.fxml
	 * 
	 * @param someUser
	 *            The current user
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

		for (Playlist p : someUser.getPlaylists()) {
			masterPlaylist.add(p);
		}
		playlistView.setItems(FXCollections.observableList(masterPlaylist));

		allSongsView.setOnMouseClicked(event -> {
			if (event.getClickCount() == 1) {
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
				if (playlistView.getSelectionModel().getSelectedIndex() == 0) {
					allSongsView.setItems(FXCollections.observableList(master.getSongs()));
					removeSongButton.setVisible(false);
				} else {

					String playlistname = playlistView.getSelectionModel().getSelectedItem().getPlaylistName();
					allSongsView.setItems(
							FXCollections.observableList(someUser.getSpecificPlaylist(playlistname).getSongs()));
					removeSongButton.setVisible(true);
				}
				deletePlaylistButton.setOnAction((buttonPressed) -> {
					// Call method that will delete the playlist
					if (playlistView.getSelectionModel().getSelectedIndex() != 0) {
						try {
							this.removePlaylist(selectedUser,
									playlistView.getSelectionModel().getSelectedItem().getPlaylistName());
						} catch (SocketException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("This button will delete this playlist...");
						errorLabel.setVisible(false);
					} else {
						errorLabel.setVisible(true);
					}

				}
						
						
				);
				
				removeSongButton.setOnAction((buttonPressed)-> {
					
					try {
						this.removeSongFromPlaylist(allSongsView.getSelectionModel().getSelectedItem(), playlistView.getSelectionModel().getSelectedItem());
					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			}
		});
	}

	/**
	 * Allows a user to add a playlist to the list of playlists created
	 */
	public void GoToPlaylistPage(ActionEvent event) {

		try {
			// Load the CreatePlaylist.fxml page
			FXMLLoader createPlaylistLoader = new FXMLLoader();
			createPlaylistLoader.setLocation(getClass().getResource("/application/CreatePlaylist.fxml"));
			Parent root = createPlaylistLoader.load();
			Scene createPlaylistScene = new Scene(root);

			// Obtain the controller to set the selected user
			CreatePlaylistController createPlaylistControl = createPlaylistLoader.getController();
			createPlaylistControl.setLoggedUser(selectedUser);

			// Load the current stage to prevent from generating a new window/popup
			Stage createPlaylistStage = (Stage) temporaryLabel.getScene().getWindow();
			createPlaylistStage.setScene(createPlaylistScene);
			createPlaylistStage.show();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("An exception has occurred");
		}
	}
	
	//Prepares the client request for calling the song removal from a playlist method
	public void removeSongFromPlaylist(Song selectedSong, Playlist selectedPlaylist) throws SocketException {
		
		ProxyInterface proxy = new Proxy(new ClientCommunicationModule());
		
		String [] param = new String[2];
		param[0] = selectedUser.getUsername();
		param[1] = new Gson().toJson(selectedPlaylist);
		param[2] = selectedSong.getSongDetails().getSongId();
		
		JsonObject result = proxy.synchExecution("removeSongFromPlaylist", param);
		Playlist playlistFromResult = new Gson().fromJson(result, Playlist.class);
		
		if(playlistFromResult == null) {
			
		}
		else {
			int index = 0;
			for(int i=0;i<selectedUser.getPlaylists().size(); i++) {
				if(selectedUser.getPlaylists().get(i).getPlaylistName().equals(playlistFromResult.getPlaylistName())) {
					selectedUser.getPlaylists().set(index, playlistFromResult);
					break;
				}
				index++;
			}
			populatePlaylists(selectedUser);
			
		}
	}

	/**
	 * Remove a playlist from the user's playlist array
	 * 
	 * @param user
	 *            User who's playlists will be updated
	 * @param name
	 *            The name of the playlist to be removed
	 * @return True if the playlist has been found and subsequently removed, false
	 *         otherwise
	 * @throws SocketException
	 */
	public void removePlaylist(User user, String playlistName) throws SocketException {

		ProxyInterface proxy = new Proxy(new ClientCommunicationModule());

		String[] param = new String[2];
		param[0] = selectedUser.getUsername();
		param[1] = playlistName;
		JsonObject result = proxy.synchExecution("removePlaylist", param);

		selectedUser = user;
		populatePlaylists(selectedUser);

	}

	/**
	 * Load all songs to user's homepage
	 */
	public void loadAllSongs() {
		try {
			Gson gson = new Gson();
			mySongs = gson.fromJson(new FileReader("music.json"), new TypeToken<List<Song>>() {
			}.getType());
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
			if (playlistView.getSelectionModel() != null && playlistView.getSelectionModel().getSelectedIndex() != 0) {
				if (event.getClickCount() == 1) {
					if (removeSongButton.isVisible()) {
						removeSongButton.setOnAction((buttonPressed) -> {
							System.out.println(
									"Song to be removed: " + allSongsView.getSelectionModel().getSelectedItem());
						});
					}
				}
			}
		});

	}

}