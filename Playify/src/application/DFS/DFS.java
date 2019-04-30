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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import application.MapReduce.Mapper;
import application.Models.DateTime;
import application.Models.Playlist;
import application.Models.Song;
import application.Models.SongResponse;
import application.Models.User;
import application.Models.UserResponse;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

import application.MapReduce.Mapper;

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
		 * Return the file found at a given index
		 * </p>
		 * @return FileJson object found
		 */
		public FileJson getFileJsonAt(int i) {
			if(i < files.size()) {
				return files.get(i);
			}
			return new FileJson();
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
		
		/**
		 * <p>
		 * Return the size of the files List
		 * </p>
		 * @return List size
		 */
		public int getSize() {
			return files.size();
		}

		@Override
		public String toString() {
			String str = "";
			for (FileJson j : files) {
				str += j.toString();
			}
			return str;
		}
	}

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
		 * @return User the found user or null
		 * @throws RemoteException
		 */
		public User searchForUserInPage(String username, DFS dfsInstance)
				throws RemoteException, IOException, Exception {
			User foundUser = null;

			String formattedReadTS = DateTime.retrieveCurrentDate();

			// Retrieve a page from the file, that contains metadata about the users
			PagesJson pageOfUsers = getPages().get(0);
			pageOfUsers.setReadTimeStamp(formattedReadTS);

			long guid = pageOfUsers.getGuid();
			System.out.println("GUID is " + guid);

			System.out.println(dfsInstance.chord);

			// Use the page guid to obtain the physical file's content that contains actual
			// information about users
			ChordMessageInterface peer = dfsInstance.chord.locateSuccessor(guid);
			RemoteInputFileStream content = peer.get(guid);

			// Traverse the content and then save it
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
			for (int i = 0; i < usersInPage.size(); i++) {
				if (usersInPage.get(i).getUsername().equals(username)) {
					return usersInPage.get(i);
				}
			}
			return null;
		}

		/**
		 * Traverses each page and retrieves its songs, and checks for the appropriate
		 * song
		 * 
		 * @throws RemoteException
		 */
		public List<Song> searchforSongsInPages(String searchInput, DFS dfsInstance)
				throws RemoteException, IOException {
			String modifiedSearchInput = searchInput.replaceAll("\\s+", "");
			List<Song> songsFromSearchResult = new ArrayList<Song>();
			// retrieve the pages of the file and traverse them one by one
			List<PagesJson> pages = this.getPages();
			for (int i = 0; i < pages.size(); i++) {
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
				// retrieve all songs from a single page, and traverse the songs to find the
				// appropriate song
				SongResponse songRepository = new Gson().fromJson(strSongResponse, SongResponse.class);
				for (int j = 0; j < songRepository.getSongsInPage().size(); j++) {
					Song currentSong = songRepository.getSongsInPage().get(j);
					System.out.println(currentSong.getSongDetails().getTitle());
					if (currentSong.getSongDetails().getTitle().equalsIgnoreCase(modifiedSearchInput)
							|| currentSong.getArtistDetails().getName().equalsIgnoreCase(modifiedSearchInput)
							|| currentSong.getArtistDetails().getTerms().equalsIgnoreCase(modifiedSearchInput)) {

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
		String referenceCount;

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

		public void setReferenceCount(String referenceCount) {
			this.referenceCount = referenceCount;
		}

		public String getReferenceCount() {
			return referenceCount;
		}

		@Override
		public String toString() {
			String result = "GUID: " + guid + "\n" + "Size: " + size + "\n" + "Creation TimeStamp: " + creationTimeStamp
					+ "\n" + "Read Time: " + readTimeStamp + "\n" + "Write Time: " + writeTimeStamp + "\n"
					+ "Reference Count: " + referenceCount + "\n";
			return result;
		}
		
		/**
		 * Adds a key value pair to the tree
		 * @param key
		 * @param value
		 */
		public void addKeyValue(String key, String value) {
			if(!tree.containsKey(key)) {
				JsonObject newJsonObject = new JsonObject();
				tree.put(key, newJsonObject);
			}
			else {
				JsonParser parser = new JsonParser();
				JsonObject valueAsJson = parser.parse(value).getAsJsonObject();
				tree.put(key, valueAsJson );
			}
		}

	}

	public TreeMap<String, JsonObject> tree;
	int port;
	public Chord chord;
	HashMap<String, Integer> counter;

	public void setTree(TreeMap<String, JsonObject> tree) {
		this.tree = tree;
	}

	public TreeMap<String, JsonObject> getTree() {
		return tree;
	}

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
		tree = new TreeMap<String, JsonObject>();
		
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
			filesJson = gson.fromJson(strMetaData, FilesJson.class);
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
	 * Changes the name of the file
	 *
	 */
	public void move(String oldName, String newName) throws Exception {

		// Retrieve the current metadata data structure
		FilesJson retrievedMetadata = this.readMetaData();
		FileJson foundFile = null;
		int index = 0;
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
				foundFile = file;

				// Write to the metadata data structure
				this.writeMetaData(retrievedMetadata);
				break;
			}
			index++;
		}
		if (foundFile != null) {
			retrievedMetadata.getFiles().set(index, foundFile);
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
	 * Creates an empty file
	 *
	 * @param filename
	 *            Name of the file
	 */
	public void create(String fileName) throws Exception {

		String formattedTS = DateTime.retrieveCurrentDate();

		// Set the creation, read, write time stamps accordingly
		FileJson newFile = new FileJson();
		newFile.setCreationTimeStamp(formattedTS);
		newFile.setReadTimeStamp(formattedTS);
		newFile.setWriteTimeStamp(formattedTS);

		// Add the file to the metadata
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

	public FileInputStream read(String fileName, int pageNumber) throws Exception {
		return null;
	}

	/**
	 * Add a component to a page (e.g. adding a newly signed up user)
	 *
	 * @param filename
	 *            Name of the file
	 * @param data
	 *            RemoteInputStream.
	 */
	public boolean appendComponent(String[] components) throws Exception {
		boolean found = false;
		FilesJson metadata = this.readMetaData();

		// get the current DateTime
		Date currentDate = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss a");
		String formattedReadTS = dateFormat.format(currentDate);
		// set the read timestamp accordingly on the file and its desired page
		FileJson chordUsersFile = metadata.getFiles().get(0);
		chordUsersFile.setReadTimeStamp(formattedReadTS);
		PagesJson pageofUsers = chordUsersFile.getPages().get(0);
		pageofUsers.setReadTimeStamp(formattedReadTS);

		// retrieve the guid of the page to search for the Chord that contains the
		// actual file
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

		// Append a user to a page of users in the chordusers.json file
		if (components.length == 2) {

			System.out.println("Lets add a user");

			String fileName = components[0];
			String userInJsonFormat = components[1];

			User registeredUser = new Gson().fromJson(userInJsonFormat, User.class);
			userRepository.getUsersList().add(registeredUser);
			String userRepositoryInJson = new Gson().toJson(userRepository);

			chordUsersFile.getPages().set(0, pageofUsers);
			metadata.getFiles().set(0, chordUsersFile);
			peer.put(guid, userRepositoryInJson);
			this.writeMetaData(metadata);
			return true;

		}

		return false;

	}
	
	//Splitting files 
	public void loadingSongsToPages () {
		try {
			
			List<Song> mySongs; 
			List<Song> songNames = new ArrayList<Song> ();
			ListView<Song> listOfSongs = null;
			
			for(int i = 1; i < 101; ++i)
			{
				for (int j = 1; j < 101; ++i)
				{
					Gson gson = new Gson();
					
					
					mySongs = gson.fromJson(new FileReader("music.json"), new TypeToken<List<Song>>(){}.getType());
		
					for(Song s : mySongs) {
						songNames.add(s);
				}
			}
			listOfSongs.setItems(FXCollections.observableList(songNames));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Append a page to a file
	 * 
	 * @param fileName
	 * @param data
	 * @throws Exception
	 */
	public void append(String fileName, RemoteInputFileStream data) throws Exception {
		Gson gson = new Gson();
		FilesJson allFiles = this.readMetaData();
		FileJson foundFile = null;

		int index = 0;
		for (int i = 0; i < allFiles.getFiles().size(); i++) {
			if (allFiles.getFiles().get(i).getName().equals(fileName)) {
				foundFile = allFiles.getFiles().get(i);

				data.connect();
				Scanner scan = new Scanner(data);
				scan.useDelimiter("\\A");
				String strPageData = "";
				while (scan.hasNext()) {
					strPageData += scan.next();
				}

				// get the current DateTime
				Date currentDate = new Date();
				DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss a");
				String formattedTS = dateFormat.format(currentDate);

				PagesJson newPage = gson.fromJson(strPageData, PagesJson.class);
				foundFile.getPages().add(newPage);
				foundFile.setReadTimeStamp(formattedTS);
				foundFile.setWriteTimeStamp(formattedTS);
				allFiles.getFiles().set(i, foundFile);

				ChordMessageInterface peer = chord.locateSuccessor(newPage.getGuid());
				peer.put(newPage.getGuid(), gson.toJson(newPage));

				this.writeMetaData(allFiles);

				break;

			}
			
			
		}
		
	}
	

	/**
	 * <p>
	 * Emits all the pages contained in the file by adding their key value mappings
	 * </p>
	 * @param key
	 * @param value
	 * @param file
	 */
	public void emit(String key, String value, FileJson file) {
		for (int i = 0; i < file.getPages().size(); i++) {
			file.getPages().get(i).addKeyValue(key, value);
		}
	}
	
	
	
	public void createFile(String file, int interval, int size) throws Exception {
		int lower = 0;
		create(file);
		for(int i = 0; i < (size-1); i++) {
			long page = md5(file + i);
			double lowerBoundInterval = (Math.floor(lower/38)) + (lower%38);
			//appendEmptyPage(file, page, lowerBoundInterval);
			lower+=interval;
		}
		
	}
	
	/**
	 */
	public void bulkTree(String file, DFS dfsInstance) throws Exception {
		int size = 0;
		FilesJson filesJson = readMetaData();
		for(int i = 0; i < filesJson.getSize(); i++) {
			if(filesJson.getFileJsonAt(i).getName().equalsIgnoreCase(file)) {
				ArrayList<PagesJson> pagesList = filesJson.getFileJsonAt(i).getPages();
				PagesJson pagesRead = pagesList.get(i);
				long pageGuid = pagesRead.getGuid();
				long page = md5(file + i);
				ChordMessageInterface peer = chord.locateSuccessor(pageGuid);
				//peer.bulk(page);
				
			}
			PagesJson page = new PagesJson();
			dfsInstance.chord.locateSuccessor(page.guid);
		}
	}

	
	/**
	 * Runs the map reduce algorithm on a set of distributed file system
	 * @param fileInput
	 * @param fileOutput
	 * @throws Exception
	 */
	public void runMapReduce(String fileInput, String fileOutput) throws Exception {
		long currGuid = chord.guid;
		int size=0;
		int networkSize = 0;
		//int networkSize = chord.successor.onNetworkSize();
		
		Mapper mapper = new Mapper();
		Mapper reducer = new Mapper();
		if(networkSize >0) {
			double interval = 1936/size;
			//createFile();
			List<PagesJson> pagesInFile = new Gson().fromJson(fileInput, new TypeToken<List<PagesJson>>(){}.getType());
			for(int i=0;i<pagesInFile.size();i++) {
				//pages[fileInput] = ++;
				ChordMessageInterface peer = chord.locateSuccessor(pagesInFile.get(i).getGuid());
				//peer.mapContext(pagesInFile.get(i), mapper, this, fileOutput + ".map");
			}
			bulkTree(fileOutput + ".map", this);
			//createFile(fileOutput, interval, size);
			
			List<PagesJson> pagesFromOutputFile = new Gson().fromJson(fileOutput, new TypeToken<List<PagesJson>>(){}.getType());
			for(int j=0; j<pagesFromOutputFile.size(); j++) {
				//pages[fileOutput]++;
				ChordMessageInterface peer = chord.locateSuccessor(pagesFromOutputFile.get(j).getGuid());
				//peer.reduceContext(pagesFromOutputFile.get(j), reducer, this, fileOutput);
			}
			bulkTree(fileOutput, this);
		}
	}
	
	public void onPageCompleted(String file)
	{	
		int value = counter.get(file);
		value--;
		counter.put(file, value);
	}

}