package application.DFS;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import application.Models.DateTime;
import application.Models.Song;
import application.Models.SongResponse;
import application.Models.User;
import application.Models.UserResponse;

public class FileJson {
	@SerializedName("name")
	@Expose
	String name;
	@SerializedName("size")
	@Expose
	Long size;
	@SerializedName("createTS")
	@Expose
	String creationTimeStamp;
	@SerializedName("readTS")
	@Expose
	String readTimeStamp;
	@SerializedName("writeTS")
	@Expose
	String writeTimeStamp;
	@SerializedName("referenceCount")
	@Expose
	String referenceCount;
	@SerializedName("pages")
	@Expose
	ArrayList<PagesJson> pages;

	public FileJson() {

	}

	/**
	 * Searches for a user by traversing all users in a single page
	 * 
	 * @return User             the found user or null
	 * @throws RemoteException  
	 */
	public User searchForUserInPage(String username, DFS dfsInstance) throws RemoteException, IOException, Exception {
		User foundUser = null;

		String formattedReadTS = DateTime.retrieveCurrentDate();
    	
		//Retrieve a page from the file, that contains metadata about the users
		PagesJson pageOfUsers = getPages().get(0);
		pageOfUsers.setReadTimeStamp(formattedReadTS);

		long guid = pageOfUsers.getGuid();
		System.out.println("GUID is " + guid);
		
		System.out.println(dfsInstance.chord);
		
		//Use the page guid to obtain the physical file's content that contains actual
		//information about users
		ChordMessageInterface peer = dfsInstance.chord.locateSuccessor(guid);
		RemoteInputFileStream content = peer.get(guid);
		
		//Traverse the content and then save it
		content.connect();
		Scanner scan = new Scanner(content);
		scan.useDelimiter("\\A");
		String strUserResponse = "";
		while (scan.hasNext()) {
			strUserResponse += scan.next();
		}
		scan.close();
		
		// retrieve the list of users on the current page
		UserResponse userRepository = new Gson().fromJson(strUserResponse, UserResponse.class);
		List<User> usersInPage = userRepository.getUsersList();
		for(int i=0; i<usersInPage.size(); i++) {
			if(usersInPage.get(i).getUsername().equals(username)) {
				return usersInPage.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Traverses each page and retrieves its songs, and checks for the appropriate song
	 * @throws RemoteException 
	 */
	public List<Song> searchforSongsInPages(String searchInput, DFS dfsInstance) throws RemoteException, IOException {
		String modifiedSearchInput = searchInput.replaceAll("\\s+", "");
		List<Song> songsFromSearchResult = new ArrayList<Song>();
		//retrieve the pages of the file and traverse them one by one
		List<PagesJson> pages = this.getPages();
		for(int i=0; i<pages.size(); i++) {
			String formattedTimeStamp = DateTime.retrieveCurrentDate();
	    	System.out.println(pages.get(i).getGuid() + " this is the best right now");
			pages.get(i).setReadTimeStamp(formattedTimeStamp);
			long guid = pages.get(i).getGuid();
			ChordMessageInterface peer = dfsInstance.chord.locateSuccessor(guid);
			RemoteInputFileStream content = peer.get(guid);
			
			content.connect();
			Scanner scan = new Scanner(content);
			String strSongResponse = "";
			while (scan.hasNext()) {
				strSongResponse += scan.next();
			}
			//retrieve all songs from a single page, and traverse the songs to find the appropriate song
			SongResponse songRepository = new Gson().fromJson(strSongResponse, SongResponse.class);
			for(int j=0; j<songRepository.getSongsInPage().size(); j++) {
				Song currentSong = songRepository.getSongsInPage().get(j);
				System.out.println(currentSong.getSongDetails().getTitle());
				if(currentSong.getSongDetails().getTitle().equalsIgnoreCase(modifiedSearchInput) || 
						currentSong.getArtistDetails().getName().equalsIgnoreCase(modifiedSearchInput) ||
						currentSong.getArtistDetails().getTerms().equalsIgnoreCase(modifiedSearchInput)) {
					
					currentSong.getSongDetails().setTitle(searchInput);
					songsFromSearchResult.add(currentSong);
					System.out.println("Found");
						return songsFromSearchResult;
				}
			}
		}
		return songsFromSearchResult;
	}

	/**
	 * 
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Long getSize() {
		return size;
	}

	public void setCreationTimeStamp(String creationTS) {
		this.creationTimeStamp = creationTS;
	}

	public String getCreationTimeStap() {
		return creationTimeStamp;
	}

	public void setReadTimeStamp(String readTS) {
		this.readTimeStamp = readTS;
	}

	public String getReadTimeStamp() {
		return readTimeStamp;
	}

	public void setWriteTimeStamp(String writeTS) {
		this.writeTimeStamp = writeTS;
	}

	public String getWriteTimeStamp(String readTS) {
		return writeTimeStamp;
	}

	public void setReferenceCount(String refCount) {
		this.referenceCount = refCount;
	}

	public String getReferenceCount() {
		return referenceCount;
	}

	public void setPages(ArrayList<PagesJson> pages) {
		this.pages = pages;
	}

	public ArrayList<PagesJson> getPages() {
		return this.pages;
	}

	@Override
	public String toString() {
		String result = "Name: " + name + "\n" + "Size: " + size + "\n" + "Creation TimeStamp: " + creationTimeStamp
				+ "\n" + "Read Time: " + readTimeStamp + "\n" + "Write Time: " + writeTimeStamp + "\n"
				+ "Reference Count: " + referenceCount + "\n" + "Pages: {\n";
		for (PagesJson tpages : pages) {
			result += (tpages.toString() + "\n");
		}
		result += "}\n";
		return result;
	}

}