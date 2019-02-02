package application;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class PlaylistController {
	
	@FXML
	private Label tempLabel;
	@FXML
	private ListView<Song> songsView;
	private User selectedUser;
	private Playlist selectedPlaylist;

	//Constructor
	public PlaylistController() {
		
	}
	
	public void setUserAndPlaylist(User theUser, Playlist thePlaylist) {
		this.selectedUser = theUser;
		this.selectedPlaylist = thePlaylist;

		this.populateSongs();
	}
	
	//This method is called to populate the songs of the user's playlist into the ListView
	public void populateSongs() {
		
		songsView.setItems(FXCollections.observableList(selectedPlaylist.getSongs()));
	}
}
