//************************************************************************
//* This class provides a central location for initializing/controlling
//* the parameters for the agent-based simulation.  All instance variables
//* and all accessor and mutator methods are static.  
//*
//* Default values for all the parameters are provided below.
//************************************************************************

public class Parameters
{
    private static int rngInitialSeed = 8675309;  // initial seed for the RNG

    private static int numAntibodies  = 2; // number of antibodies per agent
    private static int numAntigens    = 4; // number of possible antigens (initially)
    private static int numAntibiotics = 2; // number of possible antibiotics

    // lengths of bit strings representing each
    private static int antibodyLength   = 5;
    private static int antigenLength    = 20;
    private static int antibioticLength = 3;

    private static double probabilityInfectAnotherAgent      = 0.1;
    private static double probabilityBoundAntigenIsDestroyed = 0.8;
    private static double probabilityAntigenMutates          = 0.1;
    private static double probabilityInfectedAgentDies       = 0.0001;
    private static double probabilityTreatment               = 0.8;

    // 4-7 days
    private static double timeToSymptomsMin  =  96.0;
    private static double timeToSymptomsMax  = 168.0;

    // 2-3 weeks to clear 
    private static double treatmentLengthMin = 336.0; // in hours
    private static double treatmentLengthMax = 504.0;

    private static int agentGUISize    = 10; // an agent is an mxm square
    private static int defaultGridSize = 40; // NxN square grid for agents

    private static int fieldOfView = 2;  // how far agents see in each dir.

    // each type of event has its own arrival process, and these are the
    // average times between events for those processes
    private static double avgTimeBtwnMove     = 0.5;
    private static double avgTimeBtwnInteract = 1.0;

    private static boolean continuousABMonitoring = true;
      // when false, only one change at AB application: upon infection
      // when true, continually checking to see if AB should be applied

    //************************************************************
    //* ACESSOR METHODS BELOW -- just return appropriate value
    //************************************************************

    public static int getRNGInitialSeed()      { return rngInitialSeed; }

    public static int getNumAntibodies()       { return numAntibodies; }
    public static int getNumAntigens()         { return numAntigens; }
    public static int getNumAntibiotics()      { return numAntibiotics; }

    public static int getAntibodyLength()      { return antibodyLength; }
    public static int getAntigenLength()       { return antigenLength; }
    public static int getAntibioticLength()    { return antibioticLength; }

    public static int getAgentGUISize()        { return agentGUISize; }
    public static int getDefaultGridSize()     { return defaultGridSize; }

    public static double getProbInfectAnother()            { return probabilityInfectAnotherAgent; }
    public static double getProbBoundAntigenIsDestroyed()  { 
          return probabilityBoundAntigenIsDestroyed; }
    public static double getProbAntigenMutates()           { return probabilityAntigenMutates; }
    public static double getProbInfectedAgentDies()        { return probabilityInfectedAgentDies; }
    public static double getProbTreatment()                { return probabilityTreatment; }

    public static double getTimeToSymptomsMin()   { return timeToSymptomsMin;  }
    public static double getTimeToSymptomsMax()   { return timeToSymptomsMax;  }

    public static double getTreatmentLengthMin()  { return treatmentLengthMin; }
    public static double getTreatmentLengthMax()  { return treatmentLengthMax; }

    public static int    getFieldOfView()         { return fieldOfView; }

    public static double getAvgTimeBtwnMove()     { return avgTimeBtwnMove; }
    public static double getAvgTimeBtwnInteract() { return avgTimeBtwnInteract; }

    public static boolean isABMonitoredContinuously() { return continuousABMonitoring; }

    //******************************************************************
    //* MUTATOR METHODS BELOW -- set appropriate value using given input
    //******************************************************************

    public static void setRNGInitialSeed(int seed)   { rngInitialSeed = seed; }

    public static void setNumAntibodies(int num)    { numAntibodies = num; }
    public static void setNumAntigens(int num)      { numAntigens = num; }
    public static void setNumAntibiotics(int num)   { numAntibiotics = num; }

    public static void setAntibodyLength(int len)   { antibodyLength = len; }
    public static void setAntigenLength(int len)    { antigenLength = len; }
    public static void setAntibioticLength(int len) { antibioticLength = len; }

    public static void setAgentGUISize(int size)    { agentGUISize = size; }
    public static void setDefaultGridSize(int size) { defaultGridSize = size; }

    public static void setProbInfectAnother(double prob)  { probabilityInfectAnotherAgent = prob; }
    public static void setProbBoundAntigenIsDestroyed(double prob)    
        { probabilityBoundAntigenIsDestroyed = prob; }
    public static void setProbAntigenMutates(double prob)    { probabilityAntigenMutates = prob; }
    public static void setProbInfectedAgentDies(double prob) { probabilityInfectedAgentDies = prob; }
    public static void setProbTreatment(double prob)         { probabilityTreatment = prob; }

    public static void setTimeToSymptomsMin(double min)  { timeToSymptomsMin  = min; }
    public static void setTimeToSymptomsMax(double max)  { timeToSymptomsMax  = max; }

    public static void setTreatmentLengthMin(double min) { treatmentLengthMin = min; }
    public static void setTreatmentLengthMax(double max) { treatmentLengthMax = max; }

    public static void setFieldOfView(int fov)           { fieldOfView = fov; }

    public static void setAvgTimeBtwnMove(double time)     { avgTimeBtwnMove = time; }
    public static void setAvgTimeBtwnInteract(double time) { avgTimeBtwnInteract = time; }

    public static void setABMonitoredContinuously(boolean isIt) { continuousABMonitoring = isIt; }

} // end class Parameters

