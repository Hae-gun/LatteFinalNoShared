package org.techtown.lattefinalnoshared.VO;

public class RoomListData {

    String roomName;
    String startDate;
    String endDate;
    String imgUrl;

    public RoomListData(String roomName, String startDate, String endDate, String imgUrl) {
        this.roomName = roomName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imgUrl = imgUrl;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "RoomListData{" +
                "roomName='" + roomName + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
