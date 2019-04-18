package application.DFS;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


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


