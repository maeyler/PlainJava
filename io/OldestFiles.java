// Author: Eyler -- June 2014
// finds the oldest files with any extension within a given folder

import java.io.File;
import java.io.PrintStream;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class OldestFiles extends FileCrawler {

    Map<String, File> map;
   
    public OldestFiles(File f) {
        super(f);
        map = new TreeMap<String, File>();
    }
    public String getExtension(File f) {
        if (f == null) return null;
        String s = f.getName();
        int k = s.lastIndexOf('.');
        if (k < 0) return "";
        return s.substring(k+1);
    }
    public void count() {
        map.clear(); start();
    }
    protected void doFile(File f)  {
        super.doFile(f);
        String ext = getExtension(f);
        File t = map.get(ext);
        if (t == null || f.lastModified() < t.lastModified()) 
            map.put(ext, f);
    }
    protected int report(PrintStream out) {
        //String s = (ext == null? "all" : ext);
        out.println("Finding the oldest files");
        int n = map.keySet().size();
        Map<Date, File> tmp = new TreeMap<Date, File>();
        for (String k: map.keySet()) {
            File f = map.get(k);
            Date d = new Date(f.lastModified());
            tmp.put(d, f);
        }
        for (Date d: tmp.keySet()) {
            out.printf("%1$td/%1$tm/%1$tY -- %2$s %n", d, tmp.get(d));
        }
        return n + super.report(out);
    }
    
    public static void count(File f) {
        new OldestFiles(f).count();
    }
    public static void main(String[] args) {
        //all files in the startup folder
        count(new File(".")); 
    }
}
