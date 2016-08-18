#Animal Game

<p> The animal guessing game is like 20 Questions. You are essentially asked a varied number of questions untill either the game guesses what you were thinking correctly, or admits that it was wrong and does the following: 
  <ol> 
    <li> Asks what thing you were thinking of </li> 
    <li> Asks one questions that you would use to identify it </li> 
    <li> Asks what the answer to that identifying question would be </li>
  </ol>
  
It adds this information to a new tree and saves it to file when you exit, so the new game starts with the new updated tree. </p> 

<h2> How to run the game </h2> 

Play the animal guessing game by loading an existing game in a file into the program and then saving the new tree to a file so any changes made to the tree are saved for when the program is exited and we still want the new configuration of animal . The existing file contains the number of nodes of the binary tree, the data at the nodes in a pre order traversal, and the positions of the index in an in order traversal. This in order traversal of positions and pre order traversal of data is used to reconstruct the tree. Then any changes made to the tree are saved to a new file, by find the pre order and in order traversal of the tree. These two traversals are used to find the positions of inorder traversal of the new binary tree. This information is written to a file so the file now contains the new number of nodes, and then each string (i.e. data of node) and its inorder position. This new file can be used to play the game again without any loss of data from the original game.

<h2> 
