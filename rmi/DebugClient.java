package rmi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;

public class DebugClient{
    public static void main(String[] args) throws Exception{
        try {
            boolean flag = true;
            String number = "0";
            ServiceInterface service1 = (ServiceInterface) Naming.lookup("rmi://localhost:6000/DVL");
            ServiceInterface service2 = (ServiceInterface) Naming.lookup("rmi://localhost:7000/KKL");
            ServiceInterface service3 = (ServiceInterface) Naming.lookup("rmi://localhost:8000/WST");
            String id = "DVLS1111";
            UserLog userLog = new UserLog(id);
            String date = "2017-10-31";
            String room_Number = "201";
            String timeslot = "08:00-10:00";
            String[] list_Of_Timeslots = new String[4];
            list_Of_Timeslots[0] = "08:00-10:00";
            list_Of_Timeslots[1] = "10:00-10:20";
            list_Of_Timeslots[2] = "10:20-10:40";
            list_Of_Timeslots[3] = "10:40-11:00";
            System.out.println(service1.createRoom(date,room_Number, list_Of_Timeslots));
            System.out.println(service2.createRoom(date,room_Number, list_Of_Timeslots));
            System.out.println(service3.createRoom(date, room_Number, list_Of_Timeslots));

            System.out.println(service1.bookRoom(id, "KKL", date, room_Number, list_Of_Timeslots[0]));
            System.out.println(service1.bookRoom(id, "KKL", date, room_Number, list_Of_Timeslots[1]));
            System.out.println(service1.bookRoom(id, "DVL", date, room_Number, list_Of_Timeslots[0]));
            System.out.println(service1.getAvailableTimeSlot(date));
            System.out.println(service1.bookRoom(id, "WST", date, room_Number, list_Of_Timeslots[0]));
            id = "KKLS1111";
            System.out.println(service1.bookRoom(id, "KKL", date, room_Number, list_Of_Timeslots[0]));
            id = "DVLS1111";
            System.out.println(service1.cancelBooking(id,"KKLBNO1"));
            System.out.println(service1.getAvailableTimeSlot(date));
            System.out.println(service1.bookRoom(id, "WST", date, room_Number, list_Of_Timeslots[0]));
            System.out.println(service1.getAvailableTimeSlot(date));

            String[] timeslots_deleted = new String[1];
            timeslots_deleted[0] = "10:00-10:20";
            System.out.println(service2.deleteRoom(date, room_Number, timeslots_deleted));
            System.out.println(service1.bookRoom(id, "WST", date, room_Number, list_Of_Timeslots[1]));
            System.out.println(service1.getAvailableTimeSlot(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
