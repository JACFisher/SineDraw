import java.util.Scanner;

/**
 * 
 */
public class ServerListener implements Runnable
{
    ServerController master;
    Scanner in;

    public ServerListener(ServerController master)
    {
        this.master = master;

        in = new Scanner(System.in);
    }

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

