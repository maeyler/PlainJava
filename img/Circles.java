import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JFrame;
import static java.lang.Math.*;

public class Circles extends JPanel {

  static final int D = 360; 
  static final int H = D+20; 
  static final int N = 90; 
  final JFrame frm = new JFrame();

  public Circles() {
    frm.setContentPane(this);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frm.setTitle("Circles on a circle");
    frm.setSize(2*H+10, 2*H+30);
    frm.setVisible(true);
  }
  public void circlesOnCircle(Graphics g, int n, int r, int d, int R, boolean offset) {
      double delta = (offset? PI/n : 0);
      for (int k=0; k<n; k++) {
          double t = 2*PI*k/n + delta;
          int x = (int)round(d + R*cos(t));
          int y = (int)round(d + R*sin(t));
          drawCircle(g, x, y, r);
      }
  }
  public void drawCircle(Graphics g, int x, int y, int r) {
    g.fillOval(x-r, y-r, 2*r, 2*r);
  }
  public void paint(Graphics g) {
    g.setColor(Color.gray); 
    drawCircle(g, H, H, D);
    g.setColor(Color.lightGray); 
    //g.setColor(Color.green); 
    circlesOnCircle(g, N, 6, H, D-76, true);
    //g.setColor(Color.lightGray); 
    circlesOnCircle(g, N, 7, H, D-60, false);
    //g.setColor(Color.cyan); 
    circlesOnCircle(g, N, 8, H, D-42, true);
    //g.setColor(Color.orange); 
    circlesOnCircle(g, N, 9, H, D-20, false);
  }
  public static void main(String[] a) {
    new Circles();
  }
}
