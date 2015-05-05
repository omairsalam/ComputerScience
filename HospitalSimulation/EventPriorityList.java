	import java.util.*; 

public class EventPriorityList implements EventListInterface {

	private PriorityQueue<Event> listOfEvents;  

         /**
         * Default constructor for Priority List 
         */
	public EventPriorityList()
	{
		listOfEvents = new PriorityQueue<Event>();

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
		return listOfEvents.poll(); 
	}

	/**
         * Inserts an event into the Skip List in the right order i.e by the time 
         * @param newEvent The event we want to insert in the skip list 
         */
	public void insertEvent(Event newEvent)
	{
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