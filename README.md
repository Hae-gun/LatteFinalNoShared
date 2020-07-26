# LatteFinalNoShared



## 구조



![image-20200726144944200](README.assets/image-20200726144944200.png)



### Main Activity([code](https://github.com/Hae-gun/LatteFinalNoShared/blob/master/app/src/main/java/org/techtown/lattefinalnoshared/MainActivity.java))

* Frame Layout
  * Fragment 화면에 출력 (Bottom Navigation에 의해 제어됨)
* Bottom Navigation View
  * Fragment 화면 제어.
  * Login 성공시에만 제어가능.
* onBackPressed 메서드를 이용한 화면 제어.
  * 이전 Fragment 화면으로 이동.

### Service

* 소켓통신 ( Socket Network ; TCP/IP )
  * 모든 Activity 와 Fragment 에서 서버에 보낼 데이터를 받아 Server에 전달.
  * 전송시 정의한 Message 객체를 JSON 문자열로 변환하여 전송.
* Foreground Service
  * Foreground Service 를 이용한 immortal Service 구현.
  * Oreo 버전 이후부터 Foreground Service 사용을 위해 Notification 설정 필수.

### Fragments

#### Login

* userId, password 를 입력받아 서비스로 데이터 전달.

#### RoomList

#### RoomCurrentSetting

#### AlarmSetting





## 데이터 교환

### Broadcast Receiver 사용

* 통신 방식
  1. Service, Activity, Fragment 각각 Broadcast Receiver 객체를 갖음.
  2. Service 에서 Server를 통해 받은 JSON 데이터를 메세지(LatteMessage) 객체로 Parsing 하여 Protocol 을 분석.
  3. 분석된 객체를 

## Message

* LatteMessage 구조

  ```java
  public class LatteMessage {
  
      private String clientNo;
      private String roomNo;
      private String code1;
      private String code2;
      private String jsonData;
  	
    // 이하 생략
  }
  ```

* JSON Data

  * 약속한 Protocol 에 따라 code1 값을 분석한 후 그에 따른 VO객체를 jsonData 에 주입시킴.

## VO

## Protocol



