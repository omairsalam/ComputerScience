/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *Finds the closest pair of points in a set of points. If the set contains just one point, the closest pair doesn't exist.
 * @author Omair Alam
 */
import java.util.*;
import java.awt.*;
import java.lang.*;

public class FindClosest{
    
    public static ArrayList<Point> points;
    
    /**
     * Default constructor for FindClosest which puts the points in our class variable points
     */
    public FindClosest(ArrayList<Point> pointSet)
    {
        points = new ArrayList<Point>();
        points = pointSet; 
    }
    
    /**
     * Given a list of points, return a new ArrayList<Point> containing 0 entries if allPoints did not contain 
     * enough points to calculate a closest pair, or the two points with the smallest distance between them 
     * from allPoints if it did contain enough points to calculate a closest pair.
     * @param allPoints the list of points for which we are finding the two closest points 
     * @return the two closest points. 0 if there are not enough points to find the two closest ones
     */
    public static ArrayList<Point> findClosestPoints(ArrayList<Point> allPoints)
    { 
        ArrayList<Point> copyPoints = new ArrayList<Point>(allPoints); 
        Collections.sort(allPoints, new XPointCompare()); //points sorted based on x cordinates
        Collections.sort(copyPoints, new YPointCompare()); //points sorted based on the y coordinates
        
        if (allPoints.size() == 1 || allPoints.size() == 0 ) // if the initial list only contains 1 or 0 points, return an empty list 
        {
            return new ArrayList<Point>(); 
        }
        
        return regionsRecursion(allPoints,copyPoints); //otherwise, call the recursive function
    }
    
    /**
     * Uses recursion to find two closest points 
     * @param allPoints The array of points that we need to search through 
     * @param start The starting index of the array 
     * @param end The ending index of the array
     * @return An ArrayList containing two points with the shortest distance between them 
     */
    public static ArrayList<Point> regionsRecursion(ArrayList<Point> xPoints, ArrayList<Point> yPoints)
    {   
         
        if (xPoints.size() == 2 || xPoints.size() == 1 || xPoints.size() == 0 ) // base case 
        {
            return xPoints; 
        }
        
            ArrayList<Point> leftHalf = new ArrayList(xPoints.subList(0,xPoints.size()/2)); //divide left list at median
            ArrayList<Point> rightHalf =  new ArrayList(xPoints.subList((xPoints.size()/2), xPoints.size())); //divide right list at median
            ArrayList<Point> yCentre = new ArrayList<Point>(leftHalf); //will contain all the points in the delta region 
            Collections.sort(yCentre, new YPointCompare());
            
            
            ArrayList<Point> closestPair = regionsRecursion(leftHalf, yCentre); //recursively divide the left region
            yCentre.clear(); //clear the centre region
            yCentre.addAll(rightHalf);  //fill it up with values in the right half 
            Collections.sort(yCentre, new YPointCompare()); //sort the points again 
            
            
            ArrayList<Point> closestPairRight = regionsRecursion(rightHalf, yCentre); // recursively divide the right region
            
            double distanceLeft = getDistance(closestPair);
            double distanceRight = getDistance(closestPairRight); //find the smallest distance between points on the same side of the median line 
            double minDistance = 0; 
            
            if (distanceLeft>distanceRight) // if the closest pair has a greater distance than the closest pair on the right 
            {
                 minDistance = distanceRight; //make the right the closest distance 
                 closestPair = closestPairRight; //make the right the closest pair 
            }
                
            else
            {
                minDistance = distanceLeft; 
            }
            
            yCentre.clear();
            double centerX = rightHalf.get(0).getX(); //find the mean line and thats the x coordinate of the right half since we do the splitting at this coordiante
            
            //add all the points that are within the minimum distance from the mean line 
            for (Point point : yPoints)
            {
                if (Math.abs(centerX - point.x) < minDistance)
                {
                    yCentre.add(point);
                }
            }
            
            //iterate through all of the points and see whether we can find two points having a shortest distance than our expected closest pair 
            for (int i = 0; i < yCentre.size() - 1; i++)
            {
              Point point1 = yCentre.get(i);
              for (int j = i + 1; j < yCentre.size(); j++)
              {
                Point point2 = yCentre.get(j);
                if ((point2.y - point1.y) >= minDistance) // if the distance between the points is greater than the minimum distance move to the next point
                  break;
                double distance = getPointDistance(point1, point2); // find the distance between the two points 
                if (distance < minDistance) // if this distance is smaller than the minimum distance, make this the closest pair and make this distance the new smallest distance 
                {
                  closestPair.clear();
                  closestPair.add(point1);
                  closestPair.add(point2);
                  minDistance = distance;
                }
              }
            }
            return closestPair; //return the closest pair 
        
    }

   
    /**
     * Finds the distance between two points
     * @param p The first point
     * @param q The second point 
     * @return The distance between the two points
     * 
     */
    public static double getDistance(ArrayList<Point> myPoints)
    {
        if (myPoints.size() == 1 || myPoints.size() == 0 )
        {
            return Double.MAX_VALUE; //if there are only 1 or 0 points, we want the distance between them to be largest so they never get selected as shortest distances
        }
        else
        {
            return Math.pow( ( Math.pow((myPoints.get(0).getX()-myPoints.get(1).getX()),2) + Math.pow((myPoints.get(0).getY()-myPoints.get(1).getY()),2) ),0.5 );  
        }
    }
    
    /**
     * Get the distance between two points 
     * @param p Point A 
     * @param q Point B
     * @return The distance between the two points
     */
    public static double getPointDistance(Point p, Point q)
    {
            return Math.pow( ( Math.pow((p.getX()-q.getX()),2) + Math.pow((p.getY()-q.getY()),2) ),0.5 );  
    }
    
   /**
     * This sorts the points by the X coordinates
     */
    public void sortByXCoordinates() {

        Collections.sort(this.points, new XPointCompare());

    }
    
     /**
     * This sorts the points by the Y coordinates
     */
    public void sortByYCoordinates() {

        Collections.sort(this.points, new YPointCompare());

    }
    
    /**
     * Compares points based on xCoordinates 
     */
     public static class XPointCompare
        implements Comparator<Point> {
         
        @Override  
        public int compare(final Point a, final Point b) {
            if (a.x < b.x) {
                return -1;
            }
            else if (a.x > b.x) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }
     
     /**
      * Compares points based on yCoordinates
      */
      public static class YPointCompare
        implements Comparator<Point> {

        @Override  
        public int compare(final Point a, final Point b) {
            if (a.y < b.y) {
                return -1;
            }
            else if (a.y > b.y) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }
      
      public static void main(String[] args) {
          
        ArrayList<Point> myArrayList = new ArrayList<Point>(); 
        
        //TEST 1 - ONLY ONE POINT - EXPECTED RESULT EMPTY ARRAY
        myArrayList.clear();
        myArrayList.add(new Point(3,10));
        System.out.println(findClosestPoints(myArrayList));
        
        
        //TEST 2 - TWO POINTS - EXPECTED RESULT ARRAY OF SAME TWO POINTS
        myArrayList.clear();
        myArrayList.add(new Point(5,2));
        myArrayList.add(new Point(6,3));
        System.out.println(findClosestPoints(myArrayList));
       
       
        //TEST 3 - 100 Random Points between 0 and 50 - EXPECTED RESULTS - Points with same coordiantes
        myArrayList.clear();
        Random r = new Random(23173); 
        
        for (int i=0; i<100;i++)
        {
            myArrayList.add(new Point(r.nextInt(50),r.nextInt(50)));
        }
        System.out.println(findClosestPoints(myArrayList));

        
        //TEST 4 - TWO IDENTICAL POINTS 
        myArrayList.clear();
        myArrayList.add(new Point(5,2));
        myArrayList.add(new Point(5,2));
        System.out.println(findClosestPoints(myArrayList));
        
        //TEST 5 - 2 identical points and 1 random  - EXPECTED RESULTS - Points with same coordiantes 
        myArrayList.clear();
        myArrayList.add(new Point(5,2));
        myArrayList.add(new Point(5,2));
        myArrayList.add(new Point(5,-1));
        System.out.println(findClosestPoints(myArrayList));
        
        //TEST 6 - 2 pairs of identical points - EXPECTED RESULTS - Any of the same coordinate pairs
       
        myArrayList.add(new Point(5,2));
        myArrayList.add(new Point(5,2));
        myArrayList.add(new Point(5,-1));
        myArrayList.add(new Point(5,-1));
        System.out.println(findClosestPoints(myArrayList));
        
        //TEST 7 - 100 Random Points between -25 and 25 
         myArrayList.clear();
        for (int i=0; i<100;i++)
        {
            myArrayList.add(new Point(r.nextInt(50)-25,r.nextInt(50)-25));
        }
        System.out.println(findClosestPoints(myArrayList));
        
        //TEST 8 - 3 Sets of different points with the same distance between pairs , return any of the shortest distance pairs  
        myArrayList.clear();
        myArrayList.add(new Point(7,2));
        myArrayList.add(new Point(7,4));
        myArrayList.add(new Point(1,2));
        myArrayList.add(new Point(1,4));
        myArrayList.add(new Point(4,2));
        myArrayList.add(new Point(4,4));
        System.out.println(findClosestPoints(myArrayList));
        
        //TEST 9 - 4 sets of different points with same distances between all points
        myArrayList.clear();
        myArrayList.add(new Point(1,1));
        myArrayList.add(new Point(1,2));
        myArrayList.add(new Point(2,1));
        myArrayList.add(new Point(2,2));
        System.out.println(findClosestPoints(myArrayList));
        
        //TEST 10 - 10 points on the same horizontal line line 
        myArrayList.clear();
        for (int i=0; i<10;i++)
        {
            myArrayList.add(new Point(r.nextInt(1000)-25,0));
        }
        System.out.println(findClosestPoints(myArrayList));
        
        //TEST 10 - 10 points on the same vertical line 
        myArrayList.clear();
        for (int i=0; i<10;i++)
        {
            myArrayList.add(new Point(0,r.nextInt(1000)-25));
        }
        System.out.println(findClosestPoints(myArrayList));
        
        //TEST 11 - 10 points on a line of constant gradient 
        
        myArrayList.clear();
        for (int i=0; i<10;i++)
        {
            myArrayList.add(new Point(i,i));
        }
        System.out.println(findClosestPoints(myArrayList));
        
        

        
       
        
       
        
         
       

    }

}
