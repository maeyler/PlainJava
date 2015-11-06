import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.Date;
import java.text.SimpleDateFormat;

class Sayac implements Runnable {

    static final int 
        RESOLUTION = Toolkit.getDefaultToolkit().getScreenResolution(); 
    static final float 
        RES_RATIO = RESOLUTION/96f;  //default resolution is 96
    static final int GAP = scaled(8);
    static final Color COLOR = Color.yellow;
    static final Font NORM = new Font("Serif", 0, scaled(50));
    static final Font BIG  = new Font("Serif", 0, scaled(96));

    final JLabel days = new JLabel();
    final JLabel left = new JLabel("gün kaldý");
    Thread thd;
    long target;
    public Sayac() {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date d = df.parse("5.11.2028");
            target = d.getTime();
        } catch(java.text.ParseException e) {
            days.setText("ParseException");
        }
        makeFrame(); 
    }
    void setDate() {
        long diff = target - System.currentTimeMillis();
        long d = diff/(24*3600);  //double
        days.setText(String.format("%s", d/1000)); //"%.3f"
    }
    public void run() {
        System.out.println("start");
        while (Thread.currentThread() == thd) {
            try {
                setDate(); 
                Thread.sleep(10*1000);
            } catch (InterruptedException e) {
	    }
        }
        System.out.println("Stop");
    }
    public void start() {
        if (Thread.currentThread() == thd) return;
        thd = new Thread(this);
        thd.start();
    }
    public void stop() {
        if (thd != null) thd.interrupt();
        thd = null;
    }
    void makeFrame() {
        JPanel p = new JPanel();
        p.setBackground(Color.blue);
	p.setLayout(new BorderLayout(0, 0));
        p.setBorder(new EmptyBorder(0, GAP, GAP, GAP));
        days.setFont(BIG);
        days.setForeground(COLOR);
        p.add(days, "North");
        left.setFont(NORM);
        left.setForeground(COLOR);
        p.add(left, "South");

        JFrame f = new JFrame("Sayaç");  //a window
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setContentPane(p); 
        setDate(); f.pack();  //minimal size
        f.setVisible(true);   //show
        
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { stop(); }
        });
    }

    public static int scaled(int k) { return Math.round(k*RES_RATIO); }
    public static void main(String[] args) {
        new Sayac().start();;
    }
}
