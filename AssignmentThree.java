import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class AssignmentThree implements Runnable {
	public void run() {
		GUI program = new GUI();
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new AssignmentThree());
	}
}

class Display extends JPanel{
	public Display() {
		setLayout(null);
	    JLabel volumeLabel = new JLabel("Volume [bytes]", JLabel.LEFT);
	    volumeLabel.setSize(120,60);
	    volumeLabel.setLocation(20,0);
	    add(volumeLabel);
	    
	    JLabel timeLabel = new JLabel("Time [s]", JLabel.LEFT);
	    timeLabel.setSize(120,60);
	    timeLabel.setLocation(450,325);
	    add(timeLabel);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.WHITE);
		g.setColor(Color.BLACK);
		g.drawLine(50, 300, 950, 300);
		for (int i=1; i < 9; i++) {
			g.drawLine(i*100, 300, i*100, 305);
			JLabel numbers = new JLabel(Integer.toString(i*100), JLabel.CENTER);
			numbers.setSize(50,10);
			numbers.setLocation(i*100 - 25, 310);
			add(numbers);
		}
		g.drawLine(50, 50, 50, 300);
		g.drawLine(50, 300, 45, 300);
		JLabel zeroLabel = new JLabel(Integer.toString(0), JLabel.CENTER);
		zeroLabel.setSize(40,20);
		zeroLabel.setLocation(10, 300);
		add(zeroLabel);
	}
}

class GUI extends JFrame {
	private JMenuBar menuBar = new JMenuBar();
    private final JPanel radioButtonPanel = new JPanel();
    private final ButtonGroup radioButtons;
    private final JRadioButton radioButtonSourceHosts;
    private final JRadioButton radioButtonDestinationHosts;
    Display display = new Display();
	
	public GUI() {
    	super("Flow volume viewer");
    	setLayout(null);
    	setMinimumSize(new Dimension(1000,550));
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setVisible(true);	
    	
    	display.setVisible(true);
    	display.setLocation(0,100);
    	display.setSize(this.getWidth(),400);
    	add(display);
    	
    	radioButtonPanel.setLayout(new GridBagLayout());
    	GridBagConstraints constraints = new GridBagConstraints();
        radioButtons = new ButtonGroup();
    	radioButtonSourceHosts = new JRadioButton("Source hosts");
     	radioButtonSourceHosts.setSelected(true);
        radioButtons.add(radioButtonSourceHosts);
        radioButtonPanel.add(radioButtonSourceHosts, constraints);
        radioButtonPanel.setSize(300,85);
        radioButtonPanel.setLocation(0,0);        
    
		radioButtonDestinationHosts = new JRadioButton("Destination hosts");
		radioButtons.add(radioButtonDestinationHosts);
		radioButtonPanel.add(radioButtonDestinationHosts , constraints);
    	add(radioButtonPanel);
    	
    	setJMenuBar(menuBar);
    	JMenu fileMenu = new JMenu("File");    	
    	JMenuItem fileMenuOpen = new JMenuItem("Open trace file (not working)");
    	JMenuItem fileMenuQuit = new JMenuItem("Quit");
    	menuBar.add(fileMenu);
    	fileMenu.add(fileMenuOpen);
    	fileMenu.add(fileMenuQuit);
		fileMenuQuit.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
		}});
	}	   
}