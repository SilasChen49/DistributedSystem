import org.junit.Test;
import rmi.Database;

public class TestDatabase {

    @Test
    public void addRoom(){
        String s = null;
        Database database = new Database();
        String room_Number = "room111";
        String date = "2017-01-01";
        String[] list_Of_Time_Slots = new String[5];
        list_Of_Time_Slots[0]= "08:00-10:00";
        list_Of_Time_Slots[1]= "10:00-11:00";
        list_Of_Time_Slots[2]="10:59-12:00";
        list_Of_Time_Slots[3]="11:20-11:40";
        list_Of_Time_Slots[4]="11:10-11:21";
        System.out.println(database.createRoom(date,room_Number,list_Of_Time_Slots));
//        System.out.println(database.deleteRoom(date,room_Number,list_Of_Time_Slots));
        String bookingID;
        System.out.println(bookingID=database.bookRoom("4003",date,room_Number,list_Of_Time_Slots[3]));
        System.out.println(database.getAvailableTimeSlot(date));
        System.out.println(database.cancelBooking("4003", bookingID));

        System.out.println(database.getAvailableTimeSlot(date));
        System.out.println(database.bookRoom("4004",date,room_Number,list_Of_Time_Slots[3]));

        System.out.println(database.getAvailableTimeSlot(date));
    }
}
