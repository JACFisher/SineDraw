import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
 * Creates the View and the Model.  Handles user interaction with the connection bar.
 * Updates the view at regular intervals with data from the model.  Acts as a connection
 * between WorkerIn and the Model.
 */
public class WorkerController implements ActionListener
{
    WorkerView view;
    WorkerModel model;
    WorkerIn inboundSocket;
    static final int PHASE_INIT = 0; //will overlap at each extreme
    static final int AMP_INIT = 50;
    static final int FREQUENCY_INIT = 5;     

    /**
     * Constructor for objects of class WorkerController
     */
    public WorkerController()
    {
        model = new WorkerModel();
        view = new WorkerView(this);    
    }

    /**
     * Connect to the given address and port.
     */
    public void connect(String address, int port)
    {
        inboundSocket = new WorkerIn(this, address, port);
        Thread socketThread = new Thread(inboundSocket);
        socketThread.start();        
    }

    /**
     * Handles action events.  Timer refreshes the view;
     * connect button clicks parse data in the text fields
     * and attempt to establish a connection.
     */
    public void actionPerformed(ActionEvent event) 
    {
        if (event.getSource() == view.timer)
        {
            refreshView();
        } else if (event.getSource() == view.connectButton)
        {
            try {
                String address = view.ipAddress.getText();
                int port = Integer.parseInt(view.port.getText());
                connect(address, port);
            } catch (Exception e) {
                //if the user enters unusable text, do nothing
            }
        }
    }

    /**
     * Updates the components in the view; includes an update to the model to adjust
     * closer to the target data (allows for a smoother transition in the view).
     */
    private void refreshView()
    {
        model.adjustFrequency();
        model.adjustPhaseShift();
        model.adjustAmplitude();
        view.phase++;
        view.setWave(model.getFrequency(), model.getAmplitude(), model.getPhaseShift());
        if (inboundSocket != null) 
        {
            view.updateStatusBar(inboundSocket.isConnected());
        }
        view.ipAddress.repaint();
        view.port.repaint();
        view.repaint();

        if(view.phase >= 360) {
            view.phase = 0;
        }
    }

    /**
     * Parse the data received from the server and updates the model.
     */
    public void parseInput(String input)
    {
        String[] split = input.split(",", 3);
        int frequency = Integer.parseInt(split[0]);
        int amplitude = Integer.parseInt(split[1]);
        int phaseShift =  Integer.parseInt(split[2]);
        model.setWave(frequency,amplitude,phaseShift);
    }
}
