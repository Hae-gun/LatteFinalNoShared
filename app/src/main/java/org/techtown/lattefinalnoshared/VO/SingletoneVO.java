package org.techtown.lattefinalnoshared.VO;

public class SingletoneVO {
    private String Macaddress;
    private String id;
    private String pw;
    private String userNo;
    private String roomNo;
    private String role;
    private boolean authority;

    public static SingletoneVO vo = new SingletoneVO();

    private SingletoneVO() {

    }

    public static SingletoneVO getInstance() {
        return vo;
    }


    public String getMacaddress() {
        return Macaddress;
    }

    public void setMacaddress(String macaddress) {
        Macaddress = macaddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public boolean getAuthority() {
        return authority;
    }

    public void setAuthority(boolean authority) {
        this.authority = authority;
    }

    public boolean isAuthority() {
        return authority;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
