package application;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.io.FileReader;
import java.util.List;

import com.google.gson.Gson;

import javafx.fxml.FXML;

public class CreatePlaylistController {
	@FXML
	private TextField txtSearch;
	
	public void Search(ActionEvent event) { 
		try {
			Gson gson = new Gson();
			
			Playlist myPlaylist = gson.fromJson(new FileReader("users.json"), Playlist.class);
			List<Song> masterSongList = myPlaylist.getSongs();
			
		}catch (Exception e) {
			System.out.println("Search Error");
			e.printStackTrace();
		}
		
	}
}
