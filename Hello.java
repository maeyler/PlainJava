import java.awt.*;
import java.applet.Applet;
import java.util.Date;
//import mae.util.WindowCloser;

public class Hello extends Applet {
   static Applet hello;
   public Hello() {
      setBackground(Color.blue);
      setForeground(Color.yellow);
      setFont(new Font("Serif", 1, 18));
      setLayout(new GridLayout(2,1));
      add(new Label("Hello World"));
      add(new Label(new Date().toString()));
   }
   public static void main(String[] args) {
      int d = (Frame.getFrames().length == 0)?
         WindowCloser.EXIT : WindowCloser.DISPOSE;
      Frame f = new Frame("Hello");  //a window
      f.setLocation(0, 300);  //left and middle
      hello = new Hello();  //the applet p
      f.add(hello);   //add p to f
      f.pack();   //minimal size
      f.setVisible(false);   //show
      f.addWindowListener(new WindowCloser(d));
   }
}
