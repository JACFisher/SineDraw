import java.net.*;
import java.io.*;

/**
 * 
 */
public class BossHandler extends Handler
{
    DataInputStream input;   
    /**
     * Constructor for objects of class BossHandler.
     * 
     * @param master The ServerController which initialized this handler.
     * @param connection The socket to assign to this handler.
     */
    public BossHandler(ServerController master, Socket connection)
    {
        super(master, connection, "Boss");
        try {
            input = new DataInputStream( 
                new BufferedInputStream(connection.getInputStream())); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Confirm connection to ServerController then start the main loop of BossHandler.  Read
     * UTF data through connection and send that data to ServerController to update the model.
     * 
     */
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
}
