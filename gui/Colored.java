//Sample program to show simple AWT components
//Designed as test for Inspector
//10.2.2003 swing
 

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;

public class Colored implements ListSelectionListener {

    JFrame frm;
    JLabel lab;
    JTextArea text;
    JList list;
    static final int GAP = 10, MAX = 12;
    static final Color COL = Color.pink; 
    static final String MSG = "Colored Divider";
    static final Font serif = new Font("Serif", 0, 13);
    
    class Divider extends javax.swing.plaf.basic.BasicSplitPaneDivider {
       Color col;
       public Divider(SplitUI ui) { super(ui); }
       public Color getColor() { return col; }
       public void setColor(Color c) { col = c; }
       public void paint(Graphics g) {
          super.paint(g);
          g.setColor(col); 
          g.fillRect(0, 0, getWidth(), getHeight());
       }
    }
    class SplitUI extends javax.swing.plaf.basic.BasicSplitPaneUI {
       public BasicSplitPaneDivider createDefaultDivider() {
          return new Divider(this);
       }
    }
    
    public Colored() {
        int d = (Frame.getFrames().length == 0)?
          JFrame.EXIT_ON_CLOSE : JFrame.DISPOSE_ON_CLOSE; 
        frm = new JFrame(MSG);
        JComponent p = (JComponent)frm.getContentPane();
        frm.setDefaultCloseOperation(d);
        
        p.setLayout(new BorderLayout(GAP, GAP)); //hgap needed
        p.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
        p.setBackground(COL);
        p.setFont(serif);
        lab = new JLabel(MSG, SwingConstants.CENTER);
        lab.setFont(new Font("Serif", 3, 16));
        p.add(lab, "North");
        text = new JTextArea("JTextArea");
        text.setFont(serif);
        JScrollPane scr1 = new JScrollPane(text);
        scr1.setPreferredSize(new Dimension(140, 100));
        DefaultListModel A = new DefaultListModel();
        A.addElement("JList");
        for (int i = 1; i < MAX; i++)
            A.addElement("List item " + i);
        list = new JList(A);
//        list.setSelectionMode(); Multiple mode by default
        list.setBackground(Color.yellow);
        list.addListSelectionListener(this);
        list.setFont(serif);
        JScrollPane scr2 = new JScrollPane(list);
        scr2.setPreferredSize(new Dimension(112, 100));
        
        JSplitPane sp = new JSplitPane(1, scr1, scr2);
        SplitUI ui = new SplitUI();
        sp.setUI(ui);
        Divider div = (Divider)ui.getDivider();
        div.setColor(COL);
        sp.setBorder(null);
        sp.setContinuousLayout(true);
        sp.setDividerSize(6);
        p.add(sp);

        p.setToolTipText("swing components are wonderful");
        lab.setToolTipText("Really!!");
        list.setToolTipText("A cute colorful list");

        frm.setLocation(200, 0);
        frm.pack(); frm.setVisible(true);
    }
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        System.out.println(list.getSelectedValue());
    }
    public static void main(String[] args) {
        System.out.println("Begin Colored Divider");
        new Colored();
    }
}
