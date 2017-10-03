package rmi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;

public class StudentClient1 {
    public static void main(String[] args) throws Exception {
        try {
            boolean flag = true;
            String number = "0";
            StudentServiceInterface studentService = (StudentServiceInterface) Naming.lookup("rmi://localhost:6000/DVLStudent");
            System.out.println("<--------Welcome to Distributed Room Reservation System-------->");
            System.out.println("Firstly, please input your ID: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String studentID = reader.readLine();
            String campusID = studentID.substring(0,4);
            switch (campusID){
                case "DVLA":
                case "KKLA":
                case"WSTA":
                case "DVLS":
                case "KKLS":
                case "KSTS":
                    default:
                        System.out.println("WRONG StudentID! Exit!");
                        flag = false;
            }
            if (campusID.equals("DVL"))
                AdminService = (AdminServiceInterface) Naming.lookup("rmi://localhost:6001/DVLAdmin");
            else if (campusID.equals("KKL"))
                AdminService = (AdminServiceInterface) Naming.lookup("rmi://localhost:7001/KKLAdmin");
            else if (campusID.equals("WST"))
                AdminService = (AdminServiceInterface) Naming.lookup("rmi://localhost:8001/KKLAdmin");
            if (campusID.equals("DVL"))
                studentService = (StudentServiceInterface) Naming.lookup("rmi://localhost:6000/DVLStudent");
            else if (campusID.equals("KKL"))
                studentService = (StudentServiceInterface) Naming.lookup("rmi://localhost:7000/KKLStudent");
            else if (campusID.equals("WST"))
                studentService = (StudentServiceInterface) Naming.lookup("rmi://localhost:8000/KKLStudent");
            else {
            }
            String date = "";
            String room_Number = "";
            String timeslot = "";
            String bookingID = "";
            System.out.println("Welcome " + studentID);
            System.out.println("We provide some services, you could choose service by input number.\nThe services are as follows\n: ");
            System.out.println("1: 'Help'. List services we provide by typing number '1'.");
            System.out.println("2: 'Book Room'. Start to book room by typing number '2'.");
            System.out.println("3: 'Cancel Booking'. Cancel room by typing number '3'.");
            System.out.println("4: 'Get Available Room'. Get available timeslot by typing number'4'.");
            System.out.println("5: 'Exit'. Log out of the system by typing number'5'.");
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
                        System.out.println("5: 'Exit'. Log out of the system by typing number'5'.");
                        break;
                    case "2":
                        System.out.println("<--------Start Booking Room!-------->");
                        System.out.println("Please choose the date:");
                        date = reader.readLine();
                        System.out.println("Please choose the room_Number:");
                        room_Number = reader.readLine();
                        System.out.println("Please choose the timeslot:");
                        timeslot = reader.readLine();
                        bookingID = studentService.bookRoom(studentID, campusID, date, room_Number, timeslot);
                        if (bookingID.isEmpty())
                            System.out.println("Booking failed.");
                        else
                            System.out.println("Booking successfully. your bookingID is: " + bookingID);
                        break;
                    case "3":
                        System.out.println("<--------Cancelling Booked Room!-------->");
                        System.out.println("Please input the bookingID:");
                        bookingID = reader.readLine();
                        boolean isCancelled = studentService.cancelBooking(studentID, bookingID);
                        if (isCancelled)
                            System.out.println("The room is cancelled.");
                        else
                            System.out.println("Cancellation failed.");
                        break;
                    case "4":
                        System.out.println("<--------Get Available Room!-------->");
                        System.out.println("Please input date:");
                        date = reader.readLine();
                        String availableRoom = studentService.getAvailableTimeSlot(date);
                        System.out.println("The available rooms are: " + availableRoom);
                        break;
                    case "5":
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
