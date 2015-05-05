/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *Simulates each level of the skip list by creating a negative sentinel, positive sentinel, and references to the upper and lower levels
 * @author Omair Alam
 */
public class SkipListLevel {
    
    private Node negSentinal;
    private Node posSentinal; 
    private SkipListLevel upperLevel;
    private SkipListLevel lowerLevel; 
    
    /**
     * Default constructor for SkipList Level. Sets the sentinels (negative and positive infinity ) as well as the references to the level above and the level below 
     */
    public SkipListLevel()
    {
        Event negInfinity = new Event(Double.MIN_VALUE, null,null);
        Event posInfinity = new Event(Double.MAX_VALUE, null,null);
        negSentinal = new Node(negInfinity);
        posSentinal = new Node(posInfinity);
        negSentinal.setAfterNode(posSentinal);
        posSentinal.setBeforeNode(negSentinal);
        upperLevel = null;
        lowerLevel = null;
    }
    
    /**
     * Gets the level above the current level 
     * @return The level above the current level 
     */
    public SkipListLevel getUpperLevel()
    {
        return upperLevel;
    }
    
    /**
     * Gets the level below the current level
     * @return The skip list level below the current level 
     */
    
    public SkipListLevel getLowerLevel()
    {
        return lowerLevel; 
    }
    
    /**
     * Sets the upper level of a skip list level to a new skip list level 
     * @param n The SkipList level which is set as the upper level 
     */
    public void setUpperLevel(SkipListLevel n)
    {
        upperLevel = n;
    }
    
     
    /**
     * Sets the lower level of a skip list level to a new skip list level 
     * @param n The SkipList level which is set as the lower level 
     */
    public void setLowerLevel(SkipListLevel n)
    {
        lowerLevel = n;
    }
    
    /**
     * Finds the negative sentinel (- infinity ) of the skiplist level 
     * @return The negative sentinel of the Skip List level 
     */
    public Node getNegSentinal()
    {
        return negSentinal; 
    }
    
     /**
     * Finds the positive sentinel (- infinity ) of the skiplist level 
     * @return The positive sentinel of the Skip List level 
     */
     public Node getPosSentinal()
    {
        return posSentinal; 
    }
    
     /**
      * Finds an entry in the skipList level by traversing from the starting node till either that item is found or the one before it is returned 
      * @param startNode The node at which we start our search for the item in the skip list level 
      * @param Entry The data that we are trying to find 
      * @return The node of either the Entry that we searched for or the one preceeding that 
      */
    public Node find(Node startNode, Event Entry)
    {
        Node currentNode = startNode;
        
        while (currentNode.getAfterNode().getData().compareTo(Entry) <= 0)
        {
            currentNode = currentNode.getAfterNode(); 
        }
        return currentNode; 
    }
    
}
