import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class ClosestPoints extends JFrame
        implements ActionListener {
    
    // The radius in pixels of the circles drawn in graph_panel
    
    public static final int NODE_RADIUS = 5;
    public static final boolean DEBUG = false;
    
    // GUI stuff
    private JMenuBar menus;
    private JMenu optionsMenu;
    private JMenuItem toggleCoords;
    
    private CanvasPanel canvas = null;
    
    private JPanel buttonPanel = null;
    private JButton drawButton,
            clearButton, quitButton;
    
    // Data Structures for the Points
    
    /*This holds the set of vertices, all
     * represented as type Point.
     */
    private ArrayList<Point> vertices = null;
    
    // Event handling stuff
    private Dimension panelDim = null;
    
    private boolean displayCoords;

    public ClosestPoints() {
        super("Closest Points");
        setSize(new Dimension(700,575));
        
        displayCoords = false;

        menus = new JMenuBar();
        optionsMenu = new JMenu("Options");
        toggleCoords = new JMenuItem("Toggle Coordinate Display");
        toggleCoords.setActionCommand("toggleCoords");
        toggleCoords.addActionListener(this);
        
        optionsMenu.add(toggleCoords);
        menus.add(optionsMenu);
        setJMenuBar(menus);
        
        //Create the drawing area
        canvas = new CanvasPanel(this, displayCoords);
        
        Dimension canvasSize = new Dimension(700,500);
        canvas.setMinimumSize(canvasSize);
        canvas.setPreferredSize(canvasSize);
        canvas.setMaximumSize(canvasSize);
        canvas.setBackground(Color.black);
        
        // Create buttonPanel and fill it
        buttonPanel = new JPanel();
        
        Dimension buttonSize = new Dimension(200,30);
        drawButton = new JButton("Find Closest Points");
        drawButton.setActionCommand("closestPoints");
        drawButton.addActionListener(this);
        
        clearButton = new JButton("Clear");
        clearButton.setActionCommand("clearDiagram");
        clearButton.addActionListener(this);
        
        quitButton = new JButton("Quit");
        quitButton.setActionCommand("quit");
        quitButton.addActionListener(this);
        
        buttonPanel.add(drawButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);
        
	add(canvas, BorderLayout.CENTER);
	add(buttonPanel, BorderLayout.SOUTH);
        
        // Initialize main data structures
        initializeDataStructures();
        
    }
    
    public static void main(String[] args) {
        
        ClosestPoints project = new ClosestPoints();
        project.addWindowListener(new WindowAdapter() {
            public void
                    windowClosing(WindowEvent e) {
                System.exit(0);
            }
        }
        );
        project.pack();
        project.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        
        String actionCommand = e.getActionCommand();
        switch (actionCommand) {
            case "closestPoints":
                // Find the closest points
                canvas.clearEdges();
                ArrayList<Point> closest = null;
                
                // Uncomment this line when you have completed your
                // FindClosest class.
                closest = FindClosest.findClosestPoints(vertices);
                
                if (closest != null && closest.size() == 2)
                    canvas.addEdge(closest);
                //canvas.repaint();
                break;
            case "clearDiagram":
                canvas.clear();
                //canvas.repaint();
                break;
            case "quit":
                System.exit(0);
                break;
            case "toggleCoords":
                displayCoords = !displayCoords;
                canvas.setDisplayCoords( displayCoords );
                canvas.repaint();
                break;
        }
    }
    
    
    
    public void initializeDataStructures() {
        vertices = canvas.getPoints();
    }
}



