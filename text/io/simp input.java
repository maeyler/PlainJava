import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import sun.io.ByteToCharConverter;

abstract class Reader {
    abstract void close() throws IOException;
    abstract int read(char[] cc, int i, int j) throws IOException;
    int read(char[] cc) throws IOException {
        return read(cc, 0, cc.length);
    }
    int read() throws IOException {
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

class InputStreamReader extends Reader {

    private static final int bufferSize = 8192;
    private byte[] bb;     /* Input buffer */
    private ByteToCharConverter btc;
    InputStream in;

    InputStreamReader(InputStream s) {
        btc = ByteToCharConverter.getDefault();
        if (s == null) {
            throw new NullPointerException("input stream is null");
        } else {
            in = s;
            bb = new byte[bufferSize];
            return;
        }
    }
    boolean ready() {
        try {
            return in.available() > 0;
        }
        catch(IOException e) {
            return false;
        }
    }
    int read(char[] cc, int off, int len) throws IOException {
        if (off < 0 || off > cc.length || len < 0 || off + len > cc.length || off + len < 0)
            throw new IndexOutOfBoundsException();
        if (len == 0)
            return 0;
        else
            return btc.convert(bb, off, off+len, cc, off, off+len);

    }
    void close() throws IOException {
        if (in == null)
            return;
        in.close();
        in = null;
        bb = null;
        btc = null;
    }
}

class FileReader extends InputStreamReader {

    String name;
    FileReader(String fName) throws FileNotFoundException {
        super(new FileInputStream(fName));
        name = fName;
    }
}
class TestRdr {
    public static void main(String[] args) throws IOException {
       FileReader R = new FileReader("Test.dat");
       System.out.println(R.name);
       int n = R.read();
       char[] cc = new char[R.in.available()];
       R.read(cc);
    }
}
