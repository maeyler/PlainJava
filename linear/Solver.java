import number.Number;
import number.Factory;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;

public class Solver implements Runnable {

    enum Mode { none, exch, mult, addR }
    
    static final String 
        MSG  = "Linear Equation Solver -- V1.1 May 2016",
        TXT1 = "e: exchange  m: multiply  a: add row  s: solve",
        EXCH = "exchange row ",
        MULT = "multiply row ",
        ADDR = "add to row ",
        TXT2 = "x row";
    static final Toolkit TK = Toolkit.getDefaultToolkit();
    static final int 
        RESOLUTION = TK.getScreenResolution();
    static final float 
        RES_RATIO = RESOLUTION/96f;  //default resolution is 96
    static final int H = scaled(30), W = scaled(80), GAP = scaled(8);
    static final Font LARGE = scaledFont("Dialog", 0, 16);
    static final Font NORMAL = scaledFont("Dialog", 0, 13);
    static final Font SMALL = scaledFont("Dialog", 0, 10);
    
    final Matrix mat, adj;
    final JTable tab;
    final Ear ear = new Ear();
    final JFrame frm = new JFrame("Solver");
    final JLabel msg = new JLabel(MSG);
    final JButton but = new JButton("New");
    final JButton inv = new JButton("Inverse");
    final JLabel lab1 = new JLabel(TXT1);
    final JLabel lab2 = new JLabel(TXT2);
    final JTextField txt1 = new JTextField();
    final JTextField txt2 = new JTextField();
    final JLabel det = new JLabel();
    Mode mode;  int curRow = -1;
    
    public Solver() { this(W, H); }
    public Solver(int x, int y) { this(Matrix.fromCB(), x, y); }
    public Solver(Matrix m, int x, int y) {
        mat = m;
        adj = (mat.isSquare()? m.adjoinID() : null);
        JPanel pan = new JPanel(new BorderLayout(GAP, GAP));
        
        tab = new JTable(mat);
        tab.setRowHeight(H);
        tab.setFont(LARGE);
        JTableHeader head = tab.getTableHeader();
        head.setFont(NORMAL);
        head.setReorderingAllowed(false);
        //tab.setFillsViewportHeight(true);
        tab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JLabel cr = (JLabel)tab.getCellRenderer(0, 0);
        cr.setHorizontalAlignment(JLabel.CENTER);

        JScrollPane scr = new JScrollPane(tab);
        Dimension d = head.getPreferredSize();
        //System.out.println(d);
        int w = mat.getColumnCount() * W;
        int h = mat.getRowCount() * H + (d.height+3);
        scr.setPreferredSize(new Dimension(w, h));
        tab.addKeyListener(ear);
        tab.addMouseListener(ear);
        pan.add(scr, "Center");
        
        pan.add(topPanel(), "North");
        pan.add(bottomPanel(), "South");
        
        pan.setBorder(new EmptyBorder(GAP, GAP, GAP, GAP));
        tab.setToolTipText("Coefficients");
        det.setToolTipText("The result");

        frm.setContentPane(pan); 
        frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frm.setLocation(x, y);
        frm.pack(); 
        setMode(Mode.none);  //displayFields(false);
        frm.setVisible(true);
    }
    JPanel topPanel() {
        JPanel p = new JPanel();
        
        msg.setFont(NORMAL);
        msg.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(msg);
        
        but.setFont(SMALL);
        but.addActionListener(ear);
        p.add(but);
        
        inv.setFont(SMALL);
        inv.addActionListener(ear);
        if (mat.isSquare()) p.add(inv);
        return p;
    }
    JPanel composite() {
        JPanel p = new JPanel();
        
        lab1.setFont(SMALL);
        p.add(lab1);
        txt1.setFont(SMALL);
        txt1.setColumns(3);
        txt1.addActionListener(ear);
        txt1.addKeyListener(ear);
        p.add(txt1);
        
        lab2.setFont(SMALL);
        p.add(lab2);
        txt2.setFont(SMALL);
        txt2.setColumns(3);
        txt2.addActionListener(ear);
        txt2.addKeyListener(ear);
        p.add(txt2);
        return p;
    }
    JPanel bottomPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        
        p.add(composite());
        p.add(Box.createHorizontalGlue());
        
        det.setText(mat.toString());
        det.setFont(NORMAL);
        det.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(det);
        //p.add(Box.createHorizontalStrut(H));
        
        return p;
    }
    void delay(int msec) {
        try { Thread.sleep(msec);
        } catch (InterruptedException e) {}
    }
    public void run() {
        boolean done = false;
        int k = 0; 
        while (!done) {
            delay(1500);
            done = mat.forward(k);
            tab.getSelectionModel().setSelectionInterval(k, k);
            k++; display();  //tab.repaint();
        }
        if (mat.getRowCount() == mat.getColumnCount()) return;
        delay(1500); mat.backward(); display();
    }
    public void solve() {
        System.out.println("Begin Solver");
        new Thread(this).start(); 
    }
    public void display() {
        det.setText("|A| = "+mat.det); tab.repaint(); 
    }
    void report() {
        int i = tab.getSelectedRow(); int j = tab.getSelectedColumn();
        System.out.printf("row:%s, col:%s\n", i, j);
    }
    void hideFields() {
        txt1.setVisible(false); txt2.setVisible(false); 
        lab2.setVisible(false); tab.requestFocus(); 
    }
    void displayText1() {
        txt1.selectAll(); txt1.setVisible(true); txt1.requestFocus();
    }
    void displayText2(boolean focus) {
        txt2.selectAll(); txt2.setVisible(true); 
        if (focus) txt2.requestFocus();
        else lab2.setVisible(true); 
    }
    void setMode(Mode m) {
        if (mode == m) return; mode = m; 
        curRow = tab.getSelectedRow();
        if (m == Mode.none || curRow == -1) {
            lab1.setText(TXT1);
            hideFields();
        } else if (m == Mode.exch) {
            lab1.setText(EXCH+curRow+" <=> row");
            displayText2(true); 
        } else if (m == Mode.mult) {
            lab1.setText(MULT+curRow+" by ");
            displayText1(); 
        } else if (m == Mode.addR) {
            lab1.setText(ADDR+curRow+"  "); 
            displayText1(); displayText2(false);
        }
    }
    Number numFromField1() {
        Number k = Factory.parseNumber(txt1.getText());
        if (k != null || k.value() != 0) return k; 
        TK.beep(); return null;
    }
    int intFromField2() {
        Number n = Factory.parseNumber(txt2.getText());
        int j = (n == null? -1 : (int)n.value());
        if (j >= 0 && j != curRow && j < mat.M) return j; 
        TK.beep(); return -1;
    }
    void doAction() {
        if (mode == Mode.exch) {
            int j = intFromField2();
            if (j < 0) return;
            mat.exchange(curRow, j); 
        } else if (mode == Mode.mult) {
            Number k = numFromField1();
            if (k == null) return; 
            mat.multiply(curRow, k); 
        } else if (mode == Mode.addR) {
            Number k = numFromField1();
            int j = intFromField2();
            if (k == null || j < 0) return; 
            mat.addRow(curRow, k, j);
        }
        setMode(Mode.none); display();
    }

    class Ear extends MouseAdapter implements ActionListener, KeyListener {
        public void keyTyped(KeyEvent e) { 
            char c = e.getKeyChar();
            if (mode == Mode.none) switch (c) {
              case 'e': case 'x': setMode(Mode.exch); break;
              case 'm': case '*': setMode(Mode.mult); break;
              case 'a': case '+': setMode(Mode.addR); break;
              case 'n': but.doClick(); break;
              case 'i': inv.doClick(); break;
              case 's': mat.solve(false); display(); break;
            } else if (c == KeyEvent.VK_ESCAPE) setMode(Mode.none);
        }
        public void keyPressed(KeyEvent e) { }
        public void keyReleased(KeyEvent e) { }
        public void mouseClicked(MouseEvent e) {
            int i = tab.getSelectedRow();
            if (e.getClickCount() > 1) {
                //System.out.println("double click"); 
                mat.forward(i); display();
            } else if (mode == Mode.exch || mode == Mode.addR) {
                txt2.setText(""+i); doAction();
            } else if (mode == Mode.mult) TK.beep();
        }
        public void actionPerformed(ActionEvent e) {
            Object s = e.getSource();
            int x = frm.getX()+30; int y = frm.getY()+30;
            if (s == txt1 || s == txt2) doAction();
            else if (s == but) new Solver(x, y);
            else if (s == inv) new Solver(adj.clone(), x, y);
            //System.out.println("inverse");
        }
    }

    public static float scaled(float k) { return k*RES_RATIO; }
    public static int scaled(int k) { return Math.round(k*RES_RATIO); }
    public static Font scaledFont(String name, int style, float size) {
        Font f =  new Font(name, style, 1); //unit font
        return f.deriveFont(scaled(size));
    }
    public static void main(String[] args) {
        new Solver().solve(); 
    }
}
