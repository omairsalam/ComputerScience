//Omair Alam 0a6ci

import java.awt.*;
import java.lang.*; 
import java.util.*; 

public class ConvexHull
{
    private LinkedList<Point> convexHullPoints;
    private LinkedList<Point> initialPoints; 
    private LinkedList<Point> routeA; 
    private LinkedList<Point> routeB; 
    private int noOfPoints; 
    private boolean collinear;

    /**
     * @param pointSet The set of points to be considered for computing the convex hull 
     */
    public ConvexHull(LinkedList<Point> pointSet) 
    {
        initialPoints = new LinkedList<Point>();
        convexHullPoints = new LinkedList<Point>(); 
        initialPoints = pointSet; 
        noOfPoints = initialPoints.size(); 
        collinear = false; 
    }

    /**
     * Computes the convex hull from a set of points using the gift wrapping algorithm 
     * @return A linkedList<Point> containing the points of the convex
     */

    LinkedList<Point> computeConvexHull()
    {  
       //if there are no vertices, there is no convex hull
        if(initialPoints.size()==0) // 
        {
            return null;
        }
        
        removeDuplicates(initialPoints);
        
        
        //if there is one button, the convex hull is just that one button
        if (initialPoints.size()==1)
        {
            convexHullPoints.add(initialPoints.getFirst());
            return convexHullPoints;
        }
        
        
        
        
        // if there are two buttons, the convex hull is just those two points 
        if (initialPoints.size()==2)
        {
            convexHullPoints.add(initialPoints.getFirst());
            convexHullPoints.add(initialPoints.getLast());
            return convexHullPoints; 
        }
        
      
        
        for (int i=0; i<initialPoints.size()-2;i++)
        {
            if (getDeterminant((Point)initialPoints.get(i),(Point)initialPoints.get(i+1), (Point)initialPoints.get(i+2))!=0 )
            {
                collinear = false;
                break; 
            }
            else{
                collinear = true;
            }
        }
        
        LinkedList<Point> clonePoints = new LinkedList<Point>(); 
        clonePoints = new LinkedList<Point>(initialPoints); //a copy of the initial points since we don't want to mess with the actual initial points
        boolean rightTurn = true; // meaning if the determinant is 0 or lesser in my implementation 
        Point anchor = computeAnchor(clonePoints); //  //find anchor point (i.e. the point with the lowest y value and with lowest x value if need be)
        
        double maxDistance = getDistance(anchor,clonePoints.get(1)); 
        int indexOfFarthestPoint = 1; 
        
        if (collinear == true)
        {
            for (int i=2; i<clonePoints.size();i++)
            {
                if (getDistance(anchor,clonePoints.get(i))> maxDistance  )
                {
                    maxDistance = getDistance(anchor,clonePoints.get(i)); 
                    indexOfFarthestPoint = i; 
                }
            }
            
            convexHullPoints.add(anchor);
            convexHullPoints.add(clonePoints.get(indexOfFarthestPoint));
            return convexHullPoints; 
        }
      
        

        convexHullPoints.add(anchor); //add the hull to the anchor 
        clonePoints.remove(anchor);
        clonePoints.addLast(anchor); //change position of the anchor from whatever in the initialPoints location to the last. 
        Point p = anchor;  // make point p, i.e. the first point in the set of points to the anchor 

        Iterator linkedListIterator = clonePoints.listIterator(); //to move through the linked list of initial points

        //The gift wrapping algorithm 
        while (linkedListIterator.hasNext())
        {
            Point q = (Point)linkedListIterator.next(); //the q for the determinant 

            
            Iterator secondIterator = clonePoints.listIterator();  
            Point r = new Point(); 

            while (secondIterator.hasNext())
            {
                r = (Point)secondIterator.next();
                
                // if any of the 3 points are same, go to the next point since the 3 points can't be the same 
                if (r.equals(q) || r.equals(p) || q.equals(p))
                {
                    continue; 
                }
                // if the determinant is negative for any combination of (p, q and r) 
                if (getDeterminant(p, q, r)<0)
                {
                    rightTurn = true; // if its true even once, exit the while loop 
                    break; 
                }
                else if(getDeterminant(p,q,r)==0) // if there are collinear points 
                {
                        if (getDistance(p,q)<getDistance(p,r))
                        {
                            q = r;  // if r copmes after q meaning it is farther away, and all are collinear, then make the new q, r 
                        }
                        continue; 
                }
                else
                {
                    rightTurn = false; 
                    continue; 
                }
            }
            
            if (rightTurn == true)
            {
                continue; // if there is a right turn, keep iterating through 
            }
            else // if we got only left turns we have found another hull point 
            {
              p = q; //make the new hull point q, p 
              if (p.equals(anchor)) // if we have reached the anchor point, stop the while loop
              {
                  break; 
              }
              convexHullPoints.add(q); //add q to the hull 
              linkedListIterator.remove(); // remove q from the list of points we are considering for q and r 
              linkedListIterator = clonePoints.listIterator(); 
            }

        }
        
        //find anchor point (i.e. the point with the lowest y value and with lowest x value if need be)
     //   convexHullPoints.add(anchor); 
        
        return convexHullPoints; 
    }

    /**
     * Computes the shortest path between the start and the end coordinates 
     * @param start The starting coordinate of the shortest path 
     * @param end The ending coordiantes of the shortest path 
     * @return a LinkedList<Point> containing the points in order from the starting point to the ending
     * point along the shortest path
     */
    LinkedList<Point> computeShortestPath(Point start, Point end) 
    {
        boolean swappedIndices = false; 
        Point startOriginal = start; // starting point of the path 
        Point endOriginal = end; //ending point of the path 
        routeA = new LinkedList<Point>(); // the first route from start to end 
        routeB = new LinkedList<Point>(); // the second route from start to end 
        
        if (start.equals(end)) // if the first and last points are the same then the shortest path is just one point 
        {
            routeA.add(start);
            return routeA; 
        }
        
        //if start comes in the list after end, exchange their positions, this makes the iteration easier 
        if (convexHullPoints.indexOf(start) > convexHullPoints.indexOf(end)) 
        {
            Point tmp = start; 
            start = end;
            end = tmp; 
            swappedIndices = true; 
        }
        
        routeA.add(start); //start point is the first point added to the shortest path linked list 
        
        Iterator linkedListIterator = convexHullPoints.listIterator(convexHullPoints.indexOf(start)+1); //start form the point after start
        double distanceA=0;
        double distanceB=0;
        
        while(linkedListIterator.hasNext()) //keep iterating till we get to the last point on the hull 
        {
           Point q = (Point)linkedListIterator.next(); // find the next point on the hull
           if (q.equals(end) == false) // if we havent reached the ending point 
           {
               routeA.add(q); //keep adding the point we passed to the route linked list 
               start = q; //now make the point we were at, the starting point 
           }
           else
           {
               routeA.add(q); // if we have reached the ending point, add the ending point to the linked list and stop the loop 
               break; 
           }
        }
         Iterator secondIterator = routeA.listIterator(); 
         routeB = new LinkedList<Point>(convexHullPoints);// we initially make route B the whole convex hull
         
         
         //this while loop subtracts all the points that we found in route A from the points in the convex hull as those points are part of route B
         while (secondIterator.hasNext()) 
         {
            Point s = (Point)secondIterator.next(); 
            if (routeB.contains((Point)s))  // if point S is common between route B and A, then remove it from route B 
            {
                routeB.remove((Point)s);
            }
         }
         //this loop also removes the starting and ending points so add them again
         routeB.addFirst((Point)startOriginal); 
         routeB.addLast((Point)endOriginal);
         
         //find the distance of route A
         for (int i=0; i<routeA.size()-1;i++)
         {
             Point p = routeA.get(i);
             Point q = routeA.get(i+1);
             distanceA += getDistance(p,q);
             
         }
         //find the distance of route B
         for (int i=0; i<routeB.size()-1;i++)
         {
             Point p = routeB.get(i);
             Point q = routeB.get(i+1);
             distanceB += getDistance(p,q);
         }
         
       //  System.out.println("Distance route A " + distanceA );
       //  System.out.println("Distance route B " + distanceB );
         
         if (distanceA < distanceB || distanceA == distanceB) // if A is a shortest distance 
         {
             return routeA;
         }
         else // if B is a shorter distance 
         {
             return routeB; 
         }
    }

    
    /**
     * Computes the anchor (point with the lowest y and x coordinate, if need be, for a set of vertices 
     * @param clonePoints
     * @return the Point which represents the anchor of the convex hull 
     */
    
    
    Point computeAnchor(LinkedList<Point> clonePoints)
    {
        Point anchor = clonePoints.getFirst(); //make the first point the anchor point by default 
        
         for (int i=0; i<clonePoints.size(); i++) // make the lowest point the anchor 
        {
            if  (   anchor.getY() > clonePoints.get(i).getY() )
            {
                anchor = clonePoints.get(i); 
            }
        }
        for (int i=0; i<clonePoints.size(); i++) //check whether there is some point with the same Y value and in that case, make the point with the lesser X value the anchor 
        {
            if ( anchor.getY() == clonePoints.get(i).getY())
            {
                if (anchor.getX() > clonePoints.get(i).getX())
                {
                    anchor = clonePoints.get(i); 
                }
            }
        }
        
        return anchor; 
    }
    
    /**
     * Computes the determinant of 3 points to find whether we should take a left or a right turn 
     * @param p The anchor point 
     * @param q The point which acts as the base of the point for which we test the left and right turns 
     * @param r The point for which we test the left and right turns 
     * 
     * @return the value of the determinant 
     */
    double getDeterminant(Point p, Point q, Point r)
    {
        return p.getX()*q.getY() + p.getY()*r.getX() + q.getX()*r.getY() - r.getX()*q.getY() - p.getX()*r.getY() - q.getX()*p.getY();
    }
    /**
     * Removes duplicates in the Linked List
     * @param clonePoints 
     */
     void removeDuplicates(LinkedList<Point> clonePoints)
    {
        for(int i = 0; i < clonePoints.size(); i++) 
        {
            for(int j = i + 1; j < clonePoints.size(); j++)
            {
                if(clonePoints.get(i).equals(clonePoints.get(j))) 
                { 
                    clonePoints.remove(j);
                }
            }
        }     
    }
    
    /**
     * Computes the distance between two points p and q 
     * @param p
     * @param q
     * @return the value of the distance between the two points
     */
     double getDistance(Point p, Point q)
    {
        return Math.pow( ( Math.pow((p.getX()-q.getX()),2) + Math.pow((p.getY()-q.getY()),2) ),0.5 );  
    }

    public static void main (String[] args)
    {
        LinkedList<Point> myLinkedList = new LinkedList<Point>(); 
        
        myLinkedList.add(new Point(3,6));
        myLinkedList.add(new Point(3,6));
        myLinkedList.add(new Point(1,-4));
        myLinkedList.add(new Point(1,-4));
        myLinkedList.add(new Point(13,6));
        myLinkedList.add(new Point(13,6));
        myLinkedList.add(new Point(3,6));
        myLinkedList.add(new Point(3,6));
        myLinkedList.add(new Point(19,11));
        myLinkedList.add(new Point(7,1));
        myLinkedList.add(new Point(-5,-9));
        myLinkedList.add(new Point(-9,-9));
        myLinkedList.add(new Point(-7,-9));
        myLinkedList.add(new Point(15,11));
        myLinkedList.add(new Point(6,3));
        myLinkedList.add(new Point(8,4));
        myLinkedList.add(new Point(4,0));
        myLinkedList.add(new Point(4,11));
        myLinkedList.add(new Point(2,1));
        myLinkedList.add(new Point(-2,1));
        myLinkedList.add(new Point(4,3));
        myLinkedList.add(new Point(5,2));
        
        ConvexHull myConvexHull = new ConvexHull(myLinkedList);
        //System.out.println(myConvexHull.noOfPoints); 
       // System.out.println(myConvexHull.getDeterminant(p,q,r));
        myConvexHull.computeConvexHull();
       // LinkedList<Point> myShortestPath;
       // myShortestPath = myConvexHull.computeShortestPath(new Point(1,-4),new Point(7,26));
       // System.out.println("This is fun");
        //System.out.println(getDistance(new Point(3,1), new Point(4,5)));

    }

}