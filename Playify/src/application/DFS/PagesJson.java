package application.DFS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

}
