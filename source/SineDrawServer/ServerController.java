import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Creates the Model; continuously creates Handlers based on new connections; 
 * Controls the flow of information between Handlers and the Model.
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
     * Empty onstructor for objects of class ServerController.  Calls the one int constructor
     * with 0 as argument (allows Java to choose port) then prints command line usage message.
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

    /**
     * One int constructor for objects of class ServerController.
     * 
     * @param inboundPort The port for this server to listen on.
     */
    public ServerController(int inboundPort)
    {
        userListener = new ServerListener(this);
        serverRunning = true;
        model = new ServerModel();
        workers = new ArrayList<WorkerHandler>();
        this.inboundPort = inboundPort;
        scan = new Scanner(System.in);
    }

    /**
     * Initializes the ServerListener, ServerSocket, displays server use instructions,
     * then iterates over the main loop of the
     * program.  Repeatedly accepts connections and builds handlers.
     */
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

    /**
     * Assigns handlers to the given socket.  If WorkerHandler, add to workers.  If
     * unrecognized data, display message and discard.
     * 
     * @param Socket A socket which needs a handler.
     */
    private void buildHandler(Socket socket)
    {
        int type = Handler.determineType(socket);
        if (type == Handler.bossID())
        {
            boss = new BossHandler(this, socket);
            Thread bossThread = new Thread(boss);
            bossThread.start();
        } else if (type == Handler.workerID()) {
            WorkerHandler nextHandler = new WorkerHandler(this, socket);
            workers.add(nextHandler);
            Thread nextWorkerThread = new Thread(nextHandler);
            nextWorkerThread.start();
        } else {
            try {
                socket.close();
            } catch (Exception e) {
                // attempt to close, but continue if unable.  Do not let unwanted connection
                // interrupt work flow.
            }
            System.out.printf("%s\n",
                "An unrecognized service has tried to connect"); 
        }
    }

    /**
     * Displays a connection message.
     */
    public void confirmConnection(Handler born)
    {
        System.out.printf("%s\n%s\n",
            "New client connected.", 
            born.toString()
        );
    }

    /**
     * Displays a message for disconnection and handles cleanup.
     */
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

    /**
     * Valid termination of program.  Called from ServerListener when user requests exit.
     * Attempts to close each connection, displays send-off message, exits with code 0.
     */
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

    /**
     * Print each current connection.
     */
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

    /**
     * Handles data flow.  No argument returns model data.
     * 
     * @return String The data stored in the model.
     */
    public String handleFlow()
    {
        return model.getData();
    }

    /**
     * Handles data flow.  String argument sets model data.
     * 
     * @param data Current data to update the model.
     */
    public void handleFlow(String data)
    {
        model.setData(data);
    }
}
