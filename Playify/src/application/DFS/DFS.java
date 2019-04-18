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

import application.Models.DateTime;
import application.Models.Playlist;
import application.Models.Song;
import application.Models.SongResponse;
import application.Models.User;
import application.Models.UserResponse;

@SuppressWarnings("unused")
public class DFS {


	int port;
	public Chord chord;

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
		int index=0;
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
		if(foundFile !=null) {
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
		
		//Set the creation, read, write time stamps accordingly
		FileJson newFile = new FileJson();
		newFile.setCreationTimeStamp(formattedTS);
		newFile.setReadTimeStamp(formattedTS);
		newFile.setWriteTimeStamp(formattedTS);
		
		//Add the file to the metadata
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

		//get the current DateTime
		Date currentDate = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss a");
		String formattedReadTS = dateFormat.format(currentDate);
		//set the read timestamp accordingly on the file and its desired page
		FileJson chordUsersFile = metadata.getFiles().get(0);
		chordUsersFile.setReadTimeStamp(formattedReadTS);
		PagesJson pageofUsers = chordUsersFile.getPages().get(0);
		pageofUsers.setReadTimeStamp(formattedReadTS);

		//retrieve the guid of the page to search for the Chord that contains the actual file
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
	
	/**
	 * Append a page to a file
	 * @param fileName
	 * @param data
	 * @throws Exception 
	 */
	public void append(String fileName, RemoteInputFileStream data) throws Exception {
		Gson gson = new Gson();
		FilesJson allFiles = this.readMetaData();
		FileJson foundFile = null;
		
		int index=0;
		for(int i=0; i<allFiles.getFiles().size(); i++) {
			if(allFiles.getFiles().get(i).getName().equals(fileName)) {
				foundFile = allFiles.getFiles().get(i);
				
				data.connect();
				Scanner scan = new Scanner(data);
				scan.useDelimiter("\\A");
				String strPageData = "";
				while (scan.hasNext()) {
					strPageData += scan.next();
				}
				
				//get the current DateTime
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

}
