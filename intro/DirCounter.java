import java.io.File;

public class DirCounter {
   int fil, dir;
   File path;
   public DirCounter() { this("."); }
   public DirCounter(String s) { this(new File(s)); }
   public DirCounter(File p) {
       if (!p.isDirectory()) 
           throw new RuntimeException("no such directory: "+p);
       fil = 0; dir = 0; path = p;
       System.out.printf("Starting folder: %n", p);
       countFiles(p);   //may take a long time
       System.out.printf("%4s files and %s folders %n", fil, dir);
   }
   void countFiles(File d) {
       dir++; 
       File[] fa = d.listFiles();
       System.out.printf("%4s: %s %n", fa.length, d);
       for (File f: fa)
           if (!f.isDirectory()) fil++;
           else  //recursive call for each directory
               countFiles(f);
   }
   public String toString() { return path+" "+fil+" files"; }
   
   public static void main(String[] args) {
       new DirCounter();
   }
}
