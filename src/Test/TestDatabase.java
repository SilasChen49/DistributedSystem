import org.junit.Test;
import rmi.Database;

public class TestDatabase {

    @Test
    public void addRoom(){
        String s = null;
        Database database = new Database();
        String room_Number = "room111";
        String date = "2017-01-01";
        String list_Of_Time_Slots = "08:00-10:00";
        database.createRoom(room_Number,date,list_Of_Time_Slots);
        System.out.println(database.deleteRoom(room_Number,date,list_Of_Time_Slots));
    }
}
