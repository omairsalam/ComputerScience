/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *Implements storing Events of the simulation in a Skip List 
 * @author Omair Alam
 */
public class EventSkipList implements EventListInterface{
    
    public SkipList listOfEvents;  

        /**
         * Default constructor for SkipList 
         */
	public EventSkipList()
	{
		listOfEvents = new SkipList();
	}

	/**
         * Finds the number of elements in the SkipList 
         * @return The number of elements in the skip list 
         */
	public int size()
	{
		return listOfEvents.size(); 
	}


	/**
         * Find the event with the smallest time and returns it and removes it from the list
         * @return The event with the smallest time 
         */
	public Event getNextEvent()
	{
		return listOfEvents.getNext(); 
	}

	/**
         * Inserts an event into the Skip List in the right order i.e by the time 
         * @param newEvent The event we want to insert in the skip list 
         */
	public void insertEvent(Event newEvent)
	{
           //  System.out.println(size());
            if (newEvent == null)
            {
                
            }
            else
            {
                listOfEvents.add(newEvent);   
            }
            
           
	}

	/**
         * Removes an event from the SkipList
         * @param eventToRemove The event we want to remove 
         */
	public void removeEvent(Event eventToRemove)
	{
		listOfEvents.remove(eventToRemove);
	}
	
	/**
         * Tells us whether a certain element is in the list or not 
         * @param newEvent The event we want to search for in the list 
         * @return True if the element is in the list, false otherwise 
         */
	public boolean contains(Event newEvent)
	{	
		return listOfEvents.contains(newEvent);
	}
    
}
