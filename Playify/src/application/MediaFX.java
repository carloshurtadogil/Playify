package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


public class MediaFX {

	private MediaPlayer mediaplayer; 
	private User currentUser;
	private Playlist currentPlaylist;
	@FXML
	private Button backButton;
	@FXML
	private Button playSongButton;
	@FXML
	private Button pauseSongButton;
	@FXML
	private Button stopSongButton;
	
	public void setUserAndPlaylist(User user, Playlist playlist) {
		currentUser = user;
		currentPlaylist = playlist;
	}
	
	//Orchestrates play, pause, and stop functionalities of a selected song
	public void songPlayerControls() {
		
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					// Load the Playlist.fxml page for that particular user
					FXMLLoader playlistLoader = new FXMLLoader();
					playlistLoader.setLocation(getClass().getResource("/application/Playlist.fxml"));
					Parent root = playlistLoader.load();
					Scene playlistScene = new Scene(root);

					// Obtain the controller to set selected user and playlists
					PlaylistController playlistControl = playlistLoader.getController();
					playlistControl.setUserAndPlaylist(currentUser, currentPlaylist);

					// Load the current stage to prevent from generating a new window/popup
					Stage playlistStage = (Stage) backButton.getScene().getWindow();
					playlistStage.setScene(playlistScene);
					playlistStage.show();
					
					//

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		});
		
		playSongButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				mediaplayer.play();
			}
		});
		
		pauseSongButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				mediaplayer.pause();
			}
		});
		
		stopSongButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				mediaplayer.stop();
			}
		});
		
		
		//Intentionally commented this block of code since we need to fix the path
		//Media musicFile = new Media("C:\\Users\\Marianne\\Downloads\\Playify Music\\Soul");
		
		//mediaplayer = new MediaPlayer(musicFile);
		//mediaplayer.setAutoPlay(true);
		//mediaplayer.setVolume(0.1);
		
	}

}
