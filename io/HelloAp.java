import java.applet.*;
import java.awt.*;
 
/**
 * The HelloWorld class implements an applet that
 * simply displays "Hello World!".
 */
public class HelloAp extends Applet {
    public void paint(Graphics g) {
        // Display "Hello World!"
        g.drawString("Hello world!", 50, 25);
    }
}
