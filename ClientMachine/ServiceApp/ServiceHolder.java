package ClientMachine.ServiceApp;

/**
* ServiceApp/ServiceHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Service.idl
* Monday, November 6, 2017 7:44:51 o'clock PM EST
*/

public final class ServiceHolder implements org.omg.CORBA.portable.Streamable
{
  public Service value = null;

  public ServiceHolder ()
  {
  }

  public ServiceHolder (Service initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = ServiceHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    ServiceHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return ServiceHelper.type ();
  }

}
