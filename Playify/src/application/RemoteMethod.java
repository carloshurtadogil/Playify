package application;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RemoteMethod {

	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("object")
	@Expose
	private String object;
	@SerializedName("call-semantics")
	@Expose
	private String callSemantics;
	@SerializedName("param")
	@Expose
	private Param param;
	@SerializedName("return")
	@Expose
	private String _return;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getCallSemantics() {
		return callSemantics;
	}

	public void setCallSemantics(String callSemantics) {
		this.callSemantics = callSemantics;
	}

	public Param getParam() {
		return param;
	}

	public void setParam(Param param) {
		this.param = param;
	}

	public String getReturn() {
		return _return;
	}

	public void setReturn(String _return) {
		this._return = _return;
	}

}