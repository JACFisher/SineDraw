import java.net.*;
import java.io.*;
/**
 * 
 */
public class WorkerHandler extends Handler
{
    DataOutputStream output;   
    /**
     * Constructor for objects of class ServerSocketHandler
     */
    public WorkerHandler(ServerController master, Socket connection)
    {
        super(master,connection);
        try {
             output = new DataOutputStream( 
                new BufferedOutputStream(connection.getOutputStream())); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    
    @Override
    public String getType()
    {
        return "Worker";
    }
}
