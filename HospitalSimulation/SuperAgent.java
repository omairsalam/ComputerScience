import java.util.ArrayList;
import java.util.Random;

//======================================================================
//* This class implements a generic agent in our agent-based simulation.
//* The agent can be infected or not, treated or not.  Agents typically
//* have very little personality, so good luck becoming the buddy of an
//* agent.
//*
//* Essentially the purpose of this class is to hide the simulation event
//* details from the student, while allowing the student to implement an
//* Agent class that handles modeling AB/antigen/antibody binding sites.
//======================================================================
public abstract class SuperAgent
{
    // STATIC VARIABLES FOR ALL AGENTS
    protected static ArrayList<String> antigenList      = null;
    protected static ArrayList<String> antibioticList   = null;
    private   static boolean           listsInitialized = SuperAgent.initializeStaticLists();

    // event times for a particular agent
    protected double nextEventTime;      // minimum of times below
    protected Event.EventType nextEventType;  // of those listed in Event.java

    protected double nextMoveTime;
    protected double nextInteractTime;
    protected double timeOfDeath;
    protected double timeAntibioticApplied;
    protected double timeAntibioticClears;

    protected double timeOfInfection;    // used to determine when AB can be applied
    protected double timeOfTreatment;    // same as applied event time below, but we need 
                                         // this to compute likelihood of AB affecting
                                         // the antigen

    //======================================================================
    //* public SuperAgent(boolean isInfected)
    //* Parent-class constructor for an agent.  Note that the constructor
    //* for the extending class _MUST_ call this constructor as its first
    //* statement. 
    //======================================================================
    public SuperAgent(boolean isInfected)
    {
        // use a stationary Poisson arrival process to drive the movement
        // times, i.e., interarrival times are drawn from an Exponential
        // distribution with mean mu
        nextMoveTime = nextInteractTime = 0.0;  // initialize
        nextMoveTime     += getNextInterMoveTime();
        nextInteractTime += getNextInterInteractTime();  // funny, huh?

        timeOfDeath = Double.MAX_VALUE;  // initially, infinity
        timeAntibioticApplied = Double.MAX_VALUE;
        timeAntibioticClears = Double.MAX_VALUE;

        timeOfInfection = Double.MAX_VALUE;
        timeOfTreatment = Double.MAX_VALUE;
        
        // if the agent is infected initially, subclass's construction will
        // select one of the possible antigens at random as the infector/treater;
        // just update event time here
        if (isInfected) 
        { 
            timeOfInfection = 0;
        }

    }

    //======================================================================
    //* protected static void initializeStaticLists()
    //* Populates the lists of possible antigens and antibiotics.
    //======================================================================
    private static boolean initializeStaticLists()
    {
        // grab parameters for easy access
        int numAntigens = Parameters.getNumAntigens();
        int antigenLen  = Parameters.getAntigenLength();
        int numAntibiotics = Parameters.getNumAntibiotics();
        int antibioticLen  = Parameters.getAntibioticLength();

        // The simulation will have a set number of antigens created at
        // simulation startup.  Each of these antigens is a random binary
        // string of a given length.
        antigenList = new ArrayList<String>();
        int count = 0;
        while (count < numAntigens)
        {
            String antigen = new String();
            for (int j = 0; j < antigenLen; j++)
            {
                int randomBit  = SimulationManager.rng.nextInt(2); // a random 0 or 1

                antigen += randomBit;            // append the bit
            }

            if (!antigenList.contains(antigen))
            {
                antigenList.add(antigen);  // add the antigen to list end
                count++;
            }
        }

        // The simulation will have a set number of antibiotics created
        // at simulation startup.  Originally I wanted these antibiotics to
        // be substrings of one of the antigens, but if the user chooses
        // n-bit antibiotics with 2^n total antibiotics, we can be hosed in
        // an infinite loop trying to come up with all 2^n possibilities
        // which likely won't exist in the finite number of antigens.  So,
        // just create the antibiotics at random too.
        antibioticList = new ArrayList<String>();
        count = 0;
        while (count < numAntibiotics)
        {
            String antibiotic = new String();
            for (int j = 0; j < antibioticLen; j++)
            {
                int randomBit  = SimulationManager.rng.nextInt(2); // a random 0 or 1

                antibiotic += randomBit;            // append the bit
            }

            if (!antibioticList.contains(antibiotic))
            {
                antibioticList.add(antibiotic); // add the AB to the list end
                count++;
            }
        }

        return true;
    }

    //======================================================================
    //* public String getRandomAntigen()
    //* Returns a randomly selected antigen from the list of antigens.
    //======================================================================
    public static String getRandomAntigen()
    {
        int index = SimulationManager.rng.nextInt(antigenList.size());
        return new String(antigenList.get(index));
    }

    //======================================================================
    //* public String getRandomAntibiotic()
    //* Returns a randomly selected antibiotic from the list of antigens.
    //======================================================================
    public static String getRandomAntibiotic()
    {
        int index = SimulationManager.rng.nextInt(antibioticList.size());
        return new String(antibioticList.get(index));
    }

    //======================================================================
    //* public double getTimeOfInfection()
    //======================================================================
    public double getTimeOfInfection()
    {
        return timeOfInfection;
    }

    //======================================================================
    //* public void setTimeOfDeath(double time)
    //======================================================================
    public void setTimeOfDeath(double timeToDie)
    {
        timeOfDeath = timeToDie;
    }

    //======================================================================
    //* private static double Exponential(double mu)
    //======================================================================
    private static double Exponential(double mu)
    {
        return (-mu * Math.log(1.0 - SimulationManager.rng.nextDouble()));
    }

    //======================================================================
    //* private static double Uniform(double a, double b)
    //======================================================================
    private static double Uniform(double a, double b)
    {
        return (a + (b - a) * SimulationManager.rng.nextDouble());
    }


    //======================================================================
    //* protected double getNextInterMoveTime()
    //======================================================================
    protected double getNextInterMoveTime()
    {
        return Exponential(Parameters.getAvgTimeBtwnMove());
    }

    //======================================================================
    //* protected double getNextInterInteractTime()
    //======================================================================
    protected double getNextInterInteractTime()  // funny name, no?
    {
        return Exponential(Parameters.getAvgTimeBtwnInteract());
    }

    //======================================================================
    //* protected double getAntibioticClearTime()
    //======================================================================
    protected double getAntibioticClearTime()
    {
        return Uniform(Parameters.getTreatmentLengthMin(),
                       Parameters.getTreatmentLengthMax());
    }

     
    public double          getNextEventTime() { return nextEventTime; }
    public Event.EventType getNextEventType() { return nextEventType; }

    //======================================================================
    //* public void setNextMoveTime()
    //======================================================================
    public void setNextMoveTime()
    {
        nextMoveTime += getNextInterMoveTime();
    }

    //======================================================================
    //* public void setTimeOfTreatment(double time)
    //======================================================================
    public void setTimeOfTreatment(double theTime)
    {
        timeAntibioticApplied = theTime;
    }

    //======================================================================
    //* public void setNextInteractTime()
    //======================================================================
    public void setNextInteractTime()
    {
        nextInteractTime += getNextInterInteractTime();
    }

    //======================================================================
    //* private void setNextEvent()
    //======================================================================
    public void setNextEvent()
    {
        // we'll presume death is the highest priority event; this way, if
        // an agent is busy doing some event and turns out the agent should
        // die right now, the "some event" won't take precedence
        nextEventTime = timeOfDeath;
        nextEventType = Event.EventType.DEATH;

        if (nextMoveTime < nextEventTime)
        {
            nextEventTime = nextMoveTime;
            nextEventType = Event.EventType.MOVE;
        }
        if (nextInteractTime < nextEventTime)
        {
            nextEventTime = nextInteractTime;
            nextEventType = Event.EventType.INTERACT;
        } 
        if (timeAntibioticApplied < nextEventTime)
        {
            nextEventTime = timeAntibioticApplied;
            nextEventType = Event.EventType.ANTIBIOTIC_APP;
        }
        if (timeAntibioticClears < nextEventTime)
        {
            nextEventTime = timeAntibioticClears;
            nextEventType = Event.EventType.ANTIBIOTIC_CLEAR;
        }
    }

    //======================================================================
    //* public void infectWith(String newAntigen, double time)
    //* The subclass will have a similarly named method, but without the
    //* time parameter.  This allows the student to implement an infectWith
    //* method but not have to worry about event time handling.
    //======================================================================
    public void infectWith(String newAntigen, double time)
    {
        infectWith(newAntigen);
        timeOfInfection = time;
    }

    //======================================================================
    //* public void clearInfection(double time)
    //* The subclass will have a similarly named method, but without the
    //* time parameter.  This allows the student to implement a
    //* method but not have to worry about event time handling.
    //======================================================================
    public void clearInfection(double time)
    {
        clearInfection();
        timeOfInfection = time; // Double.MAX_VALUE -- just using param
                                // to have diff signature than subclass's
                                // version of this method
    }

    //======================================================================
    //* public void clearAntibiotic(double time)
    //* The subclass will have a similarly named method, but without the
    //* time parameter.  This allows the student to implement a
    //* method but not have to worry about event time handling.
    //======================================================================
    public void clearAntibiotic(double time)
    {
        clearAntibiotic();
        timeAntibioticClears = time; // Double.MAX_VALUE -- see above
    }

    //======================================================================
    //* public boolean treatAgent(double treatmentTime)
    //* The subclass will have a similarly named method, but without the
    //* time parameter.  This allows the student to implement a
    //* method but not have to worry about event time handling.
    //======================================================================
    public boolean treatAgent(double treatmentTime)
    {
        timeAntibioticApplied = Double.MAX_VALUE; // clear this event time
        boolean treated = treatAgent();           // in subclass
        if (treated)
        {
            timeOfTreatment = treatmentTime; // for computing AB's effectiveness
            timeAntibioticClears = treatmentTime + getAntibioticClearTime();
            return true;
        }
        return false;
    }

    //**********************************************************************
    //**********************************************************************
    //* For each of the methods below, a class extending this class must 
    //* implement a correspondingly named method.  This allows the student 
    //* to implement methods without worry of simulation details.  All 
    //* methods but the final one (successfullyBindsWithAntigen()) are 
    //* called by SimulationManager to make the simulation execute.
    //**********************************************************************
    //**********************************************************************

    public abstract int     getRow();
    public abstract int     getCol();
    public abstract String  getAntibody(int whichAntibody);
    public abstract String  getAntigen();
    public abstract String  getAntibiotic();
    
    public abstract ArrayList<String> getClearedAntigens();

    public abstract boolean isImmuneTo(String thisAntigen);
    public abstract boolean isInfected();
    public abstract boolean isTreated();

    public abstract void    addToImmunity(String clearedAntigen);
    public abstract void    setRowCol(int theRow, int theCol);
    public abstract void    infectWith(String anAntigen);
    public abstract void    clearInfection();
    public abstract void    clearAntibiotic();

    public abstract boolean mutateAntigen();
    public abstract boolean treatAgent();

    public abstract boolean successfulImmuneResponse( String anAntigen );

    //======================================================================
    //* public abstract boolean successfullyBindsWithAntigen(String antigen, 
    //* Any class extending this must implement a successfullyBindsWithAntigen()
    //* method.  This method is not called by any of the simulation's
    //* internal methods, but is our attempt to force modularity --- i.e.,
    //* the student's successfulImmuneResponse() method should call this method.
    //======================================================================
    public abstract boolean successfullyBindsWithAntigen( String anAntigen, 
                                                          String anAntibody);
    
    //**********************************************************************
    //**********************************************************************
}
