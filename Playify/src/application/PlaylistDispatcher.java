package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class PlaylistDispatcher {

	//Adds a song to a playlist
	public String addSongsToPlaylist(String username, String playlist, String songs) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		
		Gson theGson = new Gson();
		UserResponse theResponse = theGson.fromJson(new FileReader("users.json"), UserResponse.class);
		User foundUser = new User(null,null);
		
		List<User> usersList = theResponse.getUsersList();
		//traverses the entire list of users, and attempts to find if a particular user exists
		for (User u : usersList) {
			if (u.getUsername().equals(username)) {
				foundUser = u;
			}
		}
		
		List<Song> songsToBeAdded = theGson.fromJson(songs, new TypeToken<List<Song>>(){}.getType());
		
		Playlist temporaryPlaylist = new Playlist();
		temporaryPlaylist.setSongs(songsToBeAdded);
		
		
		return theGson.toJson(temporaryPlaylist, Playlist[].class);
		
	}
	
	//Removes a song 
	public String removeSongFromPlaylist(String username, String playlist, String songName) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		
		Gson theGson = new Gson();
		
		UserResponse theResponse = theGson.fromJson(new FileReader("users.json"), UserResponse.class);
		List<User> ultimateUserList = theResponse.getUsersList();
		
		User foundUser = new User(null,null);
		//traverses the entire list of users, and attempts to find if a particular user exists
		for (User u : ultimateUserList) {
			if (u.getUsername().equals(username)) {
				foundUser = u;
			}
		}
		
		
		//Reads the entire users.json file
		Playlist selectedPlaylist = theGson.fromJson(playlist, Playlist.class);
		List<Song> songsInPlaylist = selectedPlaylist.getSongs();
		for(int i=0; i <songsInPlaylist.size(); i++) {
			if(songsInPlaylist.get(i).getSongDetails().getTitle().equals(songName)) {
				songsInPlaylist.remove(songsInPlaylist.get(i));
			}
		}
		
		int index = 0;
		for(int i=0;i<foundUser.getPlaylists().size();i++) {
			if(foundUser.getPlaylists().get(i).getPlaylistName().equals(selectedPlaylist.getPlaylistName())) {
				index= i;
				foundUser.getPlaylists().set(i, selectedPlaylist);
				foundUser.setPlaylists(foundUser.getPlaylists());
			}
		}

		return theGson.toJson(selectedPlaylist);
	}
	
	
	
}
