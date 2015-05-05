import java.util.ArrayList;       // for, duh, Arraylist
import java.util.Random;          // for, (see comment above), Random
import javax.swing.JProgressBar;  // for, (again?), JProgressBar

//======================================================================
//* This class implements the guts of the agent-based simulation.
//*
//* In the run() method provided in this class are the details for
//* the next-event simulation that drives the agent-based model.
//======================================================================
public class SimulationManager 
{
    protected static Random rng = new Random(Parameters.getRNGInitialSeed());

    // the following are to determine the state of the simulation 
    private static final int RUNNING      = 0;
    private static final int PAUSED       = 1;
    private static final int HALTED       = 2;
    private int    state;

    private Agent[][] occupied; // a 2D array to determine where agents are

    private AgentList agentList; // a list of all agents in the simulation
    private EventListInterface eventList; // the event calendar

    private int numInfectedAgents;    // infected and uninfected agents are kept 
    private int numUninfectedAgents;  // in the same list (above), so just keep counts
    private int numTreatedAgents;

    private double time;              // the simulation clock
    private double maxSimulatedTime;  // max that time can be

    private boolean outputOn;

    //======================================================================
    //* public SimulationManager()
    //* The constructor for the class.  Only initializes the agentList to
    //* an empty list and sets time to be 0.
    //======================================================================
    public SimulationManager(boolean outputOn)
    {
        agentList = new AgentList();
        eventList = new EventPriorityList();
        time = 0;
        this.outputOn = outputOn;
    }

    //======================================================================
    //* public static Random getRandomNumberGenerator()
    //* Returns a reference to the simulation's RNG.
    //======================================================================
    public static Random getRandomNumberGenerator()
    {
        return SimulationManager.rng;
    }

    //======================================================================
    //* public void run()
    //* This method is called from within main() to begin the actual 
    //* simulation.
    //*
    //* This method houses the next-event engine that drives the agent-
    //* based model.  Essentially, as long as we haven't passed the maximum
    //* simulated time and there are still agents living, we grab the next
    //* event in simulated time, process that event (which has a corresponding 
    //* agent), and repeat.
    //======================================================================
    public void run()
    {
        int nextTimeStep = 0;  // next time to update counts in time series window
        if (outputOn)
            System.out.println(nextTimeStep + "\t" +
                               numUninfectedAgents + "\t" + 
                               numInfectedAgents + "\t" + 
                               numTreatedAgents);
        nextTimeStep++;

        // if the state is RUNNING, we want to do normal sim stuff;
        // if the state is PAUSED, we want run() to keep going, but
        //    not do normal sim stuff;
        // if the state is HALTED, we want to drop out altogether.
        while (state != HALTED)
        {
            // this is the main simulation loop; keep going so long as
            // the user hasn't interrupted, we're not past the maximum
            // time, and there are still living agents
            while (state == RUNNING)
            {
                // uncomment this line if you want to see text-based info
                // about the agent/event being handled
//                logAgentInfo();  // debugging info

                // first, find the event with most imminent event time;
                // break out of the running loop if all agents have died
                Event event = eventList.getNextEvent();
                if (event == null) { state = HALTED; break; }

                // update the simulation clock; break out if beyond max
                time = event.getTime();
                if (time >= maxSimulatedTime) { state = HALTED; break; }

                if (time > nextTimeStep)
                {
                    // we've crossed another integer time boundary, so
                    // bump the counter and update the counts in the 
                    // time series window
                    if (outputOn)
                        System.out.println(nextTimeStep + "\t" +
                                           numUninfectedAgents + "\t" + 
                                           numInfectedAgents + "\t" + 
                                           numTreatedAgents);
                    nextTimeStep++;
                }

                // now process the event based on its type
                Agent agent = event.getAgent();
                Event.EventType type = event.getType();
                switch (type)
                {
                    case MOVE:
                        // move the agent a random amt in a random direction
                        moveAgent(agent);
                        checkInfectionStatus(agent);
                        agent.setNextEvent();
                        eventList.insertEvent(agent.getNextEvent());
                        break;

                    case INTERACT:
                        // then interact with neighbor agents (if any)
                        interactWithNeighborhood(agent);
                        checkInfectionStatus(agent);
                        agent.setNextEvent();
                        eventList.insertEvent(agent.getNextEvent());
                        break;

                    case DEATH:
                        // remove from the agent list and update counts
                        agentList.remove(agent);
                        occupied[agent.getRow()][agent.getCol()] = null;

                        if (agent.isInfected())
                            numInfectedAgents--;
                        else
                            numUninfectedAgents--;

                        if (agent.isTreated()) numTreatedAgents--;

                        break;

                    case ANTIBIOTIC_APP:
                        // time for an agent to receive antibiotic
                        if (agent.treatAgent(time))
                          numTreatedAgents++;
                        checkInfectionStatus(agent);
                        agent.setNextEvent();
                        eventList.insertEvent(agent.getNextEvent());
                        break;

                    case ANTIBIOTIC_CLEAR:
                        // an agent's antibiotic is wearing off
                        agent.clearAntibiotic(Double.MAX_VALUE);
                        numTreatedAgents--;
                        checkInfectionStatus(agent);
                        agent.setNextEvent();
                        eventList.insertEvent(agent.getNextEvent());
                        break;
                }
            }
        }

    } // runSimulation()

    //======================================================================
    //* public void setup(int maxTime, ...
    //* Used to setup the simulation initially before running.
    //======================================================================
    public void setup(int maxTime, 
                      int maxAgents, 
                      int totalAgents, 
                      double pctInfected, 
                      double pctTreated)
    {
        rng.setSeed( 90210 );    

        // set the instance variables using the arguments passed in
        maxSimulatedTime = maxTime;
        time = 0;
        agentList.clear();  // just in case, clear out the agent list

        // Build the list of agents -- this needs to come after the antigens
        // and antibiotics are created because the agent constructor uses the
        // former.
        numInfectedAgents   = (int)(totalAgents * pctInfected);
        numUninfectedAgents = totalAgents - numInfectedAgents;

        // only treat the infected agents
        numTreatedAgents    = (int)(numInfectedAgents * pctTreated);

        // first, the infecteds...
        for (int i = 0; i < numInfectedAgents; i++)
        {
            // add the agent to the back of the agent list and updated the
            // event list
            boolean infected = true;
            Agent agent = new Agent( "Agent " + agentList.size(), infected);
            agentList.add(agent);
            eventList.insertEvent(agent.getNextEvent());
        }

        // now those uninfected...
        for (int i = 0; i < numUninfectedAgents; i++)
        {
            boolean infected = false;
            Agent agent = new Agent( "Agent " + agentList.size(), infected);
            agentList.add(agent);
            eventList.insertEvent(agent.getNextEvent());
        }

        // now treat the appropriate number at random...
        int treatedCount = 0;
        while (treatedCount < numTreatedAgents)
        {
            // pick an agent at random (there is a better way than discarding
            // those who are not infected, but I don't have time...)
            int index = rng.nextInt(agentList.size());
            Agent agent = agentList.get(index);

            if (agent.isInfected() && !agent.isTreated())
            {
                agent.treatAgent(time);
                treatedCount++;
            }
        }

        // place all agents at random and then draw them
        placeAgentsAtRandom();

        // initialize the simulation state for running...
        state = RUNNING;
    }

    //======================================================================
    //* private void logAgentInfo()
    //* Private method just to print out debugging information about the
    //* agents running amok on the landscape.  In a typical run, this
    //* method won't be called.
    //======================================================================
    private void logAgentInfo()
    {
        System.out.println("AGENTS:");
        for (int i = 0; i < agentList.size(); i++)
        {
            Agent a = agentList.get(i);
            System.out.println("(" + a.getRow() + "," + a.getCol() + ") " +
                " E:" + a.nextEventTime + " || " +
                " M:" + a.nextMoveTime + " I:" + a.nextInteractTime +
                " D:" + a.timeOfDeath +
                " A: " + a.timeAntibioticClears + "\n");
        }
    }

    //======================================================================
    //* private void placeAgentsAtRandom()
    //* Just drop the agents at random onto an unoccupied spot in the grid.
    //======================================================================
    private void placeAgentsAtRandom()
    {
        // any previous information of where the agents are will be wiped
        // out by this -- OK, because we will drop them all at random below
        int gridWidth = Parameters.getDefaultGridSize();
        int gridHeight = Parameters.getDefaultGridSize();
        occupied = new Agent[gridHeight][gridWidth];

        int numAgents = agentList.size();
        for (int i = 0; i < numAgents; i++)
        {
            // grab an agent...
            Agent agent = agentList.get(i);

            boolean done = false;
            while (!done)
            {
                // pick a (row,col) location at random...
                int row = rng.nextInt(gridHeight); // an int in [0,gridWidth-1]
                int col = rng.nextInt(gridWidth);  // an int in [0,gridHeight-1]

                // and if not already taken, plop the agent there
                if (occupied[row][col] == null)
                {
                    occupied[row][col] = agent;
                    done = true;
                    agent.setRowCol(row, col);
                }
            }
        }
        
    } // end of placeAgentsAtRandom()

    //======================================================================
    //* private void interactWithNeighborhood(Agent agent)
    //* Have the agent look around the Moore neighborhood (8 nearest 
    //* neighbors) at random and see to whom, or from whom, disease can be 
    //* spread. We'll presume max one interaction, regardless of the 
    //* involved agents being infected or not.
    //======================================================================
    private void interactWithNeighborhood(Agent agent)
    {
        // set up directions (N,S,E,W,NE,SE,NW,SW) for drawing w/o replacement
        int directions[] = {0,1,2,3,4,5,6,7};
        int lastIndex = 7;

        boolean interactionOccurred = false;

        while (!interactionOccurred && lastIndex > 0)
        {

            // pick a direction from the array at random w/o replacement
            int index = rng.nextInt( lastIndex + 1 ); 
            int direction = directions[index];

            // swap selected direction to end of array so it won't be selected again
            int swapValue = directions[index];
            directions[index] = directions[lastIndex];
            directions[lastIndex] = swapValue;
            lastIndex--;

            int row = agent.getRow();
            int col = agent.getCol();

            int gridHeight = Parameters.getDefaultGridSize();
            int gridWidth = Parameters.getDefaultGridSize();

            // now see if there is an agent at the neighborhood cell in the
            // selected direction
            int neighborRow = 0, neighborCol = 0;
            int distance = 1;   // looking only one cell away
            switch (direction)
            {
                case 0:  // N
                    neighborRow = row - distance;  neighborCol = col;             break;
                case 1:  // S
                    neighborRow = row + distance;  neighborCol = col;             break;
                case 2:  // E
                    neighborRow = row;             neighborCol = col + distance;  break;
                case 3:  // W
                    neighborRow = row;             neighborCol = col - distance;  break;
                case 4:  // NE
                    neighborRow = row - distance;  neighborCol = col + distance;  break;
                case 5:  // SE
                    neighborRow = row + distance;  neighborCol = col + distance;  break;
                case 6:  // NW
                    neighborRow = row - distance;  neighborCol = col - distance;  break;
                case 7:  // SW
                    neighborRow = row + distance;  neighborCol = col - distance;  break;
            }

            // treat the landscape as a torus, so that an agent falling off the 
            // bottom reappears at the top and so on
            neighborRow = (neighborRow + gridHeight) % gridHeight;
            neighborCol = (neighborCol + gridWidth)  % gridWidth;

            // grab the agent at the spot
            Agent neighborAgent = getAgentAt(neighborRow, neighborCol);

            if (neighborAgent == null) 
                continue;                     // no agent there, go try again
            else
                interactionOccurred = true;   // here's the one shot

            // need an infected and an uninfected to spread (we presume
            // for simplicity only one infection per agent)
            Agent infectedAgent = agent.isInfected() ? agent :
                (neighborAgent.isInfected() ? neighborAgent : null);
            Agent uninfectedAgent = !agent.isInfected() ? agent :
                (!neighborAgent.isInfected() ? neighborAgent : null);

            if (infectedAgent != null && uninfectedAgent != null)
            {
                String antigen = infectedAgent.getAntigen();

                // for uninfected, probabilitically determine if infection may occur
                if (rng.nextDouble() < Parameters.getProbInfectAnother())
                {
                    // call user method to determine if any antibody or
                    // antibiotic (if present) eliminates the antigen
                    boolean successfulImmuneResponse = 
                        uninfectedAgent.successfulImmuneResponse(antigen);
                                                
                    // if not staved off...
                    if (!successfulImmuneResponse)
                    {
                        // the uninfected is now infected too...
                        uninfectedAgent.infectWith(antigen, time);

                        if (!Parameters.isABMonitoredContinuously())
                            checkForTreatment(uninfectedAgent);
    
                        numInfectedAgents++;
                        numUninfectedAgents--;
                    }
                } // otherwise, probability favors this agent.
            }
    
        } // end of while (!interactionOccurred && lastIndex > 0)

        agent.setNextInteractTime();


    } // end of interactWithNeighborhood()


    //======================================================================
    //* private void checkForTreatment(Agent agent)
    //* Method to see if this agent should be treated.
    //======================================================================
    private void checkForTreatment(Agent agent)
    {
        // if agent is not already treated with an antibiotic, do so @ random;
        // but, we want to allow for a delay in antibiotic application (based
        // on symptoms arising)
        if (agent.isInfected() && !agent.isTreated() &&
            rng.nextDouble() < Parameters.getProbTreatment())
        {
            assert( agent.getTimeOfInfection() != Double.MAX_VALUE );

            // this may have been an agent who was unlucky enough to not be AB
            // treated for a long time since infection;  if so, we need to
            // make sure that the time of treatment is not sometime in the
            // past; in most cases, though, it will be a time in the future
            double earliestTreatmentTime = 
              Math.max(
                agent.getTimeOfInfection() + SimulationManager.Uniform( 
                      Parameters.getTimeToSymptomsMin(),
                      Parameters.getTimeToSymptomsMax() ),
                time );

            // schedule for the agent to be treated
            agent.setTimeOfTreatment( earliestTreatmentTime );
        }

    } // end of checkForTreatment

    //======================================================================
    //* private void mutateAntigen(Agent agent)
    //* Method to allow for mutations in an antigen.
    //* This method calls the method (of same name but different parameters)
    //* defined in the class that extends this class.
    //* This method is called every time the checkInfectionStatus() method
    //* is called, i.e., every time an event happens for the agent.  The
    //* probability of mutating should therefore be low.
    //======================================================================
    private void mutateAntigen(Agent agent)
    {
        // call the user's implemented method (of the same name but
        // different parameters) to do the mutation; note that the
        // probability determination of whether or not to mutate is
        // in the user's method -- it would make more sense here, but
        // I want to push that probability work onto the students
        agent.mutateAntigen();

        //agent.infectWith(mutatedAntigen);  // don't update infection time

    } // end of mutateAntigen()


    //======================================================================
    //* private void moveAgent(Agent agent)
    //* Method that has the agent pick a direction at random and then
    //* move as far in that direction as its FOV allows without stepping
    //* on another agent's head.
    //======================================================================
    private void moveAgent(Agent agent)
    {
        // pick a direction (N,S,E,W,NE,SE,NW,SW) at random
        int direction = rng.nextInt(8);
        // pick a random distance to move (but at least one step)
        int distance = rng.nextInt(Parameters.getFieldOfView()) + 1;

        int row = agent.getRow();
        int col = agent.getCol();

        int gridHeight = Parameters.getDefaultGridSize();
        int gridWidth = Parameters.getDefaultGridSize();

        // now move as far in that direction as possible, choosing the
        // farthest unoccupied cell within the distance
        int toRow = 0, toCol = 0;
        boolean done = false;
        do {
            switch (direction)
            {
                case 0:  // N
                    toRow = row - distance;  toCol = col;             break;
                case 1:  // S
                    toRow = row + distance;  toCol = col;             break;
                case 2:  // E
                    toRow = row;             toCol = col + distance;  break;
                case 3:  // W
                    toRow = row;             toCol = col - distance;  break;
                case 4:  // NE
                    toRow = row - distance;  toCol = col + distance;  break;
                case 5:  // SE
                    toRow = row + distance;  toCol = col + distance;  break;
                case 6:  // NW
                    toRow = row - distance;  toCol = col - distance;  break;
                case 7:  // SW
                    toRow = row + distance;  toCol = col - distance;  break;
            }

            // treat the landscape as a torus, so that an agent falling off the 
            // bottom reappears at the top and so on
            toRow = (toRow + gridHeight) % gridHeight;
            toCol = (toCol + gridWidth)  % gridWidth;

            if (occupied[toRow][toCol] == null) // no agent there
            {
                // move the agent
                occupied[row][col] = null;
                agent.setRowCol(toRow, toCol);
                occupied[toRow][toCol] = agent;
                done = true;
            }
            else
            {
                // try one cell closer
                distance--;
            }

        } while (!done && distance > 0);

        agent.setNextMoveTime();  // agent at least tried, so try again in future

    } // end of moveAgent()


    //======================================================================
    //* private void checkInfectionStatus(Agent agent)
    //* Method to see if, presuming an infected agent,  any antibody or 
    //* antibiotic present in the agent can clear the antigen.  It may be
    //* that the agent dies (probabilistically) because of the antigen.
    //======================================================================
    private void checkInfectionStatus(Agent agent)
    {
        if (agent.isInfected())
        {
            // see if the antigen is to mutate at random (the random part
            // is pushed into the student's code)
            mutateAntigen(agent);

            String antigen = agent.getAntigen();

            // call user method to determine if any of the antibodies or
            // antibiotic (if present) eliminate the antigen
            boolean successfulImmuneResponse = 
                agent.successfulImmuneResponse(antigen);

            if (successfulImmuneResponse)
            {
                // this agent is no longer infected!
                agent.clearInfection(Double.MAX_VALUE);
                numInfectedAgents--;
                numUninfectedAgents++;
            } 

            // when continuously monitoring for AB application,
            // see if it is to randomly receive treatment -- note that the
            // method below checks to see if the agent is infected
            if (Parameters.isABMonitoredContinuously())
                checkForTreatment(agent);
        }

        // if the antibodies/antibiotic haven't eliminated the antigen, may die
        if (agent.isInfected()) 
        { 
            boolean goingToDie = checkForDeath(agent); 

            // this method is only called within the main simulation loop,
            // after which we immediately set the next event, so no need
            // to call the method updating the next event here
        }

    } // end of checkInfectionStatus()

    //======================================================================
    //* private void checkForDeath(Agent agent)
    //* If the agent is infected, flip a weighted coin.  Tails means death.
    //======================================================================
    private boolean checkForDeath(Agent agent)
    {
        // handle potential agent death
        double value = rng.nextDouble();
        if (value < Parameters.getProbInfectedAgentDies())
        {
            agent.setTimeOfDeath(time);  // agent dies now
                    // just sets up the death event to occur in run() loop
            return true;
        }
        return false;

    } // end of checkForDeath()

    //======================================================================
    //* private Agent getAgentAt(int row, int col)
    //* Just returns the agent (if it exists) at [row,col].
    //======================================================================
    protected Agent getAgentAt(int row, int col)
    {
        if (occupied == null) return null;

        Agent agent = null;
        if (row >= 0 && row < occupied.length &&
            col >= 0 && col < occupied[0].length)
        {
            agent = occupied[row][col];
        }

        return agent;

    } // end of getAgentAt()

    //======================================================================
    //* protected static double Exponential(double mu)
    //* Generates an exponentially distributed random variate with mean mu.
    //======================================================================
    protected static double Exponential(double mu)
    {
        return (-mu * Math.log(1.0 - rng.nextDouble()));
    }

    //======================================================================
    //* protected static double Uniform(double a, double b)
    //* Generates a uniformly distributed random variate with mean (a+b)/2.
    //======================================================================
    protected static double Uniform(double a, double b)
    {
        return (a + (b - a) * rng.nextDouble());
    }

    // Some simple accessor methods
    public int    getNumInfectedAgents()   { return numInfectedAgents; }
    public int    getNumUninfectedAgents() { return numUninfectedAgents; }
    public int    getNumTreatedAgents()    { return numTreatedAgents; }
    public double getTime()                { return time; }

    //======================================================================
    //* public static void main(String[] args)
    //======================================================================
    public static void main(String[] args)
    {
        // first, some checking of arguments to main
        if (args.length != 3)
        {
            System.err.println("Usage: java SimulationManager " +
                " [grid size (one side)] [# agents] [0:no output 1:output]");
            System.exit(0);
        }
        int gridSize  = Integer.parseInt(args[0]);
        int numAgents = Integer.parseInt(args[1]);
        boolean outputOn = (Integer.parseInt(args[2]) == 0 ? false : true);

        if (numAgents > gridSize*gridSize)
        {
            System.err.println(
                "# agents cannot be bigger than total square grid size");
            System.exit(0);
        }


        // now construct the simulation object and make things go...
        int    maxTime     = 1000;
        int    maxAgents   = gridSize * gridSize;
        double pctInfected = 0.2;
        double pctTreated  = 0.2;

        Parameters.setDefaultGridSize(gridSize);
        SimulationManager sim = new SimulationManager(outputOn);
        sim.setup(maxTime, maxAgents, numAgents, pctInfected, pctTreated);
        sim.run();
    }

} // end of class SimulationManager
