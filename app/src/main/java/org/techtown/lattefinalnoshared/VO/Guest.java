package org.techtown.lattefinalnoshared.VO;

public class Guest {
	private String userNo;
	private String loginID;
	private String loginPW;
	private String role;
	private String authCode;
	
	public String getUserNo() {
		return userNo;
	}
	
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	
	public String getLoginID() {
		return loginID;
	}
	
	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}
	
	public String getLoginPW() {
		return loginPW;
	}
	
	public void setLoginPW(String loginPW) {
		this.loginPW = loginPW;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public String getAuthCode() {
		return authCode;
	}
	
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	
	@Override
	public String toString() {
		return "Guest [userNo=" + userNo + ", loginID=" + loginID + ", loginPW=" + loginPW + ", role=" + role
				+ ", authCode=" + authCode + "]";
	}
	
}
