/*****localhost radio/mp3 player****/
import jaco.mp3.player.MP3Player;
import jaco.mp3.player.c;

import java.net.Socket;
import java.net.URL;
import java.applet.Applet;
import java.awt.*;
import javazoom.jl.player.Player;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;




public class Radio extends Applet{
  
	private static final long serialVersionUID = 1L;

	//Set Labels
	private static JLabel Title;
	private static JLabel myIcon;
	private static JLabel Artist;
	
	static LinkedList<String> myMusicList = new LinkedList<String>();
	static LinkedList<Integer> numberBucket = new LinkedList<Integer>();
	BufferedInputStream in;
	BufferedOutputStream out;
	
	//used for shuffling
	static Random random = new Random();
	
	Socket clientSocket = null;
	
	//used for skips
	boolean closed = false;
	boolean isPlaying = false;
	static boolean tooManySkips = false;
	static int shuffle = 0;
	static int skips = 0;
	static int undoSkip = 0;
	
	//boolean
	static boolean wasPaused = false;
		
	//iRadio Icon
	static ImageIcon icon = createImageIcon("images/iradio_icon.png", "hipster");
	static ImageIcon play = createImageIcon("images/play.png", "play button");
	static ImageIcon forward = createImageIcon("images/forward.png", "skip button");
	static ImageIcon pause = createImageIcon("images/pause.png", "pause button");	
	
	//Buttons
	private static JButton Play = new JButton("Play");
	private static JButton Forward = new JButton("Skip");
	private static JButton Pause_Resume = new JButton("Pause");
	private static JButton Ok = new JButton("OK");
	
	 //Radio Buttons
	static JRadioButton HipHop = new JRadioButton("Hip-Hop");
	static JRadioButton Pop = new JRadioButton("Pop");
	static JRadioButton RandB = new JRadioButton("R&B");
	
	//Variables to play music
	Player mp3;
	BufferedInputStream bis;
	InputStream mp3_file;
	static MP3Player player = new MP3Player();
	int countSongs = 0;
	int HitorOther = 0; //Hit = 0 other = 1
	static int number;
	
	//strings
	String theSong;
	String stuff;
	static String Genre = "";
	
	public Radio(){
		
		
	}
	
	 //Do action when play button is pressed
    static ActionListener actionListenerHipHop = new ActionListener() {
        public void actionPerformed(ActionEvent actionEvent) {
        	HipHop.setSelected(true);
        	Pop.setSelected(false);
        	RandB.setSelected(false);
        	      	
        }};		
        
     //Do action when play button is pressed
     static ActionListener actionListenerPop = new ActionListener() {
        public void actionPerformed(ActionEvent actionEvent) {
            Pop.setSelected(true);
            HipHop.setSelected(false);
            RandB.setSelected(false);
            }};
            
      //Do action when play button is pressed
       static ActionListener actionListenerRandB = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
             RandB.setSelected(true);
             HipHop.setSelected(false);
             Pop.setSelected(false);
                
          }};   
                      
	
	
	/** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path,
                                               String description) {
        java.net.URL imgURL = Radio.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    public static Component musicPlayer(final JFrame someFrame){
    	//super(new GridLayout(7,2));		
		JPanel myPanel = new JPanel();
		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//Making Title
		Title = new JLabel("myRadio", JLabel.CENTER);
		Title.setFont(new Font("Arial Black", Font.BOLD, 24));
		
		myIcon = new JLabel(icon, JLabel.CENTER);
		
		Artist = new JLabel("Now Playing: ", JLabel.CENTER);
		
		Forward.setEnabled(false);

		//Do action when submit button is pressed
	    final c actionListenerPlayer = new c() {

			@Override
			public void b() {
				// TODO Auto-generated method stub 
				wasPaused = true;
			}

			@Override
			//when play is played do stuff
			public void a() {
				// TODO Auto-generated method stub
				if(wasPaused == true){
					wasPaused = false;
					return;
				}
				 Artist.setText("Now Playing: " + myMusicList.get(shuffle));
				 shuffle = random.nextInt(number) + 0;
				 
				 //make sure we don't get the same number twice
				 for(int i = 0; i < numberBucket.size(); i++){
					 if(shuffle == numberBucket.get(i)){
						 shuffle = random.nextInt(number) + 0;
						 i = 0;
					 }//if
				 }//for
				 
				 //we can add number to number bucket
				 numberBucket.add(shuffle);
				 
				 //undo skips
				 if(tooManySkips == true)
					 undoSkip++;
				 
				 //after 3 songs skip is enable again
				 if(undoSkip == 3){
					 tooManySkips = false;
					 Forward.setEnabled(true);
					 undoSkip = 0;
					 skips = 0;
				 }
				 
				 try {
					player.addToPlayList(new URL("localhost" + "music/" + Genre + "/" + "Hits/" + myMusicList.get(shuffle)));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block					
					e.printStackTrace();
				}
				
			}};
		
			//Do action when skip button is pressed
		    ActionListener actionListenerSkip = new ActionListener() {
		        public void actionPerformed(ActionEvent actionEvent) {
		        	if(player.isPaused()){
		        		Pause_Resume.setText("Pause");
		        		player.play();
		        		player.skipForward();
		        		skips++;
		        		if(skips >= 4){
			        		Forward.setEnabled(false);
			        		tooManySkips = true;
			        	}
		        	}
		        	else{	
		        	player.skipForward();
		        	skips++;
		        	if(skips >= 4){
		        		Forward.setEnabled(false);
		        		tooManySkips = true;
		        	}
		        	}//else
		        	
		        }};	
		      //Do action when pause and resume button is pressed
			    ActionListener actionListenerPauseorResume = new ActionListener() {
			        public void actionPerformed(ActionEvent actionEvent) {
			        	if(!player.isPaused()){
			        		player.pause();
			        		Pause_Resume.setText("Resume");
			        	}
			        	else{
			        		Pause_Resume.setText("Pause");
			        		player.play();
			        	}
			        	
			        }};            
			
		//Do action when play button is pressed
	    ActionListener actionListenerPlay = new ActionListener() {
	        public void actionPerformed(ActionEvent actionEvent) {
	        	Play.setEnabled(false);

	    	 // run in new thread to play in background
	            new Thread() {
	                public void run() {
	                    try {
								 URL url = new URL("localhost/index.html");
								 BufferedReader in = new BufferedReader(
									        new InputStreamReader(
									        url.openStream()));

									  String inputLine;
									  
									  String L;
									  
									  StringBuffer k = new StringBuffer("");
									  
									  //lets you know to start taking the info
									  boolean quote = false;
									  
									  //boolean to weed out the "music/"
									  boolean music = false;
									  
									  //boolean to weed out Genre
									  boolean genre = false;
									  boolean isGenre = true;
									  boolean hits = false;
									  
									  //if radio buttons were picked
									  if(Genre == "R&B"){
										  //get the .mp3 file from the source code
										  while ((inputLine = in.readLine()) != null){
											  isGenre = true;
											  quote = false;
										      for(int i = 0; i < inputLine.length(); i++){
										    	  if(inputLine.charAt(i) == '"' && quote == false){
										    	  		quote = true;
										    	  		music = true;
										    	  		genre = true;
										    	  		continue;
										    	  } //if
										    	  
										    	  //weed out the "music/" in the string
										    	  if(inputLine.charAt(i) == 'm' && quote == true && music == true){
										    		  i++;
										    		  if(inputLine.charAt(i) == 'u')
										    			  i++;
										    		  if(inputLine.charAt(i) == 's')
										    			  i++;
										    		  if(inputLine.charAt(i) == 'i')
										    			  i++;
										    		  if(inputLine.charAt(i) == 'c')
										    			  i++;
										    		  if(inputLine.charAt(i) == '/'){
										    			  music = false;
										    			  int j = i + 1;
										    			  //Check to see if "H" in Hip hop is next
										    			  if(inputLine.charAt(j) != 'R')
										    				  isGenre = false;
										    			  continue;
										    		  }
										    	  } //if
										    	  
										    	  if(inputLine.charAt(i) == 'H' && quote == true && music == false && genre == false && hits == false){
										    		     if(inputLine.charAt(i) == 'H')
										    		    	 i++;
											    	  	 if(inputLine.charAt(i) == 'i')
												    		 i++;
												    	 if(inputLine.charAt(i) == 't')
												    		 i++;
												    	 if(inputLine.charAt(i) == 's')
												    		 i++;
												    	 if(inputLine.charAt(i) == '/'){
												    		 hits = true;
												    		 continue;
												    		} 
										    	  }
										    	  
										    	  if(inputLine.charAt(i) == 'R' && quote == true && music == false && genre == true){
										    	  		i++;
										    	  		  if(inputLine.charAt(i) == '&')
											    			  i++;
											    		  if(inputLine.charAt(i) == 'B')
											    			  i++;
											    		  if(inputLine.charAt(i) == '/'){
											    			  genre = false;
											    			  continue;
											    		  }
										    	  }
										    	  									    	  
										    	  if(inputLine.charAt(i) == '"' && quote == true && music == false && isGenre == true){
										    		    quote = false;
										    		    music = false;
										    	  		genre = false;
										    	  		hits = false;
										    	  		isGenre = true;
										    		    L = k.toString();
														myMusicList.add(L);
													//	System.out.println(L);
														k = new StringBuffer("");
										    	  		break;
										    	  }//if
										    	  
										    	  if(quote == true && isGenre == true && hits == true)
										    		  k.append(inputLine.charAt(i));
										    	   
										      }//for
											  
										  }	//while		
										  
										  }//if

									  if(Genre == "Hip-Hop"){
									  //get the .mp3 file from the source code
									  while ((inputLine = in.readLine()) != null){
										  isGenre = true;
										  quote = false;
									      for(int i = 0; i < inputLine.length(); i++){
									    	  if(inputLine.charAt(i) == '"' && quote == false){
									    	  		quote = true;
									    	  		music = true;
									    	  		genre = true;
									    	  		continue;
									    	  } //if
									    	  
									    	  //weed out the "music/" in the string
									    	  if(inputLine.charAt(i) == 'm' && quote == true && music == true){
									    		  i++;
									    		  if(inputLine.charAt(i) == 'u')
									    			  i++;
									    		  if(inputLine.charAt(i) == 's')
									    			  i++;
									    		  if(inputLine.charAt(i) == 'i')
									    			  i++;
									    		  if(inputLine.charAt(i) == 'c')
									    			  i++;
									    		  if(inputLine.charAt(i) == '/'){
									    			  music = false;
									    			  int j = i + 1;
									    			  //Check to see if "H" in Hip hop is next
									    			  if(inputLine.charAt(j) != 'H')
									    				  isGenre = false;
									    			  continue;
									    		  }
									    	  } //if
									    	  
									    	//weed out the "Hits/" in the string
									    	  if(inputLine.charAt(i) == 'H' && quote == true && music == false && genre == false && hits == false){
									    		     if(inputLine.charAt(i) == 'H')
									    		    	 i++;
										    	  	 if(inputLine.charAt(i) == 'i')
											    		 i++;
											    	 if(inputLine.charAt(i) == 't')
											    		 i++;
											    	 if(inputLine.charAt(i) == 's')
											    		 i++;
											    	 if(inputLine.charAt(i) == '/'){
											    		 hits = true;
											    		 continue;
											    		} 
									    	  }
									    	  
									    	//weed out the "Hip-hop/" in the string
									    	  if(inputLine.charAt(i) == 'H' && quote == true && music == false && genre == true){
									    	  		i++;
									    	  		  if(inputLine.charAt(i) == 'i')
										    			  i++;
										    		  if(inputLine.charAt(i) == 'p')
										    			  i++;
										    		  if(inputLine.charAt(i) == '-')
										    			  i++;
										    		  if(inputLine.charAt(i) == 'H')
										    			  i++;
										    		  if(inputLine.charAt(i) == 'o')
										    			  i++;
										    		  if(inputLine.charAt(i) == 'p')
										    			  i++;
										    		  if(inputLine.charAt(i) == '/'){
										    			  genre = false;
										    			  continue;
										    		  }
									    	  }
									    	  									    	  
									    	  if(inputLine.charAt(i) == '"' && quote == true && music == false && isGenre == true){
									    		    quote = false;
									    		    music = false;
									    	  		genre = false;
									    	  		hits = false;
									    	  		isGenre = true;
									    		    L = k.toString();
													myMusicList.add(L);
													k = new StringBuffer("");
									    	  		break;
									    	  }//if
									    	  
									    	  if(quote == true && isGenre == true && hits == true)
									    		  k.append(inputLine.charAt(i));
									    	   
									      }//for
										  
									  }	//while		
									  
									  }//if
									  
									  in.close();
									  System.out.println(myMusicList.size());
									  number = myMusicList.size();
									  player.addMP3PlayerListener(actionListenerPlayer);
						 
						 Play.setText("Connected");
						 Forward.setEnabled(true);
						 
						 shuffle = random.nextInt(number) + 0;
						 numberBucket.add(shuffle);
						 player.addToPlayList(new URL("localhost/" + "music/" + Genre + "/" + "Hits/" + myMusicList.get(shuffle)));
						 player.play();		
						 
						 
	             }//try
	                    catch (Exception e) { System.out.println(e); Artist.setText("Unable to connect");}
	                    
	                }
	                
	                
	            }.start();
	        }
	    };	
	    
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new GridLayout(0,3));
		buttonPane.setMaximumSize(new Dimension(400,400));
		Play.setPreferredSize(new Dimension(20,20));
		buttonPane.add(Play);
		buttonPane.add(Forward);
		buttonPane.add(Pause_Resume);
		
		//add menu
		JMenu fileMenu = new JMenu( "File" );
    	fileMenu.setMnemonic('F');
    	
    	//create menu item
    	JMenuItem chooseGenre = new JMenuItem("New Genre");
    	chooseGenre.setMnemonic('N');
    	fileMenu.add(chooseGenre);
    	
    	chooseGenre.addActionListener(
    			new ActionListener()
    			{
    				public void actionPerformed( ActionEvent event )
    				{
    					someFrame.dispose();
    					pickGenreGUI();
    				}
    			});
    	
    	JMenuBar bar = new JMenuBar();
    	bar.add(fileMenu);
		
		JPanel titlePane = new JPanel();
		titlePane.setLayout(new GridLayout(0,1));
		titlePane.add(Title);
		titlePane.add(myIcon);
		
		JPanel playingPane = new JPanel();
		titlePane.setLayout(new GridLayout(0,1));
		titlePane.add(Artist, BorderLayout.CENTER);
		
	    Play.addActionListener(actionListenerPlay);
	    Forward.addActionListener(actionListenerSkip);
	    Pause_Resume.addActionListener(actionListenerPauseorResume);
	    myPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	    myPanel.add(titlePane, BorderLayout.CENTER);
		myPanel.add(buttonPane);
		myPanel.add(playingPane, BorderLayout.CENTER);
		someFrame.add(myPanel);
		
    	return myPanel;
    	
    }
    public static Component pickGenre(final JFrame someFrame) {
    	JPanel myPanel = new JPanel();
    	
    	JPanel myRadioB = new JPanel(new GridLayout(4,1));
    	myRadioB.add(HipHop);
    	myRadioB.add(Pop);
    	myRadioB.add(RandB);
    	myRadioB.add(Ok);
    	
    	ActionListener actionListenerOk = new ActionListener() {
           public void actionPerformed(ActionEvent actionEvent) {
                if(HipHop.isSelected()){
                	Genre = "Hip-Hop";
                	System.out.println(Genre + " selected");
                	createAndShowGUI();
                }
                if(Pop.isSelected()){
                	Genre = "Pop";
                	System.out.println(Genre + " selected");
                	createAndShowGUI();
                }
                if(RandB.isSelected()){
                	Genre = "R&B";
                	System.out.println(Genre + " selected");
                	createAndShowGUI();
                }
                someFrame.dispose();	
                   
             }};  
    	
    	HipHop.addActionListener(actionListenerHipHop);
    	Pop.addActionListener(actionListenerPop);
    	RandB.addActionListener(actionListenerRandB);
    	Ok.addActionListener(actionListenerOk);
    	   	
    	myPanel.add(myRadioB);
    	
    	someFrame.add(myPanel);
    	someFrame.setVisible(true);
    	
    	return myPanel;
    	
    	
    }

    
    private static void pickGenreGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Pick A Genre");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set frame size
        frame.setSize(250, 200);
        	        
        //Add content to the window.
        frame.add(pickGenre(frame));

        //Display the window.
        //frame.pack();
        frame.setVisible(true);
    }	
	
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Internet Radio");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set frame size
        frame.setSize(390, 400);
        	        
        //Add content to the window.
        frame.add(musicPlayer(frame));

        //Display the window.
        //frame.pack();
        frame.setVisible(true);
    }	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		pickGenreGUI();

	}

}
