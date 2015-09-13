import java.io.File;
import java.net.URL;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class GraySolution implements Runnable {

    public static GraySolution G = new GraySolution();
    final JFrame frm = new JFrame();
    final JLabel lab = new JLabel();
    Image last; BufferedImage img;
    int width, height;
    
    private GraySolution() { //singleton
        frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frm.getContentPane().add(lab);
    }
    public void displayFile(String n) throws Exception { 
        displayFile(new File(n)); 
    }
    public void displayFile(File f) throws Exception {
        displayURL(f.toURI().toURL()); //java.net.MalformedURLException
    }
    public void displayURL(URL u) {
        Image i = new ImageIcon(u).getImage(); //--> ToolkitImage
        //java.awt.Toolkit.getDefaultToolkit().getImage(u);
        //javax.imageio.ImageIO.read(u);  //--> BufferedImage
        try {
            setImage(i, u.toString()); //may fail
            last = i; //last successful file 
        } catch (Exception x) {
            System.out.println(x);
        }
    }
    public BufferedImage getImage() { return img; }
    public void setImage(Image i) { setImage(i, "Some image"); }
    public void setImage(Image i, String t) {
        int w = i.getWidth(null); int h = i.getHeight(null);
        if (w < 0 || h < 0) throw new RuntimeException("Image not loaded");
        System.out.printf("%s: %sx%s %n", t, w, h); 
        img = (i instanceof BufferedImage? (BufferedImage)i : copyOf(i));
        width = img.getWidth(); height = img.getHeight();
        lab.setIcon(new ImageIcon(img)); //resize JLabel & repaint()
        frm.pack(); frm.setTitle(t); frm.setVisible(true);
    }
    public void applyGrayFilter() { //convert img -- don't make a copy 
        GraySolution.applyGrayFilter(img); lab.repaint();
    }
    public void applyMirrorFilter() { //convert img -- don't make a copy 
        GraySolution.applyMirrorFilter(img); lab.repaint();
    }
    public void rescale(float f) { //display a scaled copy of img
        setImage(copyOf(img, f), "Scaled x"+f);  //repaint() is implicit
    }
    public void run() {
        try {
            if (last != null) setImage(last);
            else displayFile("Kedi.png");
            Thread.sleep(1000); //InterruptedException
            applyGrayFilter();
            Thread.sleep(1000); 
            rescale(0.75f);
            Thread.sleep(1000); 
            applyMirrorFilter();
        } catch (Exception x) { 
            System.out.println(x);
        }
    }
    
    final static int MAX = 800; //largest size for scaling
    final static int MIN = 6;  //smallest size for scaling
    public static BufferedImage copyOf(Image i) { return copyOf(i, 1); }
    public static BufferedImage copyOf(Image i, float f) {
        int w = (int)(f*i.getWidth(null)); 
        int h = (int)(f*i.getHeight(null));
        if (w < MIN || h < MIN) return null;
        if (w > MAX || h > MAX) { //resize if picture is large
            f = (w > h? (float)MAX/w : (float)MAX/h);
            w = (int)(f*w); h = (int)(f*h);
            System.out.printf("Resized to: %sx%s %n", w, h);
        }
        BufferedImage i2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        i2.getGraphics().drawImage(i, 0, 0, w, h, null);  //will be scaled here
        return i2;
    }
    public static void applyGrayFilter(BufferedImage i) {
        //image data is modified directly on the Raster
        WritableRaster R = i.getRaster(); 
        int[] rgb = { 0, 0, 0 }; //used for all pixels
        for (int x=0; x<i.getWidth(); x++) 
            for (int y=0; y<i.getHeight(); y++) {
                R.getPixel(x, y, rgb); 
                int m = (rgb[0]+rgb[1]+rgb[2])/3;
                rgb[0] = m; rgb[1] = m; rgb[2] = m;
                R.setPixel(x, y, rgb); 
            }
    }
    public static void applyMirrorFilter(BufferedImage i) {
        int w = i.getWidth()-1;
        for (int x=0; x<w/2+1; x++) //scan half of each row
            for (int y=0; y<i.getHeight(); y++) {
                int c1 = i.getRGB(x, y), c2 = i.getRGB(w-x, y);
                i.setRGB(x, y, c2); i.setRGB(w-x, y, c1);
            }
    }
    public static void main(String[] args) {
        new Thread(G).start();
    }
}
