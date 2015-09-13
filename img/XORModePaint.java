import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class XORModePaint extends JPanel {

  static final int H = 13;
  int x1, y1, x2, y2, move; 
  boolean erase, draw;
  JFrame frm = new JFrame();

  public XORModePaint() {
    Mover m = new Mover();
    addMouseListener(m);
    addMouseMotionListener(m);
    frm.setContentPane(this);
    frm.setTitle("Transparent panel");
    frm.setSize(400, 300);
    frm.setVisible(true);
  }
  public void drawCross(Graphics g) {
    g.setXORMode(Color.gray);
    g.drawLine(x1-H, y1+H,   x1+H, y1-H);
    g.drawLine(x1-H, y1+H+1, x1+H, y1-H+1);
    g.drawLine(x1-H, y1-H,   x1+H, y1+H);
    g.drawLine(x1-H, y1-H+1, x1+H, y1+H+1);
    g.setPaintMode();
  }
  public void drawCircle(Graphics g) {
    g.setXORMode(Color.gray);
    g.fillOval(x1-H, y1-H, 2*H, 2*H);
    g.setPaintMode();
  }
  public void paint(Graphics g) {
    if (erase) drawCircle(g);  //drawCross(g); //
    else erase = true;
    x1 = x2; y1 = y2;
    if (draw)  drawCircle(g);  //drawCross(g); //
    else draw = true;
  }
  
    class Mover extends MouseAdapter {
       public void mouseMoved(MouseEvent e) {
         x2 = e.getX();
         y2 = e.getY();
         move++; repaint();
       }
       public void mouseEntered(MouseEvent e) {
         erase = false; draw = true;
         move = 0; repaint(); 
         //System.out.println("enter");
       }
       public void mouseExited(MouseEvent e) {
         erase = true; draw = false;
         repaint(); 
         //System.out.println(move+" moves");
       }
    }
    
  public static void main(String[] a) {
    new XORModePaint();
  }
}
