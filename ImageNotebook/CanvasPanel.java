import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;
import java.awt.image.*;

public class CanvasPanel extends JPanel
{
    ImageNotebook parent = null;
    Color currentColor = Color.red;

    public CanvasPanel(ImageNotebook _parent)
    {
	    super();
	    parent = _parent;
    }

    public void paintComponent(Graphics g)
    {
	    super.paintComponent(g);

	    g.setColor(currentColor);

      if (parent.image != null)
      {
        Dimension panelSize = 
          new Dimension(parent.image.getWidth(),parent.image.getHeight());
        this.setPreferredSize(panelSize);
        this.setMaximumSize(panelSize);
        g.drawImage(parent.image,0,0,null);
        parent.scrollPane.revalidate();
      }
    }

} // CanvasPanel
