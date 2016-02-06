import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Motion implements Runnable {
   final static int M = 300, A = 200, B = 150, D =10;
   final static double DELTA = Math.PI/50; //one turn = 100 ticks 
   Panel pan = new Panel();
   double angle = 0; //in radians
   int delay = 0; 
   public Motion() {
      JFrame frm = new JFrame("Motion");
      frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frm.addWindowListener(new Closer());
      frm.setContentPane(pan); 
      frm.setSize(2*M, 2*M+50);  //left and middle
      frm.setVisible(true);
   }
   void doTick() { //called on every tick
      angle += DELTA; pan.repaint();
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
   
   class Panel extends JPanel { //drawing
      public void paint(Graphics g) {
          //g.clearRect(0, 0, 2*M, 2*M);
          double x = M + A * Math.cos(angle);
          double y = M + B * Math.sin(angle);
          g.fillOval((int)x, (int)y, D, D);
      }
   }
   class Closer extends WindowAdapter { //stops the Thread
      public void windowClosing(WindowEvent e) { stop(); }
   }
   public static void main(String[] args) {
      new Motion().start(100);  
   }
}
