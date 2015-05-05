
/**
 * This Class contains methods that deal with Objects stored in a list. It uses nodes to keep track, modify and erase objects from the list, and since every node has a both next and previous field
 * ,this implementation is of a doubly-linked linkedList 
 * It keeps track of the currentPosition of an object in a list that may contain many objects.
 * It further modifies the current position when some operations take place which change the current object. These operations include the internal 
 * iterators such as getNext, getLast, getFirst, getLast. Furthermore, methods such as insert, append, remove, replace, allow us to change the Objects
 * in a particular instance of a list. While append always inserts an object at the end of the list, and thus is not affected by the current position or the current Object, insert,
 * remove and replace all depend on the current Object. Lastly, we have a clear method as well which can completely empty the list.
 * 
 * @author Omair Alam
 * @version 03/01/14
 */
public class ObjectList implements ObjectListInterface 
{

    private Node headNode =null; //The reference variable that points to the first object in the list.
    private int numberOfEntries=0; // The total number of objects in the list (numberOfEntries is 0 if there is no object in the list, else it is always greater than 0)
    private int currentPosition=0; //he position of the current object in the list (currentPosition is 0 if there is no object, else position always greater than 0 and smaller or equal to n)

    /**
     * The default constructor for the ObjectList class. It sets the headNode to null
     * and all the integer data fields to 0. 
     */
    public ObjectList()
    {
        headNode = null;
        numberOfEntries = 0;
        currentPosition = 0;
    }

    /**
     * Return the object at the current position, or null if the list is empty, since in that case there would be no objects.
     * @return The object at the current position, or null if there is no current object
     */
    public Object  getCurrent()
    {
        int traversingPosition=1; 
        Node tmp = headNode; 

        if (isEmpty())  //if the list is empty then there can be no object at a current position 
        {
            currentPosition = 0; 
            return null;
        }

        while(tmp!=null )  //to traverse through the list 
        {
            if (traversingPosition == currentPosition) //if by traversing we finally reach the Node at which the object that we want is stored
            {
                return tmp.getData();
            }

            traversingPosition++;
            tmp = tmp.getNextNode();

        }

        return null; 
    }

    /**
     * Moves to the first position in the list and returns the object at this position(if any)
     * @return The object at the beginning of the list (or null if the list is empty) 
     */
    public Object  getFirst()
    {
        currentPosition = 1;

        if (isEmpty())  // if the list is empty, there is no first object in the list
        {
            currentPosition = 0; 
            return null;
        }

        return headNode.getData(); // the first object is the one at headNode always since this is what headNode is supposed to store; 
    }

    /**
     * Moves to the next position in the list and returns the object associated with that position. If we are at the 
     *   end of the list, the method will return the last object in the list. If the list is empty. 
     *   the method will return null since there is no object at any position. 
     *@return The next object in the list (or null if the list is empty) 

     */
    public Object  getNext()
    {
        int traversingPosition=1;
        Node tmp = headNode; 
        Node storeTmp = null;
        if (isEmpty())  //if the list is empty, there is no next object
        {
            currentPosition = 0; 
            return null; 
        }

        if (numberOfEntries == 1)  //when the list only has one item there is no next item so we return the first object again
        {
            return headNode.getData();
        }

        while (tmp!=null )   // to traverse through the list 
        {
            if (traversingPosition == (currentPosition+1) && (currentPosition != numberOfEntries))  //only move to the next item, if there is a position one greater than the current position AND if we are not at the last item because then there is no next item
            {
                currentPosition++; 
                return tmp.getData();
            }

            traversingPosition++;
            storeTmp = tmp;  //we need to store the value of the object at tmp that we have reached by traversing the list because elsewise tmp.getNextNode() will make tmp null and we will lose that object
            tmp = tmp.getNextNode();
        }

        return storeTmp.getData();
    }

    /**
     * Move to the previous position in the list and return the object associated with that position. If we are 
    currently at the beginning of the list, the method will return the object at the beginning of the list. If the list is empty, the method will return null, since there is no object
    at any position
     *   @return The previous object in the list (or null if the list is empty) 
     */

    public Object getPrevious()
    {
        Node tmp = headNode; 
        int traversingPosition = 1; 

        if (isEmpty())  //if the list is empty, then there is no previous object
        {
            return null; 
        }

        if (currentPosition == 1 || numberOfEntries==1)  //if we are at the firstItem and our list also only contains one item, then there is no previous object so we return the first object again
        {
            return headNode.getData();
        }

        while (tmp!=null )   //to traverse through the list 
        {
            if (traversingPosition == (currentPosition -1) ) //when we reach the node one previous to the current Node
            {
                currentPosition--; 
                return tmp.getData();
            }

            traversingPosition++;
            tmp = tmp.getNextNode();
        }
        currentPosition--; 
        return null; 
    }

    /**
     * Move to the last position of the list and returns the object associated with that position (if any) or null if the list is empty. 
     * @return The object at the last position of the list, or null if the list is empty. 
     */
    public Object  getLast()
    {

        if (isEmpty())  //if our list is empty, then there is no last object
        {
            currentPosition =0; 
            return null;
        }

        int traversingPosition=1; 
        Node tmp = headNode; 

        while(tmp!=null)  //this loop traverses through the whole list 
        {
            currentPosition = traversingPosition; 
            if (traversingPosition == numberOfEntries) //when we reach the last object, meaning when our pointer of the object that we are at, is equal to the number of Objects in the list
            {
                return tmp.getData();
            }
            traversingPosition++;
            tmp = tmp.getNextNode();

        }
        return null; 

    }

    /**
     * Places the provided Object at the end of the list. Makes newObject the currentObject, thus also makes currentPosition the last position in the list.
     *@param newObject The object we want to add to the end of the list
     *@return True because the placement of the object at the end of the list is always successfull.
     */

    public boolean append(Object newObject)
    {
        Node tmp = headNode; 
        Node newNode = new Node(newObject);

        if (isEmpty()) //if the list is empty then we can simply add at the headNode
        {
            headNode = newNode; 
            currentPosition = 1; 
            numberOfEntries++;
            return true; 

        }

        while (tmp != null && tmp.getNextNode()!= null)  //this loop runs till we reach the last Node, because then tmp.getNextNode() becomes null; so after this while loop, we are at the last Node in our linked list
        {
            tmp = tmp.getNextNode();
        }

        tmp.setNextNode(newNode); 
        newNode.setPreviousNode(tmp); 
        numberOfEntries++;
        currentPosition = numberOfEntries; 
        return true; 

    }

    /**
     * Places the provided Object into the list before the current object (meaning at a position before currentPosition). 
     * Set new object to be current object. 
     *@param newObject The object that we want to place in the position of the current object. 
     *@return True because the object can be successfully inserted at the specified position always 
     */

    public boolean insert(Object newObject)
    {
        int traversingPosition = 1; 
        Node newNode = new Node(newObject); //creates a new node of the object that we want to insert;
        Node tmp = headNode; 
        if (isEmpty())  //if our list is empty then we can insert at headNode, meaning at the first position in the linked List
        {
            currentPosition = 1;
            headNode = newNode;
            numberOfEntries=1;
            return true; 
        }

        if (currentPosition ==1) //if our list only contains one Object, then we only need to deal with the previousNode of tmp, as opposed to its nextNode as well 
        {
            newNode.setNextNode(tmp);
            tmp.setPreviousNode(newNode);
            headNode = newNode;
            numberOfEntries++; 
            return true; 
        }

        else
        {

            while(tmp!=null && tmp.getNextNode()!=null)  //to traverse through the linked list, stops when tmp is the last Node since then tmp.getNextNode() becomes null
            {

                if (traversingPosition == currentPosition -1)  // if the Node that we are at is one before the Node that we want to insert at, we have to modify both the previousNOde and the nextNode of tmp
                {
                    newNode.setNextNode(tmp.getNextNode());  
                    newNode.setPreviousNode(tmp);
                    tmp.setNextNode(newNode); 
                    numberOfEntries++; 
                    currentPosition = traversingPosition + 1; 
                    return true; 
                }

                traversingPosition++;
                tmp = tmp.getNextNode(); 
            }
        }
        return false; 
    }

    /**
     * Removes and returns the object at the current position of the list(if any exist). Therefore if the list is empty, null is returned. 
     * By default the previous object becomes the current object except for in the case when the object being removed is the first object. In this 
     * case the next object becomes the current object. 
     * @return The current object that was removed
     */

    public Object  remove()
    {
        int traversingPosition = 1;  //the pointer I use to traverse through the linked list, stores the position of the current node that I am at. 
        Node tmp = headNode; 
        Object removedData; 

        if (numberOfEntries==0)  //if the List is empty, then removing will result is no change;
        {
            currentPosition = 0;
            return null;
        }

        if (currentPosition == 1)  //if the List only has one object then remove the data and set the headNode to null since the list is empty now
        {
            removedData = tmp.getData(); 
            headNode = tmp.getNextNode(); 
            numberOfEntries--; 
            return removedData;
        }

        while (tmp!=null) //to traverse through the linked list, stops when tmp is the last Node since then tmp becomes null 
        {
            if (traversingPosition == currentPosition)  //if my pointer is at the currentPosition, I am at the Node that I want to remove 
            {
                if (currentPosition == numberOfEntries)  //if this is the last Node, then we only have to modify the previousNode 
                {
                    removedData= tmp.getData();
                    tmp.getPreviousNode().setNextNode(null); 
                    currentPosition = numberOfEntries -1 ; 
                    numberOfEntries--; 
                    return removedData; 

                }
                else    //if this is anywhere between the first and the last Node, we have to modify both the previousNode and the nextNode
                {
                    removedData = tmp.getData();     
                    tmp.getPreviousNode().setNextNode(tmp.getNextNode());    
                    tmp.getNextNode().setPreviousNode(tmp.getPreviousNode());
                    currentPosition = traversingPosition -1 ; 
                    numberOfEntries--; 
                    return removedData; 
                }

            }
            traversingPosition++;
            tmp = tmp.getNextNode();
        }
        return null;
    }

    /**
     * Replaces the Object in the current position with the provided newObject. If the list is empty, there is no object to replace so the method returns false
     * @param newObject The object that we want to replace the object at current position with
     *return True or False depending upon whether the replacement was successful or not respectfully, meaning whether there was an object to replace or the list was empty. 
     */ 

    public boolean replace(Object newObject)
    {
        int traversingPosition = 1; 
        Node tmp = headNode; 

        if (isEmpty())  //if the list is empty then we cannot replace anything hence we return false;
        {
            currentPosition = 0;
            return false;
        }

        if (currentPosition == 1)  //if the list only contains one object, then we set the object to the newObject and don't make any other changes
        {
            tmp.setData(newObject);  
            return true; 
        }

        while (tmp!=null )  //for traversing the list 
        {
            if (traversingPosition == currentPosition) //when we reach the positition of the object that we want to replace
            {
                tmp.setData(newObject);
                return true;
            }

            traversingPosition++;
            tmp = tmp.getNextNode(); 
        }

        return false; 

    }

    /**
     * Removes all Objects from the list by removing the reference of the headNode. Returns true regardless of whether the list is initally empty or not.
     * @return True since the headNode can be made null at any time
     */

    public boolean clear()   
    {
        headNode = null;
        numberOfEntries = 0;
        currentPosition = 0 ;
        return true; 
    }

    /**
     * Returns the number of objects in our list.
     * @return The number of objects in our list. 
     */
    public int getLength()
    {
        return numberOfEntries; 
    }

    /**

     * Returns the index (between 0 and n) of the pointer (0 if the list is empty) which is at current Position, i.e returns the position of the object that we are at in the list
     *@return The current position that we are at.
     */

    public int getCurrentPosition()
    {
        return currentPosition;
    }

    /**
     * Prints the objects in our  list, starting from the first to the last object.
     */

    public void display()
    {
        Node tmp = headNode;
        while( tmp!=null) //to traverse the List
        {
            System.out.println("Objects are: " + tmp.getData()); //to print out the objects in the correct style 
            tmp = tmp.getNextNode();
        }

    }

    /**
     * Returns true or false depending on whether the list is empty or not (meaning whether it has any objects or not)
     * @return true or false depending on whether the list is empty or not.
     */

    private boolean isEmpty()
    {
        if (headNode == null)
        {
            return true;
        }
        return false; 
    }

    /**
     * This class has methods that make a Node. The nodes created have a data field, a next field as well as a previous field. 
     */
    private class Node
    {

        private Object data;  //The data that the node has a reference of 

        private Node next; // The address of the node ahead of our current Node (the Node that our curent Node is pointing to

        private Node previous; //The address of the node behind our current Node(the node that is pointing to our current Node)

        /**
         * Creates an empty node. The Node therefore does not store reference to any data so the data field is null, it does not 
         * point to anything so its next field is null, and nothing points to it either, so its previous 
         * field is also null
         */
        private Node()
        {
            data = null;
            next = null;
            previous = null;
        }

        /**
         * Creaes a new node, which does not point to anything, therefore has a null next field as well as previous field.
         * It does however contain reference to some data, so the data field is not null
         * @param newObject The object for which we want to create a Node 
         */
        private Node(Object newObject)
        {
            data = newObject; 
            next = null;
            previous = null;
        }

        /**
         * Creates a new Node which contains a new object newObject, which points to another node, and also has a node pointing to it. 
         * If this is the last Node and so does not point to anything after it, the next Node will be null
         * @param newObject The object for which we want to create a Node
         * @param newNext The node which we want this node to point to. 
         * @param newPrevious The node which we want to point at this node (the Node behind this node)
         */

        private Node( Object newObject, Node newNext, Node newPrevious)
        {
            data = newObject;
            next = newNext;
            previous = newPrevious; 
        }

        /**
         * Creates a new node with contains a new Object newObject and which points to another Node, caled newNext.
         * @param newObject The object for which we want to create a Node
         * @param newNext THe node which we want this node to point to
         */
        private Node(Object newObject, Node newNext)
        {
            data = newObject; 
            next = newNext; 
            previous = null;
        }

        /**
         * Creates a new node which contains a new Object and which has someother node pointing to it. called newPrevious.
         * @param newPrevious The node which will point to this Node
         */
        private Node(Node newPrevious, Object newObject)
        {
            data = newObject; 
            previous = newPrevious; 
            next = null;
        }

        /**
         * Sets the node at which this Node points to.
         * @param newNext The address we want the next of the this Node to point to
         */
        public void setNextNode(Node newNext)
        {
            next = newNext;
        }

        /**
         * Sets what Node this Node points to(which Node is after this Node)
         * @param newPrevious The address we want the previous of this node to point to
         */
        public void setPreviousNode(Node newPrevious)
        {
            previous = newPrevious;
        }

        /**
         * Returns the address stored in the next field of the Node
         * @return The address stored in the next field of the Node; 
         */
        public Node getNextNode()
        {
            return next; 
        }

        /**
         * Returns the address stored in the previous field of the Node
         * @return THe addres stored in the previous field of the Node
         */
        public Node getPreviousNode()
        {
            return previous;
        }

        /**
         * Sets the data that the Node is storing 
         * @param The object that we want to store in the Node
         */
        private void setData(Object newObject)
        {
            data = newObject;
        }

        /**
         * Returns the data being stored in the data field of the Node.
         * @return THe data stored in the Node.
         */
        private Object getData()
        {
            return data; 
        }
    }
}
