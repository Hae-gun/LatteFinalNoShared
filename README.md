# LatteFinalNoShared



## 구조



![image-20200726144944200](README.assets/image-20200726144944200.png)



### Main Activity([code](https://github.com/Hae-gun/LatteFinalNoShared/blob/master/app/src/main/java/org/techtown/lattefinalnoshared/MainActivity.java))

* Frame Layout
  * 프래그먼트 화면에 출력 (Bottom Navigation에 의해 제어됨)
* Bottom Navigation View
  * 프래그먼트 화면 제어.
* onBackPressed 메서드를 이용한 화면 제어.
  * 이전 화면으로 이동.

### Service

* 소켓통신 ( Socket Network ; TCP/IP )
* Foreground Service
  * Foreground Service 를 이용한 immortal Service 구현.
  * Oreo 버전 이후부터 Foreground Service 사용을 위해 Notification 설정 필수.

### Fragments

#### Login

#### RoomList

#### RoomCurrentSetting

#### AlarmSetting



### 