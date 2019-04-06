package application.Server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import application.DFS.Chord;
import application.DFS.ChordMessageInterface;
import application.DFS.DFS;
import application.DFS.DFS.FileJson;
import application.DFS.DFS.FilesJson;
import application.DFS.DFS.PagesJson;
import application.DFS.RemoteInputFileStream;
import application.Models.Playlist;
import application.Models.Song;
import application.Models.User;
import application.Models.UserResponse;

public class PlaylistDispatcher {

	public static DFS dfs;
	
	/**
	 * Removes a specific playlist from the user's list of playlists
	 * @return
	 * @throws FileNotFoundException 
	 * @throws JsonIOException 
	 * @throws JsonSyntaxException 
	 */
	public String removePlaylist(String username, String playlistName) throws JsonSyntaxException, JsonIOException, FileNotFoundException, Exception {
		dfs = Dispatcher.dfsInstance;
		Gson gson = new Gson();
		System.out.println();
		FilesJson allFiles = dfs.readMetaData();
    	FileJson chordSongsFile = allFiles.getFiles().get(0);
    	System.out.println(chordSongsFile.toString());
		
		UserDB userDatabase = new UserDB();
		User foundUser = userDatabase.getParticularUser(username);
		Playlist particularPlaylist = foundUser.getSpecificPlaylist(playlistName);
		
		if(particularPlaylist == null) {
			JsonObject errorMessage = new JsonObject();
			errorMessage.addProperty("errorMessage", "");
			return errorMessage.toString();
		}
		else {
			foundUser.getPlaylists().remove(particularPlaylist);
			foundUser.setPlaylists(foundUser.getPlaylists());
			JsonObject successMessage = new JsonObject();
			successMessage.addProperty("successMessage", "");
			return successMessage.toString();
		}

	}
	
	
	
	/**
	 * Remove a specific song from a specific playlist for a specific user
	 * 
	 * @param username
	 *            The user's whose list of playlists is to be updated
	 * @param playlist
	 *            The user's playlist to be updated
	 * @param songName
	 *            The song to be removed from the playlist
	 * @return All the songs left in a playlist after the removal, null if playlist
	 *         does not exist
	 */
	public String removeSongFromPlaylist(String username, String playlist, String songID)
			throws JsonSyntaxException, JsonIOException, FileNotFoundException, Exception {

		UserDB userDatabase = new UserDB();
		User foundUser = userDatabase.getParticularUser(username);

		// Reads the entire users.json file
		String result = null;
		Playlist selectedPlaylist = foundUser.getSpecificPlaylist(playlist);
		if (selectedPlaylist != null) {
			// Search and remove the song in the playlist
			List<Song> songsInPlaylist = selectedPlaylist.getSongs();
			for (int i = 0; i < songsInPlaylist.size(); i++) {
				if (songsInPlaylist.get(i).getSongDetails().getSongId().equals(songID)) {
					System.out.println("Found");
					songsInPlaylist.remove(songsInPlaylist.get(i));
					break;
				}
			}

			// Update the user's playlist
			for (int i = 0; i < foundUser.getPlaylists().size(); i++) {
				if (foundUser.getPlaylists().get(i).getPlaylistName().equals(selectedPlaylist.getPlaylistName())) {
					foundUser.getPlaylists().set(i, selectedPlaylist);
					foundUser.setPlaylists(foundUser.getPlaylists());
					break;
				}
			}
			result = new Gson().toJson(selectedPlaylist);
			return result;
		}
		else {
			
		}

		return result;
	}
	

	/**
	 * Creates and adds a playlist to a user's list of playlists
	 * 
	 * @param username
	 *            The user's whose list of playlists is to be updated
	 * @param playlist
	 *            The user's playlist to be updated
	 * @param songName
	 *            The song to be removed from the playlist
	 * @return All the songs left in a playlist after the removal, null if playlist
	 *         does not exist
	 */
	public String createAndAddPlaylist(String username, String playlist)
			throws JsonSyntaxException, JsonIOException, FileNotFoundException, Exception {

		System.out.println("in the playlist dispatcher now with " + username + " " + playlist);
		
		dfs = Dispatcher.dfsInstance;
		
		Gson gson = new Gson();
		
		long guid = -1;
		
		FilesJson allFiles = dfs.readMetaData();
    	FileJson chordUserFile = allFiles.getFiles().get(0);
    	PagesJson page = chordUserFile.getPages().get(0);
    	guid = page.getGuid();
    	ChordMessageInterface chordobj = dfs.chord.locateSuccessor(guid);
    	RemoteInputFileStream rifs = chordobj.get(guid);
    	
    	rifs.connect();
		Scanner scan = new Scanner(rifs);
		scan.useDelimiter("\\A");
		String strMetaData = "";
		while (scan.hasNext()) {
			strMetaData += scan.next();
		}
		
		UserResponse userResponseJson = gson.fromJson(strMetaData, UserResponse.class);
		scan.close();
    	
    	
		boolean addPlaylist = true;
		
		User retrievedUser = null;
		
		int uindex = -1;
		int i = 0;
		for(User u : userResponseJson.getUsersList()) {
			if(u.getUsername().equals(username)) {
				uindex = i;
				retrievedUser = u;
			}
			i ++;
		}

		Playlist thePlaylist = new Gson().fromJson(playlist, Playlist.class);
		System.out.println(thePlaylist.toString());
		
		// traverse the entire user's list of playlists to determine if the name of the
		// to be added
		// playlist is taken or not
		List<Playlist> songsInPlaylist = retrievedUser.getPlaylists();
		for (Playlist pl : songsInPlaylist) {
			if (pl.getPlaylistName().equals(thePlaylist.getPlaylistName())) {
				addPlaylist = false;
				break;
			}
		}
		//If the prerequisite to add a playlist to the list works, then proceed to generate a success message
		//and update users.json accordingly
		if (addPlaylist) {
			System.out.println("Adding new playlist now....");
			
			if(uindex != -1) {
				userResponseJson.getUsersList().get(uindex).addPlaylist(thePlaylist);
				chordobj.put(guid, gson.toJson(userResponseJson));
				System.out.println();
				JsonObject successMessage = new JsonObject();
				successMessage.addProperty("successMessage", "");
				return successMessage.toString();
			}
			
			
		
		
		} //else, generate an error message
		System.out.println("failing to add new playlist now....");
		// else, return an error message stating that creating a dispatcher has failed
		JsonObject errorMessage = new JsonObject();
		errorMessage.addProperty("errorMessage", "");
		return errorMessage.toString();

	}

}