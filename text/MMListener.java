/*
 * MouseMotionEventDemo.java is a 1.2/1.3/1.4 example
 * 
 * modified Apr 2012
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import java.awt.Point;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

class MMListener implements MouseMotionListener {
    JLabel lab;
    JTextComponent txt; 
    //we need a text component and its parent panel 
    public MMListener(JTextComponent t, Component p) {
        this(t, p, null);
    }
    //... and an optional label for reporting mouse movement
    public MMListener(JTextComponent t, Component pan, JLabel b) {
        txt = t; lab = b;
        txt.addMouseMotionListener(this);
        pan.addMouseMotionListener(this);
    }
    public void mouseMoved(MouseEvent e) {
       if (e.getComponent() != txt) {
           setText("moved out", null); return;
       }
       Point p = e.getPoint();
       int k = txt.viewToModel(p);
       String s = txt.getText();
       if (!Character.isLetter(s.charAt(k))) {
           setText("not a letter", null); return;
       }
       int m = k+1, n = s.length();
       while (m < n  && Character.isLetter(s.charAt(m))) m++;
       while (k >= 0 && Character.isLetter(s.charAt(k))) k--;
       String msg = "Offset "+(k+1)+"-"+m+" at Point: "+p.x+","+p.y;
       setText(msg, s.substring(k+1, m));
    }
    void setText(String msg, String toolTip) {
        if (lab != null) lab.setText(msg);
        txt.setToolTipText(toolTip); 
    }
    public void mouseDragged(MouseEvent e) {
    }
    public static void main(String[] args) {
        MouseMotionEventDemo.createAndShowGUI();
    }
}

class MouseMotionEventDemo extends JPanel {
    JLabel lab;
    JTextArea txt;
    static final int GAP = 10, WIDTH = 40;
    static final String MSG = "Hayat güzeldir senin yanýnda";

    public MouseMotionEventDemo() {
        setLayout(new BorderLayout(GAP, GAP)); 
        setBorder(new EmptyBorder(GAP, GAP, GAP, GAP));
        
        lab = new JLabel(MSG);
        add(lab, "North");
        
        txt = new JTextArea(MSG, 5, MSG.length());
        add(new JScrollPane(txt), "Center");
        txt.setDragEnabled(true);

        //Register for mouse events on txt and this panel.
        new MMListener(txt, this, lab);
    }
    public static void createAndShowGUI() {
        JFrame f = new JFrame("MMListener Demo");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent cp = new MouseMotionEventDemo();
        f.setContentPane(cp);
        f.pack();
        f.setVisible(true);
    }
}
