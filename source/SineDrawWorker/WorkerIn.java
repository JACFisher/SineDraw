import java.net.*;
import java.io.*;
public class WorkerIn implements Runnable
{
    WorkerController master;
    Socket connection;
    DataInputStream input;
    DataOutputStream output;
    boolean uninterrupted;
    
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

    public void run()
    {
        try {
            output.writeInt(2);
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

    public boolean isConnected()
    {
        return uninterrupted;
    }
}
