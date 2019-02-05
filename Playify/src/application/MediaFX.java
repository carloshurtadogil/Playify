package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


public class MediaFX {

	private MediaPlayer mediaplayer; 
	@FXML
	private Button playSongButton;
	@FXML
	private Button pauseSongButton;
	@FXML
	private Button stopSongButton;
	
	//Orchestrates play, pause, and stop functionalities of a selected song
	public void songPlayerControls() {
		
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
