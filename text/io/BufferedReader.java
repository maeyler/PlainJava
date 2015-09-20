// Decompiled by DJ v2.9.9.61 Copyright 2000 Atanas Neshkov  Date: 08.04.2002 12:10:20
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) fieldsfirst nonlb 
// Source File Name:   BufferedReader.java

package java.io;


// Referenced classes of package java.io:
//			Reader, IOException

public class BufferedReader extends Reader {

	private static final int INVALIDATED = -2;
	private static final int UNMARKED = -1;
	private static int defaultCharBufferSize = 8192;
	private static int defaultExpectedLineLength = 80;
	private Reader in;
	private char cb[];
	private int nChars;
	private int nextChar;
	private int markedChar;
	private int readAheadLimit;
	private boolean skipLF;
	private boolean markedSkipLF;

	private final void ensureOpen() throws IOException {
		if(in == null)
			throw new IOException("Stream closed");
		else
			return;
	}

	private final void fill() throws IOException {
		int dst;
		if(markedChar <= -1) {
			dst = 0;
		} else {
			int delta = nextChar - markedChar;
			if(delta >= readAheadLimit) {
				markedChar = -2;
				readAheadLimit = 0;
				dst = 0;
			} else {
				if(readAheadLimit <= cb.length) {
					System.arraycopy(cb, markedChar, cb, 0, delta);
					markedChar = 0;
					dst = delta;
				} else {
					char ncb[] = new char[readAheadLimit];
					System.arraycopy(cb, markedChar, ncb, 0, delta);
					cb = ncb;
					markedChar = 0;
					dst = delta;
				}
				nextChar = nChars = delta;
			}
		}
		int n;
		do
			n = in.read(cb, dst, cb.length - dst);
		while(n == 0);
		if(n > 0) {
			nChars = dst + n;
			nextChar = dst;
		}
	}

	public int read() throws IOException {
		synchronized(super.lock) {
			ensureOpen();
			do {
				if(nextChar >= nChars) {
					fill();
					if(nextChar >= nChars)
						return -1;
				}
				if(!skipLF)
					break;
				skipLF = false;
				if(cb[nextChar] != '\n')
					break;
				nextChar++;
			} while(true);
			return cb[nextChar++];
		}
	}

	private final int read1(char cbuf[], int off, int len) throws IOException {
		if(nextChar >= nChars) {
			if(len >= cb.length && markedChar <= -1 && !skipLF)
				return in.read(cbuf, off, len);
			fill();
		}
		if(nextChar >= nChars)
			return -1;
		if(skipLF) {
			skipLF = false;
			if(cb[nextChar] == '\n') {
				nextChar++;
				if(nextChar >= nChars)
					fill();
				if(nextChar >= nChars)
					return -1;
			}
		}
		int n = Math.min(len, nChars - nextChar);
		System.arraycopy(cb, nextChar, cbuf, off, n);
		nextChar += n;
		return n;
	}

	public int read(char cbuf[], int off, int len) throws IOException {
		synchronized(super.lock) {
			ensureOpen();
			if(off < 0 || off > cbuf.length || len < 0 || off + len > cbuf.length || off + len < 0)
				throw new IndexOutOfBoundsException();
			if(len == 0)
				return 0;
			int n = read1(cbuf, off, len);
			if(n <= 0) {
				return n;
			} else {
				int n1;
				for(; n < len && in.ready(); n += n1) {
					n1 = read1(cbuf, off + n, len - n);
					if(n1 <= 0)
						break;
				}

				return n;
			}
		}
	}

	String readLine(boolean ignoreLF) throws IOException {
		StringBuffer s = null;
		boolean omitLF = ignoreLF || skipLF;
		synchronized(super.lock) {
			ensureOpen();
			do {
				if(nextChar >= nChars)
					fill();
				if(nextChar >= nChars)
					if(s != null && s.length() > 0)
						return s.toString();
					else
						return null;
				boolean eol = false;
				char c = '\0';
				if(omitLF && cb[nextChar] == '\n')
					nextChar++;
				skipLF = false;
				omitLF = false;
				int i;
				for(i = nextChar; i < nChars; i++) {
					c = cb[i];
					if(c != '\n' && c != '\r')
						continue;
					eol = true;
					break;
				}

				int startChar = nextChar;
				nextChar = i;
				if(eol) {
					String str;
					if(s == null) {
						str = new String(cb, startChar, i - startChar);
					} else {
						s.append(cb, startChar, i - startChar);
						str = s.toString();
					}
					nextChar++;
					if(c == '\r')
						skipLF = true;
					return str;
				}
				if(s == null)
					s = new StringBuffer(defaultExpectedLineLength);
				s.append(cb, startChar, i - startChar);
			} while(true);
		}
	}

	public String readLine() throws IOException {
		return readLine(false);
	}

	public long skip(long n) throws IOException {
		if(n < 0L)
			throw new IllegalArgumentException("skip value is negative");
		synchronized(super.lock) {
			ensureOpen();
			long r;
			for(r = n; r > 0L;) {
				if(nextChar >= nChars)
					fill();
				if(nextChar >= nChars)
					break;
				if(skipLF) {
					skipLF = false;
					if(cb[nextChar] == '\n')
						nextChar++;
				}
				long d = nChars - nextChar;
				if(r <= d) {
					nextChar += r;
					r = 0L;
					break;
				}
				r -= d;
				nextChar = nChars;
			}

			return n - r;
		}
	}

	public boolean ready() throws IOException {
		synchronized(super.lock) {
			ensureOpen();
			return nextChar < nChars || in.ready();
		}
	}

	public boolean markSupported() {
		return true;
	}

	public void mark(int readAheadLimit) throws IOException {
		if(readAheadLimit < 0)
			throw new IllegalArgumentException("Read-ahead limit < 0");
		synchronized(super.lock) {
			ensureOpen();
			this.readAheadLimit = readAheadLimit;
			markedChar = nextChar;
			markedSkipLF = skipLF;
		}
	}

	public void reset() throws IOException {
		synchronized(super.lock) {
			ensureOpen();
			if(markedChar < 0)
				throw new IOException(markedChar != -2 ? "Stream not marked" : "Mark invalid");
			nextChar = markedChar;
			skipLF = markedSkipLF;
		}
	}

	public void close() throws IOException {
		synchronized(super.lock) {
			if(in == null)
				return;
			in.close();
			in = null;
			cb = null;
		}
	}

	public BufferedReader(Reader in, int sz) {
		super(in);
		markedChar = -1;
		readAheadLimit = 0;
		skipLF = false;
		markedSkipLF = false;
		if(sz <= 0) {
			throw new IllegalArgumentException("Buffer size <= 0");
		} else {
			this.in = in;
			cb = new char[sz];
			nextChar = nChars = 0;
			return;
		}
	}

	public BufferedReader(Reader in) {
		this(in, defaultCharBufferSize);
	}

}