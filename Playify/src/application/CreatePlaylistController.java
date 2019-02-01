package application;


import java.awt.event.ActionEvent;
import java.io.FileReader;
import java.util.List;

import com.google.gson.Gson;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class CreatePlaylistController {
	@FXML
	private TextField txtSearch;
	@FXML
	private Button btnEnter;
	@FXML
	private ListView<String> listOfSongs;
	@FXML
	private Button btnAdd;
	
	public void Search(ActionEvent event) { 
		try {
			Gson gson = new Gson();
			
			//List to hold songs that match search results
			List<Song> searchResults = null;
			
			//List to hold song names
			List<String> songNames = null;
		
			Playlist myPlaylist = gson.fromJson(new FileReader("music.json"), Playlist.class);
			List<Song> masterSongList = myPlaylist.getSongs();
			
			for(Song x : masterSongList) {
				if(x.getSongName().equals(txtSearch.getText()) || x.getSongType().equals(txtSearch.getText())) {
					searchResults.add(x);
					songNames.add(x.getSongName());
				}
			}
			
			listOfSongs.getItems().addAll(songNames);
			
		}catch (Exception e) {
			System.out.println("Search Error");
			e.printStackTrace();
		}
		
	}
}
