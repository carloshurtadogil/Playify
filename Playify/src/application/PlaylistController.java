package application;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class PlaylistController {
	
	@FXML
	private Label tempLabel;
	@FXML
	private ListView<Song> songsView;
	
	@FXML
	private Button removeSongButton;
	
	@FXML
	private Button playSongButton;
	
	@SuppressWarnings("unused")
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
						System.out.println("The following song will be played");
					});			
				}
			});
		}
	}
	
	//Removes a song from the user's playlist
	public void removeSongFromPlaylist(Song selectedSong) {
		
		//Utilized for retrieving at which the playlist is stored in the user's list of playlists
		int index = 0;
		
		List<Song> songsInPlaylist = selectedPlaylist.getSongs();
		//Traverse the current playlist and find the appropriate song to delete
		for(int i=0; i<songsInPlaylist.size(); i++) {
			//If found, remove the song from the playlist
			if(songsInPlaylist.get(i).getSongName().equals(selectedSong.getSongName())) {	
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
}
