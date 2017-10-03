package rmi;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {

    public HashMap<String, Integer> booking_count = new HashMap<String, Integer>();

    public HashMap<String, HashMap<String, ArrayList<TimeSlotRecord>>> roomRecord = new HashMap<>();

    ArrayList<BookingRecord> bookingRecordList = new ArrayList<>();

    public boolean createRoom(String room_Number, String date, String[] list_Of_Time_Slots){
        boolean flag = false;

        //if date exists
        if (roomRecord.containsKey(date)){
            //if room exists
            if (roomRecord.get(date).containsKey(room_Number)){
                //for every time_slot in the list
                for (int j=0; j<list_Of_Time_Slots.length; j++) {
                    String time_slot = list_Of_Time_Slots[j];
                    TimeSlotRecord timeSlotRecord = new TimeSlotRecord(time_slot);
                    int length = roomRecord.get(date).get(room_Number).size();
                    for (TimeSlotRecord existingTimeSlot : roomRecord.get(date).get(room_Number)){
                        boolean insertAvailable = true;
                            String[] stringBefore = parseTimeSlots(existingTimeSlot.timeslot);
                            String[] stringInsert = parseTimeSlots(timeSlotRecord.timeslot);
                            if (stringBefore[1].compareTo(stringInsert[0]) > 0 || stringBefore[0].compareTo(stringInsert[1]) < 0) {
                                insertAvailable = false;
                                break;
                            }
                        if (insertAvailable)
                            roomRecord.get(date).get(room_Number).add(timeSlotRecord);

                    }
//                        if (roomRecord.get(date).get(room_Number).get(i).timeslot.compareTo(time_slot) > 0) {
//                            String[] stringBefore = parseTimeSlots(roomRecord.get(date).get(room_Number).get(i - 1).timeslot);
//                            String[] stringInsert = parseTimeSlots(time_slot);
//                            String[] stringAfter = parseTimeSlots(roomRecord.get(date).get(room_Number).get(i).timeslot);
//                            //time slot between 2 existing ones
//                            if (stringBefore[1].compareTo(stringInsert[0]) <= 0 && stringAfter[0].compareTo(stringInsert[1]) >= 0) {
//                                roomRecord.get(date).get(room_Number).add(timeSlotRecord);
//                                flag = true;
//                                break;
//                            }
//                        }
//                    //Time slot is bigger than any existing one
//                    if (!flag && time_slot.compareTo(roomRecord.get(date).get(room_Number).get(length - 1).timeslot) > 0) {
//                        roomRecord.get(date).get(room_Number).add(timeSlotRecord);
//                        flag = true;
//                    }
                }
            }
            //if room doesn't exist
            else{
                ArrayList<TimeSlotRecord> arrayList = new ArrayList<>();
                for (int i=0; i<list_Of_Time_Slots.length; i++) {
                    TimeSlotRecord timeSlotRecord = new TimeSlotRecord(list_Of_Time_Slots[i]);
                    arrayList.add(timeSlotRecord);
                    arrayList.sort();
                }
                roomRecord.get(date).put(room_Number, arrayList);
                flag = true;
            }
        }
        //if date is empty, add record directly
        else{
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(list_Of_Time_Slots);
            HashMap<String, ArrayList<String>> hashMap = new HashMap<>();
            hashMap.put(room_Number, arrayList);
            roomRecord.put(date, hashMap);
            flag = true;
        }
        return flag;
    }

    public boolean deleteRoom(String room_Number, String date, String list_Of_Time_Slots){
        // roomRecord has this list
        boolean flag = false;
        if (roomRecord.containsKey(date) && roomRecord.get(date).containsKey(room_Number) &&
                roomRecord.get(date).get(room_Number).contains(list_Of_Time_Slots)){
            if (isBooked(room_Number,date,list_Of_Time_Slots)){
                /*
                *
                * tell student the boock is canceled
                *
                * */
            }
            //delete the room directly
            roomRecord.get(date).get(room_Number).remove(list_Of_Time_Slots);
            flag = true;
        }
        // roomRecord does not have this list
        else{
            return flag;
        }
        return flag;
    }


    private class TimeSlotRecord{
        String timeslot;
        String studentID;
        TimeSlotRecord(String timeslot){
            this.timeslot = timeslot;
        }
    }


    private class BookingRecord {
        BookingRecord(){

        }
        String bookingID;
        String studentID;
        String date;
        String room_Number;
        String list_Of_Time_Slots;
    }

    boolean isBooked(String room_Number, String date, String list_Of_Time_Slots){
        for (BookingRecord record:bookingRecordList){
            if (date.equals(record.date) && room_Number.equals(record.room_Number) && list_Of_Time_Slots.equals(record.list_Of_Time_Slots)){
                return true;
            }
        }
        return false;
    }

    private String[] parseTimeSlots(String time_Slots){
        String[] string = new String[2];
        string = time_Slots.split("-");
        return string;
    }
}
