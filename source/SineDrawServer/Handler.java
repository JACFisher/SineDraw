import java.net.*;
import java.io.*;
/**
 * Abstract class Handler - write a description of the class here
 *
 * @author (your name here)
 * @version (version number or date here)
 */
public abstract class Handler implements Runnable
{
    ServerController master;
    boolean uninterrupted;
    Socket connection;
    String address;
    DataInputStream inbound;
    DataOutputStream outbound;
    int port;

    public Handler(ServerController master, Socket connection)
    {
        this.master = master;
        this.connection = connection;
        address = connection.getInetAddress().toString();
        port = connection.getLocalPort();
        uninterrupted = true;
    }

    @Override
    public String toString()
    {
        String me = String.format("%s%s\n%s%s\n",
                "[TYPE]: ", getType(),
                "[ADDRESS]: ", address
            );
        return me;
    }

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

    public void kill()
    {
        try {
            if (connection != null) connection.close();
        } catch (IOException e) {
            //acceptable exception
        }
    }

    abstract public String getType();
}
