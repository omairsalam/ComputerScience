/**
 * A node in the special-purpose tree used to play the Animal Guessing
 * Game. The fields are public so that the main class (which is the 
 * only client for this class) can efficiently adjust the tree as the
 * game is played. If both links are null, then the GameItem contains
 * an animal that the program will guess. If the links are non-null,
 * this is a question node, and the left link should be followed if
 * the player answers "yes", while the right link should be followed if
 * they answer "no".
 *
 * @author	Lewis Barnett
 * @version	1.0, 6/20/2000
 */

public class GameTreeNode {
	/**
	 * The question to ask.
	 */
	public String		question;
	
	/**
	 * Link to the left child of this node.
	 */
	public GameTreeNode	left;
	
	/**
	 * Link to the right child of this node.
	 */
	public GameTreeNode	right;
	
	/**
	 * Create a node with no children.
	 * 
	 * @param theItem	The item to store at this position in the tree.
	 */
	public GameTreeNode (String theQuestion) {
		question = theQuestion;
		left = null;
		right = null;
	}
	
	/**
	 * Create a node with the specified children.
	 * 
	 * @param theItem	The item to store at this position in the tree.
	 * @param leftChild	Reference to the left child node.
	 * @param rightChild	Reference to the right child node.
	 */
	public GameTreeNode (String theQuestion, GameTreeNode leftChild,
						GameTreeNode rightChild) {
		question = theQuestion;
		left = leftChild;
		right = rightChild;
	}

}


///////////////////////////////////////////////////////////
// Copyright 1999, 2000 L. Lewis Barnett, Joseph F. Kent //
// "Java Programming - A Laboratory Approach"            //
// Contact: lbarnett@richmond.edu, jkent@richmond.edu    //
///////////////////////////////////////////////////////////
