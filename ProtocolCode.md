```java
package androidTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;

import Test.Alarm;
import Test.AlarmData;
import Test.Guest;
import Test.Hope;
import Test.UserVO;
import Test.LatteMessage;
import Test.Reservation;
import Test.Room;
import Test.RoomDetail;
import Test.Sensor;

public class TestServer {

	public static void main(String[] args) {
		Gson gson = new Gson();

		// TODO Auto-generated method stub
		try {
			ServerSocket server = new ServerSocket(55566);
			System.out.println("서버시작");
			while (true) {
				Socket s = server.accept();
				System.out.println("유저접속" + s.getInetAddress());
				BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
				PrintWriter pr = new PrintWriter(s.getOutputStream());

				Thread t = new Thread(() -> {
					System.out.println("" + s.hashCode());
					String msg = "";
					String id = "";
					try {
						Alarm setAlarm = new Alarm();
						String userNo = "";
						while (true) {
							if (msg != null) {
								msg = br.readLine();
									
								System.out.println("getData: " + msg);
								try {
									LatteMessage lmsg = gson.fromJson(msg, LatteMessage.class);
									if ("LOGIN".equals(lmsg.getCode1())) {
										LatteMessage message = gson.fromJson(msg, LatteMessage.class);
										String clinetno = "A0000001";
										userNo = "A0000001";
										message.setCode2("SUCCESS");
										message.setClientNo(clinetno);
										Guest guest = gson.fromJson(message.getJsonData(), Guest.class);
										guest.setUserNo(clinetno);
										guest.setRole("user");
										message.setJsonData(gson.toJson(guest, Guest.class));
										msg = gson.toJson(message);
										
										pr.println();
										
									} else if ("Alarm".equals(lmsg.getCode1()) && "get".equals(lmsg.getCode2())) {
										
											LatteMessage message = gson.fromJson(msg, LatteMessage.class);
											System.out.println("JsonData : " + message.getJsonData());
											Alarm alarm = gson.fromJson(message.getJsonData(), Alarm.class);
											alarm.setHour("10");
											alarm.setMin("30");
											alarm.setFlag(true);
											setAlarm = alarm;
											String[] days = { "sun", "mon", "tue","sat" };
											alarm.setWeeks(gson.toJson(days));
											
											message.setJsonData(gson.toJson(alarm));
											msg = gson.toJson(message);
										
									} else if ("AlarmJob".equals(lmsg.getCode1()) && "get".equals(lmsg.getCode2())) {
										LatteMessage message = gson.fromJson(msg, LatteMessage.class);
										AlarmData alarmData[] = gson.fromJson(message.getJsonData(), AlarmData[].class);
										
										alarmData[0].setType("Light");
										alarmData[0].setStates("On");
										alarmData[0].setStateDetail("50");
										
										alarmData[1].setType("Bed");
										alarmData[1].setStates("0");
										
										alarmData[2].setType("Blind");
										alarmData[2].setStates("CLOSE");
										
										System.out.println("alarmData: " + gson.toJson(alarmData));
										
										message.setJsonData(gson.toJson(alarmData));
										msg = gson.toJson(message);
										
									}else if("RoomDetail".equals(lmsg.getCode1())) {
										Room res = gson.fromJson(lmsg.getJsonData(), Room.class);
										RoomDetail roomDetail = new RoomDetail();
										roomDetail.setRoomNo(res.getRoomNo());
										
										ArrayList<Sensor> sensors = new ArrayList<>();
										Sensor thermo = new Sensor("01","thermo","28");
										
										Sensor aircon = new Sensor("02","aircon","On");
										
										Sensor humid = new Sensor("03","humid","33");
										
										Sensor light = new Sensor("04","light","On","40");
										
										Sensor blind = new Sensor("05","blind","CLOSE");
										
										Sensor door = new Sensor("06","door","CLOSE");
										
//										Sensor[] set = {thermo,aircon,humid,light,blind,door};
										sensors.add(thermo);
										sensors.add(aircon);
										sensors.add(humid);
										sensors.add(light);
										sensors.add(blind);
										sensors.add(door);
										
//										Sensor sensors = new Sensor();
										
										Hope hope = new Hope();
										hope.setHopeNo(userNo);
										hope.setBed("0");
										hope.setBlind("OPEN");
										hope.setLight("50");
										hope.setTemp("30");
										
										roomDetail.setSensorList(sensors);
										roomDetail.setHope(hope);
										roomDetail.setAlarm(setAlarm);
										lmsg.setJsonData(gson.toJson(roomDetail));
										lmsg.setCode2("Success");
										msg = gson.toJson(lmsg);
									}
								} catch (Exception e) {
									System.out.println(e.toString());
								}
								
								if (msg == null || msg.equals("@exit")) {
									break;
								}

								if ("disconnect".equals(msg)) {
									break;
								}

								if (msg.contains("Light:")) {
									msg = "lightPower" + msg;
								}

								if (msg.contains("Blind:")) {
									StringBuilder sb = new StringBuilder(msg);
									sb.delete(0, 6);
									msg = "BlindState:" + sb;
								}

								if (msg.contains("loginID")) {
//								UserVO vo = gson.fromJson(msg, UserVO.class);
									Guest vo = gson.fromJson(msg, Guest.class);
									id = vo.getLoginID();
								}

								if ("RoomCurrentSetting".equals(msg)) {
									msg = "userId:" + id;
								}

//							if(msg.contains("authCode")) {
//								Guest vo =gson.fromJson(msg, Guest.class);
////								vo.setAuthCode("000000000000");
//								msg = gson.toJson(vo);
//							}

//							System.out.println("from Client: " + msg);
								pr.println(msg);
								pr.flush();
							}
						}
					} catch (IOException e2) {
						System.out.println(e2.toString());
					}
					System.out.println("유저 로그아웃");
				});
				t.start();

			}
		} catch (IOException e) {

		}

	}

}

```

