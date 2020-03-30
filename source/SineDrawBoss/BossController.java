import javax.swing.JSlider;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Creates the View and the Model.  Handles user interaction with sliders,
 * buttons, and the connection bar.  Updates the model based on user
 * input; updates the view at regular intervals with data from the model.
 */

public class BossController implements ActionListener, ChangeListener
{
    BossView view;
    BossModel model;
    BossOut outboundSocket;
    static final int PHASE_MIN = -180, PHASE_INIT = 0, PHASE_MAX = 180; //will overlap at each extreme
    static final int AMP_MIN = 0, AMP_INIT = 50, AMP_MAX = 100;
    static final int FREQUENCY_MIN = 1, FREQUENCY_INIT = 5, FREQUENCY_MAX = 10;    
    
    /**
     * Constructor for objects of class BossController.
     */
    public BossController()
    {
        model = new BossModel(FREQUENCY_INIT, AMP_INIT, PHASE_INIT);
        view = new BossView(this);
    }

    /**
     * Connect to the given address and port.
     */
    public void connect(String address, int port)
    {
        outboundSocket = new BossOut(this, address, port);
        Thread socketThread = new Thread(outboundSocket);
        socketThread.start();        
    }

    /**
     * Handles state changes of the sliders in view.
     * Reads their value and updates the model.
     */
    public void stateChanged(ChangeEvent event)
    {
        JSlider source = (JSlider) event.getSource();

        int sliderValue = source.getValue();
        if (source == view.phaseShiftSlider)
        {
            model.setPhaseShift(sliderValue);
        }
        if (source == view.amplitudeSlider)
        {
            model.setAmplitude(sliderValue);
        }
        if (source == view.frequencySlider)
        {
            model.setFrequency(sliderValue);
        }
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
     * Updates the components in the view.
     */
    private void refreshView()
    {
        view.setWave(model.getFrequency(), model.getAmplitude(), model.getPhaseShift());
        view.phase++;
        view.ipAddress.repaint();
        view.port.repaint();
        view.repaint();
        if (outboundSocket != null) 
        {
            view.updateStatusBar(outboundSocket.isConnected());
        }

        if(view.phase >= 360) {
            view.phase = 0;
        }
    }

    /**
     * Fetch the data from the model.
     */
    public String getWave()
    {
        String data = String.format("%d,%d,%d",
                model.getFrequency(), model.getAmplitude(), model.getPhaseShift());
        return data;
    }       
}
