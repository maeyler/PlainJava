// Author: Eyler -- 27.7.2009

import java.io.*;
import java.util.*;

class Copy  {
   
   final static Map<String, Set<String>> 
      map = new TreeMap<String, Set<String>>();
   
   public Copy(Reader r) throws IOException {
      int n = 0, m = 0;
      BufferedReader b = new BufferedReader(r);
      String s = b.readLine();
      while (s != null) {
          m += process(s); n++; s = b.readLine(); 
      }
      r.close(); 
      System.out.print(" Avg "+m/n+" fields  ");
      System.out.println(n+" lines");
   }
   public int process(String s) {
       String[] a = s.split("\t");
       int k = a.length;
       System.out.print(k+" ");
       return k;
   }
   public boolean add(String k, String key) {
       Set<String> t = map.get(k);
       if (t == null) {
           t = new TreeSet<String>(); mapp.put(k, t);
       }
       return t.add(key.lowerCase());
   }
   public static void main(String[] args) throws IOException  {
      String fName = "endüstri müh.txt";   
      if (args.length > 0) fName = args[0];
      long start = System.currentTimeMillis();
      new Copy(new FileReader(fName));
      System.out.println("Elapsed: "
      	+((System.currentTimeMillis()-start))+" msec");
   }
}
