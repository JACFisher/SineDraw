import java.net.*;
import java.io.*;
/**
 * Abstract class Handler.
 */
public abstract class Handler implements Runnable
{
    private static String TYPE;
    ServerController master;
    boolean uninterrupted;
    Socket connection;
    String address;
    DataInputStream inbound;
    DataOutputStream outbound;
    int port;

    /**
     * Constructor for objects of Handler type.
     * 
     * @param master The ServerController which initialized this handler.
     * @param connection The socket to assign to this handler.
     * @param type This handler's type.
     */
    public Handler(ServerController master, Socket connection, String type)
    {
        this.master = master;
        this.connection = connection;
        address = connection.getInetAddress().toString();
        port = connection.getLocalPort();
        uninterrupted = true;
        this.TYPE = type;
    }

    /**
     * Returns this handler's type and address.
     * 
     * @return String This handler's type and address.
     */
    @Override
    public String toString()
    {
        String me = String.format("%s%s\n%s%s\n",
                "[TYPE]: ", getType(),
                "[ADDRESS]: ", address
            );
        return me;
    }

    /**
     * Utility to determine the appropriate handler type for the given socket.  
     * Target connections will transmit one int first as an id.
     * 
     * @param s Socket to identify 
     */
    public static int determineType(Socket s)
    {
        try {
            DataInputStream inbound = new DataInputStream(s.getInputStream());
            return inbound.readInt();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("here!");
        }
        return -1; //-1 for failure to read inbound value
    }

    /**
     * Attempt to close this handler's socket
     */
    public void kill()
    {
        try {
            if (connection != null) connection.close();
        } catch (IOException e) {
            //acceptable exception
        }
    }

    /**
     * Return this handler's type in String form
     * 
     * @return String This handler's type.
     */
    public String getType()
    {
        return TYPE;
    }
}
