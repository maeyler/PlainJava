import java.awt.*;
import javax.swing.*;
import java.text.SimpleDateFormat;

class Sayac implements Runnable {
    JLabel disp;
    boolean running;
    long target;
    public Sayac() {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        try {
            java.util.Date d = df.parse("5.11.2028");
            target = d.getTime();
        } catch(java.text.ParseException e) {
            disp.setText("ParseException");
        }
        makeFrame(); 
    }
    public void run() {
        System.out.println("start");
        running = true;
        while (running) {
            long diff = target - System.currentTimeMillis();
            double d = diff/(24*3600);
            disp.setText(String.format("%.3f", d/1000));
            try {
                Thread.sleep(10*1000);
            } catch (InterruptedException e) {
	    }
        }
        System.out.println("Stop");
    }
    public void start() {
        if (running) return;
        running = true;
        new Thread(this).start();
    }
    public void stop() {
        running = false;
    }
    void makeFrame() {
        JPanel p = new JPanel();
        p.setBackground(Color.blue);
        //p.setForeground(Color.yellow);
	//p.setLayout(new GridLayout(2,1));
        disp = new JLabel("xxxxx");
        disp.setFont(new Font("Serif", 0, 24));
        disp.setForeground(Color.yellow);
        p.add(disp);

        JFrame f = new JFrame("Sayaç");  //a window
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //f.setLocation(0, 300);  //left and middle
        f.setContentPane(p);   //add p to f
        f.pack();   //minimal size
        f.setVisible(true);   //show
    }
    public static void main(String[] args) {
        new Sayac().start();;
    }
}
