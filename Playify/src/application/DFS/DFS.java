package application.DFS;

import java.rmi.*;
import java.net.*;
import java.util.*;
import org.json.simple.JSONObject;
import java.io.*;
import java.nio.file.*;
import java.math.BigInteger;
import java.security.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import application.Models.Playlist;
import application.Models.Song;

public class DFS {

	public class FilesJson {
		@SerializedName("files")
		@Expose
		List<FileJson> files;

		public FilesJson() {
			
		}

		public void setFiles(ArrayList<FileJson> files) {
			this.files = files;
		}

		public List<FileJson> getFiles() {
			return files;
		}
	};
	
	
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

	};



	public class PagesJson {
		@SerializedName("guid")
		@Expose
		Guid guid;
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
		@SerializedName("playlists")
		@Expose
		List<Playlist> playlists;
		@SerializedName("username")
		@Expose
		String username;
		@SerializedName("password")
		@Expose
		String password;

		public PagesJson() {

		}

		public void setGuid(Guid guid) {
			this.guid = guid;
		}

		public Guid getGuid() {
			return guid;
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
		
		public void setPlaylists(List<Playlist> playlists) {
			this.playlists = playlists;
		}
		public List<Playlist> getPlaylists(){
			return playlists;
		}
		
		public void setUsername(String username) {
			this.username = username;
		}
		public String getUsername() {
			return username;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getPassword() {
			return password;
		}

	};

	public class Guid{
		@SerializedName("release")
		@Expose
		String release;
		@SerializedName("artist")
		@Expose
		String artist;
		@SerializedName("genre")
		@Expose
		String genre;
		@SerializedName("title")
		@Expose
		String title;
		@SerializedName("songID")
		@Expose
		String songID;
		
		
		public void setRelease(String releaseID) {
			this.release = releaseID;
		}
		public String getRelease() {
			return release;
		}
		
		public void setArtist(String artist) {
			this.artist = artist;
		}
		public String getArtist() {
			return artist;
		}
		public void setGenre(String genre) {
			this.genre = genre;
		}
		public String getGenre() {
			return genre;
		}
		public void setSongId(String songID) {
			this.songID = songID;
		}
		public String getSongId() {
			return songID;
		}
		public void setSongTitle(String title) {
			this.title = title;
		}
		public String getSongTitle() {
			return title;
		}
		
	}
	
	
	
	
	

	int port;
	Chord chord;

	private long md5(String objectName) {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(objectName.getBytes());
			BigInteger bigInt = new BigInteger(1, m.digest());
			return Math.abs(bigInt.longValue());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();

		}
		return 0;
	}

	public DFS(int port) throws Exception {

		this.port = port;
		long guid = md5("" + port);
		chord = new Chord(port, guid);
		Files.createDirectories(Paths.get(guid + "/repository"));
		Files.createDirectories(Paths.get(guid + "/tmp"));
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				chord.leave();
			}
		});

	}

	/**
	 * Join the chord
	 *
	 */
	public void join(String Ip, int port) throws Exception {
		chord.joinRing(Ip, port);
		chord.print();
	}

	/**
	 * leave the chord
	 *
	 */
	public void leave() throws Exception {
		chord.leave();
	}

	/**
	 * print the status of the peer in the chord
	 *
	 */
	public void print() throws Exception {
		chord.print();
	}

	/**
	 * readMetaData read the metadata from the chord
	 *
	 */
	public FilesJson readMetaData() throws Exception {
		FilesJson filesJson = null;
		try {
			Gson gson = new Gson();
			long guid = md5("Metadata");
			ChordMessageInterface peer = chord.locateSuccessor(guid);
			RemoteInputFileStream metadataraw = peer.get(guid);
			metadataraw.connect();
			Scanner scan = new Scanner(metadataraw);
			scan.useDelimiter("\\A");
			String strMetaData = scan.next();
			System.out.println(strMetaData);
			filesJson = gson.fromJson(strMetaData, FilesJson.class);
		} catch (NoSuchElementException ex) {
			filesJson = new FilesJson();
		}
		return filesJson;
	}

	/**
	 * writeMetaData write the metadata back to the chord
	 *
	 */
	public void writeMetaData(FilesJson filesJson) throws Exception {
		long guid = md5("Metadata");
		ChordMessageInterface peer = chord.locateSuccessor(guid);

		Gson gson = new Gson();
		peer.put(guid, gson.toJson(filesJson));
	}

	/**
	 * Change Name
	 *
	 */
	public void move(String oldName, String newName) throws Exception {

		//Retrieve the current metadata data structure
		FilesJson retrievedMetadata = this.readMetaData();
		//traverse all files until the particular file is found
		for(FileJson file : retrievedMetadata.getFiles()) {
			//change the name of the file
			if(file.getName().equals(oldName)) {
				Date currentDate =  new Date();
				DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss a");
				String formattedReadTS=dateFormat.format(currentDate);
				file.setName(newName);
				file.setReadTimeStamp(formattedReadTS);
				file.setWriteTimeStamp(formattedReadTS);
				break;
			}
		}

		// Write to the metadata data structure
		this.writeMetaData(retrievedMetadata);
	}

	/**
	 * List the files in the system
	 *
	 * @param filename
	 *            Name of the file
	 */
	public String lists() throws Exception {
		Gson gson = new Gson();
		
		FilesJson allFiles = gson.fromJson("", FilesJson.class);
		//traverse the current files in the metadata
		for(int i=0;i< allFiles.getFiles().size(); i++) {
			FileJson currentFile = allFiles.getFiles().get(i);
			System.out.println(currentFile.getName() + " " + currentFile.getSize());
			//traverse the pages in the current file
			for(int j=0; j< currentFile.getPages().size(); j++) {
				PagesJson currentPage = currentFile.getPages().get(j);
				System.out.println(currentPage.getGuid() + " " + currentPage.getSize());
			}
		}
		String listOfFiles = "";

		return listOfFiles;
	}

	/**
	 * create an empty file
	 *
	 * @param filename
	 *            Name of the file
	 */
	public void create(String fileName) throws Exception {
		// TODO: Create the file fileName by adding a new entry to the Metadata
		// Write Metadata

	}

	/**
	 * delete file
	 *
	 * @param filename
	 *            Name of the file
	 */
	public void delete(String fileName) throws Exception {
		//Retrieve the current metadata data structure
		FilesJson retrievedMetadata = this.readMetaData();
		
		boolean fileFound = false;
		int index=0;
		//traverse all the files in the metadata to see if the desired file is in the list of files
		for(int i=0; i<retrievedMetadata.getFiles().size(); i++) {
			if(retrievedMetadata.getFiles().get(i).equals(fileName)) {
				fileFound = true;
			}
			index++;
		}
		//if file has been found, then remove it from the list of files
		if(fileFound ==true) {
			retrievedMetadata.getFiles().remove(index);
		}
		//update the meta data
		this.writeMetaData(retrievedMetadata);
	}
	
	
	/**
	 * Deletes a component from a page in DFS
	 * @param fileName
	 * @param component
	 * @throws Exception 
	 */
	public void deleteComponent(String fileName, String[] component) throws Exception {
		FilesJson retrievedMetadata = this.readMetaData();
		int index=0;
		//traverse all the files in the metadata to see if the desired file is in the list of files
		for(int i=0; i<retrievedMetadata.getFiles().size(); i++) {
			if(retrievedMetadata.getFiles().get(i).equals(fileName)) {
				FileJson foundFileJson = retrievedMetadata.getFiles().get(i);
				//indicates that a song must be deleted from a particular playlist
				if(component.length==2) {
					String playlistName = component[1];
					for(int j=0;j<foundFileJson.getPages().size(); j++) {
						PagesJson currentPage = foundFileJson.getPages().get(j);
						for(int k=0; k<currentPage.getPlaylists().size();k++) {
							Playlist currentPlaylist = currentPage.getPlaylists().get(k);
							if(currentPlaylist.getPlaylistName().equals(playlistName));
								currentPage.getPlaylists().remove(k);
								currentPage.setPlaylists(currentPage.getPlaylists());
								this.writeMetaData(retrievedMetadata);
								return;
						}
					}
				}
				//indicates that a song must be deleted from a playlist
				else if(component.length==3) {
					String playlistName= component[1];
					String songName = component[2];
					//traverse all pages in a FileJson
					for(int j=0; j<foundFileJson.getPages().size();j++) {
						PagesJson currentPage = foundFileJson.getPages().get(j);
						//traverse all playlists in a current page
						for(int k=0; k<currentPage.getPlaylists().size();k++) {
							Playlist currentPlaylist = currentPage.getPlaylists().get(k);
							if(currentPlaylist.getPlaylistName().equals(playlistName))
							{
								List<Song> songsForPlaylist = currentPlaylist.getSongs();
								for(int l=0; l<songsForPlaylist.size(); l++) {
									if(songsForPlaylist.get(index).getSongDetails().getTitle().equals(songName)) {
										songsForPlaylist.remove(index);
										currentPlaylist.setSongs(songsForPlaylist);
										currentPage.getPlaylists().set(k, currentPlaylist);
										this.writeMetaData(retrievedMetadata);
										return;
									}
								}
							}
						}
					}
				}	
			}
		}
	}
		
	
	
	

	/**
	 * Read block pageNumber of fileName
	 *
	 * @param filename
	 *            Name of the file
	 * @param pageNumber
	 *            number of block.
	 */
	public RemoteInputFileStream read(String fileName, int pageNumber) throws Exception {
		return null;
	}

	/**
	 * Add a page to the file
	 *
	 * @param filename
	 *            Name of the file
	 * @param data
	 *            RemoteInputStream.
	 */
	public void append(String filename, RemoteInputFileStream data) throws Exception {
		boolean found = false;
		FilesJson metadata = this.readMetaData();
		
		int index =0;
		for(int i=0;i<metadata.getFiles().size();i++)
		{
			if(filename == metadata.getFiles().get(i).getName())
			{
				//
				index = i;
				found = true;
				FileJson foundfileJson =metadata.getFiles().get(i);
				
				byte[] pageContent = data.buf;
				
				String pageContentInString = new String(pageContent, 0, pageContent.length);
				
				
				PagesJson page = new Gson().fromJson(pageContentInString, PagesJson.class);
				
				foundfileJson.getPages().add(page);
				
				metadata.getFiles().set(i, foundfileJson);
				
				this.writeMetaData(metadata);
				
			}
		}
		
	}

}
