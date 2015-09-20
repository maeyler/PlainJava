package java.io;


// Referenced classes of package java.io:
//			IOException

public abstract class InputStream {

	private static final int SKIP_BUFFER_SIZE = 2048;
	private static byte skipBuffer[];

	public abstract int read() throws IOException;

	public int read(byte b[]) throws IOException {
		return read(b, 0, b.length);
	}

	public int read(byte b[], int off, int len) throws IOException {
		if(b == null)
			throw new NullPointerException();
		if(off < 0 || off > b.length || len < 0 || off + len > b.length || off + len < 0)
			throw new IndexOutOfBoundsException();
		if(len == 0)
			return 0;
		int c = read();
		if(c == -1)
			return -1;
		b[off] = (byte)c;
		int i = 1;
		try {
			while(i < len)  {
				c = read();
				if(c == -1)
					break;
				if(b != null)
					b[off + i] = (byte)c;
				i++;
			}
		}
		catch(IOException ioexception) { }
		return i;
	}

	public long skip(long n) throws IOException {
		long remaining = n;
		if(skipBuffer == null)
			skipBuffer = new byte[2048];
		byte localSkipBuffer[] = skipBuffer;
		if(n <= 0L)
			return 0L;
		int nr;
		for(; remaining > 0L; remaining -= nr) {
			nr = read(localSkipBuffer, 0, (int)Math.min(2048L, remaining));
			if(nr < 0)
				break;
		}

		return n - remaining;
	}

	public int available() throws IOException {
		return 0;
	}

	public void close() throws IOException {
	}

	public synchronized void mark(int i) {
	}

	public synchronized void reset() throws IOException {
		throw new IOException("mark/reset not supported");
	}

	public boolean markSupported() {
		return false;
	}

	public InputStream() {
	}
}