package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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
	
	//List to hold songs that match search results
	private List<Song> searchResults = new ArrayList<Song>();
	//User logged in
	private User selectedUser;
	private Playlist newPlaylist = new Playlist();
	@SuppressWarnings("unused")
	private int volume;
	boolean paused = false;
	
	
	static Thread playSongThread;
	static String currentSongID = "";
	static InputStream currentInputStream = null;
	
	
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
	 * Transfer the volume from the previous controller
	 * @param v The volume level that the song is playing at
	 */
	public void setVolume(int v) {
		volume = v;
	}
	
	/**
	 * Prepares a client request for searching for a song
	 * @param event The button-clicked event that will trigger the code
	 */
	public void Search(ActionEvent event) { 
		try {
			
			if(!txtSearch.getText().isEmpty() || !txtSearch.getText().equals(null)) {

				ProxyInterface proxy= new Proxy(new ClientCommunicationModule());
				String [] param = new String[1];
				param[0] = txtSearch.getText();
				JsonObject result = proxy.synchExecution("searchForSongs", param);
				
				if(result.has("searchResults")) {
					List<Song> searchResults = new Gson().fromJson(result.get("searchResults"), new TypeToken<List<Song>>() {}.getType());
					
					if(searchResults !=null) {
						listOfSongs.setItems(FXCollections.observableList(searchResults));
						listOfSongs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
					}
				}
				else if(result.has("errorMessage")) {
					pLabel.setText("No songs have been found or some other type error has occurred");
				}

			}
			else {
				pLabel.setText("Search field must have a value");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Play a song as long as it is not null
	 * @param event The ActionEvent that is captured the moment that the button is clicked
	 */
	public void Play(ActionEvent event) {
		Song s = listOfSongs.getSelectionModel().getSelectedItem();
		if (s != null) {
			System.out.println("Song ID: " + s.getSongDetails().getSongId());
			PlaySong(s.getSongDetails().getSongId());
		}
	}
	
	public void Stop(ActionEvent event) {
		System.out.println("Stop clicked");
	}
	
	public void Pause(ActionEvent event) {
		System.out.println("Pause clicked");
	}
	
	/**
	 * Adds a collection of songs to the list of selectedsongs
	 * @param event The button-clicked event that will trigger the code
	 */
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
						newSongs.add(song);
					}
				}
				if(mySongs != null) {
					System.out.println("whoops");
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
	
	/**
	 * Remove a song from the list of selected songs
	 * @param event The button-clicked event that will trigger the code
	 */
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
	
	
	/**
	 * Set the Playlistname for the new playlist; this new playlist will then be added
	 * @param event The button-clicked event that will trigger the code
	 */
	public void SetPlaylistName(ActionEvent event) {
		this.newPlaylist.setPlaylistName(txtPlaylistName.getText());
	}
	
	/**
	 * Initiates the client request to create a new playlist
	 * @param event The button-clicked event that will trigger the code
	 * @throws SocketException in the event that the socket could not be connected to
	 */
	@SuppressWarnings("deprecation")
	public void createPlaylist(ActionEvent event) throws SocketException {
		newPlaylist = new Playlist();
		newPlaylist.setPlaylistName(txtPlaylistName.getText());
		
		if(newSongs.size() !=0 && !(newPlaylist.getPlaylistName()).isEmpty()) {
			
			for(int i=0; i<newSongs.size(); i++) {
				newPlaylist.addSong(newSongs.get(i));
			}
			
			
			ProxyInterface proxy = new Proxy(new ClientCommunicationModule());
			String [] param = new String[2];
			param[0]= selectedUser.getUsername();
			param[1] = new Gson().toJson(newPlaylist);
			System.out.println("here it is now : " + param[1]);
			JsonObject result = proxy.synchExecution("createAndAddPlaylist", param);
			
			if(result.has("successMessage")) {
				
				selectedUser.getPlaylists().add(newPlaylist);
				try {
					if(playSongThread!=null) {
						playSongThread.stop();
					}
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
			else if(result.has("errorMessage")) {
				pLabel.setText("Some error has occurred, or playlist name is already used by you");
			}
			
			
		}
		else {
			pLabel.setText("Can't create a new playlist since there are no songs or missing playlistname");
		};
		
		
	}
	
	/**
	 * Return to the home controller for more options
	 * @param event The button clicked event that will trigger the code.
	 */
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
	
	/**
	 * Load all songs from the music.json file to allow the user to select a song
	 */
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
	
	/**
	 * Plays a song or resumes a song that is currently paused
	 * @param id
	 */
	@SuppressWarnings("deprecation")
	public void PlaySong(String id) {
		//checks if the current song id doesn't matches the one song id selected 
		if(!currentSongID.equals(id)) {
			System.out.println("Lets play a different song now " + id + " " + currentSongID);
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
							
							e.printStackTrace();
						} catch (IOException ex) {
					        System.out.println("Error playing the audio file.");
					        ex.printStackTrace();
					        
					    } catch (JavaLayerException e) {
							
							e.printStackTrace();
						} 
					}
				};
				playSongThread.start();
			}
			
			else {
				//then proceed to play the song
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
							
							e.printStackTrace();
						} catch (IOException ex) {
					        System.out.println("Error playing the audio file.");
					        ex.printStackTrace();
					        
					    } catch (JavaLayerException e) {
							
							e.printStackTrace();
						} 
					}
				};
				playSongThread.start();
			}
			
			
		}
		else if(currentSongID.equals(id) && paused == true){
			System.out.println("Lets continue playing another song now");
			paused = false;
			//then proceed to play the song
			playSongThread = new Thread() {
				@SuppressWarnings({ "unused", "hiding" })
				public void run() {
					ProxyInterface proxy;
					try {
						proxy = new Proxy(new ClientCommunicationModule());
						InputStream is = currentInputStream;
						Player mp3player = new Player(is);
						mp3player.play();
					} catch (SocketException e) {
						
						e.printStackTrace();
					} catch (IOException ex) {
				        System.out.println("Error playing the audio file.");
				        ex.printStackTrace();
				        
				    } catch (JavaLayerException e) {
						
						e.printStackTrace();
					} 
				}
			};
			playSongThread.start();
		
		}
		
//		
//		if(playSongThread !=null) {
//			playSongThread.notify();
//		}
//		else {
//
//			 playSongThread = new Thread() {
//				public void run() {
//					ProxyInterface proxy;
//					try {
//						proxy = new Proxy(new ClientCommunicationModule());
//						InputStream is = new CECS327InputStream(id, proxy);
//						Player mp3player = new Player(is);
//						mp3player.play();
//					} catch (SocketException e) {
//						
//						e.printStackTrace();
//					} catch (IOException ex) {
//				        System.out.println("Error playing the audio file.");
//				        ex.printStackTrace();
//				        
//				    } catch (JavaLayerException e) {
//						
//						e.printStackTrace();
//					} 
//				}
//			};
//			playSongThread.start();
//		}
		
		
	}
}
