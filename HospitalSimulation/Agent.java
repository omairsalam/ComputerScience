import java.util.Random;
import java.util.ArrayList;

public class Agent extends SuperAgent
{
    // constants
    private static final int ANTIBODY_LENGTH   = Parameters.getAntibodyLength();
    private static final int ANTIGEN_LENGTH    = Parameters.getAntigenLength();
    private static final int ANTIBIOTIC_LENGTH = Parameters.getAntibioticLength();

    // static (i.e., class)  variables -- common to (shared by) all agents
    private static Random generator = SimulationManager.getRandomNumberGenerator();

    // isntance variables 
    private int row;           // vertical position of the agent
    private int column;        // horizontal position of the agent

    private String id;         // name of the agent

    private String antigen;    // the infector
    private String antibiotic; // artificial defense

    private ArrayList<String> antibodies;   // natural defense
    private ArrayList<String> clearedAntigens;

    //======================================================================
    //* public Agent(String id, boolean isInfected, boolean isTreated)
    //* Constructor for an agent.
    //======================================================================
    public Agent( String id, boolean isInfected )
    {
        super( isInfected );

        // set the agent's name
        this.id = id;

        // call our method below to place this agent at random on the grid
        this.row = 0;
        this.column = 0;

        this.antibodies = new ArrayList<String>();
        this.clearedAntigens = new ArrayList<String>();

        for (int i = 0; i < Parameters.getNumAntibodies(); i++)
        {
            String antibody = this.randomBinaryString( Agent.ANTIBODY_LENGTH );
            this.antibodies.add(antibody);
        }

        // set the antigen and antibiotic to something other than null
        // only if the parameter says to do sosomething other than null
        this.antigen    = null;
        this.antibiotic = null;
        if (isInfected)
        {
            // call our method below to create a random binary string
            // of the correct length, and fill the antigen instance variable
            this.antigen = Agent.getRandomAntigen();
        }

        this.setNextEvent();
    }

    //======================================================================
    //* public String randomBinaryString(int length)
    //* Creates a string of the provided length made of randomly chosen 
    //* 0's and 1's.
    //======================================================================
    public String randomBinaryString(int length)
    {
        String newString = new String();
        for (int i = 0; i < length; i++)
        {
            newString += generator.nextInt(2);
        }
        return newString;
    }

    //======================================================================
    //* public String toString()
    //* Returns a String representation of the agent.
    //======================================================================
    public String toString()
    {
        String description = 
            new String(
              this.id + " @(" + this.row + "," + this.column + ")\n");

        description += "\tantibodies:\n";
        for (int i = 0; i < this.antibodies.size(); i++)
        {
            description += "\t\t" + this.antibodies.get(i) + "\n";
        }

        description += "\tantigen:    " + this.antigen + "\n" +
                       "\tantibiotic: " + this.antibiotic;
        return description;

    }

    //======================================================================
    //* public Event getNextEvent()
    //======================================================================
    public Event getNextEvent()
    {
        return(new Event(nextEventTime, nextEventType, this));
    }

    // simple accessor methods below
    public int     getRow()        { return this.row;    }
    public int     getCol()        { return this.column; }

    public String  getID()         { return this.id;         }
    public String  getAntigen()    { return this.antigen;    }
    public String  getAntibiotic() { return this.antibiotic; }

    public boolean isTreated()     { return (this.antibiotic != null); }
    public boolean isInfected()    { return (this.antigen != null);    }

    public String  getAntibody(int whichAntibody)
    {
        return this.antibodies.get(whichAntibody);
    }
    
    public ArrayList<String> getClearedAntigens()
    {
        return this.clearedAntigens;
    }

    public boolean isImmuneTo(String thisAntigen)
    {
        if (this.clearedAntigens.contains(thisAntigen))
        {
          return true;
        }
        return false;
    }

    public void    addToImmunity(String clearedAntigen)
    {
        this.clearedAntigens.add(clearedAntigen);
    }

    public void    setRowCol(int theRow, int theCol)
    {
        this.row = theRow;
        this.column = theCol;
    }

    public void    infectWith(String anAntigen)
    {
        this.antigen = anAntigen;
    }

    public void    clearInfection()
    {
        this.antigen = null;
    }

    public void    clearAntibiotic()
    {
        this.antibiotic = null;
    }

    public boolean mutateAntigen()
    {
        double value = Agent.generator.nextDouble();
        if (value < Parameters.getProbAntigenMutates())
        {
            int index = Agent.generator.nextInt( this.antigen.length() );
            StringBuffer buffer = new StringBuffer( this.antigen );
            if (buffer.charAt(index) == '0')
            {
                buffer.setCharAt(index, '1');
            }
            else
            {
                buffer.setCharAt(index, '0');
            }
            this.antigen = buffer.toString();
            return true;
        }
        return false;
    }

    public boolean treatAgent()
    {
        if (this.isInfected())
        {
            this.antibiotic = Agent.getRandomAntibiotic();
            return true;
        }
        return false;
    }

    public boolean successfulImmuneResponse( String anAntigen )
    {
        if ( this.isImmuneTo( anAntigen ) )
        {
            return true;
        }

        if ( this.isTreated() )
        {
            if ( this.successfullyBindsWithAntigen( anAntigen, this.antibiotic ))
            {
                return true;
            }
        }

        for (int i = 0; i < Parameters.getNumAntibodies(); i++)
        {
            String antibody = this.getAntibody(i);
            if (this.successfullyBindsWithAntigen( anAntigen, antibody ))
            {
                this.addToImmunity(anAntigen);
                return true;
            }
        }

        return false;
    }

    public boolean successfullyBindsWithAntigen( String anAntigen, 
                                                 String anAntibody)
    {
        int length = anAntibody.length();

        int lastPosition = anAntigen.length() - anAntibody.length();
        for (int i = 0; i <= lastPosition; i++)
        {
            String partOfAntigen = anAntigen.substring(i, i + length);
            if (partOfAntigen.equals(anAntibody))
            {
                double value = Agent.generator.nextDouble();
                if (value < Parameters.getProbBoundAntigenIsDestroyed())
                {
                    return true;
                }
            }
        }
        return false;
    }
}
