package application.DFS;

import java.rmi.*;
import java.net.*;
import java.util.*;

import org.json.simple.JSONObject;

import java.io.*;
import java.nio.file.*;
import java.math.BigInteger;
import java.security.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.util.*;

/* JSON Format

{"file":
  [
     {"name":"MyFile",
      "size":128000000,
      "pages":
      [
         {
            "guid":11,
            "size":64000000
         },
         {
            "guid":13,
            "size":64000000
         }
      ]
      }
   ]
} 
*/

public class DFS {

	
	
	
	public class PagesJson {
		Long guid;
		Long size;

		public PagesJson() {

		}

		public void setGuid(Long guid) {
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

	};

	public class FileJson {
		String name;
		Long size;
		String creationTimeStamp;
		String readTimeStamp;
		String writeTimeStamp;
		String referenceCount;

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

	public class FilesJson {
		List<FileJson> files;

		public FilesJson() {

		}

		public void setFile(ArrayList<FileJson> files) {
			this.files = files;
		}

		public List<FileJson> getFiles() {
			return files;
		}
	};

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
				file.setName(newName);
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
