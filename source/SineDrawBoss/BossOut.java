import java.net.*;
import java.io.*;

/**
 * Handles the connection and data flow between this client
 * and the server.
 */
public class BossOut implements Runnable
{
    BossController master;
    Socket connection;
    DataOutputStream output;
    boolean uninterrupted;
    private static final int BOSS_ID = 1;
    
    /**
     * Constructor for objects of class BossOut
     */
    public BossOut(BossController master, String address, int port)
    {
        this.master = master;
        uninterrupted = true;
        try {
            connection = new Socket(address, port);
            output = new DataOutputStream(connection.getOutputStream());
        } catch (Exception e) {
            uninterrupted = false;
        }
    }

    /**
     * Sends an id code then sends the model data through the connection.
     */
    public void run()
    {
        try {
            output.writeInt(BOSS_ID);
            output.flush();
        } catch (IOException e) {
            //common cause: server shutdown
            uninterrupted = false;
        } catch (NullPointerException e) {
            //common cause: invalid address or port number
            uninterrupted = false;
        } 
        while (uninterrupted)
        {
            try {
                output.writeUTF(master.getWave());
                output.flush();
            } catch (IOException e) {
                uninterrupted = false; //if server shutds down, terminate
            }
        }        
        try {
            if (connection != null) connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the status of the connection
     * 
     * @return boolean True if connected; false if disconnected
     */
    public boolean isConnected()
    {
        return uninterrupted;
    }
}
