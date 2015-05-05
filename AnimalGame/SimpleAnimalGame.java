import java.io.*;

/**
 * Play the animal guessing game.
 * @author	Lewis Barnett (change this to your name)
 * @version	1.0, 6/20/2000
 */
 
public class SimpleAnimalGame  {
	// Root of the tree containing the questions
	private static GameTreeNode	gameTree = null;
	
	public static void main (String [] args) {
		// Set up a viewframe that manages the various input boxes we use.
		ViewFrame vf = new ViewFrame("Guess the Animal.");
		vf.setVisible(true);
		vf.println("Welcome to the animal guessing game.");
		vf.println("Please wait while I set up a new game.");
		
		// Load the tree of questions.
		gameTree = loadGameTree();
		
		// Play until the user declines.
		while(playGame(vf))
			;
		
		vf.println("\nThanks for playing!");	
	}
	
	/**
	 * Load the initial tree of questions to ask from a file.
	 * 
	 * @return	A reference to the root node of the tree of questions.
	 */
	public static GameTreeNode loadGameTree() {
		GameTreeNode tree = null;
		GameTreeNode leftChild1 = 
			new GameTreeNode("Is your animal a catfish?")        ;
		GameTreeNode rightChild1 = 
			new GameTreeNode("Is your animal a frog?");
		
		GameTreeNode leftQuestion = 
			new GameTreeNode("Does your animal have whiskers?",
								leftChild1, rightChild1);
		
		GameTreeNode leftChild2 = 
			new GameTreeNode("Is your animal a komodo dragon?");
		GameTreeNode rightChild2 =
			new GameTreeNode("Is your animal a gnu?");
			
		GameTreeNode rightQuestion = 
			new GameTreeNode("Does your animal have scaly skin?",
								leftChild2, rightChild2);

		tree = new GameTreeNode("Does your animal live in the water?",
								leftQuestion, rightQuestion);
		return(tree);
	}
	
	
	/**
	 * Play the guessing game, asking at the end if the player wants 
	 * to continue.
	 * 
	 * @param vf	The ViewFrame which serves as the parent for the 
	 *				input boxes.
	 * @return		true if the player wants to go again, false otherwise.
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
	 * Learn about a new animal from the player.
	 *
	 * @param t		The leaf node containing our incorrect guess.
	 * @param vf	The ViewFrame used to manage input boxes.
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
