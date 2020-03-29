import java.net.*;
import java.io.*;

/**
 * 
 */
public class BossHandler extends Handler
{
    DataInputStream input;
    /**
     * Constructor for objects of class ServerBossHandler
     */
    public BossHandler(ServerController master, Socket connection)
    {
        super(master, connection);
        try {
            input = new DataInputStream( 
                new BufferedInputStream(connection.getInputStream())); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        master.confirmConnection(this);
        while (uninterrupted)
        {
            try {
                String data = input.readUTF();
                master.handleFlow(data);
            } catch (IOException e) {
                master.severConnection(this);
                uninterrupted = false;
            }
        }
        try {
            connection.close();
        } catch (IOException e) {
            //if unable to close, move on
        }     
    }
    
    @Override
    public String getType()
    {
        return "Boss";
    }

}
