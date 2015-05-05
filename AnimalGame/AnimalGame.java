import java.io.*;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.lang.Math.*;

/**
 * Play the animal guessing game by loading an existing game in a file into the program and then saving the new tree to a file so any
 * changes made to the tree are saved for when the program is exited and we still want the new configuration of animals. The existing file contains the number of nodes of the 
 * binary tree, the data at the nodes in a pre order traversal, and the positions of the index in an in order traversal. This in order traversal of positions and pre order traversal 
 * of data is used to reconstruct the tree. Then any changes made to the tree are saved to a new file, by find the pre order and in order traversal of the tree. These two traversals
 * are used to find the positions of inorder traversal of the new binary tree. This information is written to a file so the file now contains the new number of nodes, and then each string
 * (i.e. data of node) and its inorder position. This new file can be used to play the game again without any loss of data from the original game.
 * 
 * @author  Omair ALam
 * @version 04-20-14
 */

public class AnimalGame extends JPanel {
    // Root of the tree containing the questions
    private static GameTreeNode gameTree = null;

    public static void main (String [] args) {
        String gameFileName = null;
        // Set up a viewframe that manages the various input boxes we use.
        ViewFrame vf = new ViewFrame("Guess the Animal.");

        // Gets the name of the file containing the initial binary tree 
        gameFileName = getInitialGameFile(vf);

        vf.setVisible(true);
        vf.println("Welcome to the animal guessing game.");
        vf.println("Please wait while I set up a new game.");

        // Load the tree of questions.
        gameTree = loadGameTree(gameFileName);

        // Play until the user declines.
        while(playGame(vf))
        ;
        storeToFile(gameTree, vf);   //calls the method which stores this modified game tree onto the file 

        vf.println("\nThanks for playing!");    
    }

    /**
     * Get initial game from file by opening a dialog box which allows us to choose the initial game configurations
     * @param f Parent frame for the dialog box 
     * @return The name of the selected file, or null if no file was selected 
     */

    public static String getInitialGameFile(JFrame f)
    {
        //opens dialogue box which allows the user to choose the file which contains the initial game tree configuration 
        String appDir = (String) (System.getProperty("user.dir"));
        JFileChooser d;
        d = new JFileChooser(appDir);
        d.setDialogTitle("Select Initial Tree)");
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
     * Use the initial configuration file to build the game tree
     * @param treeFileName The name of the file which contains the initial configuration of trees 
     * @return  A reference to the root node of the tree of questions.
     */

    public static GameTreeNode loadGameTree(String treeFileName) {

        int totalNumberOfItems=0; 
        String [] myQuestions = null;
        int [] inOrderPositions = null;

        GameTreeNode tree = null;

        // creates an instance of the scanner class by passing it the name of the file from which we want to read. 
        try{
            Scanner readTreeFile = new Scanner(new File(treeFileName));
            int arrayLength = Integer.parseInt(readTreeFile.nextLine()); //the first item in the file is the number of nodes in the tree
            myQuestions = new String[arrayLength];  // the array of questions will have the 'array length' number of elements
            inOrderPositions = new int[arrayLength]; // the array of inorderPositions will have the 'array length' number of elements 

            int i=0; 

            while (readTreeFile.hasNextLine())
            {
                myQuestions[i] = readTreeFile.nextLine();                   // the first item in the file after the array length is the question
                inOrderPositions[i] = Integer.parseInt(readTreeFile.nextLine()); //the question is followed by the inorder position of the question
                i++;
            }
            readTreeFile.close();
        }

        catch(FileNotFoundException e)
        {
            System.err.println("Scanner error opening file " + treeFileName);
        }
    //    System.out.println(" The questions in pre order are : " + Arrays.toString(myQuestions));
    //   System.out.println(" The positions of questions in post order are " + Arrays.toString(inOrderPositions));

        //tree = new GameTreeNode(myQuestions[1]);

        tree = buildGameTree( myQuestions, inOrderPositions, 0, inOrderPositions.length-1, 0, inOrderPositions.length-1); 

        return tree;

    }

    /**
     * Private helper method to create the tree recursively 
     * @param myQuestions The array which contains the questions in the game tree in preOrder
     * @param inOrderPositions The array which contains the positions of the questions in the game tree in inOrder
     * @param preFirst The starting index position of the elements in the array of questions
     * @param preLast The ending index position of the elements in the array of questions
     * @param inFirst The starting index position of the elements in the array of inorder indices
     * @param inLast The ending index position of the elements in the array of inorder indices
     * @return a reference of the root node of the tree of questions
     */

    private static GameTreeNode buildGameTree(String [] myQuestions, int [] inOrderPositions, int preFirst, int preLast, int inFirst, int inLast)
    {
        GameTreeNode tree = new GameTreeNode(myQuestions[preFirst]);

        // System.out.println("bT("+preFirst+","+preLast+")");

        int inRoot = inOrderPositions[preFirst];    
        int numberOfLeftNodes = inRoot - inFirst; 
        int numberOfRightNodes = inLast - inRoot; 

        if (preFirst >= preLast )
        {
            // preFirst = inOrderPositions[preFirst];
            //    System.out.println("BaseCase Hit");
            return new GameTreeNode(myQuestions[preFirst], null, null);   // if we reach a leaf(when preFirst == preLast) or if our starting index crosses our ending index, we create a tree with that question and no left and right children
        }

        else
        {
            //buld the left sub tree from one plus the starting posisition up to starting position plus the number of nodes. 
            //The inFIrst and inLast ensure that once we switch from left to right tree,
            //the  nodes in the left are not counted for the right a well
            tree.left = buildGameTree(myQuestions, inOrderPositions, preFirst+1, preFirst+numberOfLeftNodes, inFirst, inRoot -1);

             // build the right sub tree from one plus the end of the left subtree tll the end of the array of inorder indices
            tree.right =  buildGameTree( myQuestions, inOrderPositions,preFirst+numberOfLeftNodes+1,  preLast, inRoot+1, inLast);  
           

            return tree;
        }

    }

    /**
     * Play the guessing game, asking at the end if the player wants 
     * to continue.
     * 
     * @param vf    The ViewFrame which serves as the parent for the 
     *              input boxes.
     * @return      true if the player wants to go again, false otherwise.
     */
    public static boolean playGame(ViewFrame vf) {
        boolean result = false;

        // Starting at the root, follow the player's answers to a leaf.
        GameTreeNode current = gameTree;

        if (current == null)  {
            System.out.println("Game initialization failed.");
            System.exit(1);
        }

        String response = null;

        // All internal nodes have both left and right children, so we don't
        // have to check both.

        while (current.left != null) {
            // Tack "(Yes or no)" onto the question, to make the expected
            //  response clear.  I.e., "Sometimes" or "maybe" won't do.
            response = vf.readString(current.question + 
                " (Yes or no)");

            // The next question for a "yes" response is found by following
            // the left link, etc.
            if (response.equalsIgnoreCase("yes")) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        // We've reached a leaf, which should ask a question about a specific
        // animal.
        response = vf.readString(current.question + 
            " (Yes or no)");

        if (response.equalsIgnoreCase("yes")) {
            response = vf.readString(
                "I thought so! Want to play again? (Yes or no)");
            result = response.equalsIgnoreCase("yes");
        } else {
            // If we didn't guess correctly, learn about the player's animal.
            addNewAnimal(current, vf);
            response = vf.readString(
                "Thanks! Want to play again? (Yes or no)");
            result = response.equalsIgnoreCase("yes");

        }

        return(result);
    }

    /**
     * Finding the file in which we want to save our newly configured tree
     * @param f The parent frame for the dialog box 
     * @return the name of the selected file in which we want to save our newly configured tree, or null if no file was selected
     */
    private static String locationOfFile(JFrame f)
    {
        //opens dialogue box which allows the user to choose the file to which we want to save the new configuration of the tree
        String appDir = (String) (System.getProperty("user.dir"));  //goes to the user diectory(the file directory in which we are)
        JFileChooser d;
        d = new JFileChooser(appDir);
        d.setDialogTitle("Select Initial Tree)");
        int retVal = d.showSaveDialog(f);
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
     * Finds the data of the tree in preOrder traversal of the game tree and stores it in an ArrayList 
     * @param t A reference to the root of the tree of questions 
     * @param myPreOrderQuestionsList A reference to the arraylist that is storing the data of the tree in a preOrder traversal of the tree
     */
    private static void storePreorderStringsToArray(GameTreeNode t, ArrayList<String> myPreOrderQuestionsList)
    {

        // if we reach a leaf just add that leaf question to the arrayList
        if (t==null)  
        {
           // myPreOrderQuestionsList.add(t.question);
            return ;
        }

        else 
        {
            // this adds question to the array list in order of root, left right, 
            myPreOrderQuestionsList.add(t.question); //adds the root question
            storePreorderStringsToArray(t.left,myPreOrderQuestionsList);  //recursively calls the method on the left sub tree
            storePreorderStringsToArray(t.right, myPreOrderQuestionsList);  //recursively calls the method on the right sub tree

      //         System.out.println("My questions in preOrder are : "+Arrays.toString(myPreOrderQuestionsList.toArray()));
            return ;
        }

    }

    /**
     * Finds the data of the tree in inOrder traversal of the game tree and stores it in an ArrayList
     * @param t A reference to the root of the tree of questions
     * @param myInOrderQuestionsList A reference to the arraylist that is storing the data of the tree in an inOrder traversal of the tree
     */
    private static void   storeInOrderStringsToArray( GameTreeNode t, ArrayList<String> myInOrderQuestionList)
    {
        // if we reach a leaf just add that leaf queston to the array list 
        if (t==null)
        {
            //   System.out.println("String at position "+ arrayIndex[0] +" is " +t.question);
            //myInOrderQuestionList.add(t.question); 
            //System.out.println("My questions in inOrder are : "+Arrays.toString(myQuestions));
            return ;
        }

        else
        {
            // this adds questions to the array list in order of left rot right 
            storeInOrderStringsToArray(t.left, myInOrderQuestionList);  //recursively calls the method on the left sub tree
            
            myInOrderQuestionList.add(t.question);  //adds the root question to the array list
            
            //System.out.println("Recursive String at position "+ arrayIndex[0] +" is " +t.question);

            storeInOrderStringsToArray(t.right, myInOrderQuestionList);  // recursively calls the method on the right sub tree

           // System.out.println("My questions in inOrder are : "+Arrays.toString(myInOrderQuestionList.toArray()));
            
            return ;

        }

    }

    /**
     * Finds the inorder indices of the game tree by comparing the inorder and the preorder arrayLists of questions, and stores the indices to an array
     * @param t A reference to the root of the tree of questions
     * @param myQuestions The arrayList which contains the elements of the preOrder traversal of the game tree
     * @param myInOrderQuestions THe arrayList which contains the elements of the inOrder traversal of the game tree
     * @return An array which contains the inorder indices of the game tree
     */
    private static int [] storeIndicesToArray(GameTreeNode t, ArrayList<String>  myQuestions, ArrayList<String> myInOrderQuestions )
    {

        storeInOrderStringsToArray(t,myInOrderQuestions); //calls the method which adds the questions to myQuestions array list in pre order
        storePreorderStringsToArray(t,myQuestions); //calls the methods which adds the questions to myInOrderQuestions array list in in order

        Object myPreOrderArray[] = myQuestions.toArray();  //converts the pre order array list into an array
        Object myInOrderArray[] = myInOrderQuestions.toArray(); //converts the in order array list into an array
        int [] myInOrderIndices = new int [myPreOrderArray.length];   //creates an array of indices which has the same length as the previous two arrays

        //traverses the two arrays, comparing the first item in the pre order array with all of the items in the in order array. If there is a match, store the position of the element
        //in the in order list at the position of the element in the pre order list.
        for (int i=0; i<myPreOrderArray.length; i++)
        {
            for (int j=0; j< myInOrderArray.length; j++)
            {
                if (myPreOrderArray[i].equals( myInOrderArray[j]))
                {
                    myInOrderIndices[i] = j;
                }

            }
        }

        return myInOrderIndices;
    }

    /**
     * Stores the new game tree to a file 
     * @param t A reference to the root of the tree of questions
     * @param f The parent frame in which the dialog box for saving opens
     */
    private static void storeToFile(GameTreeNode t, JFrame f)
    {
        ArrayList<String> myQuestions = new ArrayList<String>();    // the pre order questions array list
        ArrayList<String> myInOrderQuestions = new ArrayList<String>();  // the inorder questions array list 
        //   int [] inOrderPositions = new int[getLengthOfFile()+1];

        //creates the inorder positions array and also populates the myQuestions and myInOrderQuestions array list because they are reference variables
        int [] inOrderPositions = storeIndicesToArray(t,myQuestions, myInOrderQuestions);  
        Object stringInOrder [] = myInOrderQuestions.toArray();
        Object stringPreOrder [] = myQuestions.toArray();
        
        System.out.println("My preOrder Array is " + Arrays.toString(stringPreOrder));
        System.out.println("My inOrder Array is " + Arrays.toString(stringInOrder));
        System.out.println("My inOrder indices are" + Arrays.toString(inOrderPositions));
        
        String fileName = locationOfFile(f); //calls the method which gets the location of the file where we want to save the new game configuration 

        try
        {
            //creates a print writer instance which will output the contens of the pre order array and the in order indices array into the file 
            PrintWriter p = new PrintWriter(fileName);
            p.println(stringPreOrder.length);  // the first item is the number of nodes in the tree, i.e. the number of elements in the pre order array

            //store the question and then its position and then repeat
            for (int i=0; i<inOrderPositions.length; i++)
            {
                p.println(stringPreOrder[i]);
                p.println(inOrderPositions[i]);

            }
            p.close();

        }
        catch (FileNotFoundException p)
        {
            System.err.println("The file you tried to create failed to create");
        }
    }

    /**
     * Learn about a new animal from the player.
     *
     * @param t     The leaf node containing our incorrect guess.
     * @param vf    The ViewFrame used to manage input boxes.
     */
    public static void addNewAnimal(GameTreeNode t, ViewFrame vf) {
        // Gather informatin about the player's animal.
        String newAnimal = vf.readString("What is your animal?");
        String newQuestion = vf.readString(
                "Type a y/n question to distinguish between my guess and your animal.");
        String newAnswer = vf.readString("What is the answer for your animal?");
        String oldAnimal = t.question;

        // Modify the current leaf node to hold the new question. The two 
        // answers will be the children.
        t.question = newQuestion;

        String newLeft = null;
        String newRight = null;
        if (newAnswer.equalsIgnoreCase("yes")) {
            newLeft = "Is your animal a(n) " + newAnimal + "?";
            newRight = oldAnimal;
        } else {
            newLeft = oldAnimal;
            newRight = "Is your animal a(n) " + newAnimal + "?";
        }

        t.left = new GameTreeNode(newLeft);
        t.right = new GameTreeNode(newRight);
        
    }

 
}

///////////////////////////////////////////////////////////
// Copyright 1999, 2000 L. Lewis Barnett, Joseph F. Kent //
// "Java Programming - A Laboratory Approach"            //
// Contact: lbarnett@richmond.edu, jkent@richmond.edu    //
///////////////////////////////////////////////////////////
