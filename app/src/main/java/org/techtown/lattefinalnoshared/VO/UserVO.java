package org.techtown.lattefinalnoshared.VO;

public class UserVO {
    private final String code="userVO";
    private String id;
    private String pw;
    private String MacAddr;


    public UserVO(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }

    public String getCode(){
        return code;
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
}
