package rmi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;

public class AdminClient1 {
    public static void main(String[] args) throws Exception {
        try {
            boolean flag = true;
            String number = "0";
            AdminServiceInterface AdminService = (AdminServiceInterface) Naming.lookup("rmi://localhost:6001/DVLAdmin");
            System.out.println("<--------Welcome to Distributed Room Reservation System-------->");
            System.out.println("Firstly, please input your AdminID: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String AdminID = reader.readLine();
            String campusID = AdminID.substring(0, 3);

            else {
                System.out.println("WRONG AdminID! Exit!");
                flag = false;
            }
            String date = "";
            String room_Number = "";
            String timeslot = "";
            String bookingID = "";
            int length = 0;
            System.out.println("Welcome " + AdminID);
            System.out.println("We provide some services, you could choose service by input number.\nThe services are as follows\n: ");
            System.out.println("1: 'Help'. List services we provide by typing number '1'.");
            System.out.println("2: 'Create Room'. Start to create room by typing number '2'.");
            System.out.println("3: 'Delete Booking'. Delete room by typing number '3'.");
            System.out.println("4: 'Exit'. Log out of the system by typing number'4'.");
            while (flag) {
                System.out.println("<---------------------------------------------------------------->");
                System.out.println("Please choose the service you want.");
                number = reader.readLine();
                switch (number) {
                    case "1":
                        System.out.println("<--------Following are the services.-------->");
                        System.out.println("1: 'HELP'. List services we provide by typing number '1'.");
                        System.out.println("2: 'Create Room'. Start to create room by typing number '2'.");
                        System.out.println("3: 'Delete Booking'. Delete room by typing number '3'.");
                        System.out.println("4: 'Exit'. Log out of the system by typing number'4'.");
                        break;
                    case "2":
                        System.out.println("<--------Start Creating Room!-------->");
                        System.out.println("Please choose the date:");
                        date = reader.readLine();
                        System.out.println("Please choose the room_Number:");
                        room_Number = reader.readLine();
                        System.out.println("Please input the timeslots number:");
                        length = Integer.parseInt(reader.readLine());
                        System.out.println("Please input the timeslots:");
                        String[] listOfTimeSlotCreate = new String[length];
                        for (int i=0; i<length; i++) {
                            System.out.print("TimeSlot"+i+":");
                            listOfTimeSlotCreate[i] = reader.readLine();
                        }
                        int createdRoomNumber = AdminService.createRoom(date, room_Number, listOfTimeSlotCreate);

                            System.out.println("The number of timeslots get created is: " + createdRoomNumber);
                        break;
                    case "3":
                        System.out.println("<--------Start Deleting Room!-------->");
                        System.out.println("Please choose the date:");
                        date = reader.readLine();
                        System.out.println("Please choose the room_Number:");
                        room_Number = reader.readLine();
                        System.out.println("Please input the timeslots number:");
                        length = Integer.parseInt(reader.readLine());
                        System.out.println("Please input the timeslots:");
                        String[] listOfTimeSlotDelete = new String[length];
                        for (int i=0; i<length; i++) {
                            System.out.print("TimeSlot"+i+":");
                            listOfTimeSlotDelete[i] = reader.readLine();
                        }
                        int deletedRoomNumber = AdminService.deleteRoom(date, room_Number, listOfTimeSlotDelete);

                        System.out.println("The number of timeslots get created is: " + deletedRoomNumber);
                        break;
                    case "4":
                        flag = false;
                        System.out.println("<--------Good Bye!-------->");
                        break;
                    default:
                        System.out.println("Please type correct number:");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
