import java.lang.Math;
import javax.swing.JFrame;
import java.awt.Graphics;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.awt.Cursor;

public class BonusBubblePanel extends JPanel implements Runnable, MouseListener

{ 
  private BufferedImage image;
  private int bonusBubblePanelXcoordinate;
  private int bonusBubblePanelYcoordinate;
  private Timer t = new Timer();
  private  int bubbleAppear;
  private  int b1;
  private int diameter = 50;
  public Score_oa6ci m1; 

  public BonusBubblePanel(Score_oa6ci m2)
  {
    m1 = m2; 
    Random x = new Random();
    Random y = new Random();
    bonusBubblePanelXcoordinate = 100 + x.nextInt(400) ;
    bonusBubblePanelYcoordinate = 100 + y.nextInt(400) ;
    setLocation(bonusBubblePanelXcoordinate,bonusBubblePanelYcoordinate);
    setSize(diameter,diameter);
    setVisible(true);
    addMouseListener(this);
    setOpaque(false); 
    //System.out.println("Bonus Panel at " + bubblePanelXcoordinate + "," + bubblePanelYcoordinate + " ");

    try
    {
      image = ImageIO.read(new File("CokeZero.png"));
    }
    catch (IOException ex)
    {
    }
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
    long a = e.getX();
    long b = e.getY();
    System.out.println(a + ", " + b);
    long centreX = (diameter/2);
    long centreY = (diameter/2);
    if ( (Math.pow(a-centreX,2) + Math.pow(b-centreY,2) ) <= (diameter*diameter/4))
    {
//      System.out.println("CLicked Inside Bonus Circle");
      m1.gainLife();
      m1.getLives(); 
      System.out.println(m1.getLives()); 
//      System.out.println("One more life");
      setVisible(false);
    }
    else
    {
 //     System.out.println("Clicked Outside");
    }
  }


  private class BonusBubbleLife extends TimerTask
  {
    public void run()
    {
      if (diameter>1)
      {
        diameter = diameter - 2;
        setSize(diameter,diameter);
        bonusBubblePanelXcoordinate++;
        bonusBubblePanelYcoordinate++;
        setLocation(bonusBubblePanelXcoordinate, bonusBubblePanelYcoordinate);
        //    System.out.println("Diameter of Bonus Panel is" + " " + diameter);
        repaint();
      }
      else 
      {
        t.cancel();
        //System.out.println("Bonus Bubble Died");
        repaint(); 
      }


    }
  }

  public void run()
  {
    //  Score_oa6ci m1 = new Score_oa6ci();
    b1 = m1.getLevel();
 //   System.out.println(m1.getLevel());
    //System.out.println("Starting Bonus bubble");
    repaint();
    t.schedule(new BonusBubbleLife(),0, (long)(((2000 - (b1*100))/2)/25)) ;
  }

  public void paint(Graphics g)
  {
    super.paintComponent(g);

      g.drawImage(image,0,0, diameter, diameter, null);


    // g.setColor(Color.MAGENTA);
      // g.fillOval(0,0,diameter+1,diameter+1);
    //   g.setColor(Color.MAGENTA);
     //  g.fillOval(0,0,diameter,diameter);
  }
}     



















