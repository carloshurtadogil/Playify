package application;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
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

import application.Models.Playlist;
import application.Models.Song;
import application.Models.User;
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
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

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
	private int volume;
	
	boolean paused = false;
	static Thread playSongThread;
	static String currentSongID = "";
	static InputStream currentInputStream = null;

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
					if(playSongThread.isAlive()) {
						playSongThread.stop();
					}
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
					PlaySong(id);
				});
				pauseButton.setOnAction((buttonPressed)->{
					try {
						PauseSong();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				
			}
		});
		// Sets a mouse clicked event for each of the Playlists
		playlistView.setOnMouseClicked(event -> {

			// User must click on an item in order to interact with it
			if (event.getClickCount() == 1) {
				
				if (playlistView.getSelectionModel().getSelectedIndex() == 0) {
					allSongsView.setItems(FXCollections.observableList(master.getSongs()));
					removeSongButton.setVisible(false);
				} else {
					//showcase all songs in a playlist if playlist is selected
					if(playlistView.getSelectionModel().getSelectedItem() != null){
						String playlistname = playlistView.getSelectionModel().getSelectedItem().getPlaylistName();
						allSongsView.setItems(
								FXCollections.observableList(someUser.getSpecificPlaylist(playlistname).getSongs()));
						removeSongButton.setVisible(true);
					}
					//indicate this is a null playlist
					else {
						System.out.println("Null Playlist");
					}
				}
				//set a listener for the delete playlist button
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
				
				//adds listener for removing a song 
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
	 * Stop any and all song threads
	 */
	public void Stop() {
		if(playSongThread !=null) {
			if(playSongThread.isAlive() && currentInputStream != null) {
				playSongThread.stop();
				currentSongID = "";
				currentInputStream = null;
			}
			
		}
	}
	
	/**
	 * Allows a user to add a playlist to the list of playlists created
	 * @param event The button-clicked event that will trigger the code
	 */
	public void GoToPlaylistPage(ActionEvent event) {

		try {
			// Load the CreatePlaylist.fxml page
			if(playSongThread!=null) {
				playSongThread.stop();
			}
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
	
	/**
	 * Go to the DFS controller
	 * @param event The button-clicked event that will trigger the code
	 */
	public void GoToDFS(ActionEvent event) {
		System.out.println("Reached1");
		try {
			FXMLLoader dfsLoader = new FXMLLoader();
			dfsLoader.setLocation(getClass().getResource("/application/DFS.fxml"));
			System.out.println("Reached2");
			Parent root = dfsLoader.load();
			System.out.println("Reached3");
			Scene dfsScene = new Scene(root);

			// Obtain the controller to set the selected user
			DFSController dfsControl = dfsLoader.getController();
			dfsControl.setLoggedUser(selectedUser);

			// Load the current stage to prevent from generating a new window/popup
			Stage createPlaylistStage = (Stage) temporaryLabel.getScene().getWindow();
			createPlaylistStage.setScene(dfsScene);
			createPlaylistStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Prepares the client request for calling the song removal from a playlist method
	 * @param selectedSong
	 * @param selectedPlaylist
	 * @throws SocketException
	 */
	public void removeSongFromPlaylist(Song selectedSong, Playlist selectedPlaylist) throws SocketException {
		
		ProxyInterface proxy = new Proxy(new ClientCommunicationModule());
		
		String [] param = new String[3];
		param[0] = selectedUser.getUsername();
		param[1] = selectedPlaylist.getPlaylistName();
		System.out.println(selectedSong.getSongDetails().getSongId());
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

		if(result.has("errorMessage") || result.has("error")) {
			
		}
		else if(result.has("successMessage")) {
			Playlist playlistToDelete = selectedUser.getSpecificPlaylist(playlistName);
			selectedUser.getPlaylists().remove(playlistToDelete);
			populatePlaylists(selectedUser);
		}

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

	/**
	 * Load all the user's playlists to the playlist view
	 * @param p The List of playlists to be added
	 * 
	 */
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
	
	/**
	 * //Pauses the song if a current song thread is executing 
	 * @throws InterruptedException
	 */
	public void PauseSong() throws InterruptedException {
		
		if(playSongThread!=null) {	
			if(playSongThread.isAlive() && currentInputStream !=null) {
				paused = true;
				playSongThread.stop();
			}
		}
	}
	
	/**
	 * Plays a song or resumes a song that is currently paused
	 * @param id
	 */
	public void PlaySong(String id) {
		//checks if the current song id doesn't matches the one song id selected 
		if(!currentSongID.equals(id)) {

			//proceed to update the current song id and stop the song
			//that is currently playing
			if(playSongThread !=null) {
				currentSongID = id;
				playSongThread.stop();
				
				//plays the new song selected
				playSongThread = new Thread() {
					public void run() {
						ProxyInterface proxy;
						try {
							proxy = new Proxy(new ClientCommunicationModule());
							InputStream is = new CECS327InputStream(id, proxy);
							currentInputStream = is;
							Player mp3player = new Player(is);
							mp3player.play();
							
						} catch (SocketException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException ex) {
					        System.out.println("Error playing the audio file.");
					        ex.printStackTrace();
					        
					    } catch (JavaLayerException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
					}
				};
				playSongThread.start();
			}
			//otherwise play the selected song
			else {
				//Create a play song thread to play the song
				playSongThread = new Thread() {
					public void run() {
						ProxyInterface proxy;
						try {
							proxy = new Proxy(new ClientCommunicationModule());
							InputStream is = new CECS327InputStream(id, proxy);
							currentInputStream = is;
							Player mp3player = new Player(is);
							mp3player.play();
						} catch (SocketException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException ex) {
					        System.out.println("Error playing the audio file.");
					        ex.printStackTrace();
					        
					    } catch (JavaLayerException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
					}
				};
				playSongThread.start();
			}
		}
		//else if the current song id matches the id sent, and if the current song is playing
		else if(currentSongID.equals(id) && paused== true){
				System.out.println("Lets continue playing another song now");
				paused = false;
				//then proceed to play the song
				playSongThread = new Thread() {
					public void run() {
						ProxyInterface proxy;
						try {
							proxy = new Proxy(new ClientCommunicationModule());
							InputStream is = currentInputStream;
							Player mp3player = new Player(is);
							mp3player.play();
						} catch (SocketException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException ex) {
					        System.out.println("Error playing the audio file.");
					        ex.printStackTrace();
					        
					    } catch (JavaLayerException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
					}
				};
				playSongThread.start();
			
		}
	}

}