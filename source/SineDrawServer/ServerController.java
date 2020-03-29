import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Write a description of class ServerController here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class ServerController implements Runnable
{
    boolean serverRunning;
    ServerSocket serverSocket;
    BossHandler boss;
    ArrayList<WorkerHandler> workers;
    ServerModel model;
    ServerListener userListener;
    private int inboundPort;
    Scanner scan;

    /**
     * Constructor for objects of class ServerController
     */
    public ServerController()
    {
        this(0);
        System.out.printf("%s\n%s\n%s\n%s\n",
            "-----------------------------------------------------------------",
            "Port will be automaically assigned.  Usage for choosing the port:",
            "$ java -jar SineServer.jar [PORT]",
            "-----------------------------------------------------------------"
        );
    }

    public ServerController(int inboundPort)
    {
        userListener = new ServerListener(this);
        serverRunning = true;
        model = new ServerModel();
        workers = new ArrayList<WorkerHandler>();
        this.inboundPort = inboundPort;
        scan = new Scanner(System.in);
    }

    public void run()
    {   
        Thread listenerThread = new Thread(userListener);
        listenerThread.start();
        try {
            serverSocket = new ServerSocket(inboundPort);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(3); //exit if unable to establish socket - no point in continuing
        }
        if (inboundPort == 0) 
        {
            inboundPort = serverSocket.getLocalPort(); //if these numbers are 0, java will automatically assign a port
        }
        System.out.printf("%s%d\n%s\n%s\n\n",
            "Listening on port: ", inboundPort,
            "To list active connections, enter \"list\".",
            "To shutdown, enter \"exit\"."
        );
        try {
            while (serverRunning)
            {

                Socket socket = serverSocket.accept();
                buildHandler(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(4); //Although program terminates here, provide data
        }
    }

    private void buildHandler(Socket socket)
    {
        int type = Handler.determineType(socket);
        if (type == 1)
        {
            boss = new BossHandler(this, socket);
            Thread bossThread = new Thread(boss);
            bossThread.start();
        } else if (type == 2) {
            WorkerHandler nextHandler = new WorkerHandler(this, socket);
            workers.add(nextHandler);
            Thread nextWorkerThread = new Thread(nextHandler);
            nextWorkerThread.start();
        } else {
            System.out.printf("%s\n",
                "An unrecognized service has tried to connect"); 
        }
    }

    public void confirmConnection(Handler born)
    {
        System.out.printf("%s\n%s\n",
            "New client connected.", 
            born.toString()
        );
    }

    public void severConnection(Handler dead)
    {
        System.out.printf("%s\n%s\n",
            "Client disconnected.",
            dead.toString()
        );
        if (dead instanceof WorkerHandler)
        {
            workers.remove(dead);
        } else if (dead instanceof BossHandler) {
            boss = null; //this keeps the boss from appearing in the connection list!
        }
    }

    public void end()
    {
        serverRunning = false;
        if (boss != null) boss.kill();
        for (WorkerHandler worker : workers)
        {
            worker.kill();
        }
        System.out.printf("Good night.\n");
        System.exit(0); //happens outside of run, as run is waiting for a socket!
    }

    public void printConnections()
    {
        if (boss == null && workers.size() == 0)
        {
            System.out.printf("%s\n\n", "No current connections.");
        } else {
            System.out.printf("%s\n", "Currently connected clients:");
        }
        if (boss != null)
        {
            System.out.printf("%s\n", boss.toString());
        }
        for (WorkerHandler worker : workers)
        {
            System.out.printf("%s\n", worker.toString());            
        }
    }

    public String handleFlow()
    {
        return model.getData();
    }

    public void handleFlow(String data)
    {
        model.setData(data);
    }
}
