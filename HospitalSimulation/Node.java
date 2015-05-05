/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *Simulates a quad node which contains 4 references to other nodes and one data reference 
 * @author Omair
 */
public class Node {
   
        private Event data;
        private Node before;
        private Node after;
        private Node up;
        private Node down; 

        /**
         * Default constructor for Node class with no parameters. Sets everything to null 
         */
        public Node()
        {
            data = null;
            before = null;
            after = null;
            up = null;
            down = null; 
        }


        /**
         * Constructor for Node class with parameter the initial data 
         * @param o 
         */
        public Node(Event o)
        {
            data = o;
            before = null;
            after = null;
            up = null;
            down = null; 
        }
        
        /**
         * Constructor for Node class which sets the data, and the 4 references 
         * @param o A reference to the  event the want to add 
         * @param n A reference to the previous node 
         * @param m A reference to the next node 
         * @param p A reference to the node above 
         * @param r a reference to the node below 
         */
        public Node(Event o, Node n, Node m, Node p, Node r)
        {
            data = o;
            before = n;
            after = m;
            up = p;
            down = r; 
        }

        /**
         * Sets the reference of the previous node 
         * @param n The node which replaces the reference of the previous node 
         */
        public void setBeforeNode(Node n)
        {
            before = n;
        }
        
        /**
         * Sets the reference of the after node 
         * @param n The node which replaces the reference of the after node 
         */
         public void setAfterNode(Node n)
        {
            after = n;
        }
         
         /**
         * Sets the reference of the above node 
         * @param n The node which replaces the reference of the above node 
         */
          public void setUpNode(Node n)
        {
            up = n;
        }
          
          /**
         * Sets the reference of the down node 
         * @param n The node which replaces the reference of the down node 
         */
           public void setDownNode(Node n)
        {
            down = n;
        }

         /**
          * Finds the reference of the node before the current node 
          * @return The reference of the node before the current node  
         */
        public Node getBeforeNode()
        {
            return before;
        }
        
        /**
          * Gets the reference of the node after the current node 
          * @return The reference of the node after the current node
         */
        public Node getAfterNode()
        {
            return after;
        }
        
        /**
          * Gets the reference of the node above the current node 
          * @return The reference of the node above the current node
         */
         public Node getUpNode()
        {
            return up;
        }
         
         /**
          * Gets the reference of the node below the current node 
         */
          public Node getDownNode()
        {
            return down;
        }
          
        /**
          * Sets the reference of the data of the current node 
         */
        public void setData(Event o)
        {
            data = o;
        }
        
         /**
          * Gets the reference of the data of the current node 
          * @return The reference of the data of the current node 
         */
        public Event getData()
        {
            return data;
        }
        
        /**
         * Compares two nodes and returns 1 if the data field in the note is greater in value than that in node n, -1 is its smaller and 0 if its equal 
         * @param n
         * @return 
         */
        public int compareData(Node n)
        {
            if (this.getData().compareTo(n.getData())>0)
            {
                return 1;
            }
            else if (this.getData().compareTo(n.getData())<0)
            {
                return -1; 
            }
            else
            {
                return 0;
            }
        }
}
