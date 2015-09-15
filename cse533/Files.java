//7.4.2004 

import java.io.*;
import java.util.*;

public class Files {
   
   static abstract class Processor {
      abstract void process(File f);
      abstract void report();
      public void iterate(File f) {
         File[] a = f.listFiles(); 
         for (int i=0; i<a.length; i++) {
            if (a[i].isDirectory()) iterate(a[i]);
            else process(a[i]);
         }
      }
   }

   static class Counter extends Processor {
      final Map map = new HashMap();
      public Counter() {}
      public void process(File f) {
         String key = f.getName();
         List d = (List)map.get(key);
         if (d == null) {
            d = new ArrayList(); 
            map.put(key, d);
         }
         d.add(f);
      }
      public void report() {
         Iterator i = map.keySet().iterator(); 
         while (i.hasNext()) {
            Object key = i.next();
            List d = (List)map.get(key);
            if (d.size() > 1)
               System.out.println(key+ "\t" +d);
         }
         System.out.println(map.size()+" files");
      }
   }
   static class Bigger extends Processor {
      static final long MIN = 1024*1024;
      final List list = new ArrayList();
      public Bigger() {}
      public void process(File f) {
         long len = f.length();
         if (len > MIN) list.add(f);
      }
      public void report() {
         Iterator i = list.iterator(); 
         while (i.hasNext()) {
            Object s = i.next();
            System.out.println(s);
         }
         System.out.println(list.size()+" long files");
      }
   }
   
   public static void usage() {
      System.out.println("usage:");
      System.out.println("java Files c[ount]|i[ndex] [FileName]");
      throw new IllegalArgumentException();
   }
   public static Processor processor(String opt) {
      opt = opt.toLowerCase();
      if ("counter".startsWith(opt)) return new Counter();
      if ("bigger".startsWith(opt)) return new Bigger();
      return null;
   }
   public static void main(String[] args) {
      if (args.length == 0 || args.length > 2) usage();
      Processor p = processor(args[0]);
      if (p == null) usage();
      File f = new File((args.length == 2)? args[1] : ".");
      p.iterate(f); p.report();
   }
}
