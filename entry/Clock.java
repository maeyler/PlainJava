import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.Date;

public class Clock implements Runnable {
   JLabel lab = new JLabel();
   int delay;
   public Clock() {
      lab.setForeground(Color.BLUE);
      lab.setFont(new Font("Serif", Font.BOLD, 24));
      doTick(); //initial call fills the label
      JFrame frm = new JFrame("Clock");
      frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frm.addWindowListener(new Closer());
      frm.setContentPane(lab); 
      frm.setLocation(100, 300);  //left and middle
      frm.pack(); frm.setVisible(true);
   }
   void doTick() { //called on every tick
      lab.setText(new Date().toString());
   }
   public void run() { //do not call from SSS
      if (Thread.currentThread().getName().startsWith("AWT")) 
          throw new Error("do not call directly -- use start()");
      while (delay > 0) {
         doTick();
         try {
             Thread.sleep(delay);
         } catch (Exception ex) {
             System.out.println("wake-up");
         }
      }
   }
   public void start(int d) {
      if (delay > 0) return;
      System.out.println("start");
      delay = d;
      new Thread(this).start();  //calls run() indirectly
   }
   public void stop() {
      if (delay <= 0) return;
      System.out.println("stop");
      delay = 0;
   }
   
   class Closer extends WindowAdapter { //stops the Thread
       public void windowClosing(WindowEvent e) { stop(); }
   }
   public static void main(String[] args) {
      new Clock().start(1000);  
   }
}
