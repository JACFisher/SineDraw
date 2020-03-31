import java.net.*;
import java.io.*;
/**
 * Handles the connection and data flow between this client and
 * the server.
 */
public class WorkerIn implements Runnable
{
    WorkerController master;
    Socket connection;
    DataInputStream input;
    DataOutputStream output;
    boolean uninterrupted;
    private static final int WORKER_ID = 2;
    
    /**
     * Constructor for objects of class BossOut
     */
    public WorkerIn(WorkerController master, String address, int port)
    {
        this.master = master;
        uninterrupted = true;
        try {
            connection = new Socket(address, port);
            input = new DataInputStream(connection.getInputStream());
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
            output.writeInt(WORKER_ID);
            output.flush();
        } catch (IOException e) {
            //common cause: server shutdown
        } catch (NullPointerException e) {
            //common cause: invalid address or port number
        }
        while (uninterrupted)
        {
            try {
                String data = input.readUTF();
                master.parseInput(data);                
            } catch (IOException e) {
                uninterrupted = false;
            }
        } 
        try {
            if (connection != null)
            {
                connection.close();
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }     
    }        

    /**
     * Returns the status of the connection
     * 
     * @return boolean True if connected; false if disconnected
     */
    public boolean isConnected()
    {
        return uninterrupted;
    }
}
