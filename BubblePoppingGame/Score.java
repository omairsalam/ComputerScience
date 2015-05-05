public interface Score
{
  // add the amount times multiplier to the current score  
  // if passed the next threshold for leveling up, level up!
  // update next level threshold.
  // return the new score.
  public long addPoints(int amount);

  // subract amount from current score
  // multiplier = 1
  // if below threshold/2, level down, update threshold
  // return the new score.
  public long subtractPoints(int amount);

  //return the current multiplier
  public int getMultiplier();

  //change the value of the multiplier to the provided value.
  //All points added from this point on are multiplied by that value
  public void setMultiplier(int mult);

  //increase multiplier by one.
  //return the new value of the multipler
  public int incrementMultiplier();

  //return the current score
  public long getScore();

  //return the current level
  public int getLevel();

  //return the number of remaining lives
  public int getLives();

  //decrement number of lives.
  //return false if out of lives, true otherwise
  public boolean loseLife();

  //increment the number lives.
  public void gainLife();
}
