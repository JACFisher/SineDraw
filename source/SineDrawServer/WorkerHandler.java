import java.net.*;
import java.io.*;
/**
 * 
 */
public class WorkerHandler extends Handler
{
    DataOutputStream output;
    private static final String TYPE = "Worker";
    /**
     * Constructor for objects of class WorkerHandler
     * 
     * @param master The ServerController which initialized this handler.
     * @param connection The socket to assign to this handler.
     */
    public WorkerHandler(ServerController master, Socket connection)
    {
        super(master, connection, "Worker");
        try {
             output = new DataOutputStream( 
                new BufferedOutputStream(connection.getOutputStream())); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Confirm connection to ServerController then start the main loop of WorkerHandler.  Reads
     * data from the model and sends that data in UTF format through the connection.
     */
    public void run()
    {
        master.confirmConnection(this);
        while (uninterrupted)
        {
            try {
                output.writeUTF(master.handleFlow());
                output.flush();
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
