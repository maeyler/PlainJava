// Author: Eyler -- 12.1.2002
// simplified       3.11.2003
// JVM 5.0           6.9.2004

import java.io.*;
import java.util.*;

abstract class Processor {
   long start;  //time in millisec
   int line;    //line number
   void init(String fName) throws IOException  {
      start = System.currentTimeMillis();
      Reader r = new FileReader(fName);
      Reader b = new BufferedReader(r);
      StreamTokenizer tok = new StreamTokenizer(b);
      tok.lowerCaseMode(true);
      tok.ordinaryChar('.');
      tok.ordinaryChar('/');
      tok.ordinaryChar('-');
      tok.wordChars('_', '_');
      tok.eolIsSignificant(true);

      line = 1;
      while (tok.nextToken() != tok.TT_EOF) 
         if (tok.ttype == tok.TT_EOL) line++;
         else if (tok.ttype == tok.TT_WORD) 
            process(tok.sval);
      r.close(); 
   }
   abstract void process(String s);
   public abstract Map<String, ?> getMap();
   public void report() {
      Map<String, ?> map = getMap();
      for (String s : map.keySet())
         System.out.println(s+ "\t" +map.get(s));
      System.out.println(map.size()+" words");
      System.out.println("Elapsed msec "
         +((System.currentTimeMillis()-start)));
   }

}

class Concordance extends Processor {
   final Map<String, List<Integer>> 
      map = new TreeMap<String, List<Integer>>();
   public Concordance(String fName) throws IOException  {
      init(fName); 
   }
   void process(String key){
         List<Integer> d = map.get(key);
         if (d == null) {
            d = new ArrayList<Integer>(); 
            map.put(key, d);
         }
         d.add(line);
   }
   public Map<String, ?> getMap() { return map; }
   public static void main(String[] args) throws IOException  {
      String s = (args.length > 0)? args[0] : "Yusuf.txt";
      new Concordance(s).report();
   }
}
