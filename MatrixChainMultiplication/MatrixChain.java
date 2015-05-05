
import java.util.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Given a sequence of matrix dimensions (that are compatible for matrix multiplication in the order given),
 * computes the minimum number of multiplications required to compute the matrix chain product,
 * and also produces a sequence of indices corresponding to where the product 
 * “breaks” occur throughout the multiplication.
 * @author Omair Alam
 */
public class MatrixChain {
    
    private LinkedList<Integer> dims = null;
    private int[][] table;  
    private int[][] breaks;
    private int size = 0; 
    private LinkedList<Integer> multBreaks = null;  
    
    /**
     * Constructs a matrix chain using dimensions of the matrices involved
     * @param dimensions The dimensions of the matrices involved, with no repeating matrices 
     */
    public MatrixChain(LinkedList<Integer> dimensions)
    {
        if (dimensions == null)
        {
            System.exit(0); 
        }
        dims = new LinkedList<Integer>(dimensions); //our arrayList of values 
        table = new int[dimensions.size()+1][dimensions.size()+1]; //create a table of values 
        size = dims.size()-1; 
        breaks = new int[dimensions.size()+1][dimensions.size()+1];
        multBreaks = new LinkedList<Integer>(); 
    }
    
    /**
     * Computes the minimum number of multiplications required to compute the product of matrices
     * and also finds the "k values" indicating where we parenthesize the matrices 
     * @return a Linked List containing in order, the number of multiplications required, then the k values, ordered from the left half to the right half 
     */
    public LinkedList<Integer> computeMinMatrixChain()
    {
        LinkedList<Integer> results = new LinkedList<Integer>(); 
        if (dims.size() == 1)
        {
            return new LinkedList<Integer>(); //dimensions aren't enough to have even one matrix 
        }
        //set the indices representing only one matrix as 0 
        int i = 0;
        int j=0; 
        for (i =1; i<=size;i++) //put zero's in all boxes which represent the number of computations required for a single matrix, which amounts to 0 
        {
            table[i][i]=0; 
        }
         
        int gap = 0; // the gap is the difference between the diagonals 
        
        for (gap = 1; gap <= size -1; gap++) //move from one diagonal to another, since our algorithm works diagonally 
        {
            for (i=1 ; i<= size - gap; i++) //
            {
                j = i + gap; 
                table[i][j] = Integer.MAX_VALUE;
                int k = 0;
                for (k = i; k <= j - 1 ; k++) //look at all the possible number of multiplications that can be done, by combinations of the columns below and the rows to the right of the current cell that we are looking at 
                {
                    int noMult = table[i][k] + table[k+1][j] + dims.get(i-1)*dims.get(k)*dims.get(j); 
                    if (noMult < table[i][j]) // if these number of multiplications are less than the current one in the cell, replace them 
                    {
                        table[i][j] = noMult; 
                        breaks[i][j] = k; //k is the break point (i.e. the point at which we will insert a paranthesis later) so keep a count of this as well
                    }
                }
            }
        }
       
        //print contents of the table of number of multiplications 
        System.out.println("CONTENTS OF TABLE ----------------------------------------------------------------------------------------------------");
        for ( int m = 0; m< table.length; m++)
        {
            for ( int n = 0; n< table.length; n++)
            {
                System.out.print(table[m][n] + "       ");
            }
            System.out.println();
        }
       
      //print contents of the table of the break points (values of k that we found ) 
        System.out.println("CONTENTS OF BREAKS ----------------------------------------------------------------------------------------------------");
        for ( int m = 0; m< breaks.length; m++)
        {
            for ( int n = 0; n< breaks.length; n++)
            {
                System.out.print(breaks[m][n] + "        ");
            }
            System.out.println();
        }
       
        System.out.println("");
        
        //the optimal number of multiplcations required are as per this algorithm are in the top right most corner 
        int noComputations = table[1][size];
       // System.out.println(" FINAL RESULTS ARE  ");
        getBreakValues(1,dims.size()-1); 
       // System.out.println("LUN" + multBreaks);
        results.add(noComputations); 
        results.addAll(multBreaks); //resultingK is the linkedlist containing the points at which we will break, i.e. the values of k 
        return results; 
    }
    
    /**
     * Finds the break values, or the values of k corresponding to the parenthesizing done to obtain solution with least multiplications and adds them to a global linked list 
     * @param i The index of the first matrix in our predefined list
     * @param j The index of the last matrix in our predefined list 
     */
    private void getBreakValues(int i, int j)
    {
	if (i == j || (i+1 == j) ) // if the next matrix is the same as current matrix ( i ==j ) or the next matrix is just the one after the current matrix, no paranthesizing needs to be done 
        {
            return;
        }
	else
        {
            multBreaks.add(breaks[i][j]); //add this break point to our linked list containing the break points 
	    getBreakValues(i, breaks[i][j]); // find the break points for the left half 
            getBreakValues(breaks[i][j] + 1, j); // find the break points for the right half 
            
        }
    }
   
    public static void main(String[] args)
    {
       LinkedList<Integer> dimensions = new LinkedList<Integer>();
       dimensions.add(30);
       
       dimensions.add(35);
       
       dimensions.add(15);
       dimensions.add(5);
       
       dimensions.add(10);
     
       dimensions.add(20);
      //    dimensions.clear();
       dimensions.add(25); 
       
       dimensions.add(10); 
      //dimensions.clear(); 
       dimensions.add(20); 
       
               /*
       dimensions.add(10);
       dimensions.add(100);
       dimensions.add(5);
       dimensions.add(50);
       
       
       dimensions.add(1);*/

       
       MatrixChain myMatrix = new MatrixChain(dimensions);
       System.out.println(myMatrix.computeMinMatrixChain()); 
    }
    
}
