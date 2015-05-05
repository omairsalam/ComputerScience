import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Math;  

public class SudokuSolver {
    private int [][] clue;
    private int [][] solution;
    /** Symbol used to indicate a blank grid position */
    public static final int BLANK = 0;
    /** Overall size of the grid */
    public static final int DIMENSION = 9;
    /** Size of a sub region */
    public static final int REGION_DIM = 3;

    // For debugging purposes -- see solve() skeleton.
    private Scanner kbd;
    private static final boolean DEBUG = false;

    /**
     * Run the solver. If args.length >= 1, use args[0] as the name of
     * a file containing a puzzle, otherwise, allow the user to browse
     * for a file.
     */
    public static void main(String [] args){
        String filename = null;
        if (args.length < 1) {
            // file dialog
            //filename = args[0];
            JFileChooser fileChooser = new JFileChooser();
            try {
                File f = new File(new File(".").getCanonicalPath());
                fileChooser.setCurrentDirectory(f);
            } catch (Exception ex) { System.out.println(ex.getMessage()); }

            int retValue = fileChooser.showOpenDialog(new JFrame());

            if (retValue == JFileChooser.APPROVE_OPTION) {
                File theFile = fileChooser.getSelectedFile();
                filename = theFile.getAbsolutePath();
            } else {
                System.out.println("No file selected: exiting.");
                System.exit(0);
            }
        } else {
            filename = args[0];
        }

        SudokuSolver s = new SudokuSolver(filename);
        if (DEBUG)
            s.print();

        if (s.solve(0, 0)){
            // Pop up a window with the clue and the solution.
            s.display();
        } else {
            System.out.println("No solution is possible.");
        }

    }

    /**
     * Create a solver given the name of a file containing a puzzle. We
     * expect the file to contain nine lines each containing nine digits
     * separated by whitespace. A digit from {1...9} represents a given
     * value in the clue, and the digit 0 indicates a position that is
     * blank in the initial puzzle.
     */
    public SudokuSolver(String puzzleName){
        clue = new int[DIMENSION][DIMENSION];
        solution = new int[DIMENSION][DIMENSION];
        // Set up keyboard input if we need it for debugging.
        if (DEBUG)
            kbd = new Scanner(System.in);

        File pf = new File(puzzleName);
        Scanner s = null;
        try {
            s = new Scanner(pf);
        } catch (FileNotFoundException f){
            System.out.println("Couldn't open file.");
            System.exit(1);
        }

        for (int i = 0; i < DIMENSION; i++){
            for (int j = 0; j < DIMENSION; j++){
                clue[i][j] = s.nextInt();
            }
        }

        // Copy to solution
        for (int i = 0; i < DIMENSION; i++){
            for (int j = 0; j < DIMENSION; j++){
                solution[i][j] = clue[i][j];
            }
        }
    }

    /**
     * Checks if the value we want to insert causes an issue i.e.
     * its duplicate is either present in the same row, col or region(mini-Array).
     * @param row The row of the position 
     * @param col The column of the position 
     * @param newValue the value we want to check.
     * @return true if there is no conflict (or false if there is a conflict)
     */

    public boolean checkNumberInput(int row, int col, int newValue)
    {

        for (int i=0; i<DIMENSION; i++)  //checks whether the same number exists in the same row 
        {
            if (solution[row][i] == newValue)
            {
                return false;
            }

        }

        for (int j=0; j<DIMENSION; j++) //checks whether the same number exists in the same column 
        {
            if (solution[j][col] == newValue)
            {
                return false;
            }
        }

        //finds the left top right coordinates of every sub array
        int newCol= (col/REGION_DIM)*REGION_DIM ;
        int newRow = (row/REGION_DIM)*REGION_DIM ;

        //initiating i and j to the origin coordiantes of the array

        //traversing through the subArray to see whether the new value has a duplicate or not
        for (int k=0; k<REGION_DIM; k++)
        {
            for (int u=0; u<REGION_DIM; u++)
            {
                if (solution[newRow+k][newCol+u] == newValue)  //checks through the sub array whether there is any number in conflict with its 8 neighbours 
                {
                    return false;
                } 
            }
        }

        return true; 
    }

    /**
     * Starting at a given grid position, generate values for all remaining
     * grid positions that do not violate the game constraints.
     *
     * @param row The row of the position to begin with
     * @param col The column of the position to begin with.
     *
     * @return true if a solution was found starting from this position,
     *          false if not.
     */

    public boolean solve(int row, int col){
        // This code will print the solution array and then wait for 
        // you to type "Enter" before proceeding. Helpful for debugging.
        // Set the DEBUG constant to true at the top of the class
        // declaration to turn this on.

        if (DEBUG) {
            System.out.println("solve(" + row + ", " + col + ")");
            print();
            kbd.nextLine();
        }

        //checks if there are any more rows to check, if not, then returns true to end recursive call

        if (row>DIMENSION-1)
        {
            return true;
        }

        //checks if the cell we are looking at contains some value or not

        if ((solution[row][col])==BLANK)   //if cell contains some value
        {
            for (int newValue=1; newValue<=DIMENSION; newValue++)   //iterates through all the possible numbers that can be put in that cell
            { 
                if (checkNumberInput(row,col,newValue))  // meaning if that number can be put in the cell, i.e. there are no conflicts with already present numbers in the cell
                {

                    solution[row][col] = newValue; //insert the new value into the solution array

                    if (col >= DIMENSION -1)  // if we have reached the last column, then move to the next row and start at the first column of that row 
                    {
                        if (solve(row+1,0))   // if we can find a  number for the new column then return true 
                            return true;

                    }
                    else
                    {       // if we have not reached the last column, we move one column ahead in the same row 
                        if(solve(row, col+1)) // if we can find a number for for the next column (meaning if solve(row, col+1) is true, then return true
                            return true;

                    }

                }
            }
        }

        else
        {
            if (col >= DIMENSION -1)  // if we are out of columns to check, we move to the next row
            {
                return solve(row+1, 0);
            }

            else // we move to the next column in the same row
            {
                return solve(row, col+1);
            }
        }

        backMove(row,col); // if conflict occurs i.e. we have a number that we can't fit, we undo the previous move because that must have been wrong, then we iterate through the cell again, starting from the previous position at which we stopped, and insert the next larger integer, and see whether that works or not
        return false;
    }

    /**
     * Resets the values that are causing a conflict (i.e no value is being placed in this cell successfully without any same values in the same row, col, or region).
     * @param row The row of the position 
     * @param col The column of the position 
     */

    public void backMove(int row, int col)
    {
        solution[row][col] = BLANK;  //sets the cell as empty (i.e containing a value of 0) if we cant seem to fit another value in the next cell that we have 
    }

    /**
     * Print a character-based representation of the solution array
     * on standard output.
     */
    public void print()
    {
        System.out.println("+---------+---------+---------+");
        for (int i = 0; i < DIMENSION; i++){
            System.out.println("|         |         |         |");
            System.out.print("|");
            for (int j = 0; j < DIMENSION; j++){
                System.out.print(" " + solution[i][j] + " ");
                if (j % REGION_DIM == (REGION_DIM - 1)){
                    System.out.print("|");
                }
            }
            System.out.println();
            if (i % REGION_DIM == (REGION_DIM - 1)){
                System.out.println("|         |         |         |");
                System.out.println("+---------+---------+---------+");
            }
        }
    }

    /**
     * Pop up a window containing a nice representation of the original
     * puzzle and out solution.
     */
    public void display(){
        JFrame f = new DisplayFrame();
        f.pack();
        f.setVisible(true);
    }

    /**
     * GUI display for the clue and solution arrays.
     */
    private class DisplayFrame extends JFrame implements ActionListener {
        private JPanel mainPanel;

        private DisplayFrame(){
            mainPanel = new JPanel();
            mainPanel.add(buildBoardPanel(clue, "Clue"));
            mainPanel.add(buildBoardPanel(solution, "Solution"));
            add(mainPanel, BorderLayout.CENTER);

            JButton b = new JButton("Quit");
            b.addActionListener(this);
            add(b, BorderLayout.SOUTH);
        }

        private JPanel buildBoardPanel(int [][] contents, String label){
            JPanel holder = new JPanel();
            JLabel l = new JLabel(label);
            BorderLayout b = new BorderLayout();
            holder.setLayout(b);
            holder.add(l, BorderLayout.NORTH);
            JPanel board = new JPanel();
            GridLayout g = new GridLayout(DIMENSION,DIMENSION);
            g.setHgap(0);
            g.setVgap(0);
            board.setLayout(g);
            Color [] colorChoices = new Color[2];
            colorChoices[0] = Color.WHITE;
            colorChoices[1] = Color.lightGray;
            int colorIdx = 0;
            int rowStartColorIdx = 0;

            for (int i = 0; i < DIMENSION; i++){
                if (i > 0 && i % REGION_DIM == 0)
                    rowStartColorIdx = (rowStartColorIdx+1)%2;
                colorIdx = rowStartColorIdx;
                for (int j = 0; j < DIMENSION; j++){
                    if (j > 0 && j % REGION_DIM == 0)
                        colorIdx = (colorIdx+1)%2;
                    JTextField t = new JTextField(""+ contents[i][j]);
                    if (contents[i][j] == 0)
                        t.setText("");
                    t.setPreferredSize(new Dimension(35,35));
                    t.setEditable(false);
                    t.setHorizontalAlignment(JTextField.CENTER);
                    t.setBackground(colorChoices[colorIdx]);
                    board.add(t);
                }
            }
            holder.add(board, BorderLayout.CENTER);
            return holder;
        }

        public void actionPerformed(ActionEvent e){
            System.exit(0);
        }
    }
}
