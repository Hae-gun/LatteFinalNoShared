package org.techtown.lattefinalnoshared.VO;

import com.google.gson.Gson;

import org.techtown.lattefinalnoshared.MainActivity;

public class LatteMessage {

    private String clientNo;
    private String roomNo;
    private String code1;
    private String code2;
    private String jsonData;

    public LatteMessage() {
    }

    public LatteMessage(String code1, String code2, String jsonData) {
        this.code1 = code1;
        this.code2 = code2;
        this.jsonData = jsonData;
    }

    public LatteMessage(String clientNo, String code1, String code2, String jsonData) {
        this.clientNo = clientNo;
        this.code1 = code1;
        this.code2 = code2;
        this.jsonData = jsonData;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getClientNo() {
        return clientNo;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    public String getCode1() {
        return code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
}
