/*
 * MouseMotionEventDemo.java is a 1.2/1.3/1.4 example
 * 
 * modified Apr 2012
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Document;
import java.awt.Point;
import java.awt.BorderLayout;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

public class MouseMotionEventDemo extends JPanel 
                                  implements MouseMotionListener {
    JLabel lab;
    JTextArea txt;
    Document doc;
    static final String    //newline = "\n", 
        MSG = "hayat güzeldir senin yaninda";
    static final int GAP = 10, WIDTH = 40;

    public MouseMotionEventDemo() {
        setLayout(new BorderLayout(GAP, GAP)); 
        setBorder(new EmptyBorder(GAP, GAP, GAP, GAP));
        setName("Panel");
        
        lab = new JLabel(MSG);
        lab.setName("Label");
        add(lab, "North");
        
        txt = new JTextArea(MSG, 5, WIDTH);
        txt.setName("TextArea");
        //txt.setEditable(false);
        add(new JScrollPane(txt), "Center");
        doc = txt.getDocument();

        //Register for mouse events on blankArea and panel.
        txt.addMouseMotionListener(this);
        addMouseMotionListener(this);
    }

    public void mouseMoved(MouseEvent e) {
       //saySomething("Mouse moved", e);
       if (e.getComponent() == this) lab.setText("Moved out");
       else {
           String s = txt.getText();
           Point p = e.getPoint();
           int k = txt.viewToModel(p);
           int m = s.indexOf(' ', k+1);
           if (m<0) m = s.length();
           lab.setText(p+" --> "+k+" "+m+" --> "+s.substring(k, m));
       }
    }

    public void mouseDragged(MouseEvent e) {
       //saySomething("Mouse dragged", e);
    }

    void saySomething(String dsc, MouseEvent e) {
        String s = dsc + " (" + e.getX() + "," + e.getY() + ") on "
                       + e.getComponent().getName();
        lab.setText(s);
        /*
        txt.append(eventDescription 
                        + " (" + e.getX() + "," + e.getY() + ")"
                        + " detected on "
                        + e.getComponent().getClass().getName()
                        + newline);
        txt.setCaretPosition(txt.getDocument().getLength());*/
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        //JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("MouseMotionEventDemo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new MouseMotionEventDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
