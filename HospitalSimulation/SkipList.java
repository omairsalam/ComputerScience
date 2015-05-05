/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Implements the methods in a SkipList such as add, remove, find, contains and size 
 * @author Omair Alam
 */
import java.util.*;
public class SkipList {
    
    private SkipListLevel topLevel;
    private Random r =  new Random(123);
    
    /**
     * Default constructor for the SkipList 
     */
    public SkipList()
    {
        topLevel = new SkipListLevel();
    }
    
    /**
     * Adds an event to the SkipList in order 
     * @param newEntry The entry we want to add to the skip list 
     */
    public void add(Event newEntry)
    {
        Node newNode = new Node(newEntry);
        Node insertAt = find(newEntry); //finds the position in the lowermost list where we need to add the new entry
       
        if (insertAt.getData().equals(newEntry)== true) //if the insertion element is the same as new element, don't insert
        {
            return; 
        }
         //book-keeping for new insertion at lowermost skip list level where our new node should be ahead of the insertAt node 
        
            newNode.setBeforeNode(insertAt); 
            newNode.setAfterNode(insertAt.getAfterNode());
            insertAt.getAfterNode().setBeforeNode(newNode); 
            insertAt.setAfterNode(newNode);           
        
        

        int currentToss = r.nextInt(2); 
        
        Node copyA = newNode;
        while (currentToss != 0) // if we get a heads
        {
            //set the above and below nodes for the new node that we got 
            Node aboveNewNode = new Node(copyA.getData());
            copyA.setUpNode(aboveNewNode);
            aboveNewNode.setDownNode(copyA); 
            
            Node copyNewNodeAfter = copyA.getAfterNode(); //create a copy of the node after the one we are at 
            Node copyNewNodeBefore = copyA.getBeforeNode(); 
            //search for a link on the right of the new node that can help us go up
            while (copyNewNodeAfter.getUpNode() == null && copyNewNodeAfter.getData().getTime()!= Double.MAX_VALUE )
            {
                //set the ahead of the new node that we added
                copyNewNodeAfter = copyNewNodeAfter.getAfterNode(); 
            }
            // if it can go up, go up 
            /*
             if (copyNewNodeAfter.getUpNode() != null)
             {
                 copyNewNodeAfter = copyNewNodeAfter.getUpNode();
             }
            */
            
           /*
            Node rightTopNode = copyNewNodeAfter.getUpNode();
            aboveNewNode.setAfterNode(rightTopNode);
            rightTopNode.setBeforeNode(aboveNewNode);*/
            
            
             //search for a link on the left of the new node that can help us go up 
             while (copyNewNodeBefore.getUpNode() == null && copyNewNodeBefore.getData().getTime()!= Double.MIN_VALUE)
            {
                //set the previous of the new node that we added 
                copyNewNodeBefore = copyNewNodeBefore.getBeforeNode(); 
            }
             // if it can go up, then go up 
             /*
             if (copyNewNodeBefore.getUpNode() != null)
             {
                 copyNewNodeBefore = copyNewNodeBefore.getUpNode();
             }
             */
            /*Node leftTopNode = copyNewNodeBefore.getUpNode();
            aboveNewNode.setBeforeNode(leftTopNode);
            leftTopNode.setAfterNode(aboveNewNode);*/
            
            if (copyNewNodeBefore.getUpNode() == null && copyNewNodeAfter.getUpNode()==null)
            {
                SkipListLevel newLevel = new SkipListLevel();
                //create a new level and set its upper and lower levels appropriately 
                newLevel.setLowerLevel(topLevel);
                topLevel.setUpperLevel(newLevel);
                //make top level this new level 
                topLevel = newLevel; 
                
                //set the upper nodes of the level that we were at to the pos and neg sentinal since we are just below them
                topLevel.getNegSentinal().setAfterNode(aboveNewNode);
                topLevel.getPosSentinal().setBeforeNode(aboveNewNode);
                
                
                //set the left and the right nodes of the above new node which we actually inserted in new level appropriately
                aboveNewNode.setBeforeNode(topLevel.getNegSentinal());
                aboveNewNode.setAfterNode(topLevel.getPosSentinal());
                
                //set the left and the right nodes of the sentinals of our new level to the above new node 
                copyNewNodeBefore.setUpNode(topLevel.getNegSentinal());
                copyNewNodeAfter.setUpNode(topLevel.getPosSentinal()); 
                topLevel.getNegSentinal().setDownNode(copyNewNodeBefore);
                topLevel.getPosSentinal().setDownNode(copyNewNodeAfter);
                
            }
            else
            {
                //set the right and the left nodes of the new nodes 
                 copyNewNodeBefore = copyNewNodeBefore.getUpNode(); 
                 copyNewNodeAfter = copyNewNodeAfter.getUpNode();
                 
                 aboveNewNode.setBeforeNode(copyNewNodeBefore);
                 aboveNewNode.setAfterNode(copyNewNodeAfter);
                 copyNewNodeBefore.setAfterNode(aboveNewNode);
                 copyNewNodeAfter.setBeforeNode(aboveNewNode); 
                 
            }
           
            copyA = aboveNewNode; 
            currentToss = r.nextInt(2);
        }
        
        // if our topmost level is not empty (i.e. has more items than the sentinals)
        if (topLevel.getNegSentinal().getAfterNode() != topLevel.getPosSentinal())
        {
            SkipListLevel newLevel = new SkipListLevel();//create a new skip list level 
            
            //link the top two levels
            topLevel.setUpperLevel(newLevel);
            newLevel.setLowerLevel(topLevel);
            
            //set the up and the down nodes of the sentinals 
            newLevel.getNegSentinal().setDownNode(topLevel.getNegSentinal());
            newLevel.getPosSentinal().setDownNode(topLevel.getPosSentinal());
            topLevel.getNegSentinal().setUpNode(newLevel.getNegSentinal());
            topLevel.getPosSentinal().setUpNode(newLevel.getPosSentinal()); 
            
            topLevel = newLevel;
        }
        
        
    }
    
    
    /**
     * Finds the number of elements in the list 
     * @return The number of elements in the list 
     */
    public int size()
    {
        SkipListLevel copyTop = topLevel; 
        
        //reach the bottom-most level 
        while (copyTop.getNegSentinal().getDownNode() != null)
        {
            copyTop = copyTop.getLowerLevel(); 
        }
        int size = 0;
        Node currentNode = copyTop.getNegSentinal();
        
        //iterate through the bottom most level till we reach the positive sentinal 
        while (currentNode.getAfterNode() != copyTop.getPosSentinal())
        {
            currentNode = currentNode.getAfterNode(); 
            size++;
            
        }
        return size; 
    }
    
    /**
     * Gets the next item in the skip list and then removes it
     * @return The next item in the skip list 
     */
    public Event getNext()
    {
        SkipListLevel copyTop = topLevel;
        
        //get to the bottom most level 
        while (copyTop.getNegSentinal().getDownNode() != null)
        {
            copyTop = copyTop.getLowerLevel(); 
        }
        
        //get the next event meaning the event at the first position in the list if such an event exists 
        if (copyTop.getNegSentinal().getAfterNode() != copyTop.getPosSentinal())
        {    
            Event nextEvent = copyTop.getNegSentinal().getAfterNode().getData();
            remove(nextEvent); // after finding this event, remove it from the list 
            return nextEvent;  // return this event 
        }
        else // the skip list level was empty 
        {
            return null; 
        }
                
    }
    /**
     * Removes a specific element from the event list if the element exists 
     * @param eventToRemove The event we want to remove from the skip list 
     */
    public void remove(Event eventToRemove)
    {
      //  System.out.println("Remove method called"); 
        //find the element that we have to remove 
        Node currentNode = find(eventToRemove);
        
        // our find method returns the element which is either that or the one just before it 
        if (currentNode.getData().equals(eventToRemove) == false)
        {
            return; 
        }
        
        //link the elements before and after the element we are trying to remove to each other so this element goes away 
        currentNode.getBeforeNode().setAfterNode(currentNode.getAfterNode());
        currentNode.getAfterNode().setBeforeNode(currentNode.getBeforeNode());
        
        //while the element that we are trying to remove has a copy above it as well 
        while (currentNode.getUpNode() != null)
        {
            currentNode = currentNode.getUpNode(); 
            //do the same book keeping we did before to link the before and the next of that element 
            currentNode.getBeforeNode().setAfterNode(currentNode.getAfterNode());
            currentNode.getAfterNode().setBeforeNode(currentNode.getBeforeNode());
        }
        
        // since topmost level only has sentinals, if the level under it only has sentinals as well, set the topmost level to that level 
        while (topLevel.getLowerLevel() != null && (topLevel.getLowerLevel().getNegSentinal().getAfterNode() == topLevel.getLowerLevel().getPosSentinal()))
        {
            topLevel = topLevel.getLowerLevel(); 
            //do book keeping to set the upper level markers for that level as null 
            topLevel.setUpperLevel(null);
            //set the upper nodes for the sentinals as null as well 
            topLevel.getNegSentinal().setUpNode(null);
            topLevel.getPosSentinal().setUpNode(null);
        }
    }
    
    /**
     * Checks whether an entry is in the list or not 
     * @param newEntry The entry which we want to check exists in the list or not 
     * @return true if the value was contained in the list and false if the value isn't in the list 
     */
    public boolean contains(Event newEntry)
    {
        Node foundNode = find(newEntry);
        // if the entry gets equal, return true, else return false
        if (foundNode.getData().equals(newEntry))
        {
            return true; 
        }
        return false; 
    }
    
    /**
     * Returns the element that is either smaller than the element we are finding or exactly that element
     * @param Entry The entry we want to find 
     * @return The Node containing the element that is either before or exactly the Node containing the data we are trying to find 
     */
    private Node find(Event Entry)
    {
       Node currentNode = topLevel.find(topLevel.getNegSentinal(),Entry); //finds node either containing that entry or the one before it 
       SkipListLevel currentLevel = topLevel;
       
       while (currentNode.getDownNode() != null) // if we can move down in search, move down 
       {
            currentLevel = currentLevel.getLowerLevel();
            currentNode = currentLevel.find(currentNode.getDownNode(),Entry); 
       }
       
       return currentNode; 
       
    }
    
    /**
     * Prints the contents of each skipList level 
     * @param myTopLevel The topLevel of the Skiplist who's contents we want to print 
     */
    public static void print(SkipListLevel myTopLevel)
    {
         while (myTopLevel.getLowerLevel() != null)
        {
            myTopLevel = myTopLevel.getLowerLevel(); 
        }
        
         int level = 0; 
        while (myTopLevel.getUpperLevel() != null)
        {
            Node currentNode = myTopLevel.getNegSentinal();
             System.out.println("Level " + level);
            while (currentNode.getAfterNode() != myTopLevel.getPosSentinal())
            {
                Double b = currentNode.getAfterNode().getData().getTime();
                System.out.println(b);
                currentNode = currentNode.getAfterNode(); 
            }
            myTopLevel = myTopLevel.getUpperLevel(); 
            level++;
        }
        
    }
    
    public static void main (String[] args)
    {
         EventSkipList myEventSkipList = new EventSkipList();
         
         Random r = new Random(); 
         for (int i=0; i<10;i++)
         {
             Event a = new Event(r.nextDouble(),null,null);
             myEventSkipList.insertEvent(a);
         }
     //    myEventSkipList.getNextEvent(); 
         
         
           print(myEventSkipList.listOfEvents.topLevel);
    }
    
    
}
