//uses www.javaxt.com/javaxt-core/javaxt.io.Image/

import java.io.File;
import java.awt.FileDialog;
import java.text.SimpleDateFormat;
import java.text.ParseException;

class DirectoryFixer implements Runnable {

    public final static DirectoryFixer DX = new DirectoryFixer(); //singleton
    final static String NOT_JPG = "Not a jpeg image   ";
    final static String DATE_NF = "Not fnd: Date taken";
    final static String DATE_OK = "Date is OK         ";
    final static String FIXED   = "Date is fixed      ";
    final static String NOT_FOUND = " not found";
    final SimpleDateFormat SDF = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");

    //variable fields
    Console cons;
    String date;
    int cnt, ok, fix;

    public String fixFileDate(File f) throws ParseException {
        String file = f.toString().toLowerCase();
        if (!file.endsWith(".jpg") && !file.endsWith(".jpeg")) 
            return NOT_JPG;
        date = Metadata.dateTaken(f);
        if (date == null || date.length() == 0) 
            return DATE_NF;
        java.util.Date d = SDF.parse(date);
        if (d.getTime() == f.lastModified()) 
            return DATE_OK;
        f.setLastModified(d.getTime());
        return FIXED;
    }
    public void fixFile(File f) {
        cnt++;
        try {
            String msg = fixFileDate(f);
            if (msg == DATE_OK) {
                ok++; msg = date;
            }
            if (msg == FIXED) {
                fix++; msg = date;
            }
            output("  "+msg+"  "+f.getName());
        } catch (ParseException e) {
            System.out.println(e);
        }
    }  
    public void fixDirectory(File p) {
        if (p == null || !p.exists()) 
            throw new RuntimeException(p+NOT_FOUND);
        cnt = 0; ok = 0; fix = 0; 
        if (!p.isDirectory()) 
            p = p.getAbsoluteFile().getParentFile();
        output("\n"+p);
        for (File f: p.listFiles()) 
            if (f.isFile()) fixFile(f);
        output(cnt+" files processed: "+ok+" OK, "+fix+" fixed");
        //System.out.printf("%s files: %s OK, %s fixed%n", cnt, ok, fix);
    }
    public void output(String s) {
        if (cons == null) 
            System.out.println(s);
        else cons.write(s);
    }
    public void run() {
        cons = new Console("DirectoryFixer");
        cons.setLocation(100, 0);
        cons.write("Please select a file to fix the modification time");
        cons.write("(All JPEG files in that directory will be fixed)");
        FileDialog FD = new FileDialog(cons, "Select file");
        FD.setLocation(500, 0);
        FD.setDirectory(System.getProperty("user.dir"));
        FD.setVisible(true); //show modal dialog
        while (FD.getFile() != null) {
             fixDirectory(new File(FD.getDirectory(), FD.getFile()));
             FD.setVisible(true);
        }
        System.out.println("Thread is terminated");
        FD.dispose(); cons.write("End of run");
    }
    
    public static void main(String[] args) {
        DX.fixDirectory(new File("."));
    }
    public static void start() {
        try {
            new Thread(DX).start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
