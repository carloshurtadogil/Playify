package application;

/**
* SongDispatcher is the main responsable for obtaining the songs 
*
* @author  Oscar Morales-Ponce
* @version 0.15
* @since   02-11-2019 
*/

import java.io.IOException;
import java.io.InputStream;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;


public class SongDispatcher
{
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
    public String getSongChunk(Long key, Long fragment) throws FileNotFoundException, IOException
    {
        byte buf[] = new byte[FRAGMENT_SIZE];

        File file = new File("./" + key);
        FileInputStream inputStream = new FileInputStream(file);
        inputStream.skip(fragment * FRAGMENT_SIZE);
        inputStream.read(buf);
        inputStream.close(); 
        // Encode in base64 so it can be transmitted 
         return Base64.getEncoder().encodeToString(buf);
    }
    
    /* 
    * getFileSize: Gets a size of the file
    * @param key: Song ID. Each song has a unique ID 
     */
    public Integer getFileSize(Long key) throws FileNotFoundException, IOException
    {
        File file = new File("./" + key);        
        Integer total =  (int)file.length();
        
        return total;
    }
    
    public String searchForSongs(String searchInput) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
    	
    	List<Song> masterPlaylist = new Gson().fromJson(new FileReader("music.json"), new TypeToken<List<Song>>() {}.getType() );
    	
    	List<Song> searchResults = new ArrayList<Song>();
    	
    	//Traverse all the songs in the master playlist
    	for(int i=0; i<masterPlaylist.size();i++) {
    		//Search by song name, artist name, or genre(terms)
    		//If result is found, then add to search results
    		if(masterPlaylist.get(i).getSongDetails().getTitle().equalsIgnoreCase(searchInput) 
    				|| masterPlaylist.get(i).getArtistDetails().getName().equalsIgnoreCase(searchInput) 
    				|| masterPlaylist.get(i).getArtistDetails().getTerms().equalsIgnoreCase(searchInput)) {
    			searchResults.add(masterPlaylist.get(i));
    		}
    	}
    	
    	//If more than one song has been found, then send the list of songs as a Json string
    	if(searchResults.size() >0) {
    		return new Gson().toJson(searchResults);
        	
    	}
    	//else, return an error message stating that Login has failed
    	JsonObject errorMessage = new JsonObject();
    	errorMessage.addProperty("errorMessage", "Incorrect username or password");
    	return errorMessage.getAsString();
    	
    	
    }
    
    
    
}
