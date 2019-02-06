package application;

import java.io.IOException;
import java.util.List;

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
import javafx.stage.Stage;

public class PlaylistController {
	
	@FXML
	private Label tempLabel;
	@FXML
	private ListView<Song> songsView;
	
	@FXML
	private Button backButton;
	@FXML
	private Button removeSongButton;
	
	@FXML
	private Button playSongButton;
	
	private User selectedUser;
	private Playlist selectedPlaylist;

	//Constructor
	public PlaylistController() {
		
	}
	
	public void setUserAndPlaylist(User theUser, Playlist thePlaylist) {
		this.selectedUser = theUser;
		this.selectedPlaylist = thePlaylist;
		this.tempLabel.setText(thePlaylist.getPlaylistName());
		this.populateSongs();
	}
	
	//This method is called to populate the songs of the user's playlist into the ListView
	public void populateSongs() {
		//Performs a null check on the playlist in case its empty
		if(selectedPlaylist.getSongs()!=null) {
			
			songsView.setItems(FXCollections.observableList(selectedPlaylist.getSongs()));
			songsView.setOnMouseClicked(event->{
				
				if(event.getClickCount() ==1) {
					//Proceeds to remove song from playlist on button click
					removeSongButton.setOnAction((removeButtonPressed) ->{
						this.removeSongFromPlaylist(songsView.getSelectionModel().getSelectedItem());
						System.out.println("The following song will be removed from the playlist");
					});
					//Proceeds to play song from playlist on button click
					playSongButton.setOnAction((playButtonPressed) ->{
						goToSongPlayer();
						System.out.println("The following song will be played");
					});			
				}
			});
		}
		
		//Back button moved outside if statement, because back button would not function
		//when the playlist is actually empty
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				try {
					// Load the Home.fxml page for that particular user
					FXMLLoader homeLoader = new FXMLLoader();
					homeLoader.setLocation(getClass().getResource("/application/Home.fxml"));
					Parent root = homeLoader.load();
					Scene homeScene = new Scene(root);

					// Obtain the controller to set selected user and playlists
					HomeController homeControl = homeLoader.getController();
					homeControl.setLoggedUser(selectedUser);

					// Load the current stage to prevent from generating a new window/popup
					Stage homeStage = (Stage) backButton.getScene().getWindow();
					homeStage.setScene(homeScene);
					homeStage.show();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (org.json.simple.parser.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	//Removes a song from the user's playlist
	public void removeSongFromPlaylist(Song selectedSong) {
		
		//Utilized for retrieving at which the playlist is stored in the user's list of playlists
		int index = 0;
		
		List<Song> songsInPlaylist = selectedPlaylist.getSongs();
		//Traverse the current playlist and find the appropriate song to delete
		for(int i=0; i<songsInPlaylist.size(); i++) {
			//If found, remove the song from the playlist
			if(songsInPlaylist.get(i).getTitle().equals(selectedSong.getTitle())) {	
				songsInPlaylist.remove(songsInPlaylist.get(i));
				selectedPlaylist.setSongs(songsInPlaylist);
				break;
			}
		}
		
		//Traverse the user's playlists, find the appropriate playlist and replace it with the updated playlist
		for(int j=0;j<selectedUser.getPlaylists().size();j++) {
			if(selectedUser.getPlaylists().get(j).getPlaylistName().equals(selectedPlaylist.getPlaylistName())){
				index = j;
				selectedUser.getPlaylists().set(j, selectedPlaylist);
				selectedUser.setPlaylists(selectedUser.getPlaylists());
			}
		}
		//Repopulate the songs in the current playlist
		this.populateSongs();
	}
	
	//Goes to the song player page
	public void goToSongPlayer() {
		Song selectedSong = songsView.getSelectionModel().getSelectedItem();
		try {
			
			FXMLLoader songPlayerLoader = new FXMLLoader();
			songPlayerLoader.setLocation(getClass().getResource("/application/SongPlayer.fxml"));
			
			Parent songPlayerRoot = songPlayerLoader.load();
			Scene songPlayerScene = new Scene(songPlayerRoot);
			
			MediaFX songPlayer = songPlayerLoader.getController();
			songPlayer.setUserAndPlaylist(selectedUser, selectedPlaylist);
			songPlayer.songPlayerControls();
			
			Stage songPlayerStage = (Stage) tempLabel.getScene().getWindow();
			songPlayerStage.setScene(songPlayerScene);
			songPlayerStage.show();
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
