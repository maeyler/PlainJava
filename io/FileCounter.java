import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class FileCounter {
   int fil, dir;
   File path;
   Map<File, Integer> map;
   
   public FileCounter() {
       this(".");  //this folder
   }
   public FileCounter(String p) {
       fil = 0; dir = 0; 
       path = new File(p);
       map = new HashMap<File, Integer>();
       System.out.println("Starting folder: "+path);
       countFiles(path);  //may take a long time
       System.out.println(fil+" files and "+dir+" folders");
   }
   void countFiles(File d) {
       if (!d.isDirectory()) {
           fil++;
       } else { //continue recursion
           dir++; 
           File[] fa = d.listFiles();
           map.put(d, fa.length);
           for (File f: fa) countFiles(f);
       }
   }
   public void list() {
       for (File k: map.keySet())
           System.out.println(k+" "+map.get(k));
   }
   public String toString() {
       return path+" "+fil+" files";
   }
   public static void main(String[] args) {
       new FileCounter().list();
   }
}
