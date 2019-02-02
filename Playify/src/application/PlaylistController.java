package application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class PlaylistController {
	
	@FXML
	private Label tempLabel;
	@FXML
	private ListView<Song> songs;
	private User selectedUser;
	private Playlist selectedPlaylist;

	//Constructor
	public PlaylistController() {
		
	}
	
	public void setUserAndPlaylist(User theUser, Playlist thePlaylist) {
		this.selectedUser = theUser;
		this.selectedPlaylist = thePlaylist;

		
		for(int i=0; i<selectedPlaylist.getSongs().size(); i++) {
			System.out.println(selectedPlaylist.getSongs().get(i).getSongName() + " sweet" );
		}
	}
}
