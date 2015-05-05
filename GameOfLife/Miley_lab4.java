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
public class Miley_lab4 extends JFrame implements ActionListener, MouseListener
{
  int genNumber=0;
  int xCordinate;
  int yCordinate; 
  int liveNeighbours=0;
  int firstGen[][] = new int[100][100];
  int secondGen[][] = new int [100][100];
  boolean clicked=false;

  public Miley_lab4() 
  {
    JButton start = new JButton("Play/Pause");
    start.setSize(100,100);
    start.setLocation(100,600);
    //start.setLayout(null); 
    //start.setVisible(true); 
    this.add(start);
    this.setLayout(null);
    this.setVisible(true);
    this.setSize(600,750); 
    this.setDefaultCloseOperation(EXIT_ON_CLOSE); 
    addMouseListener(this);
    start.addActionListener(this);
    //repaint(); 

  }
  public void actionPerformed(ActionEvent f)
  {
    if (clicked == false)         // clicked is a variable that determines whether the next generations should be calculated or not that is whether the game should continue or not 
    {clicked= true;}              // if clicked is true, continue the program, if already not continued, start it 
                                  // if clicked is false, pause the program and do not play it untill clicked is true again
    else if(clicked == true)
    { clicked = false;} 
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
    if ((a>= 50) && (b >=50) && (a<550) && (b<550) )
    {   
      if (clicked==false)
      {
      xCordinate = (int)(((a-50)/5))   ;
      yCordinate = (int)(((b-50)/5)) ;
      //System.out.println(xCordinate + " " + yCordinate);
      //System.out.println(a + " " + b);

      if (firstGen[yCordinate][xCordinate] ==0)   //if box is not already colored, clicing it will color it 
      {
        firstGen[yCordinate][xCordinate] = 1;
      }
      else if(firstGen[yCordinate][xCordinate]>=1)   // if box is already colored, clicking it will de-color it, that is make it an empty rectangle 
      {
        firstGen[yCordinate][xCordinate]=0;
      }

      repaint();
    } 
    }
  }
  public void paint(Graphics g)
  {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setStroke(new BasicStroke(1)); 
    for (int i=0; i<=firstGen.length-1;i++)
    {
      for (int j=0; j<=firstGen[i].length-1;j++)
      {
        if (firstGen[i][j] > 0)   //how to color boxes depending on their age 
        {
          if (firstGen[i][j]>0 && firstGen[i][j] <11) // if age is > 0 color black
          
          { g2d.setColor(Color.BLACK);}

          if (firstGen[i][j]>10 && firstGen[i][j] <21)
          { g2d.setColor(Color.RED); //System.out.println(firstGen[i][j]); // if age is > 10 , color red 
          }
          if (firstGen[i][j]>20 && firstGen[i][j]<31)
          { g2d.setColor(Color.YELLOW); System.out.println(firstGen[i][j]); } //if age is > 20, color yellow
          if (firstGen[i][j]>30 && firstGen[i][j]<41)
          { g2d.setColor(Color.GREEN);} // if age is > 30 color green
          if (firstGen[i][j]>40 && firstGen[i][j]<51)
          { g2d.setColor(Color.LIGHT_GRAY);} //if age is > 40 color light gray
          if (firstGen[i][j]>50 && firstGen[i][j]<61)
          { g2d.setColor(Color.PINK); }
          if (firstGen[i][j]>60 && firstGen[i][j]<71)
          { g2d.setColor(Color.ORANGE);}
          if (firstGen[i][j]>70 && firstGen[i][j]<81)
          { g2d.setColor(Color.GRAY);}
          if (firstGen[i][j]>80 && firstGen[i][j]<91)
          { g2d.setColor(Color.CYAN);}
          if (firstGen[i][j]>90 && firstGen[i][j]<501)
          { g2d.setColor(Color.MAGENTA); } 
          g2d.fillRect(50+5*j, 50+5*i, 5,5);
          g2d.setColor(Color.BLACK);
          g2d.drawRect(50+5*j, 50+5*i, 5,5);
        }
        if (firstGen[i][j]==0)    //if in the next generation the cell is 0, then make an empty rectangle in its place hence dis-coloring the box 
        {
          g2d.setColor(Color.BLACK);
          g2d.clearRect(50+5*j, 50+5*i, 5,5);
          g2d.drawRect(50 + 5*j, 50 +5*i, 5,5);
        }
      }
    }


  }

//  public void printFirstGen()
  //{
   // for ( int i = 0; i < firstGen.length; i++)
    //{
      //for ( int j = 0; j< firstGen[i].length; j++)
      //{
      //  System.out.print(firstGen[i][j]+ " "); 
      //}
      //System.out.println(""); 
   // }
    //System.out.println("     ");
 // }

  //private void setTrue( int i, int j)
 // {
   // firstGen[i][j] = 1;
 // }


  public void computeNextGen()        // computes the next generation and calls repaint() to show it on the screen
  {
   //   genNumber++;
    for (int i=0; i< firstGen.length; i++)
    { 
      for (int j=0; j<firstGen[i].length; j++)
      {
        if ((j==0) && (i!= 0) && (i!= firstGen.length-1))      //left side 
        {

          if ( firstGen[i][firstGen[i].length -1]>=1)
            liveNeighbours++;
          if (firstGen[i+1][firstGen[i].length-1] >= 1)
            liveNeighbours++;
          if (firstGen[i-1][firstGen[i].length-1] >=1)
            liveNeighbours++;
          if (firstGen[i-1][j]>=1)
            liveNeighbours++;
          if (firstGen[i+1][j]>=1)
            liveNeighbours++;
          if (firstGen[i][j+1]>=1)
            liveNeighbours++;
          if (firstGen[i-1][j+1]>=1)
            liveNeighbours++;
          if (firstGen[i+1][j+1]>=1)
            liveNeighbours++;
        }




        if ((j==firstGen.length-1) && (i!= 0) && (i!= firstGen.length-1))     //right side 
        {
          if ( firstGen[i][0]>=1)
            liveNeighbours++;
          if (firstGen[i-1][0]>=1)
            liveNeighbours++;
          if (firstGen[i+1][0]>=1)
            liveNeighbours++;
          if (firstGen[i][j-1]>=1)
            liveNeighbours++;
          if (firstGen[i+1][j]>=1)
            liveNeighbours++;
          if (firstGen[i-1][j]>=1)
            liveNeighbours++;
          if (firstGen[i-1][j-1]>=1)
            liveNeighbours++;
          if (firstGen[i+1][j-1]>=1)
            liveNeighbours++;
        }
        if ((i==0) &&( j!= 0) && (j!= firstGen[i].length -1))    //top side 
        {
          if (firstGen[firstGen.length-1][j] >=1)
            liveNeighbours++;
          if  (firstGen[firstGen.length-1][j-1]>=1)
            liveNeighbours++;
          if (firstGen[firstGen.length -1][j+1]>=1)
            liveNeighbours++;
          if (firstGen[i+1][j]>=1)
            liveNeighbours++;
          if (firstGen[i+1][j+1]>=1)
            liveNeighbours++;
          if (firstGen[i][j-1]>=1)
            liveNeighbours++;
          if (firstGen[i][j+1]>=1)
            liveNeighbours++;
          if (firstGen[i+1][j-1]>=1)
            liveNeighbours++;

        }


        if ((i==firstGen.length-1) && (j!=0) && (j!=firstGen[i].length-1))  //bottom side
        {
          if (firstGen[0][j]>=1)
            liveNeighbours++;
          if (firstGen[0][j+1] >=1)
            liveNeighbours++;
          if(firstGen[0][j-1]>=1)
            liveNeighbours++;
          if (firstGen[i][j+1]>=1)
            liveNeighbours++;
          if (firstGen[i][j-1]>=1)
            liveNeighbours++;
          if (firstGen[i-1][j-1]>=1)
            liveNeighbours++;
          if (firstGen[i-1][j+1]>=1)
            liveNeighbours++;
          if (firstGen[i-1][j]>=1)
            liveNeighbours++;
        }

        if ((i!= 0) && (i!= firstGen.length -1) && (j!=0) && (j!=firstGen[i].length-1))     //all points not on borders
        {
          if (firstGen[i+1][j+1] >=1 )
            liveNeighbours++;
          if (firstGen[i-1][j-1]>=1)
            liveNeighbours++;
          if (firstGen[i][j+1]>=1)
            liveNeighbours++;
          if (firstGen[i][j-1]>=1)
            liveNeighbours++;
          if (firstGen[i-1][j+1]>=1)
            liveNeighbours++;
          if (firstGen[i+1][j] >=1)
            liveNeighbours++;
          if (firstGen[i-1][j]>=1)
            liveNeighbours++;
          if (firstGen[i+1][j-1]>=1)
            liveNeighbours++;
        }

        if ( (i==0) && (j == 0)) // left top most corner
        {
          if ( firstGen[i][j+1]>=1 )
            liveNeighbours++;
          if (firstGen[i+1][j]>=1)
            liveNeighbours++;
          if (firstGen[i+1][j+1]>=1)
            liveNeighbours++;
          if ( firstGen[firstGen.length-1][0]>=1) 
            liveNeighbours++;
          if (firstGen[firstGen.length-1][j+1]>=1)
            liveNeighbours++;
          if (firstGen[i][firstGen[i].length-1]>=1)
            liveNeighbours++;
          if (firstGen[i+1][firstGen[i].length-1]>=1)
            liveNeighbours++;
          if (firstGen[firstGen.length-1][firstGen[i].length-1]>=1)
            liveNeighbours++;
        }


        if ( (i==0) && (j == firstGen[i].length-1)) // right top most corner 
        {
          if ( firstGen[i][j-1]>=1 )
            liveNeighbours++;
          if (firstGen[i+1][j-1]>=1)
            liveNeighbours++;
          if (firstGen[i+1][j]>=1)
            liveNeighbours++;
          if ( firstGen[firstGen.length-1][0]>=1)
            liveNeighbours++;
          if (firstGen[i][0]>=1)
            liveNeighbours++;
          if (firstGen[i+1][0]>=1)
            liveNeighbours++;
          if (firstGen[firstGen.length-1][j]>=1)
            liveNeighbours++;
          if (firstGen[firstGen.length-1][j -1]>=1)
            liveNeighbours++;
        }


        if (( i==firstGen.length-1) &&( j==0)) // left bottom most corner 
        {
          if ( firstGen[i][j+1]>=1 )
            liveNeighbours++;
          if (firstGen[i-1][j]>=1)
            liveNeighbours++;
          if (firstGen[i-1][j+1]>=1)
            liveNeighbours++;
          if ( firstGen[0][j]>=1)
            liveNeighbours++;
          if (firstGen[0][j+1]>=1)
            liveNeighbours++;
          if (firstGen[0][firstGen.length-1]>=1)
            liveNeighbours++;
          if (firstGen[i][firstGen[i].length-1]>=1)
            liveNeighbours++;
          if (firstGen[i-1][firstGen[i].length-1]>=1)
            liveNeighbours++;
        }


        if ( (i==firstGen.length-1) && (j ==firstGen[i].length-1)) // right bottom most corner 
        {
          if ( firstGen[i][j-1]>=1 )
            liveNeighbours++;
          if (firstGen[i-1][j]>=1)
            liveNeighbours++;
          if (firstGen[i-1][j-1]>=1)
            liveNeighbours++;
          if ( firstGen[i][0]>=1)
            liveNeighbours++;
          if (firstGen[i-1][0]>=1)
            liveNeighbours++;
          if (firstGen[0][j]>=1)
            liveNeighbours++;
          if (firstGen[0][j-1]>=1)
            liveNeighbours++;
          if (firstGen[0][0]>=1)
            liveNeighbours++;
        }

        if (liveNeighbours< 2)    //makes cells dead or alive based on the number of neighbours that they have
        {
          secondGen[i][j] = 0;
        }

        if (liveNeighbours > 3) 

        { 
          secondGen[i][j] = 0; 
        }

        if (firstGen[i][j] >= 1)
        { if (liveNeighbours == 3 || liveNeighbours ==2)

          {
            secondGen[i][j] = 1+  firstGen[i][j];     //this code increments the value of a particular cell who has been alive for more than one generation 
          }
        }
        if (firstGen[i][j] == 0) 
        {
          if (liveNeighbours ==3) 
          { 
            secondGen[i][j] = 1;         
          }

        }
          
        liveNeighbours=0;
      }
    }

    for (int a=0; a<firstGen.length; a++)
    {
      for (int b=0; b<firstGen[a].length; b++)
      {
      //  System.out.print(secondGen[a][b]+ " ");
        firstGen[a][b] = secondGen[a][b];
      }
      //System.out.println("");
    }
    //System.out.println(" ");
    repaint();  
    //System.out.println("THIS SUCKS");
    try
    { Thread.sleep(100);
    }
    catch(InterruptedException ie)
    {
      //System.out.println("You woke me up");
    }


  }



  public static void main(String args[])
  {
    Miley_lab4 m5 = new Miley_lab4();
    while (true)
    {
      try
      { Thread.sleep(200);}                   //checks after every 2 seconds if the action button has been pressed or not 
      catch(InterruptedException id)
      {}

      if (m5.clicked == true)
      {
        m5.computeNextGen();
        //System.out.println("WORKING");
      }

      if (m5.clicked == false); 
      {

        try
        { Thread.sleep(200);}
        catch(InterruptedException id)
        {}


      }


    }
  }
}

