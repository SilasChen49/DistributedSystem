package rmi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class Database {

    public static ConcurrentHashMap<String, Integer> booking_count = new ConcurrentHashMap<String, Integer>();

    public static ConcurrentHashMap<String, HashMap<String, ArrayList<TimeSlotRecord>>> roomRecord = new ConcurrentHashMap<>();

    static int bookingRecordNumber = 0;
    static ArrayList<BookingRecord> bookingRecordList = new ArrayList<>();

    public int createRoom(String date, String room_Number, String[] list_Of_Time_Slots) {
        int count = 0;
        int listStart = 0;
        //if date or room_number doesn't exist, add a record firstly and then do the same as usual
        if (!(roomRecord.contains(date) && roomRecord.get(date).containsKey(room_Number))) {
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
                if (stringBefore[1].compareTo(stringInsert[0]) > 0 && stringBefore[0].compareTo(stringInsert[1]) < 0) {
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
//            TimeSlotRecord timeSlotRecord = new TimeSlotRecord(timeslot); {
                // To find if there is the same timeslot
                for (Iterator<TimeSlotRecord> iterator = roomRecord.get(date).get(room_Number).iterator(); iterator.hasNext(); ) {
                    TimeSlotRecord timeSlotRecord = iterator.next();
                    if (timeSlotRecord.timeslot.equals(timeslot)) {
                        if (timeSlotRecord.studentID != null) {
                            /*
                            * inform student it is canceled
                            * */
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

    public String bookRoom(String studentID, String date, String room_Number, String timeslot) {
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
                        timeSlotRecord.studentID = studentID;
                        BookingRecord bookingRecord = new BookingRecord(studentID, date, room_Number, timeslot);
                        bookingRecordList.add(bookingRecord);
                        bookingID = bookingRecord.bookingID;
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
                if (timeSlotRecord.timeslot.equals(timeslot) && timeSlotRecord.studentID!=null && timeSlotRecord.studentID.equals(studentID)) {
                    timeSlotRecord.studentID = null;
                }
            }
        }
        return flag;
    }

    public int getAvailableTimeSlot(String date) {
        int count = 0;
        for (HashMap.Entry<String, ArrayList<TimeSlotRecord>> room: roomRecord.get(date).entrySet()){
            for (TimeSlotRecord timeSlotRecord : room.getValue()){
                if (timeSlotRecord.studentID==null)
                    count++;
            }
        }
        return count;
    }

    private class TimeSlotRecord {
        String timeslot;
        String studentID;

        TimeSlotRecord(String timeslot) {
            this.timeslot = timeslot;
        }
    }


    private class BookingRecord {
        BookingRecord(String studentID, String date, String room_Number, String timeslot) {

            this.studentID = studentID;
            this.date = date;
            this.room_Number = room_Number;
            this.timeslot = timeslot;
            bookingRecordNumber++;
            this.bookingID = "BNO" + bookingRecordNumber;
            this.wasCancelled = false;
        }
        boolean wasCancelled;
        String bookingID;
        String studentID;
        String date;
        String room_Number;
        String timeslot;
    }

    private String[] parseTimeSlots(String time_Slots) {
        String[] string = new String[2];
        string = time_Slots.split("-");
        return string;
    }
}
