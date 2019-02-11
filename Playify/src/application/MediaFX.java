package application;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.sun.jndi.toolkit.url.Uri;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;




public class MediaFX {

	private MediaPlayer mediaplayer; 
	private User currentUser;
	private Playlist currentPlaylist;
	@SuppressWarnings("unused")
	private Song currentSong;
	@FXML
	private Button backButton;
	@FXML
	private Button playSongButton;
	@FXML
	private Button pauseSongButton;
	@FXML
	private Button stopSongButton;
	
	public void setUserAndSong(User user, Playlist p, Song s) {
		currentUser = user;
		currentPlaylist = p;
		currentSong = s;
	}
	
	//Orchestrates play, pause, and stop functionalities of a selected song
	public void songPlayerControls() throws URISyntaxException {
		
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
		
		
		@SuppressWarnings("unused")
		StringBuilder songIdentificationNumber = new StringBuilder();
		
		
		
		
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
		Media song = new Media(getClass().getResource("/Music/" + currentSong.getSongDetails().getSongId() + ".mp3").toURI().toString());
		
		mediaplayer = new MediaPlayer(song);
		mediaplayer.setAutoPlay(true);
		mediaplayer.setVolume(0.1);
		
	}

}
