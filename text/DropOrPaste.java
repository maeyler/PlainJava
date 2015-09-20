//June 2004 

import java.util.List;
import java.lang.reflect.Method;
import java.awt.*;
import java.awt.event.*;
import java.awt.dnd.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DropOrPaste 
   implements DropTargetListener, MouseMotionListener {
   
   final int D = (Frame.getFrames().length==0)?
      JFrame.EXIT_ON_CLOSE : JFrame.DISPOSE_ON_CLOSE;
   final JFrame frm = new JFrame("Drag&Drop or Cut&Paste");
   final JSplitPane pan 
      = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
   final JTextArea drop = new JTextArea("Drop Area\n");
   final JScrollPane top = new JScrollPane(drop);
   final JLabel pict = new JLabel(null, null, JLabel.CENTER);
   final JTextArea text = new JTextArea();
   final JEditorPane html = new JEditorPane();
   final JList list = new JList();  //Multiple mode by default
   final JScrollPane bot = new JScrollPane(list);
   final JViewport port = bot.getViewport();
   final Action paste = new Paste();
   static final Dimension 
    TOP = new Dimension(500, 160),
    BOT = new Dimension(500, 360);
   static final int GAP = 5, W = 2;
   static final Color 
    NORM = Color.pink, DROP = Color.blue;
   static final Font
    SERIF = new Font("Serif", 0, 14),
    MONO  = new Font("Monospaced", 0, 12);
   static final Cursor 
    MOVE = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR),
    TEXT = Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
   static final Stroke THICK = new BasicStroke(2);

    
    public DropOrPaste() {
      top.setPreferredSize(TOP);
      drop.setFont(SERIF);
      drop.setBackground(Color.yellow);
      if (setDragEnabled(drop)) 
         drop.addMouseMotionListener(this);
      DropTarget dt = new DropTarget(drop, this);
      drop.setToolTipText("Drop or paste here");

      KeyStroke p = KeyStroke.getKeyStroke("control V");
      Object key = text.getInputMap().get(p);
      if (key == null) key = "paste-from-clipboard";
      drop.getActionMap().put(key, paste);

      bot.setPreferredSize(BOT);
      list.setFont(SERIF);
      setDragEnabled(list);
      html.setEditable(false);
      html.setContentType("text/html");
      setDragEnabled(html);
      text.setFont(MONO);
      text.setLineWrap(true);
      text.setWrapStyleWord(true);
      text.setEditable(false);
      setDragEnabled(text);
      //setDragEnabled(pict);
      //pict.setTransferHandler(new TransferHandler("icon"));
      
      pan.setTopComponent(top);
      pan.setBottomComponent(bot);
      pan.setDividerSize(GAP+2);
      pan.setBorder(new EmptyBorder(GAP, GAP, GAP, GAP));
      pan.setBackground(NORM);
      paste.actionPerformed(null); 
      
      frm.setContentPane(pan); 
      frm.setDefaultCloseOperation(D);
      frm.setLocation(0, 150);
      frm.pack(); frm.setVisible(true);
   }
   public void drawBox(Color c) {
      Graphics2D g = (Graphics2D)pan.getGraphics();
      g.setColor(c); g.setStroke(THICK);
      Rectangle r = top.getBounds();
      g.drawRect(r.x-1, r.y-1, r.width+1, r.height+3);
   }
   public void dragEnter(DropTargetDragEvent dtde) {
      drawBox(DROP);
   }
   public void dragExit(DropTargetEvent dte)  {
      drawBox(NORM);
   }
   public void dragOver(DropTargetDragEvent dtde)  {
   }
   public void dropActionChanged(DropTargetDragEvent dtde)  {
      drawBox(NORM);
   }
   public void drop(DropTargetDropEvent dtde)  {
      drawBox(NORM);
      dtde.acceptDrop(dtde.getDropAction());
      dtde.dropComplete(display(dtde.getTransferable()));
   }

   public void mouseDragged(MouseEvent e) { 
   }
   public void mouseMoved(MouseEvent e) {
      int p = drop.viewToModel(e.getPoint());
      int i = drop.getSelectionStart();
      int j = drop.getSelectionEnd();
      //System.out.println(i+" "+p+" "+j);
      drop.setCursor((i<=p && p<j)? MOVE : TEXT);
   }

   boolean display(Transferable t)  {
      DataFlavor[] a = t.getTransferDataFlavors();
      String s = a.length+" flavors\n";
      for (int i=0; i<a.length; i++) 
         s += a[i].getMimeType() + "\n";
      drop.setText(s); drop.select(0,0);
      try {
         if (displayList(t) || displayHTML(t)
          || displayText(t) || displayPict(t))
              return true;
         else displayString("Unknown data");
      } catch (Exception x) {
         displayString(""+x);
      }
      return false;
   }
   void displayString(String s) { 
      port.setView(pict);
      pict.setIcon(null);
      pict.setText(s);
   }
   boolean displayList(Transferable t) throws Exception { 
      DataFlavor f = DataFlavor.javaFileListFlavor;
      if (!t.isDataFlavorSupported(f)) return false;
      port.setView(list);
      List L = (List)t.getTransferData(f);
      list.setListData(L.toArray());
      return true;
   }
   boolean displayText(Transferable t) throws Exception { 
      DataFlavor f = DataFlavor.stringFlavor;
      if (!t.isDataFlavorSupported(f)) return false;
      port.setView(text);
      String s = (String)t.getTransferData(f);
      if (s == null || s.length() == 0) return false;
      text.setText(s);
      text.select(0,0);
      return true;
   }
   boolean displayHTML(Transferable t) throws Exception {
      String mime = "text/html; class=java.lang.String";
      DataFlavor f = new DataFlavor(mime);
      if (!t.isDataFlavorSupported(f)) return false;
      port.setView(html);
      String s = (String)t.getTransferData(f);
      if (s == null || s.length() == 0) return false;
      html.setText(s);
      return (html.getDocument().getLength() > 0);
   }
   boolean displayPict(Transferable t) throws Exception {
      String mime = "image/x-java-image; class=java.awt.Image";
      DataFlavor f = new DataFlavor(mime);
      if (!t.isDataFlavorSupported(f)) return false;
      port.setView(pict);
      pict.setText(null);
      pict.setIcon(new ImageIcon((Image)t.getTransferData(f)));
      return true;
   }

   static boolean setDragEnabled(JComponent c) {
      try {
         Class[] b = { Boolean.TYPE };
         Method m = c.getClass().getMethod("setDragEnabled", b);
         Boolean[] t = { new Boolean(true) };
         m.invoke(c, t);
         c.setToolTipText("Copy and Drag are enabled here");
         return true;
      } catch (Exception x) {
         System.err.println(x);
         return false;
      }
   }

   class Paste extends AbstractAction {
      public Paste() { super("Paste"); }
      public void actionPerformed(ActionEvent e) {
         Clipboard clip =
            Toolkit.getDefaultToolkit().getSystemClipboard();
         display(clip.getContents(null));
      }
   }

   public static void main(String[] args) {
      new DropOrPaste();
   }
}
