import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import sun.io.ByteToCharConverter;

abstract class Reader {
   
    abstract void close() throws IOException;
    abstract int read(char[] cc, int i, int j) throws IOException;
    int read(char[] cc) throws IOException {
        return read(cc, 0, cc.length);
    }
    int read() throws IOException { // read one char
    //  return a single char or -1
        char[] cc = new char[1];
        if (read(cc, 0, 1) == -1)
            return -1;
        else
            return cc[0];
    }
    boolean ready() throws IOException {
        return false;
    }
}

class StreamReader extends Reader {

    static final int bufferSize = 8192;
    byte[] bb;   // Input buffer
    ByteToCharConverter btc;
    InputStream in;

    StreamReader(InputStream s) {
        if (s == null) {
            throw new NullPointerException("null stream");
        } else {
            in = s; bb = new byte[bufferSize];
            btc = ByteToCharConverter.getDefault();
            return;
        }
    }
    boolean ready() throws IOException {
        return in.available() > 0;
    }
    int read(char[] cc, int off, int len) throws IOException {
        if (off < 0 || len < 0 || off + len > cc.length)
            throw new IndexOutOfBoundsException();
        if (len == 0)
            return 0;
        else {
            int n = in.read(bb, off, len);
            btc.convert(bb, off, off+len, cc, off, off+len);
            return n;
        }
    }
    void close() throws IOException {
        if (in == null) return;
        in.close(); in = null;
        bb = null; btc = null;
    }
}

class FileReader extends StreamReader {

    String name;
    FileReader(String fName) throws IOException {
        super(new FileInputStream(fName));
        name = fName;
    }
}
class TestRdr {
   
    public static void main(String[] a) throws IOException {
       Reader R = new FileReader("Test.txt");
       System.out.println(R);
       Reader I = new StreamReader(System.in);
       System.out.println(I);
       FileReader F = (FileReader)R;
       System.out.println(F.name);
       System.out.println(F.btc);
       char[] cc = new char[F.bufferSize];
       while (R.ready()) {
          int n = R.read(cc);
          System.out.print(new String(cc, 0, n));
       }
       I.close(); R.close();
    }
}
