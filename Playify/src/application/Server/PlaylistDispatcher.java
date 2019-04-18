package application.Server;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import application.DFS.ChordMessageInterface;
import application.DFS.DFS;
import application.DFS.FileJson;
import application.DFS.FilesJson;
import application.DFS.PagesJson;
import application.DFS.RemoteInputFileStream;
import application.Models.Playlist;
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
		
		//Read the metadata that is kept in the DFS
		dfs = Dispatcher.dfsInstance;
		Gson gson = new Gson();
		FilesJson allFiles = dfs.readMetaData();
		
		//Retrieve the chord songs file to obtain its first page
    	FileJson chordSongsFile = allFiles.getFiles().get(0);
    	PagesJson pageOfUsers = chordSongsFile.getPages().get(0);
    	long guid = pageOfUsers.getGuid();
    	
    	//Traverse the Chord system to find the actual physical page contents
    	ChordMessageInterface peer = dfs.chord.locateSuccessor(guid);
    	RemoteInputFileStream content = peer.get(guid);
    	content.connect();
		
    	Scanner scan = new Scanner(content);
		scan.useDelimiter("\\A");
		String strUserResponse = "";
		while (scan.hasNext()) {
			strUserResponse += scan.next();
		}
		scan.close();

		UserResponse userResponseJson = gson.fromJson(strUserResponse, UserResponse.class);
		List<User> allUsers = userResponseJson.getUsersList();
		
		//traverse the users list to find the specified user, and then attempt to delete the playlist
		boolean playlistRemoved = false;
		for(int i=0; i< allUsers.size(); i++) {
			User currentUser = allUsers.get(i);
			if(currentUser.getUsername().equals(username)) {
				for(int j=0; j<currentUser.getPlaylists().size(); j++) {
					if(currentUser.getPlaylists().get(j).getPlaylistName().equals(playlistName)) {
						currentUser.getPlaylists().remove(j);
						currentUser.setPlaylists(currentUser.getPlaylists());
						allUsers.set(i, currentUser);
						userResponseJson.setUsersList(allUsers);
						playlistRemoved = true;
						break;
					}
				}
			}
		}
		
    	//update the page in the Chord system if the playlist has been found
		if(playlistRemoved) {
			peer.put(guid, gson.toJson(userResponseJson));
			JsonObject successMessage = new JsonObject();
			successMessage.addProperty("successMessage", "");
			return successMessage.toString();
		}
		else {
			JsonObject errorMessage = new JsonObject();
			errorMessage.addProperty("errorMessage", "");
			return errorMessage.toString();
		}

	}
	
	
	
	/**
	 * <p>
	 * Remove a specific song from a specific playlist for a specific user
	 * </p>
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

		dfs = Dispatcher.dfsInstance;
		
		Gson gson = new Gson();
		
		long guid = -1;
		
		Date currentDate = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss a");
		String formattedReadTS = dateFormat.format(currentDate);
		
		FilesJson allFiles = dfs.readMetaData();
    	FileJson chordUserFile = allFiles.getFiles().get(0);
    	chordUserFile.setReadTimeStamp(formattedReadTS);
    	PagesJson page = chordUserFile.getPages().get(0);
    	guid = page.getGuid();
    	ChordMessageInterface chordobj = dfs.chord.locateSuccessor(guid);
    	RemoteInputFileStream rifs = chordobj.get(guid);
    	
    	rifs.connect();
		Scanner scan = new Scanner(rifs);
		scan.useDelimiter("\\A");
		String strUserResponse = "";
		while (scan.hasNext()) {
			strUserResponse += scan.next();
		}
		
		UserResponse userResponseJson = gson.fromJson(strUserResponse, UserResponse.class);
		scan.close();
		
		User foundUser = null;
		int index = 0;
		int uindex = -1;
		for(User u: userResponseJson.getUsersList()) {
			if(u.getUsername().equals(username)) {
				uindex = index;
				foundUser = u;
				System.out.println("User PD Found: " + u.getUsername());
			}
			index++;
		}

		// Reads the entire users.json file
		String result = null;
		Playlist selectedPlaylist = foundUser.getSpecificPlaylist(playlist);
		if (selectedPlaylist != null) {
			System.out.println("PlaylistDispatcher.removeSongFromPlaylist() Good, Not Null");
			

			if(uindex != -1) {//Update the file
				userResponseJson.getUsersList().get(uindex).removeSong(playlist, songID);//Remove the song if possible
				chordobj.put(guid, gson.toJson(userResponseJson));
				System.out.println();
				JsonObject successMessage = new JsonObject();
				successMessage.addProperty("successMessage", "");
				
				return new Gson().toJson(userResponseJson.getUsersList().get(uindex).getSpecificPlaylist(playlist));
			}
		}
		else {
			System.out.println("PlaylistDispatcher.removeSongFromPlaylist() Failed");
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
		
		Date currentDate = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss a");
		String formattedReadTS = dateFormat.format(currentDate);
		
		FilesJson allFiles = dfs.readMetaData();
    	FileJson chordUserFile = allFiles.getFiles().get(0);
    	chordUserFile.setReadTimeStamp(formattedReadTS);
    	PagesJson page = chordUserFile.getPages().get(0);
    	guid = page.getGuid();
    	ChordMessageInterface chordobj = dfs.chord.locateSuccessor(guid);
    	RemoteInputFileStream rifs = chordobj.get(guid);
    	
    	rifs.connect();
		Scanner scan = new Scanner(rifs);
		scan.useDelimiter("\\A");
		String strUserResponse = "";
		while (scan.hasNext()) {
			strUserResponse += scan.next();
		}
		
		UserResponse userResponseJson = gson.fromJson(strUserResponse, UserResponse.class);
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
				dfs.writeMetaData(allFiles);
				System.out.println();
				JsonObject successMessage = new JsonObject();
				successMessage.addProperty("successMessage", "");
				return successMessage.toString();
			} 
		}
		//else, generate an error message
		System.out.println("failing to add new playlist now....");
		// else, return an error message stating that creating a dispatcher has failed
		JsonObject errorMessage = new JsonObject();
		errorMessage.addProperty("errorMessage", "");
		return errorMessage.toString();

	}

}