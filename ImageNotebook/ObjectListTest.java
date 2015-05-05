
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class ObjectListTest for ObjectList. This tests all the public methods of the ObjectList starting off with the methods that help with iteration, and then 
 * followed by methods which use iterators to insert, append, remove and replace objects in the list. For this test class, I am storing integers in my ObjectList. 
 * In order to check the integrrity in terms of order of the list, after each insert, append, remove or replace method is called, the objects in the list are compared with the 
 * objects in an array, comparing the first object in the list with the first in the array and so on. Here the array represents what we think the list should look like, so if both the array
 * and the list are a perfect match (as determined by the helper method) the method returns true and we can be sure that the order is that which we want.
 *
 * @author  Omair Alam
 * @version 03/07/14
 */
public class ObjectListTest
{
    /*
     * Takes as parameters a list and an array. Compares the content of the array with the list, one by one meaning comparing the first object in the array with the first object in the 
     * list, then the second object in the array with the second object in the list to check whether all objects are the same and in the same order. Then it compares each element in the array
     * and the list again, but this time starting from the end of the array and the list, carrying out the same comparison. If any one object does not match up, the method returns false. 
     * @param myList The list whose consistency and order of data I want to be sure of. 
     * @param myArray The sample array which is consistent and in order thus is used as a benchmark comparison with the list to see whether the later is in order. 
     * @returns True or False depending on whether the list is consistent and in order ( meaning whether it is completely identical to the array or not) 
     */
    public boolean checkListOrder(ObjectList myList, int[] myArray)
    {
        ObjectList newList = myList;

        if (myArray.length == 0 && newList.getLength() ==0)
        {
            return true; 
        }

        if (myArray.length != newList.getLength()) //to check whether they have the same length or not
        {
            return false;
        }

        if (myArray[myArray.length -1] != newList.getLast()) //to check whether the last number is same or not
        {
            return false; 
        }

        if (myArray[0] != newList.getFirst()) //to check whether the first number in the Object list is equal to the first number in the array 
        {
            return false; 
        }

        if (myArray.length == newList.getLength())  //to traverse our list from the first object to the last object
        {
            for (int i=0; i<myArray.length; i++)
            {
                if (myArray[i] != newList.getCurrent())
                {
                    return false; 
                }
                myList.getNext(); 
            }

            for (int i=myArray.length-1; i>=0; i--)  //to back track and traverse the list from the last object to the first object 
            {
                if(myArray[i] != newList.getCurrent())
                {
                    return false;
                }
                newList.getPrevious();
            }
        }
        return true; 
    }

    @Test public void testGetCurrentPosition()
    {
        ObjectList s1 = new ObjectList(); 

        assertEquals(s1.getCurrentPosition(),0);  //to check what the position is when the list is empty.

        assertEquals(checkListOrder(s1,new int[] {}), true); 

        assertEquals(s1.insert(-10),true);         // to check what is the current position when the list has only one item
        assertEquals(checkListOrder(s1, new int[] {-10} ),true); 
        assertEquals(s1.getCurrentPosition(),1);

        assertEquals(s1.insert(2),true); 
        assertEquals(s1.getCurrentPosition(), 1);   // to check what is the current position when the list has another object added in the start 
        assertEquals(checkListOrder(s1, new int[] {2,-10}),true); 

        assertEquals(s1.getNext(), -10); 
        assertEquals(s1.getCurrentPosition(), 2);   // to check what the current position is when we call the getNext() method thus move to the next object in the list
        assertEquals(s1.insert(8),true); 
        assertEquals(s1.getLast(), -10); 
        assertEquals(s1.getCurrentPosition(), 3);    // to check what the current position is when we call the getLast() method thus move to the last object in the list
        assertEquals(checkListOrder(s1, new int[] {2,8,-10}),true); 

    }

    @Test public void testGetCurrent()
    {
        ObjectList s1 = new ObjectList();
        assertEquals(s1.getCurrent(), null); // checking when the object list is empty

        s1.append(5); //checks when the object list only has one integer
        assertEquals(s1.getCurrent(), 5); 
        s1.append(10);   //checks when the object list has two integers, whether getCurrent returns the most recent  integer added/appended
        assertEquals(s1.getCurrent(), 10); 

    }

    @Test public void testGetFirst()
    {
        ObjectList s1 = new ObjectList(); //testing whether the last number entered is the first one to be outputted
        s1.insert(-10);
        s1.insert(2);
        s1.insert(8);
        s1.insert(11);
        s1.insert(14);
        s1.insert(21);
        assertEquals(s1.getFirst(),21);

        ObjectList s2 = new ObjectList(); //testing when the list is empty
        assertEquals(s2.getFirst(),null);

        ObjectList s3 = new ObjectList(); //testing when there is only one integer in the list
        s1.insert(-10);
        assertEquals(s1.getFirst(),-10);

        ObjectList s4 = new ObjectList(); //testing to check whether ascending or descending numbers effect getFirst()
        s4.insert(8);
        s4.insert(2);
        assertEquals(s4.getFirst(),2); 

    }

    @Test public void testGetNext()
    {
        ObjectList s1 = new ObjectList();
        ObjectList s2 = new ObjectList();
        ObjectList s3 = new ObjectList();
        ObjectList s4 = new ObjectList();

        assertEquals(s1.getNext(), null); //checks when the list is empty 
        s1.insert(-10); 

        assertEquals(s1.getNext(), -10); //since there is no next item, this method should return the first item 
        s1.insert(2);

        s1.insert(8);
        s1.insert(11);
        s1.insert(14);
        s1.insert(21);

        assertEquals(s1.getNext(),14); //checks whether the getNext is successfully itterating through the ObjectList it or not
        assertEquals(s1.getNext(),11);
        assertEquals(s1.getNext(),8);
        assertEquals(s1.getNext(),2);
        assertEquals(s1.getNext(),-10);

    }

    @Test public void testGetPrevious()
    {
        ObjectList s1 = new ObjectList();
        ObjectList s2 = new ObjectList();
        ObjectList s3 = new ObjectList();
        ObjectList s4 = new ObjectList();

        assertEquals(s1.getPrevious(), null); // checks whether the getPrevious returns null when the list is empty

        s1.insert(-10); 
        assertEquals(s1.getPrevious(), -10); // checks whether getPrevious returns the first object when the list only has one object 

        s1.insert(2);
        assertEquals(s1.getPrevious(), 2);
        s1.insert(8);
        s1.insert(11);
        s1.insert(14);
        s1.insert(21);

        assertEquals(s1.getPrevious(),21); //should return the first object because there is nothing behind the first integer in the objectList
        assertEquals(s1.getNext(),14); //moves one forward in the object list
        assertEquals(s1.getPrevious(),21); //moves one back in the object list

    }

    @Test public void testGetLast()
    {
        ObjectList s1 = new ObjectList(); //testing whether the last number entered is the last one to be outputted
        s1.insert(-10);
        s1.insert(2);
        s1.insert(8);
        s1.insert(11);
        s1.insert(14);
        s1.insert(21);
        assertEquals(s1.getLast(),-10);

        ObjectList s2 = new ObjectList(); //testing when the list is empty
        assertEquals(s2.getLast(),null);

        ObjectList s3 = new ObjectList(); //testing when there is only one integer in the list
        s1.insert(-10);
        assertEquals(s1.getLast(),-10);

        ObjectList s4 = new ObjectList(); //testing to check whether ascending or descending numbers effect getLast()
        s4.insert(8);
        s4.insert(2);
        assertEquals(s4.getLast(),8); 
    }

    @Test public void testInsert()
    {
        ObjectList s1 = new ObjectList();

        assertEquals(checkListOrder(s1,new int[] {}), true); //checks whether an empty Object list has the same contents as an empty array 

        assertEquals(s1.insert(-10),true);  //checks the order of items when there is only one integer in the object list
        assertEquals(checkListOrder(s1, new int[] {-10} ),true); 

        assertEquals(s1.insert(2),true); //checks the order of items when there are two integers in the object list
        assertEquals(checkListOrder(s1, new int[] {2,-10}),true); 

        assertEquals(s1.getNext(), -10); //changes currentPosition to -10

        assertEquals(s1.insert(8),true);  //check whether it can insert in the middle of the list or not
        assertEquals(checkListOrder(s1, new int[] {2,8,-10}),true); 
        assertEquals(s1.getNext(),8);

        assertEquals(s1.insert(11),true);
        assertEquals(checkListOrder(s1, new int[] {2,11,8,-10} ),true); 
        assertEquals(s1.getNext(),11);

        assertEquals(s1.insert(14), true);
        assertEquals(checkListOrder(s1, new int[] {2,14,11,8,-10} ),true); 
        assertEquals(s1.getNext(),14);

        assertEquals(s1.insert(21), true); 
        assertEquals(checkListOrder(s1, new int[] {2,21,14,11,8,-10} ),true); 

    }

    @Test public void testAppend()
    {
        ObjectList s1 = new ObjectList();

        assertEquals(s1.append(66), true); 
        assertEquals(checkListOrder(s1, new int[] {66}),true); //checks whether we can append to an empty list

        assertEquals(s1.append(-5), true);
        assertEquals(checkListOrder(s1, new int[] {66,-5}),true); //checks whether appending inserts an object as the last position in the list 
    }

    @Test public void testRemove()
    {
        ObjectList s1 = new ObjectList();

        assertEquals(s1.remove(), null); // when the list is empty, there should be no object to remove so the value returned should be null
        assertEquals(checkListOrder(s1, new int[] {}),true);

        assertEquals(s1.insert(25),true);
        assertEquals(checkListOrder(s1, new int[] {25}),true);
        assertEquals(s1.insert(14),true);
        assertEquals(checkListOrder(s1, new int[] {14,25}),true);
        assertEquals(s1.append(45),true);
        assertEquals(checkListOrder(s1, new int[] {14,25,45}),true);
        assertEquals(s1.getLast(),45);
        assertEquals(s1.insert(18),true);
        assertEquals(checkListOrder(s1, new int[] {14,25,18,45}),true);
        assertEquals(s1.getNext(),25);
        assertEquals(s1.getNext(),18);

        assertEquals(s1.remove(),18); //since the currentObject is the first object, the remove method should remove the first object in the list and make the nextObject the currentObject 
        assertEquals(checkListOrder(s1, new int[] {14,25,45}),true); 
        assertEquals(s1.remove(),14);
        assertEquals(checkListOrder(s1, new int[] {25,45}),true);
        assertEquals(s1.getCurrent(),25); 

        assertEquals(s1.getNext(), 45);
        assertEquals(s1.remove(),45); //since the current Position is now at 45, the remove method should remove 45
        assertEquals(checkListOrder(s1, new int[] {25}),true);

        assertEquals(s1.insert(19),true);
        assertEquals(checkListOrder(s1, new int[] {19, 25}),true);
        assertEquals(s1.getNext(),25);
        assertEquals(s1.insert(-9),true);
        assertEquals(checkListOrder(s1, new int[] {19,-9,25 }),true);

        assertEquals(s1.getNext(),-9); 
        assertEquals(s1.remove(),-9);  //checks whether we can successfully remove from the middle of the list 
        assertEquals(checkListOrder(s1, new int[] {19,25}),true);

    }

    @Test public void testReplace()
    {
        ObjectList s1 = new ObjectList();

        assertEquals(s1.replace(5), false); //checking whether replace works when the list is empty
        assertEquals(checkListOrder(s1, new int[] {}),true);

        assertEquals(s1.insert(5),true);
        assertEquals(checkListOrder(s1, new int[] {5}),true);

        assertEquals(s1.replace(9),true);
        assertEquals(checkListOrder(s1, new int[] {9}),true); //replaces item at the start of the list 

        assertEquals(s1.append(10),true);
        assertEquals(checkListOrder(s1, new int[] {9,10}),true);

        assertEquals(s1.getNext(),10);
        assertEquals(s1.insert(0),true);
        assertEquals(checkListOrder(s1, new int[] {9,0,10}),true); 
        assertEquals(s1.getNext(),0); 

        assertEquals(s1.replace(-1),true);
        assertEquals(checkListOrder(s1, new int[] {9,-1,10}),true); //replaces object in the middle of the list 
        assertEquals(s1.getNext(),-1); 

        assertEquals(s1.getNext(),10);
        assertEquals(s1.replace(18),true);
        assertEquals(checkListOrder(s1, new int[] {9,-1,18}),true); //replaces object at the end of the list

    }

    @Test public void testClear() 
    {
        ObjectList s1 = new ObjectList();

        assertEquals(s1.clear(),true); // to check whether the an empty list can be cleared 

        assertEquals(s1.insert(-10),true); 
        assertEquals(checkListOrder(s1, new int[] {-10} ),true); 

        assertEquals(s1.insert(2),true); 
        assertEquals(checkListOrder(s1, new int[] {2,-10}),true); 

        assertEquals(s1.getNext(), -10); 

        assertEquals(s1.insert(8),true);  
        assertEquals(checkListOrder(s1, new int[] {2,8,-10}),true); 
        assertEquals(s1.getNext(),8);

        assertEquals(s1.clear(),true);
        assertEquals(checkListOrder(s1, new int[] {}),true); 
    }

    @Test public void testGetLength() 
    {
        ObjectList s1 = new ObjectList(); 
        assertEquals(s1.getLength(), 0); // to check whether the length is 0 when there are no objects in the list 
        assertEquals(checkListOrder(s1,new int[] {}), true); 

        assertEquals(s1.insert(-10),true);  //to check whether the length is 1 after one object is in the list 
        assertEquals(checkListOrder(s1, new int[] {-10} ),true); 

        assertEquals(s1.insert(2),true); 
        assertEquals(checkListOrder(s1, new int[] {2,-10}),true); 

        assertEquals(s1.getNext(), -10); 

        assertEquals(s1.insert(8),true);   // to check whether the length is increases proportionally as objects are added to the list 
        assertEquals(checkListOrder(s1, new int[] {2,8,-10}),true); 
        assertEquals(s1.getNext(),8);

        assertEquals(s1.insert(11),true);
        assertEquals(checkListOrder(s1, new int[] {2,11,8,-10} ),true); 
        assertEquals(s1.getNext(),11);
    }

    /*
    @Test public void testIsEmpty()
    {
    ObjectList s1 = new ObjectList(); 

    assertEquals(s1.isEmpty(),true); // checks if the list is empty when it has no object 
    assertEquals(checkListOrder(s1, new int[] {} ),true); 

    assertEquals(s1.insert(2),true); 
    assertEquals(checkListOrder(s1, new int[] {2,-10}),true); 

    assertEquals(s1.isEmpty(), false); //checks whether list is considered empty when it has one object
    }
     */

}
