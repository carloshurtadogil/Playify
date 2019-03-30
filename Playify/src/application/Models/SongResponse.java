package application.Models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class utilized to retrieve songs from a page's file that contains songs and their information
 * @author mtome
 *
 */
public class SongResponse {
	@SerializedName("songsInPage")
	@Expose
	private List<Song> songsInPage = new ArrayList<Song>();
	
	public void setSongsInPage(List<Song>songsInPage) {
		this.songsInPage = songsInPage;
	}
	
	public List<Song> getSongsInPage(){
		return this.songsInPage;
	}

}
