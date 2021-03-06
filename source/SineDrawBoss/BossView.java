import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.FocusManager;

//components:
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

//layout/spacing:
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import javax.swing.Box;

/**
 * The interactable display for the user.
 */

public class BossView extends JPanel
{
    BossController master;

    private int width, height, frequency, amplitude, phaseShift;
    private int centerX, centerY;
    private static final int SPEED = 4;
    private boolean barState; // state of the status bar; true for connected

    Timer timer;
    int phase;

    JFrame frame;
    JSlider phaseShiftSlider;
    JSlider amplitudeSlider;
    JSlider frequencySlider;
    JPanel statusPanel;
    JLabel statusMessage;
    JLabel statusLight;
    JComponent[] connectionComponents;
    JButton connectButton;
    JTextField ipAddress;
    JTextField port;

    /**
     * Constructor for objects of class BossView
     */
    public BossView(BossController master) {
        this.master = master;
        frequency = master.FREQUENCY_INIT;
        amplitude = master.AMP_INIT;
        phaseShift = master.PHASE_INIT;
        phase = 0;

        buildSliders();
        buildStatusPanel();
        buildFrame();       

        timer = new Timer(SPEED, master);
        timer.start();
    }

    /**
     * Build the sliders and attach listeners
     */
    private void buildSliders()
    {
        phaseShiftSlider = new JSlider(JSlider.HORIZONTAL, master.PHASE_MIN, master.PHASE_MAX, phaseShift);
        phaseShiftSlider.addChangeListener(master);
        phaseShiftSlider.setMajorTickSpacing(1);
        phaseShiftSlider.setPaintTicks(true);

        amplitudeSlider = new JSlider(JSlider.HORIZONTAL, master.AMP_MIN, master.AMP_MAX, amplitude);
        amplitudeSlider.addChangeListener(master);
        amplitudeSlider.setMajorTickSpacing(1);
        amplitudeSlider.setPaintTicks(true);

        frequencySlider = new JSlider(JSlider.HORIZONTAL, master.FREQUENCY_MIN, master.FREQUENCY_MAX, frequency);
        frequencySlider.addChangeListener(master);
        frequencySlider.setMajorTickSpacing(1);
        frequencySlider.setPaintTicks(true);  
    }

    /**
     * Build the connection status panel
     */
    private void buildStatusPanel()
    {
        statusMessage = new JLabel(" CONNECTION STATUS:  ");
        statusLight = new JLabel("\u2B24");

        statusLight.setForeground(Color.RED);
        statusPanel = new JPanel();

        ipAddress = new JTextField(10) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); //placeholder text that disappears on focus; top accepted answer on stack overflow, originates from oracle forums
                if(getText().isEmpty() && ! (FocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == this)){
                    Graphics2D g2 = (Graphics2D)g.create();
                    g2.setBackground(Color.gray);
                    g2.drawString("IP ADDRESS", 5, 15);
                    g2.dispose();
                }
            }
        };
        ipAddress.setMinimumSize(new Dimension(60,30));
        ipAddress.setMaximumSize(new Dimension(60,30));

        port = new JTextField(5) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if(getText().isEmpty() && ! (FocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == this)){
                    Graphics2D g2 = (Graphics2D)g.create();
                    g2.setBackground(Color.gray);
                    g2.drawString("PORT", 5, 15);
                    g2.dispose();
                }
            }        
        };
        port.setMinimumSize(new Dimension(40,30));
        port.setMaximumSize(new Dimension(40,30));     

        connectButton = new JButton("Connect");
        connectButton.addActionListener(master);

        connectionComponents = new JComponent[] {ipAddress, port, connectButton}; //I am lazy and want to iterate over these

        BoxLayout statusLayout = new BoxLayout(statusPanel, BoxLayout.X_AXIS);
        statusPanel.setLayout(statusLayout);
        statusPanel.add(statusMessage);
        statusPanel.add(statusLight);
        statusPanel.add(Box.createHorizontalGlue());
        statusPanel.add(ipAddress);
        statusPanel.add(port);
        statusPanel.add(connectButton);
    }

    /**
     * Builds the frame and adds components
     */
    private void buildFrame()
    {
        frame = new JFrame();
        frame.setSize(450,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("SineDrawBoss");

        BoxLayout frameLayout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
        frame.setLayout(frameLayout);
        frame.add(this);
        frame.add(phaseShiftSlider);
        frame.add(amplitudeSlider);
        frame.add(frequencySlider);
        frame.add(statusPanel);

        frame.setVisible(true); 
    }

    /**
     * Sets the frequency, amplitude, and phase shift.
     * 
     * @param frequency The sine wave's frequency
     * @param amplitude The sine wave's amplitude
     * @param phaseShift The sine wave's phase shift
     */
    public void setWave(int frequency, int amplitude, int phaseShift)
    {
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.phaseShift = phaseShift;
    }

    /**
     * Determine the current state of connection and update 
     * the status bar if appropriate.
     * 
     * @param status The connection status (true: connected; false: disconnected)
     */
    public void updateStatusBar(boolean status)
    {
        if (status != barState)
        {
            if (barState)
            {
                setStatusBarDisconnected(); // if barState was true (connected), update to disco
            } else {
                setStatusBarConnected();
            }
            barState = !barState; // toggle at end
        }
    }

    /**
     * Set the status bar to disconnected state.
     */
    private void setStatusBarDisconnected()
    {
        statusLight.setForeground(Color.RED);
        for (JComponent component : connectionComponents)
        {
            component.setEnabled(true);
        }
    }

    /**
     * Set the status bar to connected state.
     */
    private void setStatusBarConnected()
    {
        statusLight.setForeground(Color.GREEN);
        for (JComponent component : connectionComponents)
        {
            component.setEnabled(false);
        }
    }

    /**
     * Paint the view with an x and y axis followed by the sine wave
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        width = getWidth();
        height = getHeight();
        centerX = width / 2;
        centerY = height / 2;

        g.fillRect(0, centerY-1, width, 3);
        g.fillRect(0, 0, 2, getHeight());

        drawSineWaves(g, phase);
    }

    /**
     * Paint the sine wave based on the current phase
     * 
     * @param phase The current phase of the wave
     */
    private void drawSineWaves(Graphics g, int phase) {
        for(double x = -2 * centerX; x <= 2 * centerX; x = x + 0.5) {
            double y = amplitude * Math.sin((x + phase) * frequency * (Math.PI / 180));

            int x1 = (int) x + phaseShift;
            int y1 = (int) y;
            int x2 = (int) x;

            g.setColor(Color.RED);
            g.drawLine(centerX + x2, centerY - y1 - 1, centerX + x2, centerY - y1 - 1);
            g.drawLine(centerX + x2, centerY - y1, centerX + x2, centerY - y1);
            g.drawLine(centerX + x2, centerY - y1 + 1, centerX + x2, centerY - y1 + 1);

            g.setColor(Color.BLUE);
            g.drawLine(centerX + x1, centerY - y1 - 1, centerX + x1, centerY - y1 - 1);
            g.drawLine(centerX + x1, centerY - y1, centerX + x1, centerY - y1);
            g.drawLine(centerX + x1, centerY - y1 + 1, centerX + x1, centerY - y1 + 1);
        }
    }    
}