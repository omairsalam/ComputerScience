//ViewFrame.java
// An extension of java.swing.JFrame to provide an input/output
// window for programs in CMSC 150
// Copyright 1999 by Joseph F. Kent

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A class for displaying program output and supporting
 * input via dialog windows. The class extends java.swing.JFrame
 * and is based on Java 1.2.
 * @copyright July, 1999
 * @author J. F. Kent
 * @version 1.0 alpha
 * <p>
 * An object of this class has three
 * major components:
 * <ul>
 *  <li>a toolbar for action buttons, initially containing a "Quit"
 *  button;
 *  <li>a text area for displaying output;
 *  <li>an optional Canvas component supplied by the creator of 
 *  the object which allows for non-textual output such as drawings
 *  and/or images.
 * </ul>
 * <p>
 * An object-oriented paradigm developed in Smalltalk was
 * known as MVC for Model-ViewFrame-Controller. A program could be
 * understood as having three components, some of which could be 
 * combined:
 * <ul>
 * Model: the data and state information<br>
 * ViewFrame:  the visual component that displays information associated
 *        with the data. A program might have several ViewFrames of the data.<br>
 * Controller: the part of the program that interacts with the user and
 *        may cause changes to the model. 
 * </ul>
 * The ViewFrame and Controller are
 * often combined in a single visual component with buttons and/or
 * menus. <b>This is the most common use of this class.</b>
 */
public class ViewFrame extends JFrame
{
    protected Canvas theCanvas = null;
    protected JToolBar theToolBar = null;
    protected JTextArea theTextArea = null;
    protected JScrollPane theScrollPane = null;
    protected boolean  inputToOutputEcho = false;
    
    private Container contentPane;
    
    // used to set preferred minimum sizes
    public static final int WIDTH  = 640;
    public static final int HEIGHT = 480;
    
    
    /**
     * Uses s as the title of the window and c
     * as the canvas component
     * @parameter s is the optional title of the JFrame
     * @parameter c is the optional Canvas object provided by the user
     */
    public ViewFrame(String s, Canvas c)
    {
        super(s);
        //addWindowListener( new HandleWindowDeath() );
        // code to modify the LookAndFeel to "Windows". Default is "Metal"
        /*
        try
        {
           // UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch(UnsupportedLookAndFeelException ex1)
        {
            System.err.println("Unsupported Look and Feel\n" + ex1);
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
        */
        
        contentPane = getContentPane();
        theTextArea = new JTextArea(6, 40);
        theTextArea.setMinimumSize(new Dimension(ViewFrame.WIDTH/2, ViewFrame.HEIGHT/4));
        theScrollPane = new JScrollPane( theTextArea );
        JViewport jv1 = new JViewport();
        jv1.setView(new JLabel("Program Output"));
        theScrollPane.setColumnHeader(jv1);
        theCanvas = c;
        theToolBar = new JToolBar();
        theToolBar.add(new QuitAction("Exit",this));
        contentPane.add( theToolBar, BorderLayout.NORTH);
        contentPane.add( theScrollPane,  BorderLayout.CENTER);
        if (theCanvas != null )
        {
            contentPane.add( theCanvas, BorderLayout.SOUTH);
        }
        setSize(ViewFrame.WIDTH,ViewFrame.HEIGHT);
    }
    
    /**
     * Uses title "ViewFrame" and has no Canvas component
     */
    public ViewFrame()
    {
        this("ViewFrame ", null);
    }
    
    /**
     * No Canvas component, s is title
     */
    public ViewFrame(String s)
    {
        this(s, null);
    }
    
    /**
     * Uses default title of "ViewFrame"
     * @parameter c is the specified Canvas component
     */
    public ViewFrame(Canvas c)
    {
        this("ViewFrame", c);
    }
    
    /**
     * Accesses the boolean flag that indicates whether or not
     * input from dialogs is echoed to the output text area.
     * The default value of this flag is false.
     */
    public boolean getIOEcho()
    {
        return inputToOutputEcho;
    }
    
    /**
     * sets the value of the boolean flag that indicates whether
     * input from dialogs is echoed to the output text area.
     */
    public void setIOEcho(boolean b)
    {
        inputToOutputEcho = b;
    }
    
    /**
     * Provide a replacement or new Canvas component to the JFrame
     */
    public void setCanvas(Canvas c )
    {
        if (c == null) return;
        if (theCanvas != null)
        {
            contentPane.remove(theCanvas);
        }
        theCanvas = c;
        contentPane.add(theCanvas, BorderLayout.SOUTH);
        // force the change to appear
        validate();
    }
        
    /**
     * obtain a reference to the (possibly null) Canvas component
     */
    public Canvas getCanvas()
    {
        return theCanvas;
    }
    
    /**
     * Clears all output text from the output text area
     */
    public void clearOutputArea()
    {
        theTextArea.setText("");
    }
    
    
    /**
     * clears the Canvas component if it is defined.
     * <p> ALERT:
     * This may not work with extensions to Canvas which modify
     * the paint() method as resizing the window causes a call to
     * repaint() and ultimately to paint().
     */
    public void clearCanvas()
    {
        if (theCanvas == null) return;
        
        Dimension d =theCanvas.getSize();
        Graphics g = theCanvas.getGraphics();
        g.clearRect(0,0,d.width,d.height);
        
    }


    /**
     * provides a way to add a button to the toolbar component.
     * @param a is an extension of the AbstractAction class
     */
    public void addActionButton( AbstractAction a)
    {
        theToolBar.add(a);
        this.validate();
    }
                             


    /**
     * sends a string to the output text area (a JTextArea) followed
     * by a carriage return.
     * @param s is String to be displayed
     */
    public void println(String s)
    {
        print(s + "\n");
        
    }
    
    
    /**
     * sends a string to the output text area without appending
     * a carriage return.
     * Scrolling is crudely adjusted so that the line is visible
     * even when the new string might not be in the visible
     * part of the output text area.
     * @param s is the string to be displayed
     */
    public void print(String s)
    {
        theTextArea.append(s);
        // we wish to be sure that the scrollbar is set at the
        // bottom if it exists.
        JScrollBar vsb = theScrollPane.getVerticalScrollBar();
        if (vsb == null)
        {
            System.out.println("No scroll bar");
        }
        else
        {
            int max = vsb.getMaximum();
            vsb.setValue(max);
        }
    }

    /**
     *  reads an integer via a pop-up dialog.
     * If non-integer data is entered, an error dialog is
     * displayed and the request repeated until a proper
     * value is entered.
     * @param prompt is a string that appears in the pop-up dialog.
     * @return the numerical value entered in the dialog window.
     */
    public int readInt(String prompt)
    {
        int v = 0;
        String s = JOptionPane.showInputDialog(this, prompt,
            "readInt Dialog", JOptionPane.PLAIN_MESSAGE);
        while ( s == null )
        {
            JOptionPane.showMessageDialog(null, "You must enter an integer!",
                "ERROR", JOptionPane.ERROR_MESSAGE);
             s = JOptionPane.showInputDialog(this, prompt,
                    "readInt Dialog", JOptionPane.PLAIN_MESSAGE);
        }
        // check to see if s is an integer
        s = s.trim();
        try {
            v = Integer.parseInt(s);
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(null, "Not an integer!",
                "ERROR", JOptionPane.ERROR_MESSAGE);
            return readInt(prompt);
        }
        if (inputToOutputEcho) println("input> " + s);
        return v;
    }
    
    /**
     *  reads an integer via a pop-up dialog.
     * If non-integer data is entered, an error dialog is
     * displayed and the request repeated until a proper
     * value is entered.
     * A default prompt of "Enter an integer" is used.
     * @return the numerical value entered in the dialog window.
     */

    public int readInt()
    {
        return readInt("Enter an integer");
    }
    
    /**
     *  reads a double via a pop-up dialog.
     * If non-floating point data is entered, an error dialog is
     * displayed and the request repeated until a proper
     * value is entered.
     * @param prompt is a string that appears in the pop-up dialog.
     * @return the numerical value entered in the dialog window.
     */
    public double readDouble(String prompt)
    {
        double v = 0.0;
        String s = JOptionPane.showInputDialog(this, prompt,
            "readDouble Dialog", JOptionPane.PLAIN_MESSAGE);
        while ( s == null )
        {
            JOptionPane.showMessageDialog(null, "You must enter a number!",
                "ERROR", JOptionPane.ERROR_MESSAGE);
             s = JOptionPane.showInputDialog(this, prompt,
                    "readDouble Dialog", JOptionPane.PLAIN_MESSAGE);
        }
        // check to see if s is an double
        s = s.trim();
        try {
            v = Double.parseDouble(s);
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(null, "Not a number!",
                "ERROR", JOptionPane.ERROR_MESSAGE);
            return readDouble(prompt);
        }
        if (inputToOutputEcho) println("input> " + s);
        return v;
    }
    
    /**
     *  reads a double via a pop-up dialog.
     * If non-floating point data is entered, an error dialog is
     * displayed and the request repeated until a proper
     * value is entered.
     * The default prompt "Enter a number" is used.
     * @return the numerical value entered in the dialog window.
     */

    public double readDouble()
    {
        return readDouble("Enter a number");
    }

    /**
     * reads in a string value via a pop-up dialog
     * @param prompt is the message string in the dialog
     * @return the string entered in the dialog window.
     */
    public String readString(String prompt)
    {
        String s = JOptionPane.showInputDialog(this, prompt,
            "readString Dialog", JOptionPane.PLAIN_MESSAGE);
        if (inputToOutputEcho) println("input> " + s);
        return s;
    }
    
    /**
     * reads in a string value via a pop-up dialog
     * The default prompt "Enter a string" is used.
     * @return the string entered in the dialog window.
     */
    public String readString()
    {
        return readString("Enter a string");
    }
    

    /**
     * displays a warning or error message in a pop-up
     * window with an Ok button. 
     * @param s is message displayed
     */
     public void showWarningMsg( String s )
    {
        JOptionPane.showMessageDialog(this, s, "Warning",
            JOptionPane.WARNING_MESSAGE);
    }
    
    
}


    
        