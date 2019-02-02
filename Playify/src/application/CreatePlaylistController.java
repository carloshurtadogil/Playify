package application;


import java.awt.event.ActionEvent;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
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
	@FXML
	private TextField txtPlaylistName;
	@FXML
	private Button btnSet;
	
	//List to hold songs that match search results
	private List<Song> searchResults = new ArrayList<Song>();
	
	public void Search(ActionEvent event) { 
		try {
			Gson gson = new Gson();
			
			//List to hold song names
			List<String> songNames = new ArrayList<String>();
		
			Playlist myPlaylist = gson.fromJson(new FileReader("music.json"), Playlist.class);
			List<Song> masterSongList = myPlaylist.getSongs();
			
			for(Song x : masterSongList) {
				if(x.getSongName().equals(txtSearch.getText()) || x.getSongType().equals(txtSearch.getText())) {
					searchResults.add(x);
					songNames.add(x.getSongName());
				}
			}
			
			listOfSongs.setItems(FXCollections.observableList(songNames));
			listOfSongs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			
		}catch (Exception e) {
			System.out.println("Search Error");
			e.printStackTrace();
		}
		
	}
	
	public void Add(ActionEvent event) {
		ObservableList<String> selectedSongs;
		selectedSongs = listOfSongs.getSelectionModel().getSelectedItems();
		
		for(String songName: selectedSongs) {
			for(Song song: searchResults) {
				if(songName.equals(song.getSongName())) {
					//Add to playlist
				}
			}
		}
	}
	
	public void SetPlaylistName(ActionEvent event) {
		
	}
}
