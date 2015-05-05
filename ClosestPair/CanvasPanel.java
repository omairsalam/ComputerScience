
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//import javax.swing.event.*;
//import java.awt.geom.*;
import java.util.*;

public class CanvasPanel extends JPanel
        implements MouseListener, MouseMotionListener {
    
    ClosestPoints parent = null;
    ArrayList<Point> vertices = null;
    ArrayList<Edge> edges = null;
    
    Color currentColor = Color.red;
    // Determine whether point coordinates are displayed on the canvas
    private Boolean displayCoords;
    private static final boolean DEBUG = false;
    
    public CanvasPanel(ClosestPoints _parent, boolean showCoords) {
        super();
        addMouseListener(this);
        addMouseMotionListener(this);
        parent = _parent;
        vertices = new ArrayList<Point>();
        edges = new ArrayList<Edge>();
        displayCoords = showCoords;
    }
    
    /**
     * Get the value of the displayCoords instance variable
     * 
     * @return displayCoords value
     */
    public boolean getDisplayCoords(){
        return displayCoords;
    }
    
    /**
     * Change the value of the displayCoords instance variable
     * 
     * @param newValue New value for displayCoords
     */
    public void setDisplayCoords( boolean newValue ){
        displayCoords = newValue;
    }

    public void addVertex(Point v){
        vertices.add(v);
        repaint();
    }
    
    public void addEdge(Edge e) {
        edges.add(e);
        repaint();
    }
    
    public void addEdge(ArrayList<Point> pts){
        Edge e = new Edge(pts.get(0), pts.get(1));
        edges.add(e);
        repaint();
    }
    
    public void changeColor() {
        if (currentColor.equals(Color.red)) {
            currentColor = Color.yellow;
        } else {
            currentColor = Color.red;
        }
        repaint();
    }
    
    public void clear(){
        vertices.clear();
        edges.clear();
        repaint();
    }
    
    public void clearEdges(){
        edges.clear();
        repaint();
    }
    
    public ArrayList<Point> getPoints(){
        return vertices;
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(currentColor);
        
        ListIterator<Point> iterator = vertices.listIterator(0);
        
        // Draw all points
        Point currentVertex = null;
        
        while (iterator.hasNext()){
            currentVertex = iterator.next();
            g.fillOval(currentVertex.x - parent.NODE_RADIUS,
                    currentVertex.y - parent.NODE_RADIUS,
                    2*parent.NODE_RADIUS, 2*parent.NODE_RADIUS);
            if (displayCoords) {
                StringBuilder coords = new StringBuilder();
                coords.append("(");
                coords.append(currentVertex.x);
                coords.append(", ");
                coords.append(currentVertex.y);
                coords.append(")");
                g.drawString(coords.toString(), 
                        currentVertex.x - 70, currentVertex.y + 20 );
            }
        }
        
        // Edge between closest points
        g.setColor(Color.green);
        ListIterator<Edge> edgeIterator = edges.listIterator(0);
        
        Edge currentEdge = null;
        int ptCnt = 0;
        
        while (edgeIterator.hasNext()){
            currentEdge = edgeIterator.next();
            Point p1 = currentEdge.getOnePoint();
            Point p2 = currentEdge.getOtherPoint();
            if (DEBUG) {
                System.out.println("Drawing edge " + currentEdge);
                g.drawString(""+ ptCnt, p1.x + 5, p1.y);
                ptCnt++;
            }
            g.fillOval(p1.x - parent.NODE_RADIUS,
                    p1.y - parent.NODE_RADIUS,
                    2*parent.NODE_RADIUS, 2*parent.NODE_RADIUS);
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
            g.fillOval(p2.x - parent.NODE_RADIUS,
                    p2.y - parent.NODE_RADIUS,
                    2*parent.NODE_RADIUS, 2*parent.NODE_RADIUS);
        }
    }
    
    // MouseListener methods
    public void mouseClicked(MouseEvent e) {
        Point click_point = e.getPoint();
        vertices.add(click_point);
        repaint();
    }
    
    public void mouseExited(MouseEvent e) {}
    
    public void mouseEntered(MouseEvent e) {}
    
    public void mouseReleased(MouseEvent e) {}
    
    public void mousePressed(MouseEvent e) {}
    
    // MouseMotionListener methods
    public void mouseDragged(MouseEvent e) {}
    
    public void mouseMoved(MouseEvent e) {}
}







