//Omair Alam oa6ci

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

/**************************************************************************
 * This class implements a GUI for the convex hull problem, primarily using
 * Java Swing components, a local CanvasPanel class (for drawing the points
 * convex hull), and a local ConvexHull class that goes about the business
 * of computing the convex hull and determining the shortest path given 
 * starting and ending points.
 *
 * @author  Lewis Barnett, Barry Lawson, Doug Szajda
 * @version 22.1.2015
 **************************************************************************/
public class AppWindow extends JFrame implements ActionListener 
{
    // constants
    private static final int WINDOW_WIDTH  = 700; // main window width
    private static final int WINDOW_HEIGHT = 575; // original = 575 // main window height

    // messages to keep the user informed
    protected static final String CLICK_MSG = 
        "Click background to select points or click a button...";
    protected static final String START_PT_MSG = 
        "Click to select shortest path starting point...";
    protected static final String END_PT_MSG = 
        "Click to select shortest path ending point...";

    // GUI components
    private CanvasPanel canvas; // displays clicked points and the hull
    private JButton     colorButton;
    private JButton     clearButton;
    private JButton     quitButton;
    private JButton     shortestPathButton;
    private JButton     convexHullButton; 
    private JLabel      userMessage;
    private JLabel      coordinateLabel; // keeps content of coordinatesLabel
    private Boolean     labelState; //keeps state of the JLabel
    private JRadioButtonMenuItem checkCoordinates; // keeps state of 
    

    private ConvexHull  convexHull;
    
    /**************************************************************************
     * Constructs an AppWindow as a front-end to the convex hull problem 
     * solution, with appropriate GUI components.
     *
     **************************************************************************/
    public AppWindow() 
    {
        // call super class methods to set up the window
        super();
	    setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("Convex Hull App");
        setLayout(new BorderLayout());
        setResizable(false);

        // create the clickable drawing area
        canvas = new CanvasPanel(this);
        canvas.setBackground(Color.darkGray);

        Dimension canvasSize = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT-75);
        canvas.setMinimumSize(canvasSize);
        canvas.setPreferredSize(canvasSize);
        canvas.setMaximumSize(canvasSize);
        

         // Creates a menubar for a JPanel
        JMenuBar menuBar;
        menuBar = new JMenuBar();
        
         // Adds a file tab in the menu which opens two further options 
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        
        //Adds an exit button and a display coordinates button to the file menu 
         JMenuItem exitActionButton = new JMenuItem("Exit");
        // JMenuItem coordinatesButton = new JMenuItem("Coordinates");
         checkCoordinates = new JRadioButtonMenuItem ("Show Clicked Points Coordinates");
         exitActionButton.setActionCommand("exitAction");
      //   coordinatesButton.setActionCommand("showCoordinates");
         checkCoordinates.setActionCommand("checkAction");
         
     
        // Create a ButtonGroup and add both radio Button to it. Only one radio
        // button in a ButtonGroup can be selected at a time.
     //   ButtonGroup bg = new ButtonGroup();
     //   fileMenu.add(coordinatesButton);
         //adds these buttons to the fileMenu
        fileMenu.add(checkCoordinates);
        fileMenu.add(exitActionButton);
        
        //more book-keeping 
        fileMenu.setMnemonic(KeyEvent.VK_A);
        fileMenu.getAccessibleContext().setAccessibleDescription(
        "The only menu in this program that has menu items");
        
        //sets the characteristics of the coordinates label
        coordinateLabel = new JLabel();
        coordinateLabel.setSize(400,10);
        coordinateLabel.setLocation(200,80);
        coordinateLabel.setOpaque(true);
        labelState = false; 
        

        // create buttons and set up listeners appropriately
        
        convexHullButton = new JButton("Convex Hull");
        convexHullButton.setActionCommand("convexHull");
        convexHullButton.addActionListener(this);
        convexHullButton.setEnabled(false);  // enable after convex hull found
        
        
        shortestPathButton = new JButton("Shortest Path");
        shortestPathButton.setActionCommand("shortestPath");
        shortestPathButton.addActionListener(this);
        shortestPathButton.setEnabled(false);  // enable after convex hull found

        colorButton = new JButton("Change Color");
        colorButton.setActionCommand("changeColor");
        colorButton.addActionListener(this);

        clearButton = new JButton("Clear");
        clearButton.setActionCommand("clearDiagram");
        clearButton.addActionListener(this);

        quitButton = new JButton("Quit");
        quitButton.setActionCommand("quit");
        quitButton.addActionListener(this);

        // keeps the user updated on what to do next -- centered in the window;
        // at first, the user should just click the canvas or an enabled button
        userMessage = new JLabel(CLICK_MSG, SwingConstants.CENTER);

        // drop the buttons into the button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(convexHullButton);
        buttonPanel.add(shortestPathButton);
        buttonPanel.add(colorButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);
        buttonPanel.add(coordinateLabel); 
        

        // put the user message and panel of buttons together in the
        // bottom part of the main window, with message on top of buttons
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(menuBar, BorderLayout.NORTH);
        northPanel.add(coordinateLabel);
        
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(userMessage, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        // add the canvas and message/button panel to the window
        add(canvas, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);

        // make the entire window visible
        setVisible(true);

        convexHull = null;  // this will need to be updated later
        
        exitActionButton.addActionListener(this); //listens to the exit button
    //    coordinatesButton.addActionListener(this);
        checkCoordinates.addActionListener(this); //listens to the check button

    }

    /**************************************************************************
     * This method is called whenever one of the JButton listeners detects 
     * a button click.  Handle each button click appropriately below.
     *
     * @param  event The ActionEvent triggering this method.
     **************************************************************************/
    public void actionPerformed(ActionEvent event) 
    {
        String buttonIdentifier = event.getActionCommand();

        if (buttonIdentifier.equals("changeColor")) 
        {
            // change the points/lines color
            canvas.changeColor();
        } 
        else if (buttonIdentifier.equals("clearDiagram")) 
        {
            // if the diagram is cleared, put buttons back in original states
            shortestPathButton.setEnabled(false);
            colorButton.setEnabled(true);
            convexHullButton.setEnabled(false);

            // wipe the canvas of all drawing
            canvas.clear();

            // encourage the user to click more points...
            setUserMessage(CLICK_MSG);

        }
        else if (buttonIdentifier.equals("quit"))
        {
            // program quits without question
            System.exit(0);
        }
        else if (buttonIdentifier.equals("shortestPath"))
        {
            // disable other buttons appropriately -- while selecting 
            // endpoints for shortest path, disable all buttons except
            // clear and quit
            shortestPathButton.setEnabled(false);
            colorButton.setEnabled(false);

            // encourage the user to click on a button that will be the
            // start of the shortest path
            setUserMessage(START_PT_MSG);

            // call CanvasPanel method to select the shortest path
            // starting and ending points... (see selectEndpoints in
            // CanvasPanel.java)
            canvas.selectEndpoints();
        }
        
        else if (buttonIdentifier.equals("convexHull"))
        {
            convexHull = new ConvexHull(canvas.getPoints()); //create an instance of the convex hull class
            canvas.setConvexHull(convexHull.computeConvexHull()); //compute the convex hull
            convexHullButton.setEnabled(false);//disable the button since we just clicked it 
            colorButton.setEnabled(true); 
            shortestPathButton.setEnabled(true);
            
        }
        
        else if(buttonIdentifier.equals("exitAction"))
        {
            System.exit(0); //exit the program 
        }
        
        else if (buttonIdentifier.equals("checkAction")) // if the radio button is pressed
        {
              if (checkCoordinates.isSelected() == false) // if it is not checked do not show the coordiantes
            {
             //   System.out.println("Going inside loop");
                labelState = false; 
                coordinateLabel.setVisible(false);
            }
              else // if it is checked, show the coordiantes 
            {
                labelState = true; 
                coordinateLabel.setVisible(true);
                
            }
              //  System.out.println("Going inside loop"); 
               // coordinateLabel.setText("The coordinates are" );
                LinkedList<Point> myCoordiantes = canvas.getPoints(); 
                String coordinatesString = new String("Vertices are ");
                String myString = new String();
                String myXString = new String(""); 
                String myYString = new String(); 
                for (int i=0; i<myCoordiantes.size();i++)
                {
            //        System.out.print(myCoordiantes.get(i)); 
                    myString += myCoordiantes.get(i); 
                    myXString += myString.substring(17,20); 
              //      System.out.println("myXstring is " + myXString); 
                    myYString += myString.substring(23,26); 
               //     System.out.println("myYstring is " + myYString); 
                    coordinatesString += "(" + myXString + ", " + myYString + ") , "; 
                    myXString = "";
                    myYString = ""; 
                    myString = ""; 
                   
                    coordinatesString+= "";
                }
        //        System.out.println(coordinatesString); 
                coordinateLabel.setText(coordinatesString); //sets the text of the coordinates for printing to the coordinates label
        
        }
        /*
        else if(buttonIdentifier.equals("showCoordinates"))
        {
            if (labelState == false)
            {
                System.out.println("Going inside loop");
                labelState = true; 
                coordinateLabel.setVisible(true);
            }
            else
            {
                labelState = false; 
                coordinateLabel.setVisible(false);
                
            }
                System.out.println("Going inside loop"); 
               // coordinateLabel.setText("The coordinates are" );
                LinkedList<Point> myCoordiantes = canvas.getPoints(); 
                String coordinatesString = new String("Vertices are ");
                String myString = new String();
                String myXString = new String(""); 
                String myYString = new String(); 
                for (int i=0; i<myCoordiantes.size();i++)
                {
                    System.out.print(myCoordiantes.get(i)); 
                    myString += myCoordiantes.get(i); 
                    myXString += myString.substring(17,20); 
                    System.out.println("myXstring is " + myXString); 
                    myYString += myString.substring(23,26); 
                    System.out.println("myYstring is " + myYString); 
                    coordinatesString += "(" + myXString + ", " + myYString + ") , "; 
                    myXString = "";
                    myYString = ""; 
                    myString = ""; 
                   
                    coordinatesString+= "";
                }
                System.out.println(coordinatesString); 
                coordinateLabel.setText(coordinatesString);
        }*/
    }
    

    /**************************************************************************
     * Given a starting point and ending point in the context of an existing
     * convex hull, this method uses implemented methods in the ConvexHull
     * and CanvasPanel classes to compute and then draw the shortest path on
     * the canvas.
     *
     * @param startPoint The starting Point for computing the shortest path.
     * @param endPoint   The ending Point for computing the shortest path.
     **************************************************************************/
    public void computeShortestPath(Point startPoint, Point endPoint)
    {
        // use ConvexHull to compute the shortest path
        LinkedList<Point> shortestPath = 
        convexHull.computeShortestPath(startPoint, endPoint);

        // set (and therefore draw) the shortest path on the canvas
        canvas.setShortestPath(shortestPath);

        // enable buttons appropriately so the user may continue to interact
        shortestPathButton.setEnabled(true);
        colorButton.setEnabled(true);
    }

    /**************************************************************************
     * This method sets the JLabel that appears in the bottom of the GUI,
     * indicating to the user what steps to take next.  See the String 
     * constants defined in AppWindow.  Note that this method is called
     * from CanvasPanel.
     *
     * @param msg A user-help String that will appear in the bottom GUI.
     **************************************************************************/
    public void setUserMessage(String msg)
    {
        userMessage.setText(msg);
    }

    /**************************************************************************
     * This method simply disables the shortest path button.  This method is
     * called from CanvasPanel when, for example, the user has already computed
     * a convex hull but then clicks more points, potentially invalidating the
     * computed hull (i.e., a new hull should be computed).
     **************************************************************************/
    public void disableShortestPath()
    {
        shortestPathButton.setEnabled(false);
    }
    
    public void enableShortestPath()
    {
        shortestPathButton.setEnabled(true);
    }
    
    
    
    public void setConvexHullButton(boolean state)
    {
        convexHullButton.setEnabled(state); 
    }

    // This method allows AppWindow to be run from the command line.
    public static void main(String[] args) 
    {
        AppWindow project = new AppWindow();
    }
	
} // class AppWindow
