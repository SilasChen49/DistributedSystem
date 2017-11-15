package rmi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

public class DVLDatabase extends Database {

    private static Hashtable<String, Integer> booking_count = new Hashtable<>();

    private static ConcurrentHashMap<String, HashMap<String, ArrayList<TimeSlotRecord>>> roomRecord = new ConcurrentHashMap<>();

    private static ArrayList<BookingRecord> bookingRecordList = new ArrayList<>();
}
