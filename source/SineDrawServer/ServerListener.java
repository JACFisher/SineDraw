import java.util.Scanner;
/**
 * Waits for input from the user through System.in and interprets it for ServerController.
 */
public class ServerListener implements Runnable
{
    ServerController master;
    Scanner in;

    /**
     * Constructor for objects of type ServerListener
     */
    public ServerListener(ServerController master)
    {
        this.master = master;

        in = new Scanner(System.in);
    }

    /**
     * Main loop.  Waits for user input, interprets it, and selects an appropriate response
     * for ServerController.
     */
    public void run()
    {
        boolean running = true;
        while (running)
        {
            String inbound = in.nextLine().toUpperCase();
            if (inbound.equals("EXIT"))
            {
                System.out.println(); //space for formatting
                in.close();
                running = false;
                master.end();                
            } else if (inbound.equals("LIST"))
                System.out.println(); //space for formatting
                master.printConnections();
        }
    }        
}

