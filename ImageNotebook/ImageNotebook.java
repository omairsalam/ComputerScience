// Modified source code from Doug Szajda's Algorithms class, F 2004

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;

import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

public class ImageNotebook extends JFrame 
  implements ActionListener, MouseListener {

    // GUI stuff
    Container    contentPane = null;
    CanvasPanel  canvas      = null;
    JScrollPane  scrollPane  = null;
    JLabel       label       = null;

    JPanel    buttonPanel = null;
    JButton[] buttonArray = null;
    static final Dimension buttonSize  = new Dimension(80,35);
    
    // Event handling stuff
    Dimension panelDim = null;

    // THE STUDENT'S DOUBLY LINKED LIST HOLDING IMAGES
    ObjectList linkedList = null;
    BufferedImage image = null;

    //==========================================================================
    //*   private createNewButton(Jbutton b)
    //==========================================================================
    private JButton createNewButton(String text)
    {
      JButton b = new JButton(text);
      b.setMinimumSize(buttonSize);
      b.setPreferredSize(buttonSize);
      b.setMaximumSize(buttonSize);
      b.setAlignmentX(Component.CENTER_ALIGNMENT);
      b.setActionCommand(text);
      b.addActionListener(this);
      b.setBackground(Color.darkGray);
      b.setForeground(Color.lightGray);
      b.setBorder(BorderFactory.createCompoundBorder(
                           BorderFactory.createLineBorder(Color.gray),
                           b.getBorder()));

      return b; 
    } // createNewButton
  
    //==========================================================================
    //* public ImageNotebook()
    //* Constructor
    //==========================================================================
    public ImageNotebook()
    {
      super("My Image Notebook");
      final int width = 800;
      setSize(new Dimension(width,575));

      // Initialize main data structures
      linkedList = new ObjectList();

      // the content pane
      contentPane = getContentPane();
      contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

      // create the drawing area
      canvas = new CanvasPanel(this);
      canvas.addMouseListener(this);

      Dimension canvasSize = new Dimension(width,500);
      canvas.setMinimumSize(canvasSize);
      canvas.setPreferredSize(canvasSize);
      canvas.setMaximumSize(canvasSize);
      canvas.setBackground(Color.black);

      // create the JScrollPane and and the JPanel (canvas) to it
      // note that resizing is done in CanvasPanel based on image dims
      scrollPane = new JScrollPane(canvas);

      // Create buttonPanel and fill it
      buttonPanel = new JPanel();
      Dimension panelSize = new Dimension(width,50);
      buttonPanel.setMinimumSize(panelSize);
      buttonPanel.setPreferredSize(panelSize);
      buttonPanel.setMaximumSize(panelSize);
      buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
      /*
      buttonPanel.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.blue),
                            buttonPanel.getBorder()));
      */

    
      buttonArray = new JButton[10];

      buttonArray[0] = createNewButton("Front");
      buttonArray[1] = createNewButton("Prev");
      buttonArray[2] = createNewButton("Next");
      buttonArray[3] = createNewButton("End");
      buttonArray[4] = createNewButton("Insert");
      buttonArray[5] = createNewButton("Append");
      buttonArray[6] = createNewButton("Remove");
      buttonArray[7] = createNewButton("Replace");
      buttonArray[8] = createNewButton("Clear");
      buttonArray[9] = createNewButton("Quit");


      for (int i = 0; i < buttonArray.length; i++)
      {
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(buttonArray[i]);
      }
      buttonPanel.add(Box.createHorizontalGlue()); // why this extra?

      label = new JLabel(linkedList.getCurrentPosition() + " of " +
          linkedList.getLength());
      label.setAlignmentX(Component.CENTER_ALIGNMENT);
      label.setAlignmentY(Component.CENTER_ALIGNMENT);
      /*
      label.setBorder(BorderFactory.createCompoundBorder(
                           BorderFactory.createLineBorder(Color.gray),
                           label.getBorder()));
      */

      //contentPane.add(canvas);
      contentPane.add(scrollPane);
      contentPane.add(label);
      contentPane.add(buttonPanel);

    } // ImageNotebook()

    //==========================================================================
    //* public static void main(String[] args) {
    //==========================================================================
    public static void main(String[] args) {
  
      ImageNotebook notebook = new ImageNotebook();

      notebook.addWindowListener(new WindowAdapter()
      {
        public void windowClosing(WindowEvent e) {
          System.exit(0);
        }
      }
      );

      notebook.pack();
      notebook.setVisible(true);

    } // main


  
    //==========================================================================
    //*   private class MyFilter extends javax.swing.filechooser.FileFilter
    //==========================================================================
    private class MyFilter extends javax.swing.filechooser.FileFilter
    {
      public boolean accept(File file) {
        String filename = file.getName();
        return 
          (filename.endsWith(".gif") || filename.endsWith(".GIF") ||
           filename.endsWith(".jpg") || filename.endsWith(".JPG") ||
           filename.endsWith(".jpeg") || filename.endsWith(".JPEG") ||
           file.isDirectory());
      }
      public String getDescription() {
          return "*.gif\n*.jpg\n*.jpeg\n*.GIF\n*.JPG\n*.JPEG";
      }
    }

    //==========================================================================
    //*   private BufferedImage selectImage()
    //==========================================================================
    private BufferedImage selectImage()
    {
      BufferedImage newImage = null;

      JFileChooser fileChooser = new JFileChooser();
      try {
        File f = new File(new File(".").getCanonicalPath());
        fileChooser.setCurrentDirectory(f);
      } catch (Exception ex) { System.out.println(ex.getMessage()); }

      fileChooser.addChoosableFileFilter(new MyFilter());
  
      int retValue = fileChooser.showOpenDialog(canvas);
  
      if (retValue == JFileChooser.APPROVE_OPTION)
      {
        File imageFile = fileChooser.getSelectedFile();
        if (fileChooser.getName(imageFile).endsWith(".gif") || 
            fileChooser.getName(imageFile).endsWith(".GIF") ||
            fileChooser.getName(imageFile).endsWith(".jpg") || 
            fileChooser.getName(imageFile).endsWith(".JPG") ||
            fileChooser.getName(imageFile).endsWith(".jpeg") || 
            fileChooser.getName(imageFile).endsWith(".JPEG"))
        {
          try {
            newImage = ImageIO.read(imageFile);
          } catch (Exception ex) { System.out.println(ex.getMessage()); }
        }
        else
        {
          JOptionPane.showMessageDialog(canvas, "Must be GIF or JPEG image", 
            "Image Selection Error", JOptionPane.ERROR_MESSAGE);
          return null;
        }
      }
  
      return newImage;

    } // selectImage()

    //======================================================================
    //* public void actionPerformed(ActionEvent e) {
    //======================================================================
    public void actionPerformed(ActionEvent e) {

      String buttonIdentifier = e.getActionCommand();

      if (buttonIdentifier.equals("Front"))
      {
        //System.out.println(buttonIdentifier);
        image = (BufferedImage) linkedList.getFirst();
      } 
      else if (buttonIdentifier.equals("Next"))
      {
        //System.out.println(buttonIdentifier);
        image = (BufferedImage) linkedList.getNext();
      } 
      else if (buttonIdentifier.equals("Prev"))
      {
        //System.out.println(buttonIdentifier);
        image = (BufferedImage) linkedList.getPrevious();
      } 
      else if (buttonIdentifier.equals("End"))
      {
        //System.out.println(buttonIdentifier);
        image = (BufferedImage) linkedList.getLast();
      } 
      else if (buttonIdentifier.equals("Append"))
      {
        //System.out.println(buttonIdentifier);
        BufferedImage newImage = selectImage();
        if (newImage != null)
        {
          linkedList.append(newImage);
          image = newImage;
        }
      }
      else if (buttonIdentifier.equals("Insert"))
      {
        //System.out.println(buttonIdentifier);

        BufferedImage newImage = selectImage();
        if (newImage != null)
        {
          linkedList.insert(newImage);
          image = newImage;
        } 
      }
      else if (buttonIdentifier.equals("Replace"))
      {
        //System.out.println(buttonIdentifier);

        BufferedImage newImage = selectImage();
        if (newImage != null)
        {
          linkedList.replace(newImage);
          image = (BufferedImage) linkedList.getCurrent();
        }
      } 
      else if (buttonIdentifier.equals("Remove"))
      {
        //System.out.println(buttonIdentifier);
        linkedList.remove();
        image = (BufferedImage) linkedList.getCurrent();
      } 
      else if (buttonIdentifier.equals("Clear"))
      {
        //System.out.println(buttonIdentifier);
        linkedList.clear();
        image = null;
      } 
      else if (buttonIdentifier.equals("Quit"))
      {
          System.exit(0);
      } 

      label.setText(linkedList.getCurrentPosition() + " of " +
                    linkedList.getLength());
      canvas.repaint();

    } // actionPerformed 

    //==========================================================================
    //* public void mouseClicked(MouseEvent e) {
    //==========================================================================
    public void mouseClicked(MouseEvent e) {
      label.setText(linkedList.getCurrentPosition() + " of " +
                    linkedList.getLength());
      canvas.repaint();
    }

    //==========================================================================
    //* public void mouseExited(MouseEvent e) {}
    //==========================================================================
    public void mouseExited(MouseEvent e) {}

    //==========================================================================
    //* public void mouseEntered(MouseEvent e) {}
    //==========================================================================
    public void mouseEntered(MouseEvent e) {}

    //==========================================================================
    //* public void mouseReleased(MouseEvent e) {}
    //==========================================================================
    public void mouseReleased(MouseEvent e) {}

    //==========================================================================
    //* public void mousePressed(MouseEvent e) {}
    //==========================================================================
    public void mousePressed(MouseEvent e) {}

    //==========================================================================
    //* public void mouseDragged(MouseEvent e) {}
    //==========================================================================
    public void mouseDragged(MouseEvent e) {}
}



