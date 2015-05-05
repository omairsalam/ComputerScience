/**
 * Interface for classes used as the event calendar for the agent-based
 * antibiotic resistance simulation.
 * 
 * @author lbarnett
 * @version 2/12/2015
 */
public interface EventListInterface {
    public int size();
    public Event getNextEvent();
    public void insertEvent(Event e);
    public void removeEvent(Event e);
    public boolean contains(Event e);
}
