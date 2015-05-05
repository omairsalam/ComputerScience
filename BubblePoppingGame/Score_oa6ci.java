import java.lang.Math;
import java.lang.Integer; 

public class Score_oa6ci implements Score  
{
  public long score;
  public int multiplier=1;
  public int level=0;
  public int lives=3; 
  public int diameter; 
  public long valueofDisc;


  public long addPoints(int amount)
  {
    if (amount >= 0)  
    {
      score = score + (amount*multiplier);
      //valueofDisc = (100 - diameter + 10)*25*(level+1);
      //score = score + (valueofDisc*multiplier);

    }
    else { 
      score = 0;
    }
    return score;
  }

  public long subtractPoints(int amount)
  { 
    if (score - amount< 0)  
    {
      score = 0;
      multiplier = 1; 

    }  else if  (amount == 0)
    { return score; }

    else 
    {
      score = score - amount;
      multiplier = 1 ;

    } 
    return score; 

  }

  public void setMultiplier(int mult)
  {
    multiplier = mult;

  } 

  public int getMultiplier()
  {
    return multiplier;       
  }


  public int incrementMultiplier()
  {
    multiplier = multiplier + 1;
    return multiplier; 
  }

  public long getScore() 
  {
    return score;
  }

  public int getLevel()
  {
    if (score < 10000 && score >= 0 )
    { 
      level = 0;
    }
    else
      level = 1 +  (int)(((Math.log(score/10000))/(Math.log(2))));


    return level;
  }

  public int getLives()
  {
    return lives; 
  }

  public boolean loseLife()
  {
    boolean livesLeft = true; 

    if (lives > 1)
    {
      lives =  lives -1;

      livesLeft = true;
    }
    else 
    {

      lives = 0; 
      livesLeft = false; 
    }
    return livesLeft;
  }

  public void gainLife()
  {
    lives = lives + 1;
  }


  public static void main(String args[])
  {
    Score_oa6ci s0 = new Score_oa6ci();
    s0.setMultiplier(2); 
    s0.incrementMultiplier();
    s0.addPoints(500);
    s0.subtractPoints(2);
    int myMultiplier = s0.getMultiplier();
    System.out.println(myMultiplier);
    long myScore = s0.getScore();
    System.out.println(myScore);
    s0.loseLife();
    s0.loseLife(); 
    System.out.println(s0.loseLife());
    int myLives = s0.getLives();
    System.out.println(myLives);
    int myLevel = s0.getLevel();
    System.out.println(myLevel);



    Score_oa6ci s1 = new Score_oa6ci();
    s1.setMultiplier(2);
    s1.incrementMultiplier(); 
    s1.subtractPoints(2);  
    s1.addPoints(500);
    myMultiplier = s1.getMultiplier();
    System.out.println(myMultiplier);
    myScore = s1.getScore();
    System.out.println(myScore);
    s1.loseLife();
    System.out.println(s1.loseLife());
    s1.gainLife();
    myLives = s1.getLives();
    System.out.println(myLives);
    myLevel = s1.getLevel();
    System.out.println(myLevel);





    Score_oa6ci s2 = new Score_oa6ci();
    s2.setMultiplier(2);
    s2.subtractPoints(0);
    s2.addPoints(100);
    myMultiplier = s2.getMultiplier();
    System.out.println(myMultiplier);
    myScore = s2.getScore();
    System.out.println(myScore);
    System.out.println(s2.loseLife());
    s2.gainLife();
    myLives = s2.getLives();
    System.out.println(myLives);
    myLevel = s2.getLevel();
    System.out.println(myLevel);



    Score_oa6ci s3 = new Score_oa6ci();
    s3.setMultiplier(2);
    s3.addPoints(100);
    s3.subtractPoints(50);
    s3.incrementMultiplier(); 
    myMultiplier = s3.getMultiplier();
    System.out.println(myMultiplier);
    myScore = s3.getScore();
    System.out.println(myScore);
    System.out.println(s3.loseLife());
    s3.gainLife();
    myLives = s3.getLives();
    System.out.println(myLives);
    myLevel = s3.getLevel();
    System.out.println(myLevel);



    Score_oa6ci s4 = new Score_oa6ci();
    s4.setMultiplier(1);
    s4.addPoints(Integer.MAX_VALUE);
    s4.subtractPoints(0);
    myMultiplier = s4.getMultiplier();
    System.out.println(myMultiplier);
    myScore = s4.getScore();
    System.out.println(myScore);
    System.out.println(s4.loseLife());
    s4.gainLife();
    myLives = s4.getLives();
    System.out.println(myLives);
    myLevel = s4.getLevel();
    System.out.println(myLevel);


    Score_oa6ci s5 = new Score_oa6ci();
    s5.setMultiplier(1);
    s5.addPoints(Integer.MAX_VALUE);
    s5.subtractPoints(Integer.MAX_VALUE);
    myMultiplier = s5.getMultiplier();
    System.out.println(myMultiplier);
    myScore = s5.getScore();
    System.out.println(myScore);
    System.out.println(s5.loseLife());
    s5.gainLife();
    myLives = s5.getLives();
    System.out.println(myLives);
    myLevel = s5.getLevel();
    System.out.println(myLevel);

    Score_oa6ci s6 = new Score_oa6ci();
    s6.setMultiplier(1);
    s6.addPoints(0);
    s6.subtractPoints(Integer.MAX_VALUE);
    myMultiplier = s6.getMultiplier();
    System.out.println(myMultiplier);
    myScore = s6.getScore();
    System.out.println(myScore);
    System.out.println(s6.loseLife());
    s6.gainLife();
    myLives = s6.getLives();
    System.out.println(myLives);
    myLevel = s6.getLevel();
    System.out.println(myLevel);



    Score_oa6ci s7 = new Score_oa6ci();
    s7.setMultiplier(Integer.MAX_VALUE);
    s7.addPoints(1);
    s7.subtractPoints(0);
    myMultiplier = s7.getMultiplier();
    System.out.println(myMultiplier);
    myScore = s7.getScore();
    System.out.println(myScore);
    System.out.println(s7.loseLife());
    s7.gainLife();
    myLives = s7.getLives();
    System.out.println(myLives);
    myLevel = s7.getLevel();
    System.out.println(myLevel);


    Score_oa6ci s8 = new Score_oa6ci();
    s8.setMultiplier(Integer.MAX_VALUE);
    s8.addPoints(1);
    s8.subtractPoints(1);
    myMultiplier = s8.getMultiplier();
    System.out.println(myMultiplier);
    myScore = s8.getScore();
    System.out.println(myScore);
    System.out.println(s8.loseLife());
    s8.gainLife();
    myLives = s8.getLives();
    System.out.println(myLives);
    myLevel = s8.getLevel();
    System.out.println(myLevel);


    Score_oa6ci s9 = new Score_oa6ci();
    s9.setMultiplier(Integer.MAX_VALUE);
    s9.incrementMultiplier();
    s9.addPoints(1);
    s9.subtractPoints(0);
    myMultiplier = s9.getMultiplier();
    System.out.println(myMultiplier);
    myScore = s9.getScore();
    System.out.println(myScore);
    System.out.println(s9.loseLife());
    s9.gainLife();
    myLives = s9.getLives();
    System.out.println(myLives);
    myLevel = s9.getLevel();
    System.out.println(myLevel);



    Score_oa6ci s10 = new Score_oa6ci();
    s10.setMultiplier(Integer.MAX_VALUE);
    s10.addPoints(Integer.MAX_VALUE);
    s10.subtractPoints(0);
    myMultiplier = s10.getMultiplier();
    System.out.println(myMultiplier);
    myScore = s10.getScore();
    System.out.println(myScore);
    System.out.println(s10.loseLife());
    s10.gainLife();
    myLives = s10.getLives();
    System.out.println(myLives);
    myLevel = s10.getLevel();
    System.out.println(myLevel);

    Score_oa6ci s11 = new Score_oa6ci();
    s11.addPoints(Integer.MAX_VALUE);
    s11.setMultiplier(2);
    s11.subtractPoints(0);
    myMultiplier = s11.getMultiplier();
    System.out.println(myMultiplier);
    myScore = s11.getScore();
    System.out.println(myScore);
    System.out.println(s11.loseLife());
    s11.gainLife();
    myLives = s11.getLives();
    System.out.println(myLives);
    myLevel = s11.getLevel();
    System.out.println(myLevel);

    Score_oa6ci s12 = new Score_oa6ci();
    System.out.println(s12.loseLife());
    myLives = s12.getLives();
    System.out.println(myLives);


    Score_oa6ci s13 = new Score_oa6ci();
    s13.gainLife();
    System.out.println(s13.loseLife());
    myLives = s13.getLives();
    System.out.println(myLives);

    Score_oa6ci s14 = new Score_oa6ci();
    System.out.println(s14.loseLife());
    System.out.println(s14.loseLife());
    System.out.println(s14.loseLife());
    System.out.println(s14.loseLife());
    myLives = s14.getLives();
    System.out.println(myLives);



    Score_oa6ci s15 = new Score_oa6ci();
    s15.setMultiplier(2);
    s15.addPoints(160000);
    s15.subtractPoints(0);
    myMultiplier = s15.getMultiplier();
    System.out.println(myMultiplier);
    myScore = s15.getScore();
    System.out.println(myScore);
    s15.gainLife();
    myLives = s15.getLives();
    System.out.println(myLives);
    myLevel = s15.getLevel();
    System.out.println(myLevel);

    Score_oa6ci s16 = new Score_oa6ci();
    s16.setMultiplier(2);
    s16.addPoints(-500);
    s16.subtractPoints(0);
    myMultiplier = s16.getMultiplier();
    System.out.println(myMultiplier);
    myScore = s16.getScore();
    System.out.println(myScore);
    s16.gainLife();
    myLives = s16.getLives();
    System.out.println(myLives);
    myLevel = s16.getLevel();
    System.out.println(myLevel);


    Score_oa6ci s17 = new Score_oa6ci();
    s17.setMultiplier(1);
    s17.addPoints(79999);
    s17.subtractPoints(0);
    myMultiplier = s17.getMultiplier();
    System.out.println(myMultiplier);
    myScore = s17.getScore();
    System.out.println(myScore);
    s17.gainLife();
    myLives = s17.getLives();
    System.out.println(myLives);
    myLevel = s17.getLevel();
    System.out.println(myLevel);


    Score_oa6ci s18 = new Score_oa6ci();
    s18.addPoints(19999);
    myScore = s18.getScore();
    System.out.println(myScore);
    myLevel = s18.getLevel();
    System.out.println(myLevel);

    Score_oa6ci s19 = new Score_oa6ci();
    s19.addPoints(39999);
    s19.setMultiplier(1);
    s19.subtractPoints(0);
    myMultiplier = s19.getMultiplier();
    System.out.println(myMultiplier);
    myScore = s19.getScore();
    System.out.println(myScore);
    System.out.println(s19.loseLife());
    s19.gainLife();
    myLives = s19.getLives();
    System.out.println(myLives);
    myLevel = s19.getLevel();
    System.out.println(myLevel);

    Score_oa6ci s20 = new Score_oa6ci();
    s20.addPoints(39999);
    myScore = s20.getScore();
    System.out.println(myScore);

    Score_oa6ci s21 = new Score_oa6ci();
    s21.subtractPoints(39999);
    myScore = s21.getScore();
    System.out.println(myScore);


    Score_oa6ci s22 = new Score_oa6ci();
    s22.setMultiplier(39999);
    myMultiplier = s22.getMultiplier();
    System.out.println(myMultiplier);
    myScore = s22.getScore();
    System.out.println(myScore);


    Score_oa6ci s23 = new Score_oa6ci();
    s23.subtractPoints(39999);
    myScore = s23.getScore();
    System.out.println(myScore);


    Score_oa6ci s24 = new Score_oa6ci();
    myLives = s24.getLives();
    System.out.println(myLives);
    myLevel = s24.getLevel();
    System.out.println(myLevel);



    Score_oa6ci s25 = new Score_oa6ci();
    s25.gainLife();    
    myLives = s25.getLives();
    System.out.println(myLives);

    Score_oa6ci s26 = new Score_oa6ci();
    s26.setMultiplier(39999);
    s26.incrementMultiplier();
    myMultiplier = s26.getMultiplier();
    System.out.println(myMultiplier);

    Score_oa6ci s27 = new Score_oa6ci();
    System.out.println(s27.loseLife());
    System.out.println(s27.loseLife());
    System.out.println(s27.loseLife());
    System.out.println(s27.loseLife());
    s27.gainLife();
    myLives = s27.getLives();
    System.out.println(myLives);
  }
}
