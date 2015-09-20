package java.io;


// Referenced classes of package java.io:
//			IOException

public abstract class Reader {

	private static final int maxSkipBufferSize = 8192;
	protected Object lock;
	private char skipBuffer[];

	public int read() throws IOException {
		char cb[] = new char[1];
		if(read(cb, 0, 1) == -1)
			return -1;
		else
			return cb[0];
	}

	public int read(char cbuf[]) throws IOException {
		return read(cbuf, 0, cbuf.length);
	}

	public abstract int read(char ac[], int i, int j) throws IOException;

	public long skip(long n) throws IOException {
		if(n < 0L)
			throw new IllegalArgumentException("skip value is negative");
		int nn = (int)Math.min(n, 8192L);
		synchronized(lock) {
			if(skipBuffer == null || skipBuffer.length < nn)
				skipBuffer = new char[nn];
			long r;
			int nc;
			for(r = n; r > 0L; r -= nc) {
				nc = read(skipBuffer, 0, (int)Math.min(r, nn));
				if(nc == -1)
					break;
			}

			return n - r;
		}
	}

	public boolean ready() throws IOException {
		return false;
	}

	public boolean markSupported() {
		return false;
	}

	public void mark(int readAheadLimit) throws IOException {
		throw new IOException("mark() not supported");
	}

	public void reset() throws IOException {
		throw new IOException("reset() not supported");
	}

	public abstract void close() throws IOException;

	protected Reader() {
		skipBuffer = null;
		lock = this;
	}

	protected Reader(Object lock) {
		skipBuffer = null;
		if(lock == null) {
			throw new NullPointerException();
		} else {
			this.lock = lock;
			return;
		}
	}
}