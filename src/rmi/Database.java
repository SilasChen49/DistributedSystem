package rmi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

public class Database {

    static class SingletonHolder {
        static Database instance = new Database();
    }

    public static Database getInstance() {
        return SingletonHolder.instance;
    }

    public static HashMap<String, Integer> booking_count = new HashMap<>();

    private static Hashtable<String, HashMap<String, ArrayList<TimeSlotRecord>>> roomRecord = new Hashtable<>();

    private static ArrayList<BookingRecord> bookingRecordList = new ArrayList<>();

    public int createRoom(String date, String room_Number, String[] list_Of_Time_Slots) {
        int count = 0;
        int listStart = 0;
        if (!(roomRecord.containsKey(date) && roomRecord.get(date).containsKey(room_Number))) {
            System.out.println();
            ArrayList<TimeSlotRecord> arrayList = new ArrayList<>();
            TimeSlotRecord timeSlotRecord = new TimeSlotRecord(list_Of_Time_Slots[listStart]);
            listStart++;
            arrayList.add(timeSlotRecord);
            HashMap<String, ArrayList<TimeSlotRecord>> hashMap = new HashMap<>();
            hashMap.put(room_Number, arrayList);
            // if contains date and room_Number
            if (roomRecord.contains(date) && !roomRecord.get(date).containsKey(room_Number)) {
                roomRecord.get(date).put(room_Number, arrayList);
            } else if (!roomRecord.contains(date)) {
                roomRecord.put(date, hashMap);
            }
            count++;
        }

        //Now, date and room_Number exist. Then do as usual
        //if date and room_Number exists
        //for every time_slot in the list
        for (int j = listStart; j < list_Of_Time_Slots.length; j++) {
            String timeslot = list_Of_Time_Slots[j];
            TimeSlotRecord timeSlotRecord = new TimeSlotRecord(timeslot);
            //check if timeslot conflicts
            boolean insertAvailable = true;
            for (TimeSlotRecord existingTimeSlot : roomRecord.get(date).get(room_Number)) {
                String[] stringBefore = parseTimeSlots(existingTimeSlot.timeslot);
                String[] stringInsert = parseTimeSlots(timeSlotRecord.timeslot);
                if ((stringBefore[1].compareTo(stringInsert[0]) > 0 && stringBefore[0].compareTo(stringInsert[1]) < 0)
                        || (stringBefore[0].equals(stringInsert[0]) && stringBefore[1].equals(stringInsert[1]))) {
                    insertAvailable = false;
                    break;
                }
            }
            if (insertAvailable) {
                roomRecord.get(date).get(room_Number).add(timeSlotRecord);
                count++;
            } else {
                insertAvailable = false;
            }
        }
        return count;
    }

    public int deleteRoom(String date, String room_Number, String[] list_Of_Time_Slots) {
        // for every timeslot in the list
        int count = 0;
        if (roomRecord.containsKey(date) && roomRecord.get(date).containsKey(room_Number)) {
            for (int i = 0; i < list_Of_Time_Slots.length; i++) {
                String timeslot = list_Of_Time_Slots[i];
                // To find if there is the same timeslot
                for (Iterator<TimeSlotRecord> iterator = roomRecord.get(date).get(room_Number).iterator(); iterator.hasNext(); ) {
                    TimeSlotRecord timeSlotRecord = iterator.next();
                    // if someone has booked the room, reduce the booking_count
                    if (timeSlotRecord.timeslot.equals(timeslot)) {
                        if (timeSlotRecord.studentID != null) {
                            // modify the booking count
                            String data = "modifyCount," + timeSlotRecord.studentID +"," + "-1";
                            String campusName = timeSlotRecord.studentID.substring(0,3);
                            ServiceImpl.UDPTransort(data, campusName);
//                            booking_count.put(timeSlotRecord.studentID, booking_count.get(timeSlotRecord.studentID) - 1);
                        }
                        //delete the room now
                        iterator.remove();
                        count++;
                    }
                }
            }
        } else {
            count = 0;
        }

        return count;
    }

    public String bookRoom(String studentID, String campusName, String date, String room_Number, String timeslot) {
        String bookingID = "";
        if (roomRecord.containsKey(date) && roomRecord.get(date).containsKey(room_Number)) {
            for (TimeSlotRecord timeSlotRecord : roomRecord.get(date).get(room_Number)) {
                //booking success
                if (timeSlotRecord.timeslot.equals(timeslot)) {
                    // someone has booked it yet
                    if (timeSlotRecord.studentID != null) {
                    }
                    // this room timeslot is available
                    else {
                        // get booking_count(studentID)
                        // if it is smaller than 3.
                        String data = "getBookingCount," + studentID;
                        String studentCampus = studentID.substring(0,3);
                        String reply = ServiceImpl.UDPTransort(data, studentCampus);
                        int count = -1;
                        try {
                            count = Integer.parseInt(reply);
                        }catch (Exception e){
                            System.out.println("Getting Count Error!");
                        }
                        System.out.println(count);
                        if (count < 3) {
                            timeSlotRecord.studentID = studentID;
                            BookingRecord bookingRecord = new BookingRecord(studentID, campusName, date, room_Number, timeslot);
                            bookingRecordList.add(bookingRecord);
                            bookingID = bookingRecord.bookingID;
                            data = "modifyCount," + timeSlotRecord.studentID +"," + "+1";
                            ServiceImpl.UDPTransort(data, studentCampus);
                        }

                    }
                }
            }
        }
        return bookingID;
    }

    public boolean cancelBooking(String studentID, String bookingID) {
        boolean flag = false;
        String date = "";
        String room_Number = "";
        String timeslot = "";
        for (BookingRecord bookingRecord : bookingRecordList) {
            if (bookingID.equals(bookingRecord.bookingID) && !bookingRecord.wasCancelled) {
                flag = true;
                date = bookingRecord.date;
                room_Number = bookingRecord.room_Number;
                timeslot = bookingRecord.timeslot;
                bookingRecord.wasCancelled = true;
                break;
            }
        }
        if (flag) {
            for (Iterator<TimeSlotRecord> iterator = roomRecord.get(date).get(room_Number).iterator(); iterator.hasNext(); ) {
                TimeSlotRecord timeSlotRecord = iterator.next();
                if (timeSlotRecord.timeslot.equals(timeslot) && timeSlotRecord.studentID != null && timeSlotRecord.studentID.equals(studentID)) {
                    timeSlotRecord.studentID = null;
                }
            }
        }
        return flag;
    }

    public int getAvailableTimeSlot(String date) {
        int count = 0;
        if (roomRecord.containsKey(date))
            for (HashMap.Entry<String, ArrayList<TimeSlotRecord>> room : roomRecord.get(date).entrySet()) {
                for (TimeSlotRecord timeSlotRecord : room.getValue()) {
                    if (timeSlotRecord.studentID == null)
                        count++;
                }
            }
        return count;
    }

    public void modifyBookingCount(String studentID, int x){
        if (!booking_count.containsKey(studentID))
            booking_count.put(studentID,0);
        booking_count.put(studentID, booking_count.get(studentID)+x);
        System.out.println("studentID:" + booking_count.get(studentID));
    }

    private String[] parseTimeSlots(String time_Slots) {
        String[] string = new String[2];
        string = time_Slots.split("-");
        return string;
    }
}

class TimeSlotRecord {
    String timeslot;
    String studentID;

    TimeSlotRecord(String timeslot) {
        this.timeslot = timeslot;
    }
}


class BookingRecord {
    static int bookingRecordNumber = 0;

    BookingRecord(String studentID, String campusName, String date, String room_Number, String timeslot) {

        this.studentID = studentID;
        this.date = date;
        this.room_Number = room_Number;
        this.timeslot = timeslot;
        this.campusName = campusName;
        bookingRecordNumber++;
        this.bookingID = campusName + "BNO" + bookingRecordNumber;
        this.wasCancelled = false;
    }

    boolean wasCancelled;
    String bookingID;
    String studentID;
    String campusName;
    String date;
    String room_Number;
    String timeslot;
}
