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
		 * 
		 * @return FileJson object found
		 */
		public FileJson getFileJsonAt(int i) {
			if (i < files.size()) {
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
		 * 
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

			List<Song> songsFromSearchResult = new ArrayList<Song>();
			// retrieve the pages of the file and traverse them one by one
			List<PagesJson> pages = this.getPages();

			HashMap<Integer, String> pageIntervalMappings = new HashMap<Integer, String>();

			String[] tokens = searchInput.split(" ");

			for (int j = 0; j < tokens.length; j++) {
				PagesJson selectedPage = null;
				char firstChar = tokens[j].charAt(0);

				if (firstChar >= 'A' && firstChar <= 'E') {
					selectedPage = pages.get(0);
				} else if (firstChar >= 'F' && firstChar <= 'J') {
					selectedPage = pages.get(1);
				} else if (firstChar >= 'K' && firstChar <= 'O') {
					selectedPage = pages.get(2);
				} else if (firstChar >= 'P' && firstChar <= 'T') {
					selectedPage = pages.get(3);
				} else if (firstChar >= 'U' && firstChar <= 'Z') {
					selectedPage = pages.get(4);
				}

				if (selectedPage == null) {
					continue;
				} else {
					String formattedTimeStamp = DateTime.retrieveCurrentDate();

					selectedPage.setReadTimeStamp(formattedTimeStamp);
					long guid = selectedPage.getGuid();
					ChordMessageInterface peer = dfsInstance.chord.locateSuccessor(guid);
					RemoteInputFileStream content = peer.get(guid);

					content.connect();
					@SuppressWarnings("resource")
					Scanner scan = new Scanner(content);
					scan.useDelimiter("\\A");
					String strSongResponse = "";
					while (scan.hasNext()) {
						strSongResponse += scan.next();
					}
					// retrieve all songs from a single page, and traverse the songs to find the
					// appropriate song
					JsonObject songRepository = new Gson().fromJson(strSongResponse, JsonObject.class);

					Set<Map.Entry<String, JsonElement>> entries = songRepository.entrySet();

					for (Map.Entry<String, JsonElement> entry : entries) {
						if (tokens[j].equals(entry.getKey())) {
							System.out.println(tokens[j] + " " + entry.getKey());
							SongResponse songResponse = new Gson().fromJson((entry.getValue()).getAsJsonObject(),
									SongResponse.class);
							List<Song> songs = songResponse.getSongsInPage();
							for (int k = 0; k < songs.size(); k++) {
								songsFromSearchResult.add(songs.get(k));
								System.out.println(songs.get(k).getSongDetails().getTitle() + " cool thing bro");
							}
							break;
						}
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
		@SerializedName("upperBound")
		@Expose
		String upperBound;
		@SerializedName("lowerBound")
		@Expose
		String lowerBound;

		public PagesJson(String text) {

		}

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

		public void setUpperBound(String upperBound) {
			this.upperBound = upperBound;
		}

		public String getUpperBound() {
			return upperBound;
		}

		public void setLowerBound(String lowerBound) {
			this.lowerBound = lowerBound;
		}

		public String getLowerBound() {
			return lowerBound;
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
		 * 
		 * @param key
		 * @param value
		 */
/*		public void addKeyValue(String key, List<JsonObject> value, DFS instance) {
			
			if(!instance.tree.containsKey(key)) {
				instance.tree.put(key, value);
			}
			else {
				List<JsonObject> contents = instance.tree.get(key);
				contents.addAll(value);
				instance.tree.put(key, contents);
			}
		}*/

	}

	public TreeMap<String, List<JsonObject>> tree;
	int port;
	public Chord chord;
	HashMap<String, Integer> counter;

	public void setTree(TreeMap<String, List<JsonObject>> tree) {
		this.tree = tree;
	}

	public TreeMap<String, List<JsonObject>> getTree() {
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
		tree = new TreeMap<String, List<JsonObject>>();
		counter = new HashMap<String, Integer>();

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
		System.out.println("WHAT THE HECK");
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
		@SuppressWarnings("resource")
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

	// Splitting files
	@SuppressWarnings("null")
	public void loadingSongsToPages() {
		try {

			List<Song> mySongs;
			List<Song> songNames = new ArrayList<Song>();
			ListView<Song> listOfSongs = null;

			for (int i = 1; i < 101; ++i) {
				for (int j = 1; j < 101; ++i) {
					Gson gson = new Gson();

					mySongs = gson.fromJson(new FileReader("music.json"), new TypeToken<List<Song>>() {
					}.getType());

					for (Song s : mySongs) {
						songNames.add(s);
					}
				}
				listOfSongs.setItems(FXCollections.observableList(songNames));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	FileJson getFile(String fileName) 
	{
		FilesJson metadata = this.readMetaData();
		FileJson foundFile = null;

		for (int i = 0; i < metadata.getFiles().size(); i++) {
			if (metadata.getFiles().get(i).getName().equals(fileName)) {
				return metadata.getFiles().get(i);
			}	
		}
		return fileJson;
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
		FileJson foundFile = getFile(fileName);

		int index = 0;
	
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
				scan.close();
				break;

			}

		}

	}

	/**
	 * <p>
	 * Emits all the pages contained in the file by adding their key value mappings
	 * </p>
	 * 
	 * @param key
	 * @param values
	 * @param file
	 * @throws Exception
	 */
	public void emit(String key, JsonObject values, String file) throws Exception {

		// traverse the metadata, till we get the specified file
		FileJson chosenFile  = getFile(file);

				// traverse the file's pages, and call the key value mapping
				for (int i = 0; i < chosenFile.getPages().size()-1; i++) {
					if(chosenFile.getPages().get(i).getLowerBound().compareTo(key) <=0 &&
						key.compareTo(chosenFile.getPages().get(i+1).getLowerBound())<0) {
						Long guid = chosenFile.getPages().get(i).getGuid();
						ChordMessageInterface c = chord.locateSuccessor(guid);
						c.addKeyValue(key, values);
						break;
					}
					
			}

	}

	/**
	 * Creates a new file with five pages, and then updates the current metadata
	 * @param file
	 * @param interval
	 * @param size
	 * @throws Exception
	 */
	public void createFile(String file, double interval, int size) throws Exception {
		int lower = 0;
		String formattedTS = DateTime.retrieveCurrentDate();

		// Set the creation, read, write time stamps accordingly
		FileJson newFile = new FileJson();
		newFile.setName(file);
		newFile.setCreationTimeStamp(formattedTS);
		newFile.setReadTimeStamp(formattedTS);
		newFile.setWriteTimeStamp(formattedTS);

		ArrayList<PagesJson> pages = new ArrayList<PagesJson>();

		// Add the file to the metadata
		FilesJson retrievedMetadata = this.readMetaData();
		
		//for every newly created file, create associated pages to it
		for (int i = 0; i < 5; i++) {
			PagesJson newPage = this.new PagesJson("");
			long thePageGuid = md5(file + i);
			newPage.setGuid(thePageGuid);
			newPage.setCreationTimeStamp(formattedTS);
			newPage.setReadTimeStamp(formattedTS);
			newPage.setWriteTimeStamp(formattedTS);

			switch (i) {
			case 0:
				newPage.setLowerBound("A");
				newPage.setUpperBound("E");
				break;
			case 1:
				newPage.setLowerBound("F");
				newPage.setUpperBound("J");
				break;
			case 2:
				newPage.setLowerBound("K");
				newPage.setUpperBound("O");
				break;
			case 3:
				newPage.setLowerBound("P");
				newPage.setUpperBound("T");
				break;
			case 4:
				newPage.setLowerBound("U");
				newPage.setUpperBound("Z");
				break;

			}
			pages.add(newPage);
		}
		//Set the newly created pages to the file, and add the recently made file to the metadata
		newFile.setPages(pages);
		retrievedMetadata.getFiles().add(newFile);
		//write to the metadata
		this.writeMetaData(retrievedMetadata);

	}

	/**
	 * @param file
	 *            File to store
	 * @param dfsInstance
	 *            Instance of the DFS Class with the treemap information to store
	 */
	public void bulkTree(String file, DFS dfsInstance) throws Exception {
		int size = 0;
		FilesJson filesJson = readMetaData();
		for (int i = 0; i < filesJson.getSize(); i++) {
			// Search the file
			if (filesJson.getFileJsonAt(i).getName().equalsIgnoreCase(file)) {
				// File found
				ArrayList<PagesJson> pagesList = filesJson.getFileJsonAt(i).getPages();
				for (int j=0; j< pagesList.size()  ; j++)
				{
					PagesJson pagesRead = pagesList.get(j);
					long pageGuid = pagesRead.getGuid();
					long page = md5(file + j);
					ChordMessageInterface peer = chord.locateSuccessor(pageGuid);
					peer.bulk(page, this);
				}
				break;
			}
			
			
		}
	}

	/**
	 * Runs the map reduce algorithm on a set of distributed file system
	 * 
	 * @param fileInput
	 * @param fileOutput
	 * @throws Exception
	 */
	public void runMapReduce(String fileInput, String fileOutput) throws Exception {

		long currGuid = chord.guid;
		int size = 1;
		int networkSize = 1;
		double interval = 0;
		Mapper mapper = new Mapper();
		Mapper reducer = new Mapper();

		FilesJson retrievedMetadata = this.readMetaData();
		counter.put(fileInput, 1);
		// wait until the network size is above 0, this is obtained after a full cycle

		interval = 1936 / size;
		createFile(fileOutput + ".map", interval, size);

		System.out.println("File created!!!!");

		// traverse the files of the metadata, until the particular file is found
		for (int j = 0; j < retrievedMetadata.getFiles().size(); j++) {
			if (retrievedMetadata.getFiles().get(j).getName().equals(fileInput)) {
				List<PagesJson> pagesFromInputFile = retrievedMetadata.getFiles().get(j).getPages();
				// traverse every page, increment the counter by 1, and call the mapContext
				// method
				for (int i = 0; i < 10; i++) {
					int currentCount = counter.get(fileInput);
					currentCount++;
					counter.put(fileInput, currentCount);
					ChordMessageInterface peer = chord.locateSuccessor(pagesFromInputFile.get(i).getGuid());
					peer.mapContext(pagesFromInputFile.get(i).getGuid(), mapper, this, fileOutput + ".map");
					System.out.println("what!!");
					
					System.out.println(currentCount);
				}
				break;
			}
		}
		
		for(Map.Entry<String, List<JsonObject>> entry: tree.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue().toString());
		}
		
		System.out.println("From the top of TreeMap");
		
//
//		// wait until the value of the key in the counter hashmap is equal to 0
//		while (counter.get(fileInput) == 0) {
//
//		}
//
		bulkTree(fileOutput + ".map", this);
		createFile(fileOutput, interval, size);
//
//		List<PagesJson> pagesFromOutputFile = new Gson().fromJson(fileOutput, new TypeToken<List<PagesJson>>() {
//		}.getType());
//		for (int j = 0; j < pagesFromOutputFile.size(); j++) {
//			int currentCount = counter.get(fileOutput);
//			currentCount++;
//			counter.put(fileInput, currentCount);
//			ChordMessageInterface peer = chord.locateSuccessor(pagesFromOutputFile.get(j).getGuid());
//			peer.reduceContext(pagesFromOutputFile.get(j), reducer, this, fileOutput);
//		}
//
//		while (counter.get(fileInput) == 0) {
//			Thread.sleep(10);
//			bulkTree(fileOutput, this);
//		}
//
	}

	/**
	 * Decrements the counter for a certain page
	 * 
	 * @param file
	 */
	public void onPageCompleted(String file) {
		int value = counter.get(file);
		value--;
		counter.put(file, value);
	}

}