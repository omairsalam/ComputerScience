// QuitAction.java

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;


/**
 * a class to handle the termination of a program associated
 * a Window or Frame or JFrame object.
 */
public class QuitAction extends AbstractAction
{
    Window theFrame;
    
    /**
     * We require a string with the button's label and a
     * reference to a window that will be destroyed.
     * @param s is the string used on the associated JButton
     * @param f is the Window/Frame/JFrame component that will
     * be distroyed when the action is activated.
     */
    public QuitAction(String s, Window f)
    {
        super(s);
        theFrame = f;
    }
    
    /**
     * the method called when the associated JButton is clicked.
     * We pop up a confirmation dialog and if the user says to continue
     * we kill the Java VM - killing all active windows.
     */
    public void actionPerformed( ActionEvent e )
    {
        int ans = JOptionPane.showConfirmDialog(theFrame,
                    "Do you wish to exit the application?",
                    "Confirmation", 
                     JOptionPane.YES_NO_CANCEL_OPTION);
        if (ans == JOptionPane.YES_OPTION)
        { 
            theFrame.setVisible(false);
            theFrame.dispose();
            System.exit(0);
        }
    }
}