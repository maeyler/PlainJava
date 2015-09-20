
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;

public class TestHTML extends MouseAdapter 
    implements MouseMotionListener, ActionListener {

    final static String MSG = "Test HTML";
    final static String HEAD = "<FONT size=5 FACE=me_quran><center>";
    JTextArea text;
    JEditorPane html;
    JLabel lab;
    JButton but;
    JFrame frm;
    String word;
    int selB, selE;
    
    final static Font 
      ARIAL = new Font("SansSerif", 0, 14),  
      SMALL = new Font("SansSerif", 0, 12);    
    final static Cursor 
      MOVE = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR),
      HAND = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR),
      TEXT = Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
    final static int GAP = 4;
    final static Color COL = Color.cyan;
    final static Font font = new Font("Serif", 0, 14);
    
    public TestHTML() {
        int d = (Frame.getFrames().length==0)?
           JFrame.EXIT_ON_CLOSE : JFrame.DISPOSE_ON_CLOSE;

        JPanel pan = new JPanel();
        pan.setLayout(new BorderLayout(GAP, GAP-4)); 
        pan.setBorder(new EmptyBorder(GAP, GAP, GAP, GAP));
        pan.setBackground(COL);
        pan.setFont(font);
        lab = new JLabel(MSG);  //, SwingConstants.CENTER);
        lab.setName("title");
        lab.setFont(new Font("Serif", 3, 16));
        //lab.setForeground(Color.black);
        pan.add(lab, "North");
        
        text = new JTextArea("JTextArea\n");
        text.setName("area");
        text.setLineWrap(true);
        text.setFont(font);
        JScrollPane scr1 = new JScrollPane(text);
        scr1.setPreferredSize(new Dimension(300, 500));
        pan.add(scr1, "Center");

        html = new JEditorPane();
        html.setContentType("text/html");
        html.setEditable(false);
        html.addMouseListener(this);
        html.addMouseMotionListener(this);
        JScrollPane scr2 = new JScrollPane(html);
        scr2.setPreferredSize(new Dimension(350, 500));
        pan.add(scr2, "East");
        
        but = new JButton("Copy text from JTextArea to JEditorPane");
        but.addActionListener(this);
        pan.add(but, "South");

        frm = new JFrame(MSG);
        frm.setContentPane(pan); 
        frm.setDefaultCloseOperation(d);
        frm.setLocation(100, 150);
        frm.pack(); frm.setVisible(true);
    }
    public void setMessage(String s) {
        lab.setText(s); 
    }
    public void setMessage(String s, Cursor c) {
        setMessage(s); html.setCursor(c); 
    }
    public void setHTML(String s) {
        html.setText(HEAD+s); 
    }
    public void actionPerformed(ActionEvent e) {
        setHTML(text.getText());
    }
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() != 1) return;
        //String s = html.getToolTipText();
        if (word == null) return; 
        setMessage(word);
        String s = text.getText();
        String h = s.substring(0, selB)+" <FONT color=blue> " 
            + s.substring(selB, selE) +" </FONT> "+s.substring(selE);
        setHTML(h);
    }
    public void mouseExited(MouseEvent e) {
        setMessage(" "); //moved out
        setHTML(text.getText());
        word = null;
    }
    public void mouseMoved(MouseEvent e) {
       int k = html.viewToModel(e.getPoint());
       if (html.hasFocus() && html.getSelectionStart()<=k && k<html.getSelectionEnd()) {
           setMessage("(on selection)", MOVE); return;
       }
       String s = text.getText(); //"";
       int m = s.length();   //html.getDocument().getLength(); 
       /*try {
           s = html.getText(0, m);
       } catch (BadLocationException x) {
	   setMessage("BadLocation "+m, TEXT); return;
       } */  
       if (!Character.isLetter(s.charAt(k))) {
           setMessage("(not a letter)", TEXT); return;
       }
       selB = k; selE = k+1;
       while (!Character.isWhitespace(s.charAt(selB-1))) selB--;
       while (!Character.isWhitespace(s.charAt(selE))) selE++;
       setMessage(selB+"-"+selE, HAND);
       word = ""; 
       for (int i=selB; i<selE; i++)
           if (Character.isLetter(s.charAt(i))) 
               word += s.charAt(i);
       html.setToolTipText(word);
    }
       public static void main(String[] args) {
         System.out.println("Begin TestHTML");
         new TestHTML();
    }
}
