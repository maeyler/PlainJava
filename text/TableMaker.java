//June 2004

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;

public class TableMaker {
   //public static final TableMaker ins = new TableMaker();
   public static final Clipboard 
      clip = Toolkit.getDefaultToolkit().getSystemClipboard();
   static final char TAB = '\t';
   static final String
      title = "Grades",
      HEAD  = "<HTML>\n<HEAD>\n<meta http-equiv=Content-Type"
      +" content=\"text/html; charset=iso-8859-9\">\n<TITLE>",
      $HEAD = "</TITLE>\n</HEAD>\n",
      BODY  = "<BODY>\n",
      $BODY = "</BODY>\n</HTML>\n",
      TABLE  = "<TABLE border=1 cellPadding=3 style=\"border-collapse:collapse;\">\n",
      $TABLE = "</TABLE>\n",
      TR  = "  <TR>\n  ",   $TR = "\n  </TR>\n",
      TD  = "<TD>",   $TD = "</TD>",
      BR  = "<BR>\n";
   
   TableMaker() {
      //if (ins != null) throw new RuntimeException("singleton");
   }
   
   public static void doFile(File f) throws IOException {
      System.out.println(f+"  Size: "+f.length());
      convert(new FileReader(f));
   }
   public static void doClipboard() {
      Transferable data = clip.getContents(null); 
      /*DataFlavor[] a = data.getTransferDataFlavors();
      DataFlavor b = DataFlavor.selectBestTextFlavor(a);
      try {
         System.out.println("Clipboard Reader");
         convert(b.getReaderForText(data));
         return;
      } catch (Exception x) {
      }*/
      //DataFlavor p = DataFlavor.plainTextFlavor;
      DataFlavor p = DataFlavor.stringFlavor;
      if (!data.isDataFlavorSupported(p)) return;
      try {
         String d = data.getTransferData(p).toString();
         System.out.println("Clipboard Text: "+d.length());
         convert(p.getReaderForText(data));
         //convert(new StringReader(d));
      } catch (Exception x) {
         System.err.println(x);
      }
   }
   public static void convert(Reader rdr) throws IOException {
      //String title = "Test";
      OutputStream os = new FileOutputStream(title+".html");
      PrintStream out = new PrintStream(os);
      out.print(HEAD + title + $HEAD);
      out.print(BODY);
      BufferedReader in = new BufferedReader(rdr);
      boolean table = false;
      String s;
      while ((s = in.readLine()) != null) {
         String[] a = split(s, String.valueOf(TAB));
         if (a.length == 0) continue;
         if (!table && a.length>1) {//do once
            table = true; out.print(BR + TABLE);
         }
         if (!table) out.print(s + BR);
         else {
            out.print(TR);
            for (int i=0; i<a.length; i++) 
               out.print(TD + a[i] + $TD);
            out.print($TR);
         }
      }
      if (table) out.print($TABLE);
      out.print($BODY);
      rdr.close(); out.close();
   }
   public static void printTransferable(Transferable data) {
      System.out.println(toString(data));
   }
   public static Object[] clipContents() {
      return contentsOf(clip.getContents(null));
   }
   public static String toString(Transferable data) {
      DataFlavor[] a = data.getTransferDataFlavors();
      String s = a.length+" flavors\n";
      for (int i=0; i<a.length; i++) 
         s += a[i].getMimeType() + "\n";
         //+"  "+a[i].getDefaultRepresentationClass().getName());
      return s;
   }
   public static Object[] contentsOf(Transferable data) {
      DataFlavor[] a = data.getTransferDataFlavors();
      Object[] c = new Object[a.length];
      for (int i=0; i<a.length; i++) 
         try {
            c[i] = data.getTransferData(a[i]);
         } catch (Exception x) {
            c[i] = null;
         }
      return c;
   }

   public static String[] split(String s, String c) {
      StringTokenizer tok = new StringTokenizer(s, c);
      List L = new ArrayList();
      while (tok.hasMoreTokens()) L.add(tok.nextToken());
      return (String[])L.toArray(new String[L.size()]);
   }
   public static void main(String[] args) throws IOException {
      //ins.doFile(new File(title+".txt"));
      doClipboard();
      printTransferable(clip.getContents(null));
   }
}


