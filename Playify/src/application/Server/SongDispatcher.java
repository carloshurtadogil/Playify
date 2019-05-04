package application.Server;

/**
* SongDispatcher is the main responsable for obtaining the songs 
*
* @author  Oscar Morales-Ponce
* @version 0.15
* @since   02-11-2019 
*/

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import application.OsUtils;
import application.DFS.DFS;
import application.DFS.DFS.FileJson;
import application.DFS.DFS.FilesJson;
import application.Models.Song;
import java.io.FileNotFoundException;


public class SongDispatcher
{
	public static DFS dfs;
    static final int FRAGMENT_SIZE = 8192; 
    
    public SongDispatcher()
    {
        
    }
    
    /* 
    * getSongChunk: Gets a chunk of a given song
    * @param key: Song ID. Each song has a unique ID 
    * @param fragment: The chunk corresponds to 
    * [fragment * FRAGMENT_SIZE, FRAGMENT_SIZE]
    */
    public String getSongChunk(String key, Long fragment) throws FileNotFoundException, IOException
    {
        byte buf[] = new byte[FRAGMENT_SIZE];
        String fileDir = System.getProperty("user.dir");
        if(OsUtils.isWindows()) {
        	fileDir += ("\\Playify\\src\\Music\\" + key + ".mp3");
        } else if(OsUtils.isMac()) {
        	fileDir += ("/Playify/src/Music/" + key + ".mp3");
        }
        File file = new File(fileDir);
        FileInputStream inputStream = new FileInputStream(file);
        inputStream.skip(fragment * FRAGMENT_SIZE);
        inputStream.read(buf);
        inputStream.close();
        JsonObject obj = new JsonObject();
        obj.addProperty("ret", Base64.getEncoder().encodeToString(buf));
        // Encode in base64 so it can be transmitted 
         return obj.toString();//Base64.getEncoder().encodeToString(buf);
    }
    
    /* 
    * getFileSize: Gets a size of the file
    * @param key: Song ID. Each song has a unique ID 
     */
    
    public String getFileSize(String key) throws FileNotFoundException, IOException{
    	String fileDir = System.getProperty("user.dir");
        if(OsUtils.isWindows()) {
        	fileDir += ("\\Playify\\src\\Music\\" + key + ".mp3");
        } else if(OsUtils.isMac()) {
        	fileDir += ("/Playify/src/Music/" + key + ".mp3");
        }
    	System.out.println(fileDir);
    	File file = new File(fileDir);
    	Long total = file.length();
    	String fileSizeInString = Long.toString(total);
    	JsonObject fileSize = new JsonObject();
    	fileSize.addProperty("fileSize", fileSizeInString);
    	System.out.println("FileSize: " + fileSizeInString);
    	return fileSize.toString();
    }
    
   /**
    * Searches for available songs in music.json
    * @param searchInput
    * @return
    * @throws JsonIOException
    * @throws JsonSyntaxException
    * @throws FileNotFoundException
    */
    public String searchForSongs(String searchInput) throws JsonIOException, JsonSyntaxException, FileNotFoundException, Exception {
    	Date currentDate = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss a");
		String formattedReadTS = dateFormat.format(currentDate);
    	
    	dfs = Dispatcher.dfsInstance;
    	FilesJson allFiles = dfs.readMetaData();
    	
    	FileJson chordSongsFile = allFiles.getFiles().get(2);
    	chordSongsFile.setReadTimeStamp(formattedReadTS);
    	allFiles.getFiles().set(2, chordSongsFile);
    	
    	List<Song> searchResults = chordSongsFile.searchforSongsInPages(searchInput, dfs);

    	//If more than one song has been found, then send the list of songs as a Json string
    	if(searchResults.size() >0) {

    		JsonElement element = new Gson().toJsonTree(searchResults, new TypeToken<List<Song>>() {}.getType());
    		JsonArray songsArray= element.getAsJsonArray();
    		
    		JsonObject searchResultsInJson = new JsonObject();
    		searchResultsInJson.add("searchResults", songsArray);
    		return searchResultsInJson.toString();
    		
    	}
    	//else, return an error message stating that Login has failed
    	JsonObject errorMessage = new JsonObject();
    	errorMessage.addProperty("errorMessage", "No songs match the following criteria");
    	return errorMessage.toString();
    }
}