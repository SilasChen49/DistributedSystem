package ClientMachine.ServiceApp;


/**
* ServiceApp/_ServiceStub.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Service.idl
* Monday, November 6, 2017 7:44:51 o'clock PM EST
*/

public class _ServiceStub extends org.omg.CORBA.portable.ObjectImpl implements Service
{

  public String sayHello ()
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("sayHello", true);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return sayHello (        );
            } finally {
                _releaseReply ($in);
            }
  } // sayHello

  public String bookRoom (String studentID, String campusName, String date, String room_Number, String timeslot)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("bookRoom", true);
                $out.write_string (studentID);
                $out.write_string (campusName);
                $out.write_string (date);
                $out.write_string (room_Number);
                $out.write_string (timeslot);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return bookRoom (studentID, campusName, date, room_Number, timeslot        );
            } finally {
                _releaseReply ($in);
            }
  } // bookRoom

  public String getAvailableTimeSlot (String date)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("getAvailableTimeSlot", true);
                $out.write_string (date);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return getAvailableTimeSlot (date        );
            } finally {
                _releaseReply ($in);
            }
  } // getAvailableTimeSlot

  public boolean cancelBooking (String studentID, String bookingID)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("cancelBooking", true);
                $out.write_string (studentID);
                $out.write_string (bookingID);
                $in = _invoke ($out);
                boolean $result = $in.read_boolean ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return cancelBooking (studentID, bookingID        );
            } finally {
                _releaseReply ($in);
            }
  } // cancelBooking

  public int createRoom (String date, String room_Number, String[] list_Of_Time_Slots)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("createRoom", true);
                $out.write_string (date);
                $out.write_string (room_Number);
                TimeSeqHelper.write ($out, list_Of_Time_Slots);
                $in = _invoke ($out);
                int $result = $in.read_long ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return createRoom (date, room_Number, list_Of_Time_Slots        );
            } finally {
                _releaseReply ($in);
            }
  } // createRoom

  public int deleteRoom (String date, String room_Number, String[] list_Of_Time_Slots)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("deleteRoom", true);
                $out.write_string (date);
                $out.write_string (room_Number);
                TimeSeqHelper.write ($out, list_Of_Time_Slots);
                $in = _invoke ($out);
                int $result = $in.read_long ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return deleteRoom (date, room_Number, list_Of_Time_Slots        );
            } finally {
                _releaseReply ($in);
            }
  } // deleteRoom

  public String changeReservation (String booking_id, String studentID, String campusName, String date, String room_Number, String timeslot)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("changeReservation", true);
                $out.write_string (booking_id);
                $out.write_string (studentID);
                $out.write_string (campusName);
                $out.write_string (date);
                $out.write_string (room_Number);
                $out.write_string (timeslot);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return changeReservation (booking_id, studentID, campusName, date, room_Number, timeslot        );
            } finally {
                _releaseReply ($in);
            }
  } // changeReservation

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:ServiceApp/Service:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }

  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException
  {
     String str = s.readUTF ();
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     org.omg.CORBA.Object obj = orb.string_to_object (str);
     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();
     _set_delegate (delegate);
   } finally {
     orb.destroy() ;
   }
  }

  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException
  {
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     String str = orb.object_to_string (this);
     s.writeUTF (str);
   } finally {
     orb.destroy() ;
   }
  }
} // class _ServiceStub
