import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Play the game of life in one dimension, with a graphical user interface.
 * GUILife1D extends the AWT Panel class so that an object created from it
 * can be used to hold other AWT components. We implement the ActionListener
 * interface to indicate that GUILife1D can handle mouse events to components
 * it holds.
 */
public class GUILife1D extends JPanel implements ActionListener {
    // Visual version of world's state uses colors to represent dead and live.
    public static final Color LIVE_COLOR = Color.blue;
    public static final Color DEAD_COLOR = Color.black;
    
    // String version of the world's state uses these symbols.
    public static final char LIVE_CELL = '*';
    public static final char DEAD_CELL = '-';

    private JPanel 	buttonPanel;
    private JPanel 	displayPanel;
    private JPanel	filePanel;
    private JLabel[] 	cells;
    private JTextField	fileDisplayed;
    private JButton  	advanceButton;
    private JButton	quitButton;

    /**
     * This method is called when we execute this class as a program.
     * It handles the input of the initial configuration and creating
     * the GUILife1D object that manages the Graphical user interface.
     */
    public static void main(String args[]) {
	StringBuffer world = null;	// Initial configuration of the "world"
	String worldFile = null;	// Name of the file containing world.

	// All graphical applications must have a top level frame object
	// that is used to display their visual interface.
	JFrame myFrame = new JFrame("GUILife1D");

	// Get name of file containing initial world configuration.
	worldFile = getInitialWorldFile(myFrame);

	// Retrieve the contents of the world file.
	world = inputWorld(worldFile);
	
	if (world.length() < 4) {
	    System.out.println (
		"Sorry, the program only works for worlds with four");
	    System.out.println ("or more cells.\n");
	    System.exit(0);
	}

	// Life1DGame is a class which implements a visual version of
	// the one dimensional Game of Life given an initial configuration.
	// Life1DGame extends the AWT Panel class.
	GUILife1D myGame = new GUILife1D(world.toString(), worldFile);

	// Add the game to the Frame and display it.
	myFrame.add(myGame);
	myFrame.pack();
	myFrame.setVisible(true);
    }

    /**
     * Construct GUILife1D object with the initial cell configuration
     * given in the form of a string of characters. Note that this
     * constructor is not static because it accesses the data fields
     * that belong to the object that main will create.
     * @param	initialWorld	A string indicating which cells should be
     *				live and which dead initially.
     */
    public GUILife1D(String initialWorld, String worldFile) {

	// This method essentially sets up the GUI components.
	// BorderLayout lets us stack one panel atop the other
	// vertically.
	setLayout(new BorderLayout());

	// Create the array of cells. Each is a Label, and we use the
	// background color to indicate which cells are live and which
	// aren't.
	displayPanel = new JPanel();

	// FlowLayout "flows" new element horizontally.
	displayPanel.setLayout(new FlowLayout());
	cells = new JLabel[initialWorld.length()];
	for (int i = 0; i < initialWorld.length(); i++) {
	    cells[i] = new JLabel();
	    cells[i].setOpaque(true);
	    cells[i].setPreferredSize(new Dimension(16, 24));
	    if (initialWorld.charAt(i) == LIVE_CELL) {
		cells[i].setBackground(LIVE_COLOR);
	    } else {
		cells[i].setBackground(DEAD_COLOR);
	    }
	    displayPanel.add(cells[i]);
	}

	// We also want a panel to contain the control buttons.
	buttonPanel = new JPanel();
	buttonPanel.setLayout(new FlowLayout());

	advanceButton = new JButton ("Advance");
	advanceButton.addActionListener(this);
	buttonPanel.add(advanceButton);

	quitButton = new JButton ("Quit");
	quitButton.addActionListener(this);
	buttonPanel.add(quitButton);

	// And a panel to display the file name
	filePanel = new JPanel();
	filePanel.setLayout(new FlowLayout());
	JLabel l = new JLabel("File");
	filePanel.add(l);
	fileDisplayed = new JTextField(30);
    fileDisplayed.addActionListener(this);
    fileDisplayed.setActionCommand("text");
	fileDisplayed.setText(worldFile);
	filePanel.add(fileDisplayed);

	// This arranges the display panel above the panel containing
	// the control buttons.
	add(filePanel, "North");
	add(displayPanel, "Center");
	add(buttonPanel, "South");
    }

    /**
     * Get the name of the file from which the initial configuration
     * should be read, using the standard file dialog box provided
     * by the AWT. This method is static because it doesn't need
     * to access any of the data members. It is called before any
     * object of type GUILife1D is created.
     * @param 	f	parent Frame for the dialog box.
     * @return	The name of the selected file, or null if no file was
     *		selected.
     */
    public static String getInitialWorldFile(JFrame f) {
	String appDir = (String) (System.getProperty("user.dir"));

	JFileChooser d;
	d = new JFileChooser(appDir);
	d.setDialogTitle("Select Initial World)");
	int retVal = d.showOpenDialog(f);
	String fileName = null;
        String fileSeparator = (String) (System.getProperty("file.separator"));
	if(retVal == JFileChooser.APPROVE_OPTION) {
	    fileName = d.getSelectedFile().getName();
	} else {
	    System.exit(1);
	}

	//String fileName = d.getDirectory() + d.getFile();
	System.out.println("filename = " + fileName);
	if (fileName == null) {
	    // Cancel button was pushed -- assume user wants to quit.
	    System.out.println(
		"No file was specified: program will exit.");
	    System.exit(0);
	}

	return fileName;
    }

    /**
     * Read in an initial world state. This function hides a number of
     * details about how the file is accessed. It is also static because
     * it is called before we create an object of type GUILife1D - the
     * string we return is actually used as the parameter to the
     * GUILife1D constructor.
     * @param worldFileName	The name of a file containing an initial
     *				configuration for the world.
     * @return	A StringBuffer containing the initial configuration.
     */
    public static StringBuffer inputWorld (String worldFileName) {
	FileReader worldFile;
	BufferedReader worldReader = null;
	int worldLength = 0;
	
	try {
	    worldFile = new FileReader(worldFileName);
	    worldReader = new BufferedReader(worldFile);
	} catch (Exception e) {
	    System.out.println(
		"Couldn't open file " + worldFileName + ".");
	    e.printStackTrace();
	    System.exit(1);
	}

	StringBuffer s = null;
	try {
	    String worldString = worldReader.readLine();
	    s = new StringBuffer(worldString);
	} catch (Exception e) {
	    System.err.println(
		"Couldn't read in initial world.");
	    e.printStackTrace();
	    System.exit(1);
	}

	return s;
    }

    /**
     * This is the method that is called whenever an event occurs, such
     * as a mouse click.
     * @param	evt	An object containing information about the
     *			event that occurred.
     */
    public void actionPerformed (ActionEvent evt) {

	// This tells us what caused this event. It should be the
	// label of one of the buttons.
	String s = evt.getActionCommand();
    System.out.println(evt);
    System.out.println(fileDisplayed.getText());

	if (s.equals("Advance")) {
	    // The "Advance" button was clicked.
	    // Change the display to show the next generation. First,
	    // we convert the current state to a string for easier
	    // manipulation.
	    StringBuffer oldWorld = displayToSB();

	    // Now, use the string to generate a string for the next
	    // state of the world.
	    StringBuffer newWorld = generateNextWorld(oldWorld);

	    // Now, use the new string to change the display.
	    updateDisplay(newWorld);
	} else if (s.equals("Quit")) {
	    // The "Quit" button was clicked. Just exit the program.
	    System.out.println("Terminating at user's request.");
	    System.exit(0);
	} else {
	    // Sanity check...
	    System.out.println("Got event - " + s);
	}
    }

    /**
     * Examine the cells in the display, and create a string representation
     * of the state of the world. '*' indicates a live cell and '-'
     * indicates a dead cell in the resulting string.
     * @return	A StringBuffer containing the character representation
     *		of the current state of the world.
     */
    private StringBuffer displayToSB() {
	// Because the cells array is a data member of this class, we
	// automatically have access to it here -- we don't have to
	// pass it in as a parameter.
	StringBuffer worldState = new StringBuffer(cells.length);

	for (int i = 0; i < cells.length; i++) {
	    if (cells[i].getBackground() == LIVE_COLOR) {
		worldState.append(LIVE_CELL);
	    } else {
		worldState.append(DEAD_CELL);
	    }
	}

	return worldState;
    }

    /**
     * Scan the current state of the world and calculate
     * the positions for the next generation.  To avoid generating
     * inconsistencies, the new state of the world is kept in a separate
     * StringBuffer.
     * @param	w	The current state of the world.
     * @return		The new state of the world.
     */

    private StringBuffer generateNextWorld (StringBuffer w)
    {
	int neighbor_cnt;	/* Number of live neighbors of cell i */
	StringBuffer newWorld = new StringBuffer(w.length());

	/* Check each cell in the world to see how it will change */
	for (int i = 0; i < w.length(); i++) {

	    /* Find out how many live neighbors the current cell has */
	    neighbor_cnt = countLiveNeighbors (i, w);

	    /* Actions differ depending on whether the cell is live now */
	    if (w.charAt(i) == LIVE_CELL) {
		switch (neighbor_cnt) {
		  case 0:
		  case 1:
		  case 3:
		    newWorld.insert(i, DEAD_CELL);	// Dies
		    break;
		  default:
		    newWorld.insert(i, LIVE_CELL);	// Remains alive
		    break;
		}
	    } else if (w.charAt(i) == DEAD_CELL) {
		switch (neighbor_cnt) {
		  case 0:
		  case 1:
		  case 4:
		    newWorld.insert(i, DEAD_CELL);	// Remains dead
		    break;
		  default:
		    newWorld.insert(i, LIVE_CELL);	// Becomes alive
		    break;
		}
	    } else {	// Don't know what to do with cell
		System.err.println (
		    "Unknown cell value (" + 
		    w.charAt(i) + ") at index " + i + ".");
		System.exit (0);
	    }
	}

	return newWorld;
    }

    /**
     * Find out how many neighbors of cell are alive.  Be careful
     * of boundary situations
     * @param cell	the position of the cell to examine.
     * @param	w	the current state of the world.
     * @return		How many neighbors are alive.
     */
    private int countLiveNeighbors (int cell, StringBuffer w) {
	int	liveCount;		// Number of live neighbors for cell
	int	worldSize = w.length();	// Length of the world string

	// Cells at either end of the world string do not have four neighbors
	// and must be treated specially. We'll consider the world to be
	// a ring.
	if (cell == 0) 
	    liveCount = live(w.charAt(1)) + live(w.charAt(2)) + 
		live(w.charAt(worldSize - 2)) + live(w.charAt(worldSize - 1));
	else if (cell == 1) 
	    liveCount = live(w.charAt(0)) + live(w.charAt(2)) + 
		live(w.charAt(3)) + live(w.charAt(worldSize -1));
	else if (cell == worldSize - 2)
	    liveCount = live(w.charAt(cell-2)) + live(w.charAt(cell-1)) + 
		live(w.charAt(cell+1)) + live(w.charAt(0));
	else if (cell == worldSize - 1)
	    liveCount = live(w.charAt(cell-2)) + live(w.charAt(cell-1)) +
		live(w.charAt(0)) + live(w.charAt(1));
	else	/* All cells that actually have four neighbors */
	    liveCount = live(w.charAt(cell-2)) + live(w.charAt(cell-1)) 
		+ live(w.charAt(cell+1)) + live(w.charAt(cell+2));
	
	return liveCount;
    }

    /**
     * Does the character c represent a live cell or a dead cell?
     * @param	c	A char representing the state of a single
     * 			cell of the world.
     * @return	This function is used for counting purposes, so return 1
     * 		if c is live, 0 if dead. These quantities can simply be
     *          added together to determine the number of live neighbors.
     */
    private int live (char c) {
	if (c == LIVE_CELL)
	    return 1;
	else
	    return 0;
    }

    /**
     * Change the background colors of the cells to indicate a new
     * configuration of the world.
     * @param	world	A StringBuffer containing a new configuration
     *			for the world.
     */
    private void updateDisplay(StringBuffer world) {
	for (int i = 0; i < world.length(); i++) {
	    if (world.charAt(i) == '*') {
		cells[i].setBackground(LIVE_COLOR);
	    } else {
		cells[i].setBackground(DEAD_COLOR);
	    }
	}
    System.out.println("Not repainting");
	//displayPanel.repaint();
    }
}
