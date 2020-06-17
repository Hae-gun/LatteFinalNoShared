package org.techtown.lattefinalnoshared.VO;

public class Alarm {
	private String 	alarmNo;
//	private String userNo;
	private String 	hour;
	private String 	min;
	private String 	weeks;
//	private boolean flag;
	private String flag;

//	public String getUserNo() {
//		return userNo;
//	}
//
//	public void setUserNo(String userNo) {
//		this.userNo = userNo;
//	}

	public String getAlarmNo() {
		return alarmNo;
	}

	public void setAlarmNo(String alarmNo) {
		this.alarmNo = alarmNo;
	}
	
	public String getHour() {
		return hour;
	}
	
	public void setHour(String hour) {
		this.hour = hour;
	}
	
	public String getMin() {
		return min;
	}
	
	public void setMin(String min) {
		this.min = min;
	}
	
	public String getWeeks() {
		return weeks;
	}
	
	public void setWeeks(String weeks) {
		this.weeks = weeks;
	}
	
//	public boolean isFlag() {
//		return flag;
//	}
//
//	public void setFlag(boolean flag) {
//		this.flag = flag;
//	}


	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "Alarm{" +
				"alarmNo='" + alarmNo + '\'' +
				", hour='" + hour + '\'' +
				", min='" + min + '\'' +
				", weeks='" + weeks + '\'' +
				", flag=" + flag +
				'}';
	}
}
