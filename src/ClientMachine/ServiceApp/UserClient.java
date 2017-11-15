package ClientMachine.ServiceApp;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;

public class UserClient {
    static Service service;
    public void run(String[] args) throws Exception {
        try {
            ORB orb = ORB.init(args, null);
            org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");
            NamingContextExt ncRef =
                    NamingContextExtHelper.narrow(objRef);
            boolean flag = true;
            String number = "0";
            System.out.println("<--------Welcome to Distributed Room Reservation System-------->");
            System.out.println("Firstly, please input your ID: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String userID = reader.readLine();
            String campusID = userID.substring(0, 3);
            if (campusID.equals("DVL"))
                service = ServiceHelper.narrow(ncRef.resolve_str("DVL"));
            else if (campusID.equals("KKL"))
                service = ServiceHelper.narrow(ncRef.resolve_str("KKL"));
            else if (campusID.equals("WST"))
                service = ServiceHelper.narrow(ncRef.resolve_str("WST"));
            else {
                System.out.println("WRONG USERID! EXIT!");
                flag = false;
            }
            String date = "";
            String room_Number = "";
            String timeslot = "";
            String bookingID = "";
            String campusName = "";
            String action = "";
            String result = "";
            int length = 0;
            UserLog userLog = new UserLog(userID);
            if (userID.charAt(3) == 'S') {
                System.out.println("Welcome " + userID);
                System.out.println("We provide some services, you could choose service by input number.\nThe services are as follows\n: ");
                System.out.println("1: 'Help'. List services we provide by typing number '1'.");
                System.out.println("2: 'Book Room'. Start to book room by typing number '2'.");
                System.out.println("3: 'Cancel Booking'. Cancel room by typing number '3'.");
                System.out.println("4: 'Get Available Room'. Get available timeslot by typing number'4'.");
                System.out.println("5: 'Change Reservation'. Change reservation by typing number'5'.");
                System.out.println("6: 'Exit'. Log out of the system by typing number'6'.");
                while (flag) {
                    System.out.println("<---------------------------------------------------------------->");
                    System.out.println("Please choose the service you want.");
                    number = reader.readLine();
                    switch (number) {
                        case "1":
                            System.out.println("<--------Following are the services.-------->");
                            System.out.println("1: 'HELP'. List services we provide by typing number '1'.");
                            System.out.println("2: 'Book Room'. Start to book room by typing number '2'.");
                            System.out.println("3: 'Cancel Booking'. Cancel room by typing number '3'.");
                            System.out.println("4: 'Get Available Room'. Get available timeslot by typing number'4'.");
                            System.out.println("5: 'Change Reservation'. Change reservation by typing number'5'.");
                            System.out.println("6: 'Exit'. Log out of the system by typing number'6'.");
                            break;
                        case "2":
                            System.out.println("<--------Start Booking Room!-------->");
                            System.out.println("Please chooose the campus:");
                            campusName = reader.readLine();
                            System.out.println("Please choose the date:");
                            date = reader.readLine();
                            System.out.println("Please choose the room_Number:");
                            room_Number = reader.readLine();
                            System.out.println("Please choose the timeslot:");
                            timeslot = reader.readLine();
                            bookingID = service.bookRoom(userID, campusName, date, room_Number, timeslot);
                            if (bookingID.isEmpty()) {
                                result = "Booking failed.";
                                System.out.println(result);
                            } else {
                                result = "Booking successfully. your bookingID is: " + bookingID;
                                System.out.println(result);
                            }
                            action = "Book Room";

                            break;
                        case "3":
                            System.out.println("<--------Cancelling Booked Room!-------->");
                            System.out.println("Please input the bookingID:");
                            bookingID = reader.readLine();
                            boolean isCancelled = service.cancelBooking(userID, bookingID);
                            if (isCancelled) {
                                result = "The room is cancelled.";
                                System.out.println(result);
                            } else {
                                result = "Cancellation failed.";
                                System.out.println(result);
                            }
                            action = "Cancel Booked Room.";
                            break;
                        case "4":
                            System.out.println("<--------Get Available Room!-------->");
                            System.out.println("Please input date:");
                            date = reader.readLine();
                            String availableRoom = service.getAvailableTimeSlot(date);
                            action = "Get Available Room.";
                            result = "The available rooms are: " + availableRoom;
                            System.out.println(result);
                            break;
                        case "5":
                            System.out.println("<--------Change Reservation!-------->");
                            System.out.println("Please input booking ID:");
                            bookingID = reader.readLine();
                            System.out.println("Please chooose the campus:");
                            campusName = reader.readLine();
                            System.out.println("Please choose the date:");
                            date = reader.readLine();
                            System.out.println("Please choose the room_Number:");
                            room_Number = reader.readLine();
                            System.out.println("Please choose the timeslot:");
                            timeslot = reader.readLine();
                            bookingID = service.changeReservation(bookingID, userID, campusName, date, room_Number, timeslot);
                            if (bookingID.equals("false"))
                                System.out.println("Change reservation is failed!");
                            else
                                System.out.println("Change reservation successfully. your new bookingID is: "+bookingID);
                            break;
                        case "6":
                            flag = false;
                            System.out.println("<--------Good Bye!-------->");
                            break;
                        default:
                            System.out.println("Please type correct number:");
                            break;
                    }
                    if (number.equals("2") || number.equals("3") || number.equals("4"))
                        userLog.writeFile(action, result);
                }
            } else if (userID.charAt(3) == 'A') {
                System.out.println("Welcome " + userID);
                System.out.println("We provide some services, you could choose service by input number.\nThe services are as follows\n: ");
                System.out.println("<---------------------------------------------------------------->");
                System.out.println("Please choose the service you want.");
                System.out.println("1: 'HELP'. List services we provide by typing number '1'.");
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
                            try {
                                length = Integer.parseInt(reader.readLine());
                            } catch (Exception e) {
                                System.out.println("Number is invalid:");
                                break;
                            }
                            System.out.println("Please input the timeslots:");
                            String[] listOfTimeSlotCreate = new String[length];
                            for (int i = 0; i < length; i++) {
                                System.out.print("TimeSlot" + i + ":");
                                listOfTimeSlotCreate[i] = reader.readLine();
                            }
                            int createdRoomNumber = service.createRoom(date, room_Number, listOfTimeSlotCreate);
                            action = "Create Room.";
                            result = "The number of timeslots get created is: " + createdRoomNumber;
                            System.out.println(result);
                            break;
                        case "3":
                            System.out.println("<--------Start Deleting Room!-------->");
                            System.out.println("Please choose the date:");
                            date = reader.readLine();
                            System.out.println("Please choose the room_Number:");
                            room_Number = reader.readLine();
                            System.out.println("Please input the timeslots number:");
                            try {
                                length = Integer.parseInt(reader.readLine());
                            } catch (Exception e) {
                                System.out.println("Number is invalid:");
                                break;
                            }
                            System.out.println("Please input the timeslots:");
                            String[] listOfTimeSlotDelete = new String[length];
                            for (int i = 0; i < length; i++) {
                                System.out.print("TimeSlot" + i + ":");
                                listOfTimeSlotDelete[i] = reader.readLine();
                            }
                            int deletedRoomNumber = service.deleteRoom(date, room_Number, listOfTimeSlotDelete);
                            action = "Delete Room.";
                            result = "The number of timeslots get deleted is: " + deletedRoomNumber;
                            System.out.println(result);
                            break;
                        case "4":
                            flag = false;
                            System.out.println("<--------Good Bye!-------->");
                            break;
                        default:
                            System.out.println("Please type correct number:");
                            break;
                    }
                    if (number.equals("2") || number.equals("3"))
                        userLog.writeFile(action, result);
                }
            } else {
                System.out.println("WRONG USERID! EXIT!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
