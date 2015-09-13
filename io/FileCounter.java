// Author: Eyler -- Oct 2012
// counts all files of with a given extension within a given folder

import java.io.File;
import java.io.PrintStream;
import java.util.Map;
import java.util.TreeMap;

public class FileCounter extends FileCrawler {

    String ext;
    Map<File, Integer> map;
   
    public FileCounter(File f) {
        super(f);
        map = new TreeMap<File, Integer>();
    }
    public void setExtension(String x) {
        if (x == null) ext = null;
        else ext = "."+x;
        map.clear();
    }
    public void count(String x) {
        setExtension(x); start();
    }
    protected void doFile(File f)  { //override to do nothing
    }
    protected void doFolder(File d, File[] a)  {
        super.doFolder(d, a);
        int n = 0;
        for (File f: a) if (f != null && f.isFile()) {
            //count all files in d ending with ext
            if (ext == null || f.getName().endsWith(ext)) n++;
        }
        if (n > 0) {
            map.put(d, n); fil += n;
        }
    }
    protected int report(PrintStream out) {
        String s = (ext == null? "all" : ext);
        out.println("Counting "+s+" files");
        int n = map.keySet().size();
        if (out==System.out && n>20) n = 0;
        else for (File k: map.keySet()) 
            out.println(k+" "+map.get(k));
        return n + super.report(out);
    }
    
    public static void count(File f, String x) {
        new FileCounter(f).count(x);
    }
    public static void main(String[] args) {
        //all files in the startup folder
        count(new File("."), null); 
    }
}
