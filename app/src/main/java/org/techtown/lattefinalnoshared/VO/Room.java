package org.techtown.lattefinalnoshared.VO;

public class Room {
	private String roomNo;
	private String roomName;
	private String roomSSID;
	private String imgUrl;
	
	
	public String getRoomNo() {
		return roomNo;
	}
	
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	
	public String getRoomName() {
		return roomName;
	}
	
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	public String getRoomSSID() {
		return roomSSID;
	}
	
	public void setRoomSSID(String roomSSID) {
		this.roomSSID = roomSSID;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}
	
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	@Override
	public String toString() {
		return "Room [roomNo=" + roomNo + ", roomName=" + roomName + ", roomSSID=" + roomSSID + ", imgUrl=" + imgUrl
				+ "]";
	}
	
}
