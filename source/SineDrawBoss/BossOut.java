import java.net.*;
import java.io.*;
public class BossOut implements Runnable
{
    BossController master;
    Socket connection;
    DataOutputStream output;
    boolean uninterrupted;
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

    public void run()
    {
        try {
            output.writeInt(1);
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
                uninterrupted = false;
            }
        }        
        try {
            if (connection != null) connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected()
    {
        return uninterrupted;
    }
}
