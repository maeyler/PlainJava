//16.11.2003

import java.io.*;
import java.net.URL;
import java.util.Iterator;

class FileIterator implements Iterator {
   
   BufferedReader rdr;  //Reader object
   String current;      //current line
   
   FileIterator(String name) throws IOException {
      this(new File(name)); 
   }
   FileIterator(File f) throws IOException { 
      this(new FileInputStream(f)); 
   }
   FileIterator(URL u) throws IOException { 
      this(u.openStream()); 
   }
   FileIterator(InputStream in) {
      rdr = new BufferedReader(new InputStreamReader(in));
      nextLine();
   }
   void nextLine() { //read next line from the Reader
      try {
         current = rdr.readLine();
         if (current == null) rdr.close();
      } catch (IOException x) {
         throw new RuntimeException(x.getMessage());
      }
   }
   public boolean hasNext() { //have more lines?
      return (current != null);
   }
   public Object next() { //return current & read a line
      String s = current; nextLine(); 
      return s;
   }
   public void remove() { //unsupported operation
      throw new UnsupportedOperationException("remove");
   }
   static void test(Iterator i) {
      while (i.hasNext()) System.out.println(i.next());
   }
   public static void main(String[] a) throws IOException {
       test(new FileIterator("FileIterator.java"));
       System.out.println("----------------------");
       test(new FileIterator(new URL("http://java.com")));
   }
}
