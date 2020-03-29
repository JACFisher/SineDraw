import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.FocusManager;

//components:
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComponent;

//layout/spacing:
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import javax.swing.Box;
/**
 * 
 */
public class WorkerView extends JPanel
{
    WorkerController master;

    private int width, height, frequency, amplitude, phaseShift;
    private int centerX, centerY;
    private static final int SPEED = 4;
    private boolean barState;

    Timer timer;
    int phase;

    JFrame frame;
    JPanel statusPanel;
    JLabel statusMessage;
    JLabel statusLight;
    JComponent[] connectionComponents;
    JButton connectButton;
    JTextField ipAddress;
    JTextField port;

    /**
     * Constructor for objects of class WorkerView
     */
    public WorkerView(WorkerController master)
    {
        this.master = master;
        frequency = master.FREQUENCY_INIT;
        amplitude = master.AMP_INIT;
        phaseShift = master.PHASE_INIT;

        buildStatusPanel();
        buildFrame();       

        phase = 0;

        timer = new Timer(SPEED, master);
        timer.start();        
    }

    private void buildFrame()
    {
        frame = new JFrame();
        frame.setSize(450,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("SineMaster");

        BoxLayout frameLayout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
        frame.setLayout(frameLayout);
        frame.add(this);
        frame.add(statusPanel);

        frame.setVisible(true); 
    }

    private void buildStatusPanel()
    {
        statusMessage = new JLabel("  CONNECTION STATUS:  ");
        statusLight = new JLabel("\u2B24");

        statusLight.setForeground(Color.RED);
        statusPanel = new JPanel();

        ipAddress = new JTextField(10) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

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

        connectionComponents = new JComponent[] {ipAddress, port, connectButton};

        BoxLayout statusLayout = new BoxLayout(statusPanel, BoxLayout.X_AXIS);
        statusPanel.setLayout(statusLayout);
        statusPanel.add(statusMessage);
        statusPanel.add(statusLight);
        statusPanel.add(Box.createHorizontalGlue());
        statusPanel.add(ipAddress);
        statusPanel.add(port);
        statusPanel.add(connectButton);
    }

    public void setWave(int frequency, int amplitude, int phaseShift)
    {
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.phaseShift = phaseShift;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        width = getWidth();
        height = getHeight();
        centerX = width / 2;
        centerY = height / 2;

        g.drawLine(0, centerY, width, centerY);

        drawSineWaves(g, phase);
    }

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

    private void setStatusBarDisconnected()
    {
        statusLight.setForeground(Color.RED);
        for (JComponent component : connectionComponents)
        {
            component.setEnabled(true);
        }
    }

    private void setStatusBarConnected()
    {
        statusLight.setForeground(Color.GREEN);
        for (JComponent component : connectionComponents)
        {
            component.setEnabled(false);
        }
    }    

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
            g.drawLine(0, 0, 0, getHeight());

            g.setColor(Color.BLUE);

            g.drawLine(centerX + x1, centerY - y1 - 1, centerX + x1, centerY - y1 - 1);
            g.drawLine(centerX + x1, centerY - y1, centerX + x1, centerY - y1);
            g.drawLine(centerX + x1, centerY - y1 + 1, centerX + x1, centerY - y1 + 1);
        }
    }    
}
