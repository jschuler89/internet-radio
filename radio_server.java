/****Radio Server that listens for main client****/
import java.awt.GridLayout;
import java.io.*;
import java.net.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Radio_Server extends JPanel{
  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Set Labels
	private JLabel myIcon;
	
	//iRadio Icon
	ImageIcon icon = createImageIcon("images/radio_icon.jpg", "hipster");
	
	//Text field for connection verification
	public static JTextField connection = new JTextField("Not Connected");

	public Radio_Server(){
		super(new GridLayout(2,1));
		
		myIcon = new JLabel(icon);
		
		add(myIcon);
		add(connection);
		
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path,
                                               String description) {
        java.net.URL imgURL = Radio_Server.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("iRadio Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set frame size
        frame.setSize(230, 200);
        	        
        //Add content to the window.
        frame.add(new Radio_Server());

        //Display the window.
        //frame.pack();
        frame.setVisible(true);
    }   
  
	public static void main(String[] args) throws IOException {
    	createAndShowGUI();   
    	int x = 0;
    	 boolean listening = true;
    	 ServerSocket serverSocket = null;
    	 try {
    	     serverSocket = new ServerSocket(4442);
    	 } catch (IOException e) {
    	     System.err.println("Could not listen on port: 4444.");
    	  System.exit(1);
    	 }

    	 while(listening) {
    	     Socket clientSocket = serverSocket.accept();
    	     connection.setText("Connected");
    	     (new Handle(clientSocket, x , connection)).start();
    	     x++;
    	 }
    	 
    	 serverSocket.close();
    	    }
    }
