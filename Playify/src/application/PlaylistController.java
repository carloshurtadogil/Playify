package application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PlaylistController {
	
	@FXML
	private Label tempLabel;
	private User selectedUser;
	private Playlist selectedPlaylist;
	
	//Constructor
	public PlaylistController() {
		
	}
	
	public void setUserAndPlaylist(User theUser, Playlist thePlaylist) {
		this.selectedUser = theUser;
		this.selectedPlaylist = thePlaylist;

	}
}
