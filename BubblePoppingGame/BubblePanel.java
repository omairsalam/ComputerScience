import java.lang.Math;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;
import java.util.Random;
import javax.swing.JPanel; 
import java.lang.Thread;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.awt.Cursor; 
import java.applet.*; 
import java.awt.event.*; 
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.applet.Applet;
import java.applet.AudioClip;

public class BubblePanel extends JPanel implements Runnable, MouseListener 
{

  private BufferedImage image; 
  private BufferedImage image2;
  public static int streak;
  public static int bubblesExploded;
  public   int threadNumber;
  private int bubblePanelXcoordinate; 
  private int bubblePanelYcoordinate;
  private Timer t = new Timer();
  private  int b1;
  private int diameter=10;
  public int pointsAdded;
  public int pointsSubtracted;
  public Score_oa6ci m1;


  URL url = EllenPortia.class.getResource("mouseClick.wav");
  AudioClip clip = Applet.newAudioClip(url);


  public BubblePanel(Score_oa6ci m2)
  {

    try 
    {
      image = ImageIO.read(new File("CocaCola.png"));
    }
    catch (IOException ex)
    {
    }



    try
    {
      image2 = ImageIO.read(new File("CokeZero.png"));
    }
    catch (IOException ex)
    {
    }

    m1 = m2;
    Random x = new Random();
    Random y = new Random();
    bubblePanelXcoordinate = 100 + x.nextInt(400) ;
    bubblePanelYcoordinate = 100 + y.nextInt(400) ;
    //    setLocation(bubblePanelXcoordinate,bubblePanelYcoordinate);
    setSize(diameter,diameter);
    setVisible(true);
    addMouseListener(this);
    setOpaque(false);
    setBackground(Color.RED);

    //System.out.println("Panel at " + bubblePanelXcoordinate + "," + bubblePanelYcoordinate + " ");
    //  BubbleTimer bt = new BubbleTimer();
    //  bt.start();

  }


  public void mouseExited(MouseEvent e)
  {
  }
  public void mouseClicked(MouseEvent e)
  {
  }

  public void mouseEntered(MouseEvent e)
  {
  }

  public void mouseReleased(MouseEvent e)

  {
  }
  public void mousePressed(MouseEvent e)
  {
    int b1 = m1.getLevel();
    long a = e.getX();
    long b = e.getY();
    //    System.out.println(a + ", " + b); 
    long centreX = (diameter/2);
    long centreY = (diameter/2);
    if ( (Math.pow(a-centreX,2) + Math.pow(b-centreY,2) ) <= (diameter*diameter/4))
    {
      clip.play();
      if (diameter<65)
      {
        streak++;
      }
      else 
      {
        streak =0;
        m1.setMultiplier(1); 
      }

      if (diameter>95)
      {
        pointsAdded =(5000*(b1+1));
        m1.addPoints(pointsAdded);
        //    System.out.println(m1.getScore()); 
      }
      else {
        pointsAdded = (100 - diameter + 10)*25*(b1+1);
        m1.addPoints(pointsAdded);
        m1.getLevel();
        //   System.out.println(" Total Score is "+ m1.getScore()); 
      }
      //  System.out.println(pointsAdded + " by clicking bubble");
      setVisible(false);
      //  System.out.println("CLicked Inside Circle");
      t.cancel();
    }
    else
    {
      streak=0;
      m1.setMultiplier(1);
      //  System.out.println("Clicked Outside and Points subtracted"); 
      m1.subtractPoints(250);
      m1.getLevel();
      //  System.out.println(m1.getScore()); 
    }

    if ((streak%5==0)  && (streak>0))
    {
      m1.incrementMultiplier();
    }

    // System.out.println("The streak is " + streak);
    // System.out.println("The multiplier is " + m1.getMultiplier());
  }

  private class BubbleLife extends TimerTask
  {
    public void run()
    {
      if (diameter<=100)
      {
        diameter = diameter + 2;
        setSize(diameter,diameter);
        bubblePanelXcoordinate--;
        bubblePanelYcoordinate--;
        setLocation(bubblePanelXcoordinate,bubblePanelYcoordinate);
        //   System.out.println("Diameter of " + threadNumber + " is" + " " + diameter);
        repaint();
      }
      else
      { 
        //:    System.out.println("Bubble " + threadNumber + " Died");
        setVisible(false);
        pointsSubtracted = 2500*(b1+1);
        m1.subtractPoints(pointsSubtracted);
        //       System.out.println(pointsSubtracted + " have been lost");
        //      System.out.println(m1.getScore());
        m1.getLevel(); 
        bubblesExploded++;
        //   System.out.println("Bubble Exploded");
        if (((m1.getLevel()+ 1) - bubblesExploded) <= 0) 
        {
          m1.loseLife();
          m1.getLives();
          //    System.out.println("One life lost");

          //  if (m1.getLives()==0)
          //  {
          //    System.exit(0); 
          //  }

        }
        streak=0;
        m1.setMultiplier(1);
        //    System.out.println(m1.getLives()); 
        repaint();
        t.cancel();
      }
    }
  }




  public void run()
  {
    b1 = m1.getLevel();
    //   System.out.println(b1); 
    //  System.out.println(m1.getLevel());
    //  System.out.println("Starting bubble");
    t.schedule(new BubbleLife(),100,(long)((2000 - (b1*100))/45));   //time for which bubble lives
    repaint(); 
  }
  public void paint(Graphics g)
  {
    //  Random z = new Random();
    //  Graphics2D g2d = (Graphics2D) g;
    super.paintComponent(g);

    g.drawImage(image,0,0, diameter, diameter, null);

    // int color = z.nextInt(6);
    //  if (color==1)
    //  { g2d.setColor(Color.GREEN);}
    //   if (color==2)
    //   { g2d.setColor(Color.RED);}
    //   if (color==3)
    //    {g2d.setColor(Color.WHITE);}
    //  g2d.setBackground(Color.RED);
    //   g2d.setColor(Color.BLUE);
    //    g2d.fillOval(0,0,diameter,diameter);
  }
}
