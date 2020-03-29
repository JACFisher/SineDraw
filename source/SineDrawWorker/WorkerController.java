import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
 * 
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

    public void connect(String address, int port)
    {
        inboundSocket = new WorkerIn(this, address, port);
        Thread socketThread = new Thread(inboundSocket);
        socketThread.start();        
    }
    
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

    public void parseInput(String input)
    {
        String[] split = input.split(",", 3);
        int frequency = Integer.parseInt(split[0]);
        int amplitude = Integer.parseInt(split[1]);
        int phaseShift =  Integer.parseInt(split[2]);
        model.setWave(frequency,amplitude,phaseShift);
    }
}
