import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.awt.*;
/**
 * @author Saqib Zaidi Sahib
 * @paper COMPSCI 230
 * @AUID 222479856
 */
public class Viewer extends JFrame  {
	private static final long serialVersionUID = 1L;
	class Display extends JPanel {
		private static final long serialVersionUID = 1L;
		JLabel label = new JLabel("600", JLabel.CENTER);
		JLabel leftNumber = new JLabel(Integer.toString(0), JLabel.CENTER);
		public Display() {
			setLayout(null);
		    JLabel lab1 = new JLabel("Volume [bytes]", JLabel.LEFT);
		    lab1.setSize(100,50);
		    lab1.setLocation(10,0);
		    add(lab1);
		    
		    JLabel timeLabel = new JLabel("Time [s]", JLabel.LEFT);
		    timeLabel.setSize(100,50);
		    timeLabel.setLocation(480,300);
		    add(timeLabel);   
		}
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBackground(Color.WHITE);
			g.setColor(Color.BLACK);			
		    label.setSize(50,10);
		    label.setLocation(925,310);
		    add(label);
			g.drawLine(50, 300, 950, 300);
			g.drawLine(50, 50, 50, 300);
			g.drawLine(50, 300, 45, 300);
			leftNumber.setSize(30,10);
			leftNumber.setLocation(20, 295);
			add(leftNumber);
			g.setColor(Color.RED);
			for (Point point : points) {
				g.drawLine((int) point.getX(), (int) point.getY() - 1, (int) point.getX(), (int) point.getY() + 1);
				g.drawLine((int) point.getX() - 1, (int) point.getY(), (int) point.getX() + 1, (int) point.getY());
			}
			g.setColor(Color.BLACK);
			int maximum = 600;
			if (maximumTime != 0) {
				label.setText(Integer.toString(maximumTime));
				maximum = maximumTime;
			}
			for (JLabel label : timeLabels) {
				remove(label);
			}
			timeLabels.clear();
			for (JLabel label : byteLabels) {
				remove(label);
			}
			byteLabels.clear();
			g.drawLine(950, 300,  950, 305);
			if (maximumBytes != 0) {
				for (int i = 1; i<8; i++) {
					g.drawLine(50, (int) (300 - 232*((double) i/7)), 45, (int) (300 - 232*((double)i/7)));
					JLabel byteLabel = new JLabel("A", JLabel.CENTER);
					byteLabel.setSize(50,10);
					String textNumber;
					if (i < 5) {
						 textNumber = Integer.toString(i * 200) + "k";
					} else {
						textNumber = Double.toString((double) (i * 200) / 1000) + "M";
					}				
					byteLabel.setText(textNumber);
					byteLabel.setLocation(0, (int) (300 - 232*((double) i/7)) - 5);
					byteLabels.add(byteLabel);
					add(byteLabels.get(i-1));
				}
			} 
			for (int i = 0; i< maximum / 50; i++) {
				g.drawLine(50 + 900/(maximum/50)*i, 300, 50 + 900/(maximum/50)*i, 305);
				JLabel number = new JLabel("A", JLabel.CENTER);
				number.setText(Integer.toString(i*50));
				number.setSize(50,10);
				number.setLocation(i*900/(maximum/50) + 25, 310);
				timeLabels.add(number);
				add(timeLabels.get(i));	
			}
		}
	}
	private ArrayList<JLabel> timeLabels = new ArrayList<JLabel>();
	private ArrayList<JLabel> byteLabels = new ArrayList<JLabel>();
	private File filename;
	private Scanner fileScanner = null;
	private final JPanel ipAddressesComboBoxPanel = new JPanel();
	private final JPanel filterAddressesComboBoxPanel = new JPanel();
	private final JPanel portsComboBoxPanel = new JPanel();
	private JComboBox<Object> ipAddressesComboBox = new JComboBox<>();
	private JComboBox<Object> filterAddressesComboBox = new JComboBox<>();
	private JComboBox<Object> portsComboBox = new JComboBox<>();
	private JMenuBar menuBar = new JMenuBar();
	private ArrayList<Point> points = new ArrayList<Point>();
	private int maximumTime;
	private int maximumBytes;
	private int interval = 2;
	private Set<String> sourceHosts = new HashSet<String>();
	private Set<String> destinationHosts = new HashSet<String>();
    private final JPanel radioButtonPanel = new JPanel();
    private final ButtonGroup radioButtons = new ButtonGroup();
	private SortedSet<String> ips;
	private SortedSet<String> ipss;
	private SortedSet<String> portSet;
    private final JRadioButton radioButtonSourceHosts = new JRadioButton("Source hosts");
    private final JRadioButton radioButtonDestinationHosts;
    private ActionListener actionListener = new ActionListener() {
		@Override
    	public void actionPerformed(ActionEvent event) {
        	createGraph((String) ipAddressesComboBox.getSelectedItem(),
					(String) filterAddressesComboBox.getSelectedItem(),
					(String) portsComboBox.getSelectedItem());
		}
    };
    private String[] hosts = {"sourceHosts", "Off"};
    private Display display = new Display();
	public Viewer() {
    	super("Flow volume viewer");
    	setLayout(null);
    	setPreferredSize(new Dimension(1000,500));
    	setMinimumSize(new Dimension(1000,500));
    	setMaximumSize(new Dimension(1000,500));
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setVisible(true);	
    	display.setVisible(true);
    	display.setLocation(0,100);
    	display.setSize(this.getWidth(),400);
    	add(display);	
    	radioButtonPanel.setLayout(new GridBagLayout());
    	GridBagConstraints c = new GridBagConstraints();
    	c.gridx = 0;
    	c.gridy = GridBagConstraints.RELATIVE;
    	c.anchor = GridBagConstraints.WEST;
     	radioButtonSourceHosts.setSelected(true);
        radioButtons.add(radioButtonSourceHosts);
        radioButtonPanel.add(radioButtonSourceHosts, c);
        radioButtonPanel.setSize(200,100);
        radioButtonPanel.setLocation(0,0);
        
        ipAddressesComboBoxPanel.add(ipAddressesComboBox, c);
        ipAddressesComboBoxPanel.setLayout(new GridBagLayout());
        ipAddressesComboBoxPanel.setSize(150,100);
        ipAddressesComboBoxPanel.setLocation(200,0);
        ipAddressesComboBoxPanel.setVisible(false);
        add(ipAddressesComboBoxPanel);
        
        filterAddressesComboBoxPanel.add(filterAddressesComboBox, c);
        filterAddressesComboBoxPanel.setLayout(new GridBagLayout());
        filterAddressesComboBoxPanel.setSize(150,100);
        filterAddressesComboBoxPanel.setLocation(400,0);
        filterAddressesComboBoxPanel.setVisible(false);
        add(filterAddressesComboBoxPanel);
        
        portsComboBoxPanel.add(portsComboBox, c);
        portsComboBoxPanel.setLayout(new GridBagLayout());
        portsComboBoxPanel.setSize(150,100);
        portsComboBoxPanel.setLocation(600,0);
        portsComboBoxPanel.setVisible(false);
        add(portsComboBoxPanel);
        
	    Comparator<String> ipComparator = new Comparator<String>() {
		      @Override
		      public int compare(String ip1, String ip2) {
		    	try {
		        return numericalAddress(ip1).compareTo(numericalAddress(ip2));
		    	} catch (Exception e) {
		    		return 1;
		    		}
		    	}
		      };
		 
		 Comparator<String> portComparator = new Comparator<String>() {
			      @Override
			      public int compare(String ip1, String ip2) {
			    	  try {
				        if (Integer.parseInt(ip1) < Integer.parseInt(ip2)){
				        	return -1;
				        }  return 1;
			    	  } catch (Exception e) {
			    		  return 1;
			    	  }
			      }
			 };
		 ips = new TreeSet<String>(ipComparator);
		 ipss = new TreeSet<String>(ipComparator);
		 portSet = new TreeSet<String>(portComparator);
		 radioButtonDestinationHosts = new JRadioButton("Destination hosts");
		 radioButtons.add(radioButtonDestinationHosts );
		 radioButtonPanel.add(radioButtonDestinationHosts , c);
		 radioButtonDestinationHosts.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				hosts[0] = "destinationHosts";
				ipAddressesComboBoxPanel.removeAll();
				
				ips.clear();
				destinationHosts.remove("Destination");
				ips.addAll(destinationHosts);
				
				ipss.clear();
				ipss.add("Source");
				ipss.addAll(sourceHosts);
				
				ipAddressesComboBox = new JComboBox<>(ips.toArray());
				ipAddressesComboBoxPanel.add(ipAddressesComboBox);
				
				filterAddressesComboBoxPanel.removeAll();
				filterAddressesComboBox = new JComboBox<>(ipss.toArray());
				filterAddressesComboBoxPanel.add(filterAddressesComboBox);
				
				portsComboBoxPanel.removeAll();
				portsComboBox = new JComboBox<>(portSet.toArray());
				portsComboBoxPanel.add(portsComboBox);
				
				
    			createGraph((String) ipAddressesComboBox.getSelectedItem(),
    					(String) filterAddressesComboBox.getSelectedItem(),
    					(String) portsComboBox.getSelectedItem());
    			ipAddressesComboBox.addActionListener(actionListener);
    			filterAddressesComboBox.addActionListener(actionListener);
    			portsComboBox.addActionListener(actionListener);
			    
			    
			    
			    setSize(getWidth(),getHeight() + 1);
			    setSize(getWidth(),getHeight() - 1);
    		}	
    	});
    	radioButtonSourceHosts.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			hosts[0] = "sourceHosts";
    			
				ipss.clear();
				sourceHosts.remove("Source");
				ipss.addAll(sourceHosts);

				ips.clear();
				ips.add("Destination");
				ips.addAll(destinationHosts);		
    			
    			ipAddressesComboBoxPanel.removeAll();
    			ipAddressesComboBox = new JComboBox<>(ipss.toArray());
    			ipAddressesComboBoxPanel.add(ipAddressesComboBox);	
				
				filterAddressesComboBoxPanel.removeAll();
				filterAddressesComboBox = new JComboBox<>(ips.toArray());
				filterAddressesComboBoxPanel.add(filterAddressesComboBox);
				
				portsComboBoxPanel.removeAll();
				portsComboBox = new JComboBox<>(portSet.toArray());
				portsComboBoxPanel.add(portsComboBox);
    			
    			createGraph((String) ipAddressesComboBox.getSelectedItem(),
    					(String) filterAddressesComboBox.getSelectedItem(),
    					(String) portsComboBox.getSelectedItem());
				ipAddressesComboBox.addActionListener(actionListener);
    			filterAddressesComboBox.addActionListener(actionListener);
    			portsComboBox.addActionListener(actionListener);
			    setSize(getWidth(),getHeight() + 1);
			    setSize(getWidth(),getHeight() - 1);
		    	
    		}	
    	});
    	add(radioButtonPanel);
    	setJMenuBar(menuBar);
    	JMenu fileMenu = new JMenu("File");    	
    	fileMenu.setMnemonic('F');
    	JMenuItem fileMenuOpen = new JMenuItem("Open trace file");
    	JMenuItem fileMenuSave = new JMenuItem("Save graph");
    	JMenuItem fileMenuSetInterval = new JMenuItem("Set Interval");
    	JMenuItem fileMenuQuit = new JMenuItem("Quit");
    	menuBar.add(fileMenu);
    	fileMenu.add(fileMenuOpen);
    	fileMenu.add(fileMenuSave);
    	fileMenu.add(fileMenuSetInterval);
    	fileMenu.add(fileMenuQuit);
		fileMenuOpen.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fileChooser = new JFileChooser(".");
				    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
				    fileChooser.setFileFilter(filter);
				    int retval = fileChooser.showOpenDialog(Viewer.this);
					Scanner lineScanner = null;
    				if (retval == JFileChooser.APPROVE_OPTION) {
    					try {
    						filename = fileChooser.getSelectedFile();
    						lineScanner =  new Scanner(fileChooser.getSelectedFile());
    						fileScanner =  new Scanner(fileChooser.getSelectedFile());
						} catch (FileNotFoundException e1) {
							JOptionPane.showMessageDialog(display, "Sorry, but that file does not exist! Try again.");
						}
    					try {
	    					sourceHosts.clear();
	    					destinationHosts.clear();
	    					Set<String> ports = new HashSet<String>();
	    					if (lineScanner!=null) {
	    						while (lineScanner.hasNextLine()) {
	    							String[] parts = lineScanner.nextLine().split("\t");
	    							if (parts[2].matches("^192\\.168\\.0\\.\\d{0,3}$")) {sourceHosts.add(parts[2]);}
	    							if (parts[4].matches("^10\\.0\\.\\d{0,3}\\.\\d{0,3}$")) {destinationHosts.add(parts[4]);}
	    							if (parts[3].matches("^\\d{1,}$")) {ports.add(parts[3]);}
	    							if (parts[5].matches("^\\d{1,}$")) {ports.add(parts[5]);}
	    						}
	    						ips.clear();
	    						ipss.clear();
	    						portSet.clear();
	    						
	    						if (hosts[0] == "sourceHosts") {
	    							ips.add("Destination");
	    						} else {
	    							ipss.add("Source");
	    						}
	    						
	    						portSet.add("Ports");
	    					
	    						ips.addAll(destinationHosts);
	    						ipss.addAll(sourceHosts);
	    						portSet.addAll(ports);
	    						
	    						ipAddressesComboBoxPanel.removeAll();
	    						filterAddressesComboBoxPanel.removeAll();
	    						portsComboBoxPanel.removeAll();
	    						
	    						ipAddressesComboBox = (hosts[0] == "sourceHosts") ? (new JComboBox<>(ipss.toArray())) : (new JComboBox<>(ips.toArray()));
	    						filterAddressesComboBox = (hosts[0] == "sourceHosts") ?  (new JComboBox<>(ips.toArray())) : (new JComboBox<>(ipss.toArray()));
	    						portsComboBox = new JComboBox<>(portSet.toArray());
	    						
	    						ipAddressesComboBoxPanel.add(ipAddressesComboBox);
	    						filterAddressesComboBoxPanel.add(filterAddressesComboBox);
	    						portsComboBoxPanel.add(portsComboBox);
	    						
	    						ipAddressesComboBoxPanel.setVisible(true);
	    						filterAddressesComboBoxPanel.setVisible(true);
	    						portsComboBoxPanel.setVisible(true);
	    						
	    						createGraph((String) ipAddressesComboBox.getSelectedItem(),
	    								(String) filterAddressesComboBox.getSelectedItem(),
	    								(String) portsComboBox.getSelectedItem());
	    				    	ipAddressesComboBox.addActionListener(actionListener);
	    		    			filterAddressesComboBox.addActionListener(actionListener);
	    		    			portsComboBox.addActionListener(actionListener);
	    					    setSize(getWidth(),getHeight() + 1);
	    					    setSize(getWidth(),getHeight() - 1);
	    					   
	    					}	
    					} catch (Exception exception) {
    						JOptionPane.showMessageDialog(display, "This is not a valid file.");
    					}
    				}
				}
			}
		);
		fileMenuQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		fileMenuSetInterval.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String test1= JOptionPane.showInputDialog("Please input interval in seconds: ");
				try{
					interval = Integer.parseInt(test1);
				} catch (Exception exception) {
					JOptionPane.showMessageDialog(display, "Please type an integer.");
				}
				createGraph((String) ipAddressesComboBox.getSelectedItem(),
						(String) filterAddressesComboBox.getSelectedItem(),
						(String) portsComboBox.getSelectedItem());
			}
		});
		
		fileMenuSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
			    BufferedImage image = new BufferedImage(display.getWidth(), display.getHeight(), BufferedImage.TYPE_INT_RGB);
			    display.print(image.getGraphics()); // or: panel.printAll(...);
			    try {
			        ImageIO.write(image, "jpg", new File("graph.jpg"));
			        JOptionPane.showMessageDialog(display, "graph.jpg has been saved! Check your project folder.");
			    }
			    catch (IOException e) {
			    	JOptionPane.showMessageDialog(display, "Something weird occured. Please try again.");
			    }
			}	
		});
		
		JOptionPane.showMessageDialog(display, 
				"Hello fellow GitHub user! \n\n"
				+ ""
				+ "You are running a project created by me in Java.\n"
				+ ""
				+ ""
				+ "Features:\n"
				+ " - Set Interval\n"
				+ " - Save Graph (as graph.jpg)\n"
				+ " - Source/Destination Host filter\n"
				+ " - Port filter\n\n"
				+ ""
				+ ""
				+ "Thanks,\n\n"
				+ ""
				+ "Jacob Zaidi");
	}
	/**
	 * Converts IP address XX.YY.ZZ.AA into XXYYZZAA form for sorting purposes
	 * @param ip
	 * @return l
	 */
	static Long numericalAddress(String ip) {
		Scanner sc = new Scanner(ip);
		sc.useDelimiter("\\.");
		Long l = (sc.nextLong() << 24) + (sc.nextLong() << 16) + (sc.nextLong() << 8) + (sc.nextLong());
		sc.close();
		return l;
	}
	
	static Integer numericalPort(String port) {
		Integer l = Integer.getInteger(port);
		return l;
	}
	/** Allows us to find the maximum and make an ArrayList of points for plotting on the graph 
	 *  @param ipAddress, filterAddress, portAddress
	 */
	public void createGraph(String ipAddress, String filterAddress, String portAddress) {
		Scanner tempScanner = null;
		try {tempScanner = new Scanner(filename);} catch (Exception e) {}
		double maximum = 0;
		double maximumY = 0;
		if (tempScanner!=null) {
			while (tempScanner.hasNextLine()) {
				String[] parts = tempScanner.nextLine().split("\t");	
				if (((parts[2].equals(ipAddress) || (parts[4].equals(ipAddress)) 
						|| (ipAddress == "Source") || (ipAddress == "Destination") ))
						
				&&	((parts[4].equals(filterAddress) || (parts[2].equals(filterAddress)) 
						|| (filterAddress == "Source") || (filterAddress == "Destination")))
				&&	((parts[3].equals(portAddress) || (parts[5].equals(portAddress) || (portAddress == "Ports") )))
				)
				{
					if (Double.parseDouble(parts[1]) > maximum) 
						maximum = Double.parseDouble(parts[1]);
					if (Double.parseDouble(parts[7]) > maximumY) 
						maximumY = Double.parseDouble(parts[7]);
				}
			}
			maximumBytes = (int) maximumY/100 * 100;
			maximumTime = ((int) (maximum)+50)/50 * 50;
		}
		points.clear();
		double previous = 0;
		try{tempScanner = new Scanner(filename);} catch (Exception e) {}
		if (tempScanner!=null) {
			while (tempScanner.hasNextLine()) {
				String[] parts = tempScanner.nextLine().split("\t");
				if (((parts[2].equals(ipAddress) || (parts[4].equals(ipAddress)) 
						|| (ipAddress == "Source") || (ipAddress == "Destination") ))
						
				&&	((parts[4].equals(filterAddress) || (parts[2].equals(filterAddress)) 
						|| (filterAddress == "Source") || (filterAddress == "Destination")))
				&&	((parts[3].equals(portAddress) || (parts[5].equals(portAddress) || (portAddress == "Ports") )))
				&& (Double.parseDouble(parts[1]) - interval >= previous) )
				
				
				{
					points.add(new Point((int) (50 + 900*(Double.parseDouble(parts[1])) / maximumTime), (int) (300 - 250*(Integer.parseInt(parts[7])) /1500))); 
					previous = Double.parseDouble(parts[1]);
				}
			}
			display.repaint();
		}
	}
	
}