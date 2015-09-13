//Dec 2013  for CSE 470

import java.io.File;
import java.util.List;
import java.util.Arrays;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;

public class FileSelection implements Transferable {
   
    static final DataFlavor LST = DataFlavor.javaFileListFlavor;
    static final DataFlavor STR = DataFlavor.stringFlavor;
    static final DataFlavor IMG = DataFlavor.imageFlavor;
    static final Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
    static final int H = 16;

    final List<File> lst;  final String str;  final Image img;
    
    public FileSelection(File[] a) {
        this(Arrays.asList(a));
    }
    public FileSelection(List<File> data) {
        lst = data; //.clone();
        int w = 20*H; int h = (data.size()+1)*H;
        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        //g.fillRect(0 ,0, w, h);
        //g.setColor(Color.black);
        String s = ""; int y = 0;
        for (File f: data) {
            String t = f.toString();
            s += "\n"+t;  y += H;
            g.drawString(t, H, y);
        }
        str = s;
    }
    public DataFlavor[] getTransferDataFlavors() {
	return new DataFlavor[] { LST, STR, IMG };
    }
    public boolean isDataFlavorSupported(DataFlavor f) {
	return f.equals(LST) || f.equals(STR) || f.equals(IMG);
    }
    public Object getTransferData(DataFlavor f) 
            throws UnsupportedFlavorException, java.io.IOException {
	if (f.equals(LST)) return lst;
	if (f.equals(STR)) return str;
	if (f.equals(IMG)) return img;
	throw new UnsupportedFlavorException(f);
    }

    public static void main(String[] args) throws Exception {
        File p = new File(".").getAbsoluteFile().getParentFile();
        File[] a = p.listFiles();
        clip.setContents(new FileSelection(a), null);
        System.out.println(a.length+" files in "+p);
    }
}
