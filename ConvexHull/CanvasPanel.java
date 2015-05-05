//Omair Alam oa6ci
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;

/**
 * ************************************************************************
 * This class implements the drawing canvas for displaying a convex hull.
 *
 * @author Lewis Barnett, Barry Lawson, Doug Szajda
 * @version 22.1.2015
 *************************************************************************
 */
public class CanvasPanel extends JPanel
        implements MouseListener, MouseMotionListener {

    // note the RGB components for darkGray are #A9A9A9, so same

    private static final int DARK_GRAY_RGB = (int) Color.darkGray.getRed();
    private static final int NODE_RADIUS = 5; // radius of drawn points

    // note that Point instances stored herein will be in (x,y) coordinates
    // relative to Swing -- i.e., x increases left to right and y increases
    // top of window to bottom (contrary to typical orientation)
    private LinkedList<Point> vertices;
    private LinkedList<Point> convexHull;
    private LinkedList<Point> shortestPath;

    private Color currentColor;  // color to draw points and convex hull

    private boolean selectingEndpoints; // false: clicking new points;
    // true: clicking for shortest path
    private Point startingPoint; // stores starting point for shortest path
    private Point endingPoint;   // stores ending point for shortest path

    private AppWindow parent; // need reference for calling methods in AppWindow

    /**
     * ************************************************************************
     * Constructs a new CanvasPanel to be included in AppWindow.
     *
     * @param parent A reference to the AppWindow instance holding this panel.
     *************************************************************************
     */
    public CanvasPanel(AppWindow parent) {
        super();
        addMouseListener(this);
        addMouseMotionListener(this);

        this.parent = parent;  // need a reference to AppWindow for signalling

        vertices = new LinkedList<Point>();
        convexHull = new LinkedList<Point>();
        shortestPath = new LinkedList<Point>();

        selectingEndpoints = false;  // false: allo

        startingPoint = endingPoint = null;

        currentColor = Color.lightGray;  // initial default color
    }

    /**
     * ************************************************************************
     * This method selects a color at random and changes the color of the drawn
     * points and convex hull lines. The color chosen is at least as bright as
     * the dark gray background.
     *************************************************************************
     */
    public void changeColor() {
        // pick a new color at random, at least as bright as the darkGray bg
        Random rng = new Random();
        int offset = DARK_GRAY_RGB + 10;
        currentColor
                = new Color(rng.nextInt(256 - offset) + offset,
                        rng.nextInt(256 - offset) + offset,
                        rng.nextInt(256 - offset) + offset);
        repaint();
    }

    /**
     * ************************************************************************
     * This method clears the canvas of any drawing, including all clicked
     * vertices, any convex hull, and any shortest path with its indicating
     * starting and ending points.
     *************************************************************************
     */
    public void clear() {
        // remove all Points from the existing data structures
        vertices.clear();
        if (convexHull!=null) // if the hull is not made, then don't try to remove an empty linked list 
        {
            convexHull.clear();
        }
        shortestPath.clear();

        selectingEndpoints = false;  // in case clear during endpoint selection
        startingPoint = endingPoint = null;

        repaint();
    }

    /**
     * ************************************************************************
     * This method returns a linked list of all the clicked Points in the
     * canvas, where each Point's y-value has been flipped so that (0,0)
     * corresponds to the lower-left of the window.
     *
     * @return LinkedList<Point> The list of Points clicked in the canvas.
     *************************************************************************
     */
    public LinkedList<Point> getPoints() {
        // create and return a copy of the points stored herein;
        // translate the reversed Y direction of the canvas where (0,0) is upper
        // left to typical view of (0,0) being in bottom left
        Dimension dimension = this.getSize();
        LinkedList<Point> listOfPoints = new LinkedList<Point>();
        for (int i = 0; i < vertices.size(); i++) {
            Point p = vertices.get(i);
            p = new Point(p.x, (int) dimension.getHeight() - p.y);
            listOfPoints.add(p);
            //  System.out.println("ma lun"); 
        }
        return (listOfPoints);
    }
    /*
    public LinkedList<Point> convertToSwing(LinkedList<Point> convexHull) {
        // create and return a copy of the points stored herein;
        // translate the reversed Y direction of the canvas where (0,0) is upper
        // left to typical view of (0,0) being in bottom left
        Dimension dimension = this.getSize();
        LinkedList<Point> myList = new LinkedList<Point>();
        for (int i = 0; i < convexHull.size(); i++) {
            Point p = convexHull.get(i);
            p = new Point((int)p.getX(), (int) dimension.getHeight() - (int)p.getY());
            myList.add(p);
            //  System.out.println("ma lun"); 
        }
        return (myList);
    }*/
    
    
    
    

    /**
     * ************************************************************************
     * This method is called whenever the canvas should be redrawn (e.g., when
     * we invoke repaint()), handling drawing of all clicked points, any
     * computed convex hull, and any computed shortest path plus its endpoints.
     *
     * @param g The Graphics component used for drawing.
     *************************************************************************
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // standard practice is to convert to Graphics2D
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(currentColor);

        // draw each clicked vertex, centered at (x,y) with defined radius
        for (Point p : vertices) {
            g2d.fillOval(p.x - NODE_RADIUS, p.y - NODE_RADIUS,
                    2 * NODE_RADIUS, 2 * NODE_RADIUS);
            // System.out.println("Malun");
        }
       // System.out.println("Inside repaint method");
        // draw the convexHull (if it has been computed)
        if (convexHull != null && convexHull.size()>1) {
         //  System.out.println("before statement");
            g2d.drawLine((int)convexHull.getFirst().getX(),(int)convexHull.getFirst().getY(),(int)convexHull.getLast().getX(),(int)convexHull.getLast().getY());
         //   System.out.println("afterstatement");
            for (int i=0; i<convexHull.size()-1;i++)
            {
                g2d.drawLine((int)convexHull.get(i).getX(),(int)convexHull.get(i).getY(),(int)convexHull.get(i+1).getX(),(int)convexHull.get(i+1).getY());
         //       System.out.println("The coordinates are " + convexHull.get(i));
            }
           
        }

        // encircle the starting and ending points of the shortest path
        // (if they have been chosen)
        if (startingPoint != null) {
            g2d.setColor(Color.green);  // highlight start by green
            Point p = startingPoint;
            g2d.drawOval(p.x - (2 * NODE_RADIUS), p.y - (2 * NODE_RADIUS),
                    4 * NODE_RADIUS, 4 * NODE_RADIUS);
            g2d.setColor(currentColor); // revert to old color
        }
        if (endingPoint != null) {
            g2d.setColor(Color.red); // highlight end by red
            Point p = endingPoint;
            g2d.drawOval(p.x - (2 * NODE_RADIUS), p.y - (2 * NODE_RADIUS),
                    4 * NODE_RADIUS, 4 * NODE_RADIUS);
            g2d.setColor(currentColor); // revert to old color
        }

        // draw the shortest path (if it has been computed)
        if (shortestPath != null && startingPoint !=null && endingPoint !=null) {
            g2d.setColor(Color.orange);
            for(int i=0; i<shortestPath.size()-1;i++) //iterate through the linked list and draw a line between each set of 2 points
            {
                g2d.drawLine((int)shortestPath.get(i).getX(),(int)shortestPath.get(i).getY(),(int)shortestPath.get(i+1).getX(),(int)shortestPath.get(i+1).getY());
            }
        }

    }

    /**
     * ************************************************************************
     * This method is used to turn on the selection of the starting and ending
     * points of the shortest path. Note the use of selectingEndpoints along
     * with startingPoint and endingPoint in identifying where in that selection
     * process the user is (see also mousClicked() below).
     *************************************************************************
     */
    public void selectEndpoints() {
        startingPoint = endingPoint = null;  // reset if already selected
        shortestPath.clear();
        repaint(); // repaint, as start/end circles may need to be removed

        selectingEndpoints = true;  // toggle Boolean indicating selecting is on
    }

    /**
     * ************************************************************************
     * This method clears the selection of shortest path endpoints, if for any
     * reason they should be invalidated (e.g., computing a different shortest
     * path).
     *************************************************************************
     */
    public void clearEndpoints() {
        startingPoint = endingPoint = null;
        shortestPath.clear();
        repaint(); // repaint, as start/end circles may need to be removed
    }

    /**
     * ************************************************************************
     * This method is used to set the convex hull data structure stored in the
     * CanvasPanel, thereby resulting in that hull being drawn. (See the
     * paintComponent method in CanvasPanel.)
     *
     * @param hull LinkedList of Points corresponding to computed convex hull.
     *************************************************************************
     */
    public void setConvexHull(LinkedList<Point> hull) {
        // Note that what's coming from the user via ConvexHull will assume that
        // (0,0) is lower left; translate the y-values so that all values stored
        // inside this object will be the typical Swing orientation with (0,0)
        // in the upper left
        Dimension dimension = this.getSize();
        convexHull = new LinkedList<Point>();
        for (Point p : hull) {
            Point p_xlate = new Point(p.x, (int) dimension.getHeight() - p.y);
            convexHull.add(p_xlate);
        }

        repaint(); // redraw the canvas so the hull will be displayed
    }

    /**
     * ************************************************************************
     * This method is used to set the shortest path data structure stored in the
     * CanvasPanel, thereby resulting in that path being drawn. (See the
     * paintComponent method in CanvasPanel.)
     *
     * @param hull LinkedList of Points corresponding to computed shortest path.
     *************************************************************************
     */
    public void setShortestPath(LinkedList<Point> path) {
     
        Dimension dimension = this.getSize();
        shortestPath = new LinkedList<Point>();
        for (Point p : path) {
            Point p_xlate = new Point(p.x, (int) dimension.getHeight() - p.y);
            shortestPath.add(p_xlate);
        }
        repaint(); // redraw the canvas so the hull will be displayed
    }

    // MouseListener methods
    public void mouseClicked(MouseEvent e) {
        
       boolean rightClick = false; // if we haven't right clicked 
        Point clickedPoint = e.getPoint();
    //  System.out.println("here I am 1 ");
        
        if (!selectingEndpoints) {
            // System.out.println("here I am 2 ");
            // if not selecting endpoints, just add clicked point to
            // the list of vertices...
            
             if (e.getModifiers() == MouseEvent.BUTTON3_MASK) // if there was a right click 
            {
               //   System.out.println("here I am 3 ");
                rightClick = true; // we have right clicked 
 
                for (int i=0; i<vertices.size();i++) // iterate through the coordinates and remove any points from vertices linked list if they match our right clicked point 
                {

                    if (clickedPoint.equals(vertices.get(i)))
                    {
                        Point p = vertices.get(i);
                        vertices.remove(i);
                    }
                }           //remove the point from the vertices 
                
                if(vertices.size()<2)
                {
              //      System.out.println("goes inside this condition");
                    parent.setConvexHullButton(false); // if the number of points that we remove make the size of the list of points smaller than 2, disable the convex hull button
                    parent.disableShortestPath();
                    repaint();
                }
                else
                {
             //       System.out.println("goes inside this condition 2");
                    parent.setConvexHullButton(true);
                    if (shortestPath == null)
                    {
                        parent.disableShortestPath();
                    }
                    repaint();
                }
              
                        
             }
                
            
             else // if it was a left click 
             {
             //    System.out.println("here I am 4 ");
                 rightClick = false; 
                 vertices.add(clickedPoint);
                 
                 if (vertices.size()<2)
                 {
                     parent.setConvexHullButton(false);
                     parent.disableShortestPath();
                 }
                 
                 else
                 {
                     parent.setConvexHullButton(true); 
                     if (shortestPath == null)
                     {
                        parent.disableShortestPath();  
                     }
                 }
                 repaint(); 
             }
           
        
           

            // if clicking additional points after a hull and/or shortest
            // path have been computed, remove the shortest path -- may no
            // longer make sense
            startingPoint = endingPoint = null;
      
            
            if (convexHull == null)
            {
                shortestPath.clear();
                parent.disableShortestPath();
                repaint();
            }
            
          //   System.out.println("here I am 5 ");
            if(vertices.size()>1) // enable the convex hull button if we have more than 1 point in the vertices linked list 
            {
              //  System.out.println("MA KO LUN");
                parent.setConvexHullButton(true);
                repaint(); 
            }
            else
            {
             //    System.out.println("MA KO LUN 2");
                 parent.setConvexHullButton(false);
                 repaint(); 
            }
            
          //  System.out.println(convexHull.size());
            if (convexHull.size()==0)
            {
              //  System.out.println("true lun");
                convexHull.clear(); 
               // parent.setConvexHullButton(false);
                repaint(); 
            }
            
            if (convexHull != null) // remove the convex hull if another left click made after its computation
            {
              // System.out.println("MA KO LUN 3");
                
                if (rightClick == false) // if it was a left click, just clear the convex hull; 
                {
                    convexHull.clear(); 
                    parent.disableShortestPath();
                    repaint();
                }
                
                if (vertices.size()==1)
                {
                    convexHull.clear(); 
                    parent.setConvexHullButton(false);
                }
                else
                {
                   // System.out.println("Inside if statement");
                    for (int i=0; i<convexHull.size();i++) // if it was a right click check whether the click was on the hull points or not 
                    {
                        if (clickedPoint.equals(convexHull.get(i)))
                        {
                            //System.out.println("Inside inner most condition");
                            convexHull.clear();
                            parent.setConvexHullButton(true);
                            parent.disableShortestPath();
                            repaint();
                        }
                        else
                        {
                             parent.setConvexHullButton(false);
                             parent.enableShortestPath();
                             repaint(); 
                        }
                    }
                    
                }
            }

            repaint();
        } else {
            // if selecting endpoints, we need to toggle state from starting
            // point to ending point;
            // make sure the point is in the convex hull, but need to check 
            // within a radius (it's hard to click right on the point!)
            for (Point p : convexHull) {
                if (p.x - NODE_RADIUS <= clickedPoint.x
                        && clickedPoint.x <= p.x + NODE_RADIUS
                        && p.y - NODE_RADIUS <= clickedPoint.y
                        && clickedPoint.y <= p.y + NODE_RADIUS) {
                    if (startingPoint == null) {
                        startingPoint = p;
                        repaint();  // draw the start pt green circle
                        parent.setUserMessage(AppWindow.END_PT_MSG);
                        break;
                    } else // endingPoint == null
                    {
                        endingPoint = p;
                        repaint();  // draw the end pt red circle
                        selectingEndpoints = false;

                        // remember to convert y values before sending back to user...
                        Dimension dimension = this.getSize();
                        Point startPoint = new Point(startingPoint.x,
                                (int) dimension.getHeight() - startingPoint.y);
                        Point endPoint = new Point(endingPoint.x,
                                (int) dimension.getHeight() - endingPoint.y);

                        // have AppWindow do the right thing to compute shortest path
                        parent.computeShortestPath(startPoint, endPoint);

                        parent.setUserMessage(AppWindow.CLICK_MSG);
                        break;
                    }
                }

            }
        }

    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    // MouseMotionListener methods
    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }
}
