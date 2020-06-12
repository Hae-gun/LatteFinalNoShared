package org.techtown.lattefinalnoshared.VO;

import java.util.List;

public class RoomDetail {
	private String 	roomNo;
	private List<Sensor> sensorList;
	private Hope 	hope;
	private Alarm 	alarm;
	
	
	public String getRoomNo() {
		return roomNo;
	}
	
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	
	public List<Sensor> getSensorList() {
		return sensorList;
	}
	
	public void setSensorList(List<Sensor> sensorList) {
		this.sensorList = sensorList;
	}
	
	public Hope getHope() {
		return hope;
	}
	
	public void setHope(Hope hope) {
		this.hope = hope;
	}
	
	public Alarm getAlarm() {
		return alarm;
	}
	
	public void setAlarm(Alarm alarm) {
		this.alarm = alarm;
	}
	
	@Override
	public String toString() {
		return "RoomDetail [roomNo=" + roomNo + ", sensorList=" + sensorList + ", hope=" + hope + ", alarm=" + alarm
				+ "]";
	}
	
}
