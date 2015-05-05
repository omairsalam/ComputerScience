import java.util.*;
import java.awt.*;

public class Edge {
    private Point a;
    private Point b;

    public Edge (Point p1, Point p2){
	a = p1;
	b = p2;
    }

    public Point getOnePoint(){
	return a;
    }

    public Point getOtherPoint(){
	return b;
    }

    public String toString(){
	StringBuffer sb = new StringBuffer();
	sb.append("[(a.x = " + a.x + ", a.y = " + a.y + ")  "+
			   "(b.x = " + b.x + ", b.y = " + b.y + ")]");
	return sb.toString();
    }
}
