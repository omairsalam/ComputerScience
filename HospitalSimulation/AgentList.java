import java.util.ArrayList;

/**************************************************************************
 * Implements a data structure for holding Agent objects for the agent-based
 * simulation.
 * 
 * @author  L. Barnett, B. Lawson
 * @version 1.00
 **************************************************************************/
public class AgentList
{
    private ArrayList<Agent> agentList;

    /**************************************************************************
     * Constructor for the AgentList class.
     **************************************************************************/
    public AgentList()
    {
        agentList = new ArrayList<Agent>();
    }

    /**************************************************************************
     * Adds an agent to the agent list.
     * @param agent An instance of Agent to add to the list.
     **************************************************************************/
    public void add(Agent agent)
    {
        agentList.add(agent);
    }

    /**************************************************************************
     * Clears the list of all agents.
     **************************************************************************/
    public void clear()
    {
        agentList.clear();
    }

    /**************************************************************************
     * Fetches and returns the Agent instance at the specified index.
     * @param index Position (starting at 0) of agent in the list to fetch. 
     * @return Reference to the Agent instance at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of range.
     **************************************************************************/
    public Agent get(int index) throws IndexOutOfBoundsException
    {
        return(agentList.get(index));
    }

    /**************************************************************************
     * Removes the first occurrence of an Agent instance from the list, if that
     * instance exists.
     * @param Reference to the Agent instance to be removed.
     * @return true if the list contains the specified agent.
     **************************************************************************/
    public boolean remove(Agent agent)
    {
        boolean existed = agentList.remove(agent);
        return(existed);
    }

    /**************************************************************************
     * Fetches the size of the agent list.
     * @return number of agents in the agent list
     **************************************************************************/
    public int size() { return(agentList.size()); }

}
