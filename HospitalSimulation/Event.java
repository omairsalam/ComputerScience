import java.util.ArrayList;

/**************************************************************************
 * Implements a class for an event type object for the agent-based 
 * simulation.
 * @author   L. Barnett, B. Lawson
 * @version  1.00
 **************************************************************************/
public class Event implements Comparable<Event>
{
    public static enum EventType { 
        DEATH, MOVE, INTERACT, ANTIBIOTIC_APP, ANTIBIOTIC_CLEAR
    }

    private double    time;
    private EventType type;
    private Agent     agent;

    /**************************************************************************
     * Constructor for an Event object.
     * @param  time  The time of the corresponding event.
     * @param  type  Event type -- one of MOVE, INTERACT, DEATH, ANTIBIOTIC_APP, 
     *               or ANTIBIOTIC_CLEAR.
     * @param  agent A reference to the agent instance associated with the
     *               event.
     **************************************************************************/
    public Event(double time, EventType type, Agent agent)
    {
        this.time  = time;
        this.type  = type;
        this.agent = agent;
    }

    /**************************************************************************
     * Returns the time of this event.
     * @return The time this event is to occur.
     **************************************************************************/
    public double getTime()  
    { 
        return(time);  
    }

    /**************************************************************************
     * Returns the type of this event.
     * @return The type of this event: one of MOVE, INTERACT, DEATH, 
     *         ANTIBIOTIC_APP, or ANTIBIOTIC_CLEAR.
     **************************************************************************/
    public EventType getType()  
    { 
        return(type);  
    }

    /**************************************************************************
     * Returns the Agent instance associated with this event.
     * @return Reference to Agent instance associated with this event.
     **************************************************************************/
    public Agent getAgent() 
    { 
        return(agent); 
    }

    /**************************************************************************
     * Determines whether two Event instances are the same
     * @param other Another Event instance.
     * @return true, if the two instances have the same time, type, and same
     *         Agent instance reference.
     **************************************************************************/
    public boolean equals(Event other)
    {
        if (time == other.time && type == other.type 
            && agent.equals(other.agent))
        {
            return(true);
        }
        return(false);
    }

    /**************************************************************************
     * Compares two events.
     * @param  other Reference to another event for comparison.
     * @return -1 if this event should occur earlier in the list; 1 if after
     **************************************************************************/
    public int compareTo(Event other)
    {
        if (time < other.time)
          return(-1);
        else if (time == other.time)
        {
            // see enum of event types above
            if (type == other.type)
                return(0);
            else if (type == EventType.DEATH && 
                     other.type != EventType.DEATH)
                return(-1);
            else if (type == EventType.MOVE && 
                     (other.type != EventType.DEATH && 
                      other.type != EventType.MOVE))
                return(-1);
            else if (type == EventType.INTERACT && 
                     (other.type == EventType.ANTIBIOTIC_APP ||
                      other.type == EventType.ANTIBIOTIC_CLEAR))
                return(-1);
            else if (type == EventType.ANTIBIOTIC_APP && 
                     other.type == EventType.ANTIBIOTIC_CLEAR)
                return(-1);
            else
                return(1);
        }
        else if (time > other.time)
          return(1);

        return(0); // equal
    }
}
