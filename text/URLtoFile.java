import java.io.*;
import java.net.URL;

class URLtoFile {

    public static final int M = 32768; //buffer size
    
    public URLtoFile(String s1, String s2) throws Exception {
        this(new URL(s1), new File(s2));
    }
    public URLtoFile(URL u, File f) throws IOException {
        System.out.println(f+": ");
        InputStream in = u.openStream();
        OutputStream out = new FileOutputStream(f);
        int b = 0; 
        byte[] buf = new byte[M]; //buffer
        while (true) {
            int m = in.read(buf); 
            if (m <= 0) break;
            out.write(buf, 0, m);
            b = b + m;
            System.out.println(m);
        }
        in.close(); out.close(); 
        System.out.println(b+" bytes");
    }
   public static void main(String[] args) throws Exception {
       String s1 = 
           "http://www.isyatirim.com.tr/in_LLT_stockslistedonise.aspx";
       String s2 = "test.html";
       new URLtoFile(s1, s2);
   }
}
// http://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html
