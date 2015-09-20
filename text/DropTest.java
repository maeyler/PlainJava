//June 2004

import java.io.File;
import java.util.List;
import java.lang.reflect.Method;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.dnd.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DropTest 
	implements DropTargetListener, MouseMotionListener {

    final int D = (Frame.getFrames().length==0)?
           JFrame.EXIT_ON_CLOSE : JFrame.DISPOSE_ON_CLOSE;
    final JFrame frm = new JFrame("Drag and Drop Test");
    final JPanel pan = new JPanel();
    final JTextArea text = new JTextArea("JTextArea\n");
    final JScrollPane scr1 = new JScrollPane(text);
    final JList list = new JList();  //Multiple mode by default
    final JScrollPane scr2 = new JScrollPane(list);
    static final int GAP = 5, W = 2;
    static final Color COL = Color.pink;
    static final Font font = new Font("Serif", 0, 13);
    static final Cursor 
       MOVE = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR),
       TEXT = Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
    
    public DropTest() {
        //System.err.println(D);

        pan.setLayout(new BorderLayout(GAP, GAP));
        pan.setBorder(new EmptyBorder(GAP, GAP, GAP, GAP));
        pan.setBackground(COL);
        text.setFont(font);
        if (setDragEnabled(text)) text.addMouseMotionListener(this);
        DropTarget dt = new DropTarget(text, this);
        scr1.setPreferredSize(new Dimension(240, 100));
        pan.add(scr1, "Center");
        File[] a = new File(".").getAbsoluteFile().listFiles();
        java.util.Arrays.sort(a);
        list.setListData(a);
        list.setFont(font);
        list.setBackground(Color.yellow);
        setDragEnabled(list);
        pan.add(scr2, "East");
        text.setToolTipText("Drop here");
        list.setToolTipText("Drag is enabled");

        frm.setContentPane(pan); 
        frm.setDefaultCloseOperation(D);
        frm.setLocation(0, 250);
        frm.pack(); frm.setVisible(true);
    }
   final Stroke st = new BasicStroke(2);
   public void drawBox(Color c) {
        Graphics2D g = (Graphics2D)pan.getGraphics();
        g.setColor(c); g.setStroke(st);
        Rectangle r = scr1.getBounds();
        g.drawRect(r.x-1, r.y-1, r.width+1, r.height+1);
   }
   public void dragEnter(DropTargetDragEvent dtde) {
        drawBox(Color.blue);
   }
   public void dragExit(DropTargetEvent dte)  {
        drawBox(COL);
   }
   public void dragOver(DropTargetDragEvent dtde)  {
   }
   public void dropActionChanged(DropTargetDragEvent dtde)  {
        drawBox(COL);
   }
   public void drop(DropTargetDropEvent dtde)  {
        drawBox(COL);
        Transferable t = dtde.getTransferable();
        dtde.acceptDrop(dtde.getDropAction());
        text.setText(TableMaker.toString(t));
        if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
          try {
            DataFlavor f = DataFlavor.javaFileListFlavor;
            List L = (List)t.getTransferData(f);
            list.setListData(L.toArray());
          } catch (Exception x) {
            System.err.println(x);
          }
        }
   }

   public void mouseDragged(MouseEvent e) { 
   }
   public void mouseMoved(MouseEvent e) {
      int p = text.viewToModel(e.getPoint());
      int i = text.getSelectionStart();
      int j = text.getSelectionEnd();
      //System.out.println(i+" "+p+" "+j);
      text.setCursor((i<=p && p<j)? MOVE : TEXT);
   }

   static boolean setDragEnabled(JComponent c) {
      try {
         //Class[] b = { Boolean.TYPE };
         Method m = c.getClass().getMethod("setDragEnabled", Boolean.TYPE);
         //Boolean[] t = { new Boolean(true) };
         m.invoke(c, true);
         return true;
      } catch (Exception x) {
         System.err.println(x);
         return false;
      }
   }

   public static void main(String[] args) {
      new DropTest();
   }
}


