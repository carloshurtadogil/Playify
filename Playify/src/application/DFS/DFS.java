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
import application.Models.SongResponse;
import application.Models.User;
import application.Models.UserResponse;

@SuppressWarnings("unused")
public class DFS {

	public class FilesJson {
		@SerializedName("files")
		@Expose
		List<FileJson> files;

		/**
		 * <p>
		 * Default Constructor
		 * </p>
		 */
		public FilesJson() {

		}

		/**
		 * <p>
		 * Set the current instance of the files ArrayList with a new instance
		 * </p>
		 * 
		 * @param files
		 *            Files to be added
		 */
		public void setFiles(ArrayList<FileJson> files) {
			this.files = files;
		}

		/**
		 * <p>
		 * Retrieve the current list of files
		 * </p>
		 * 
		 * @return The current list of files
		 */
		public List<FileJson> getFiles() {
			return files;
		}

		@Override
		public String toString() {
			String str = "";
			for (FileJson j : files) {
				str += j.toString();
			}
			return str;
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

		/**
		 * Searches for a user by traversing all users in a single page
		 * 
		 * @return
		 * @throws RemoteException
		 */
		public User searchForUserInPage(String username, DFS dfsInstance) throws RemoteException, IOException, Exception {
			User foundUser = null;
			
			PagesJson pageOfUsers = getPages().get(0);
			
			long guid = pageOfUsers.getGuid();
			System.out.println("GUID is " + guid);
			
			System.out.println(dfsInstance.chord);
			
			ChordMessageInterface peer = dfsInstance.chord.locateSuccessor(guid);
			RemoteInputFileStream content = peer.get(guid);
			
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
			
			List<Song> songsFromSearchResult = new ArrayList<Song>();
			//retrieve the pages of the file and traverse them one by one
			List<PagesJson> pages = this.getPages();
			for(int i=0; i<pages.size(); i++) {
				PagesJson currentPage = pages.get(i);
				long guid = currentPage.getGuid();
				ChordMessageInterface peer = dfsInstance.chord.locateSuccessor(guid);
				RemoteInputFileStream content = peer.get(guid);
				
				content.connect();
				Scanner scan = new Scanner(content);
				scan.useDelimiter("\\A");
				String strSongResponse = "";
				while (scan.hasNext()) {
					strSongResponse += scan.next();
				}
				//retrieve all songs from a single page, and traverse the songs to find the appropriate song
				SongResponse songRepository = new Gson().fromJson(strSongResponse, SongResponse.class);
				for(int j=0; j<songRepository.getSongsInPage().size(); j++) {
					Song currentSong = songRepository.getSongsInPage().get(j);
					if(currentSong.getSongDetails().getTitle().equalsIgnoreCase(searchInput) || 
							currentSong.getArtistDetails().getName().equalsIgnoreCase(searchInput) ||
							currentSong.getArtistDetails().getTerms().equalsIgnoreCase(searchInput)) {
						songsFromSearchResult.add(currentSong);
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

	};

	public class PagesJson {
		@SerializedName("guid")
		@Expose
		Long guid;
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
		Long referenceCount;

		public void setGuid(long guid) {
			this.guid = guid;
		}

		public Long getGuid() {
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

		public void setReferenceCount(Long referenceCount) {
			this.referenceCount = referenceCount;
		}

		public Long getReferenceCount() {
			return referenceCount;
		}

		@Override
		public String toString() {
			String result = "GUID: " + guid + "\n" + "Size: " + size + "\n" + "Creation TimeStamp: " + creationTimeStamp
					+ "\n" + "Read Time: " + readTimeStamp + "\n" + "Write Time: " + writeTimeStamp + "\n"
					+ "Reference Count: " + referenceCount + "\n";
			return result;
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
		System.out.println("Called DFS Constructor");
		this.port = port;
		System.out.println("Calling GUID");
		long guid = md5("" + port);
		System.out.println("Generated GUID: " + guid);
		chord = new Chord(port, guid);
		System.out.println("Chord Created");
		Files.createDirectories(Paths.get(guid + "/repository"));
		Files.createDirectories(Paths.get(guid + "/tmp"));
		System.out.println("File Directories created");
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
			System.out.println("GUID From ReadMetadata: " + guid);
			ChordMessageInterface peer = chord.locateSuccessor(guid);
			RemoteInputFileStream metadataraw = peer.get(guid);
			metadataraw.connect();
			Scanner scan = new Scanner(metadataraw);
			scan.useDelimiter("\\A");
			String strMetaData = "";
			while (scan.hasNext()) {
				strMetaData += scan.next();
			}
			filesJson = gson.fromJson(strMetaData,
					FilesJson.class);
			System.out.println("Carlos's Json:\n" + filesJson.toString());
			System.out.println("Carlos");
			scan.close();
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

		// Retrieve the current metadata data structure
		FilesJson retrievedMetadata = this.readMetaData();
		// traverse all files until the particular file is found
		for (FileJson file : retrievedMetadata.getFiles()) {
			// change the name of the file
			if (file.getName().equals(oldName)) {
				Date currentDate = new Date();
				DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss a");
				String formattedReadTS = dateFormat.format(currentDate);
				file.setName(newName);
				file.setReadTimeStamp(formattedReadTS);
				file.setWriteTimeStamp(formattedReadTS);
				// Write to the metadata data structure
				this.writeMetaData(retrievedMetadata);
				break;
			}
		}
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
		// traverse the current files in the metadata
		for (int i = 0; i < allFiles.getFiles().size(); i++) {
			FileJson currentFile = allFiles.getFiles().get(i);
			System.out.println(currentFile.getName() + " " + currentFile.getSize());
			// traverse the pages in the current file
			for (int j = 0; j < currentFile.getPages().size(); j++) {
				PagesJson currentPage = currentFile.getPages().get(j);
				// System.out.println(currentPage.getGuid() + " " + currentPage.getSize());
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
		FileJson newFile = new FileJson();
		FilesJson retrievedMetadata = this.readMetaData();
		retrievedMetadata.getFiles().add(newFile);
		this.writeMetaData(retrievedMetadata);
	}

	/**
	 * delete file
	 *
	 * @param filename
	 *            Name of the file
	 */
	public void delete(String fileName) throws Exception {
		// Retrieve the current metadata data structure
		FilesJson retrievedMetadata = this.readMetaData();

		boolean fileFound = false;
		int index = 0;
		// traverse all the files in the metadata to see if the desired file is in the
		// list of files
		for (int i = 0; i < retrievedMetadata.getFiles().size(); i++) {
			if (retrievedMetadata.getFiles().get(i).equals(fileName)) {
				fileFound = true;
			}
			index++;
		}
		// if file has been found, then remove it from the list of files
		if (fileFound == true) {
			FileJson selectedFile = retrievedMetadata.getFiles().get(index);
			for (PagesJson page : selectedFile.getPages()) {
				long guid = page.getGuid();
				ChordMessageInterface peer = chord.locateSuccessor(guid);
				peer.delete(guid);
			}
			retrievedMetadata.getFiles().remove(index);
		}
		// update the meta data
		this.writeMetaData(retrievedMetadata);
	}

	
	// * Deletes a component from a page in DFS, either deletes a song from a playlist
	// * or simply deletes a playlist from a list of playlists
	// * 
	// * @param fileName
	// * @param component
	// * @throws Exception 
	
//	public boolean deleteComponent(String fileName, String[] component) throws Exception {
//		FilesJson retrievedMetadata = this.readMetaData();
//		int index = 0;
//		// traverse all the files in the metadata to see if the desired file is in the
//		// list of files
//		for (int i = 0; i < retrievedMetadata.getFiles().size(); i++) {
//			if (retrievedMetadata.getFiles().get(i).equals(fileName)) {
//				FileJson foundFileJson = retrievedMetadata.getFiles().get(i);
//				// indicates that a playlist must be deleted from the user's list of playlists
//				if (component.length == 3) {
//					String username = component[1];
//					String playlistName = component[2];
//					PagesJson pageofUsers = foundFileJson.getPages().get(0);
//					
//					long guid = pageofUsers.getGuid();
//					ChordMessageInterface peer = chord.locateSuccessor(guid);
//					RemoteInputFileStream data = peer.get(guid);
//					
//					byte[] dataContext = data.buf;
//					
//					String chordUsersFileInString = new String(dataContext, 0, dataContext.length);
//					
//					UserResponse userRepo = new Gson().fromJson(chordUsersFileInString, UserResponse.class);
//					for(int j=0; j<userRepo.getUsersList().size();j++) {
//						User currentUser = userRepo.getUsersList().get(j);
//						if(currentUser.getUsername().equals(username)) {
//							currentUser.removePlaylist(playlistName);
//							userRepo.getUsersList().set(j, currentUser);
//							
//							String userRepositoryInJsonFormat = new Gson().toJson(userRepo);
//							peer.put(guid, userRepositoryInJsonFormat);
//							this.writeMetaData(retrievedMetadata);
//						}
//					}
//					
//				}
//				// indicates that a song must be deleted from a playlist
//				else if (component.length == 4) {
//					String username = component[1];
//					String playlistName = component[2];
//					String songName = component[3];		
//					
//					
//					PagesJson pageofUsers = foundFileJson.getPages().get(0);
//					
//					long guid = pageofUsers.getGuid();
//					ChordMessageInterface peer = chord.locateSuccessor(guid);
//					RemoteInputFileStream data = peer.get(guid);
//					
//					byte[] dataContext = data.buf;
//					
//					String chordUsersFileInString = new String(dataContext, 0, dataContext.length);
//					
//					UserResponse allUsers = new Gson().fromJson(chordUsersFileInString, UserResponse.class);
//					User foundUser = allUsers.findUser(username);
//					Playlist foundPlaylist = foundUser.getSpecificPlaylist(playlistName);
//					
//					for(int j=0; j<foundPlaylist.getSongs().size();j++) {
//						if(foundPlaylist.getSongs().get(j).getSongDetails().getTitle().equals(songName)) {
//							
//						}
//					}
//					
//					
//				}
//			}
//		}
//
//	}

	

	public FileInputStream read(String fileName, int pageNumber) throws Exception {
		return null;
	}

	/**
	 * Add a component to a page
	 *
	 * @param filename
	 *            Name of the file
	 * @param data
	 *            RemoteInputStream.
	 */
	public boolean appendComponent(String[] components) throws Exception {
		boolean found = false;
		FilesJson metadata = this.readMetaData();

		// Append a user to a page of users in the chordusers.json file
		if (components.length == 2) {
			String fileName = components[0];
			String userInJsonFormat = components[1];

			Date currentDate = new Date();
			DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss a");
			String formattedReadTS = dateFormat.format(currentDate);
			
			FileJson chordUsersFile = metadata.getFiles().get(0);
			chordUsersFile.setReadTimeStamp(formattedReadTS);
			PagesJson pageofUsers = chordUsersFile.getPages().get(0);
			pageofUsers.setReadTimeStamp(formattedReadTS);

			long guid = pageofUsers.getGuid();
			ChordMessageInterface peer = chord.locateSuccessor(guid);
			RemoteInputFileStream content = peer.get(guid);

			content.connect();
			Scanner scan = new Scanner(content);
			scan.useDelimiter("\\A");
			String strUserResponse = "";
			while (scan.hasNext()) {
				strUserResponse += scan.next();
			}
			UserResponse userRepository = new Gson().fromJson(strUserResponse, UserResponse.class);

			User registeredUser = new Gson().fromJson(userInJsonFormat, User.class);

			userRepository.getUsersList().add(registeredUser);
			String userRepositoryInJson = new Gson().toJson(userRepository);

			peer.put(guid, userRepositoryInJson);

			this.writeMetaData(metadata);
			return true;

		}
		// Append a newly created playlist to an existing user's list of playlists
		else if (components.length == 3) {
			String fileName = components[0];
			String username = components[1];
			String playlistInJsonFormat = components[2];

			FileJson chordUsersFile = metadata.getFiles().get(0);
			PagesJson pageofUsers = chordUsersFile.getPages().get(0);

			long guid = pageofUsers.getGuid();
			ChordMessageInterface peer = chord.locateSuccessor(guid);

			RemoteInputFileStream data = peer.get(guid);

			byte[] content = data.buf;

			String chordUsersFileInString = new String(content, 0, content.length);

			UserResponse userRepo = new Gson().fromJson(chordUsersFileInString, UserResponse.class);
			Playlist playlistToBeAdded = new Gson().fromJson(playlistInJsonFormat, Playlist.class);

			int index = 0;
			User currentUser = null;
			for (int i = 0; i < userRepo.getUsersList().size(); i++) {
				currentUser = userRepo.getUsersList().get(i);
				if (currentUser.getUsername().equals(username)) {
					currentUser.getPlaylists().add(playlistToBeAdded);
					index++;
					break;
				}
			}

			if (currentUser != null) {
				userRepo.getUsersList().set(index, currentUser);
				String userRepositoryInJson = new Gson().toJson(userRepo);
				return true;
			}

			return false;

		}
		return false;
	}

}
