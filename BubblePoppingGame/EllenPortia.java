import java.lang.Thread;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLayeredPane;
import java.util.Random;
import javax.swing.JLabel; 
import javax.swing.JButton;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.lang.Math;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*; 
import java.io.IOException; 
import java.net.URL;
import java.applet.Applet;
import java.applet.AudioClip;

public class EllenPortia extends JFrame implements MouseListener
{
  public static boolean gameOver = false;
  private Score_oa6ci m1;
  Toolkit toolkit = Toolkit.getDefaultToolkit();
  Image image = toolkit.getImage("mouseClick.png");
  Point hotSpot = new Point(0,0);
  Cursor cursor = toolkit.createCustomCursor(image, hotSpot, "mouseClick.png");
  public EllenPortia(Score_oa6ci m2)
  {
    m1 = m2;
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(900,700);
    setVisible(true);
    setLayout(null);
    addMouseListener(this);
    setCursor(cursor);

  }

  public static void main(String args[])
  {

    Score_oa6ci m1 = new Score_oa6ci() ;
    EllenPortia ep = new EllenPortia(m1);
    Random r = new Random();
    int b2 = m1.getMultiplier();
    Random s = new Random();
    // PlayingField pf = new PlayingField(m1);
    JLayeredPane jp = new JLayeredPane();
    JLabel scoreLabel = new JLabel();
    JLabel levelLabel = new JLabel(String.valueOf(m1.getLevel()));
    JLabel livesLabel = new JLabel();
    JLabel multiplierLabel = new JLabel(); 
    JLabel gameOverLabel = new JLabel();
    scoreLabel.setOpaque(false);
    levelLabel.setOpaque(false);
    gameOverLabel.setFont(levelLabel.getFont().deriveFont(25.0f));
    levelLabel.setFont(levelLabel.getFont().deriveFont(15.0f));
    scoreLabel.setSize(200,50);
    scoreLabel.setFont(levelLabel.getFont().deriveFont(15.0f));
    livesLabel.setSize(150,50);
    livesLabel.setFont(levelLabel.getFont().deriveFont(15.0f));
    multiplierLabel.setSize(100,50);
    levelLabel.setSize(100,50);
    multiplierLabel.setFont(levelLabel.getFont().deriveFont(15.0f));
    scoreLabel.setLocation(50,550);
    levelLabel.setLocation(200,550);
    livesLabel.setLocation(300,550);
    multiplierLabel.setLocation(450,550);

    URL url = EllenPortia.class.getResource("wav_file.wav");
    AudioClip clip = Applet.newAudioClip(url);
    clip.play();


        try {
        ep.setContentPane(new JLabel ( new ImageIcon(ImageIO.read(new File("background.png")))));
        }
        catch(IOException iiff) 
        {
        }
       ep.pack();
       ep.setVisible(true); 

    jp.setSize(700,700);
    jp.setLocation(0,0);
    ep.add(jp);
    ep.add(scoreLabel); 
    ep.add(levelLabel);
    ep.add(livesLabel);
    ep.add(multiplierLabel);
    jp.setVisible(true);
    ep.repaint();

    for (int i=0;; i++)
    {
      if (((i%(30 + (s.nextInt(30))))==0) && (i!=0))
      {
        BonusBubblePanel bb = new BonusBubblePanel(m1);
        Thread t = new Thread(bb);
        t.start();
        bb.setVisible(true);
        jp.add(bb,i);
        scoreLabel.setText("Score is " + String.valueOf(m1.getScore()));
        levelLabel.setText("Level is " + String.valueOf(m1.getLevel()));
        livesLabel.setText("Lives remaining " + String.valueOf(m1.getLives()));
        multiplierLabel.setText("Multiplier is " +String.valueOf(m1.getMultiplier()));




      }
      else  
      {
        BubblePanel bp = new BubblePanel(m1);
        bp.setVisible(true);
        jp.add(bp,i);
        Thread t = new Thread(bp);
        t.start();
        scoreLabel.setText("Score is " + String.valueOf(m1.getScore()));
        levelLabel.setText("Level is " + String.valueOf(m1.getLevel()));
        livesLabel.setText("Lives remaining " + String.valueOf(m1.getLives()));
        multiplierLabel.setText("Multiplier is " +String.valueOf(m1.getMultiplier()));
        if (m1.getLives()==0)
        {
          gameOver = true; 
          try
          {
            Thread.sleep(3000);
            ep.repaint();
            gameOverLabel.setText("PORTIA HAS STORMED AWAY");
            gameOverLabel.setLocation(120,230);
            gameOverLabel.setSize(500,150);
            gameOverLabel.setOpaque(false);
            // System.out.println("Code not working");
            ep.add(gameOverLabel);
          }
          catch(InterruptedException iid)
          {
          }
          try
          {
            Thread.sleep(3000);
            System.exit(0);
          }
          catch(InterruptedException iid)
          {
          }

        }


      }
      try
      {
        int b1 = m1.getLevel();
        int bubbleAppear = 1000 - (int)(b1*50) + r.nextInt(1000) -500;
        Thread.sleep(bubbleAppear);
      }
      catch(InterruptedException ie)
      {
      }

      ep.repaint();
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

    m1.setMultiplier(1);
    //System.out.println("Clicked Outside and Points subtracted");
    m1.subtractPoints(250);
    m1.getLevel();
    //System.out.println(m1.getScore());

  }

  public void paintComponent(Graphics g)
  {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Color.WHITE);
    if (gameOver==true)
    {
      g2d.fillRect(50,50,600,600);
    }
  }


}

