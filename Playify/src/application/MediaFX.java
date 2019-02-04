package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


public class MediaFX extends Application {

	MediaPlayer mediaplayer; 
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) {
		
		Button btn_play, btn_pause, btn_stop;
		
		btn_play = new Button("Start");
		btn_pause = new Button("Pause");
		btn_stop = new Button("Stop");
		
		btn_play.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				mediaplayer.play();
			}
		});
		
		btn_pause.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				mediaplayer.pause();
			}
		});
		
		btn_stop.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				mediaplayer.stop();
			}
		});
		
		Media musicFile = new Media("C:\\Users\\Marianne\\Downloads\\Playify Music\\Soul");
		
		mediaplayer = new MediaPlayer(musicFile);
		//mediaplayer.setAutoPlay(true);
		mediaplayer.setVolume(0.1);
		
		VBox root = new VBox();
		root.getChildren().addAll(btn_play, btn_pause, btn_stop);
		
		Scene scene = new Scene(root, 500, 500);
		stage.setScene(scene);
		
		stage.show();
		
	}

}
