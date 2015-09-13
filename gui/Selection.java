//Dec 2012  for CSE 495 
//modification of java.awt.datatransfer.StringSelection

import java.util.List;
import java.io.File;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.*;

public class Selection implements Transferable {
   
    static final DataFlavor LST = DataFlavor.javaFileListFlavor;
    static final DataFlavor STR = DataFlavor.stringFlavor;
    static final DataFlavor IMG = DataFlavor.imageFlavor;
    static final Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();

    final Object data;   
    final DataFlavor flavor;
     
    public Selection(List<File> lst) {
        flavor = LST; data = lst;
    }
    public Selection(String str) {
        flavor = STR; data = str;
    }
    public Selection(Image img) {
        flavor = IMG; data = img;
    }
    public DataFlavor[] getTransferDataFlavors() {
	return new DataFlavor[] { flavor };
    }
    public boolean isDataFlavorSupported(DataFlavor f) {
	return flavor.equals(f);
    }
    public Object getTransferData(DataFlavor f) 
            throws UnsupportedFlavorException, java.io.IOException {
	if (flavor.equals(f)) return data;
	else throw new UnsupportedFlavorException(f);
    }

    static void copy(Transferable t) {
        clip.setContents(t, null);
    }
    public static void copy(List<File> lst) {
        copy(new Selection(lst));
    }
    public static void copy(String str) {
        copy(new Selection(str));
    }
    public static void copy(Image img) {
        copy(new Selection(img));
    }
    public static Object paste(DataFlavor f) 
            throws UnsupportedFlavorException, java.io.IOException {
        return clip.getData(f);
    }
    public static void main(String[] args) throws Exception {
        //print text in the clipboard, if any
        if (clip.isDataFlavorAvailable(STR))
            System.out.println(paste(STR));
        else System.out.println("No text in the clipboard");
        //copy a string to the clipboard
        copy("Copy String was successful");
    }
}
