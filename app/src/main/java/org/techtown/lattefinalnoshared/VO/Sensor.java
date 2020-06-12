package org.techtown.lattefinalnoshared.VO;

public class Sensor {
	private String 	sensorNo;
	private String 	type;
	private String  deviceNo;
	private String 	states;
	private String 	stateDetail;
	
	
	public String getSensorNo() {
		return sensorNo;
	}
	
	public void setSensorNo(String sensorNo) {
		this.sensorNo = sensorNo;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getDeviceNo() {
		return deviceNo;
	}
	
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	
	public String getStates() {
		return states;
	}
	
	public void setStates(String states) {
		this.states = states;
	}
	
	public String getStateDetail() {
		return stateDetail;
	}
	
	public void setStateDetail(String stateDetail) {
		this.stateDetail = stateDetail;
	}
	
	@Override
	public String toString() {
		return "Sensor [sensorNo=" + sensorNo + ", type=" + type + ", deviceNo=" + deviceNo + ", states=" + states
				+ ", stateDetail=" + stateDetail + "]";
	}
	
}
