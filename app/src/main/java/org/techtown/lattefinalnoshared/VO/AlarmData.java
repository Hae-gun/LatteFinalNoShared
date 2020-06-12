package org.techtown.lattefinalnoshared.VO;

public class AlarmData {
	private String dataNo;
	private String userNo;
	private String type;
	private String states;
	private String stateDetail;
	
	
	public String getDataNo() {
		return dataNo;
	}
	
	public void setDataNo(String dataNo) {
		this.dataNo = dataNo;
	}
	
	public String getUserNo() {
		return userNo;
	}
	
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
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
		return "AlarmData [dataNo=" + dataNo + ", userNo=" + userNo + ", type=" + type + ", states=" + states
				+ ", stateDetail=" + stateDetail + "]";
	}
	
}
