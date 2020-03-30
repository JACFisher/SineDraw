/**
 * Contains main method for SineDrawServer
 */
public class ServerApplication
{
    public static void main(String[] args)
    {
        if (args.length > 0)
        {
            int minimumPort = 1024; //minimum unreserved port
            int maximumPort = 65535; //maximum port
            try {
                int port = Integer.parseInt(args[0]);
                if (port < minimumPort || port > maximumPort) 
                {
                    throw new IllegalArgumentException();
                } else {
                    ServerController server = new ServerController(port);
                    Thread serverThread = new Thread(server);
                    serverThread.start();
                }
            } catch (NumberFormatException e) { //if Integer cannot parse args[0] into int
                System.out.printf("%s%s\n%s\n",
                    "You gave port number: ", args[0],
                    "This is not a valid port number."
                );
                System.exit(1);
            } catch (IllegalArgumentException e) { //if port is lower than 1024 or higher than 65536
                System.out.printf("%s%s\n%s%d%s%d\n%s\n",
                    "You gave port number: ", args[0],
                    "The usable range is between ", minimumPort, " and ", maximumPort,
                    "Please choose within this range, or enter no arguments to let the application choose automatically."
                );
                System.exit(2);
            }
        } else {
            ServerController server = new ServerController();
            Thread serverThread = new Thread(server);
            serverThread.start();
        }
    }
}
