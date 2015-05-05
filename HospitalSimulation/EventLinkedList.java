	import java.util.*; 

public class EventLinkedList implements EventListInterface {

	private ArrayList<Event> listOfEvents;  


        /**
         * Default constructor for ArrayList  
         */
	public EventLinkedList()
	{
		listOfEvents = new ArrayList<Event>();

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
		Event minTimeEvent = listOfEvents.get(0);
		listOfEvents.remove(0);
		return minTimeEvent; 
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
                    int addingIndex = 0;
                    while ( addingIndex < listOfEvents.size() && newEvent.compareTo(listOfEvents.get(addingIndex))> 0)
                    {
                        addingIndex++;
                    }
                    if (addingIndex == listOfEvents.size())
                    {
                        listOfEvents.add(newEvent);
                    }
                    else
                    {
                        listOfEvents.add(addingIndex, newEvent); 
                    }
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