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

	//gets the name
	public String getName() {
		return name;
	}
	//sets the name
	public void setName(String name) {
		this.name = name;
	}
	//gets the object
	public String getObject() {
		return object;
	}
	//sets the object
	public void setObject(String object) {
		this.object = object;
	}
	//gets the call semantics
	public String getCallSemantics() {
		return callSemantics;
	}
	//sets the call semantics
	public void setCallSemantics(String callSemantics) {
		this.callSemantics = callSemantics;
	}
	//gets the param
	public Param getParam() {
		return param;
	}
	//sets the param
	public void setParam(Param param) {
		this.param = param;
	}
	//gets the return
	public String getReturn() {
		return _return;
	}
	//sets the return
	public void setReturn(String _return) {
		this._return = _return;
	}

}