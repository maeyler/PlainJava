import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

class ToolsReader {

    byte[] b = new byte[18];
    int version;
    
    public ToolsReader(File f) {
      try {
        //System.out.println(f+" "+f.length());
        ZipFile zf = new ZipFile(f);
        //System.out.println(zf+" "+zf.size());
        ZipEntry e = zf.getEntry("com/sun/tools/javac/Main.class");
        //System.out.println(e+" "+e.getSize());
        InputStream in = zf.getInputStream(e);
        //System.out.println(in.available()+" bytes");
        in.read(b); in.close();
        version = b[7];
      } catch (Exception x) {
        System.out.println(x);
      }
    }
    public String toString() {
        if (version == 45) return "V1.3";
        if (version == 46) return "V1.4";
        if (version > 48) return "V"+(version-44);
        return "unknown:"+version;
    }
    public static void report(String n) {
        File f = new File(n);
        String s = (f.exists()? ": "+f.length() : "  NOT FOUND");
        System.out.println(f+s);
        if (!f.exists()) return;
        ToolsReader t = new ToolsReader(f);
        System.out.println("Version: "+t.version+" -> "+t);
    }
    public static void main(String[] args) {
        report("\\java\\jars\\tools5.jar");
        report("\\java\\work\\tools6.jar");
        report("\\java\\jdk7\\lib\\tools.jar");
    }
}
/* output
\java\jars\tools5.jar: 6786469 bytes
Version: 49 -> V5
\java\work\tools6.jar: 12605641
Version: 49 -> V5  <-- V6 olmalýydý!!
\java\jdk7\lib\tools.jar: 15176132
Version: 51 -> V7
*/
