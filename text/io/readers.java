abstract class Reader {
	abstract public int read(char c[], int i, int j) throws IOException;
    abstract void close() throws IOException;
	int read(char cbuf[]) throws IOException {
		return read(cbuf, 0, cbuf.length);
	}
	int read() throws IOException {
		char cb[] = new char[1];
		if(read(cb, 0, 1) == -1)
			return -1;
		else
			return cb[0];
	}
	int read(char cbuf[]) throws IOException {
		return read(cbuf, 0, cbuf.length);
	}
	boolean ready() throws IOException {
		return false;
	}
}

class InputStreamReader extends Reader {

    private static final int defaultByteBufferSize = 8192;
    private byte bb[];     /* Input buffer */
	private ByteToCharConverter btc;
	private InputStream in;

	public InputStreamReader(InputStream in) {
		this(in, ByteToCharConverter.getDefault());
	}
	public InputStreamReader(InputStream in, String enc) throws UnsupportedEncodingException {
		this(in, ByteToCharConverter.getConverter(enc));
	}
	private InputStreamReader(InputStream in, ByteToCharConverter btc) {
		super(in);
		nBytes = 0;
		nextByte = 0;
		if(in == null) {
			throw new NullPointerException("input stream is null");
		} else {
			this.in = in;
			this.btc = btc;
			bb = new byte[8192];
			return;
		}
	}
	private final boolean inReady() {
		try {
			return in.available() > 0;
		}
		catch(IOException ioexception) {
			return false;
		}
	}
	public int read(char cbuf[], int off, int len) throws IOException {
		synchronized(super.lock) {
			ensureOpen();
			if(off < 0 || off > cbuf.length || len < 0 || off + len > cbuf.length || off + len < 0)
				throw new IndexOutOfBoundsException();
			if(len == 0)
				return 0;
			else
				return fill(cbuf, off, off + len);
		}
	}
	public boolean ready() throws IOException {
		synchronized(super.lock) {
			ensureOpen();
			return nextByte < nBytes || inReady();
		}
	}
	public void close() throws IOException {
		synchronized(super.lock) {
			if(in == null)
				return;
			in.close();
			in = null;
			bb = null;
			btc = null;
		}
    }
}

public class FileReader extends InputStreamReader {

	public FileReader(String fileName) throws FileNotFoundException {
		super(new FileInputStream(fileName));
	}
	public FileReader(File file) throws FileNotFoundException {
		super(new FileInputStream(file));
	}
	public FileReader(FileDescriptor fd) {
		super(new FileInputStream(fd));
	}
}