// Author: Eyler -- 12.1.2002
// simplified       3.11.2003
// JVM 5.0           6.9.2004

import java.io.*;
import java.util.*;

class Concordance  {
   
   //final static Map map = new TreeMap();
   final static Map<String, List<Integer>> 
      map = new TreeMap<String, List<Integer>>();

   public static void main(String[] args) throws IOException  {
      long start = System.currentTimeMillis();
      String fName = "Phone.txt";   //"Yusuf.txt";
      if (args.length > 0) fName = args[0];
      
      Reader r = new FileReader(fName);
      Reader b = new BufferedReader(r);
      StreamTokenizer tok = new StreamTokenizer(b);
      tok.lowerCaseMode(true);
      tok.ordinaryChar('.');
      tok.ordinaryChar('/');
      tok.ordinaryChar('-');
      tok.wordChars('_', '_');
      tok.eolIsSignificant(true);

      int line = 1;
      while (tok.nextToken() != tok.TT_EOF) 
         if (tok.ttype == tok.TT_EOL) line++;
         else if (tok.ttype == tok.TT_WORD) {
            String key = tok.sval;
            //List d = (List)map.get(key);
            List<Integer> d = map.get(key);
            if (d == null) {
               d = new ArrayList<Integer>();  //new ArrayList();
               map.put(key, d);
            }
            d.add(line);  //new Integer(line));
         }
      r.close(); 
      
      //Iterator i = map.keySet().iterator(); 
      //while (i.hasNext()) {
      //   String s = (String)i.next();
      //}
      for (String s : map.keySet())
         System.out.println(s+ "\t" +map.get(s));
      System.out.println(map.size()+" words");
      
      System.out.println("Elapsed msec "
      	+((System.currentTimeMillis()-start)));
   }
}
