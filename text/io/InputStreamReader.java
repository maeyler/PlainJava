package java.io;

import sun.io.ByteToCharConverter;
import sun.io.ConversionBufferFullException;

// Referenced classes of package java.io:
//			Reader, IOException, InputStream, UnsupportedEncodingException

public class InputStreamReader extends Reader {

	private static final int defaultByteBufferSize = 8192;
	private ByteToCharConverter btc;
	private InputStream in;
	private byte bb[];
	private int nBytes;
	private int nextByte;

	public String getEncoding() {
		synchronized(super.lock) {
			if(btc != null)
				return btc.getCharacterEncoding();
			else
				return null;
		}
	}

	private final void malfunction() {
		throw new InternalError("Converter malfunction (" + btc.getCharacterEncoding() + ") -- please submit a bug report via " + System.getProperty("java.vendor.url.bug"));
	}

	private final int convertInto(char cbuf[], int off, int end) throws IOException {
		int nc = 0;
		if(nextByte < nBytes)
			try {
				nc = btc.convert(bb, nextByte, nBytes, cbuf, off, end);
				nextByte = nBytes;
				if(btc.nextByteIndex() != nextByte)
					malfunction();
			}
			catch(ConversionBufferFullException conversionbufferfullexception) {
				nextByte = btc.nextByteIndex();
				nc = btc.nextCharIndex() - off;
			}
		return nc;
	}

	private final int flushInto(char cbuf[], int off, int end) throws IOException {
		int nc = 0;
		try {
			nc = btc.flush(cbuf, off, end);
		}
		catch(ConversionBufferFullException conversionbufferfullexception) {
			nc = btc.nextCharIndex() - off;
		}
		return nc;
	}

	private final int fill(char cbuf[], int off, int end) throws IOException {
		int nc = 0;
		if(nextByte < nBytes)
			nc = convertInto(cbuf, off, end);
		for(; off + nc < end; nc += convertInto(cbuf, off + nc, end)) {
			if(nBytes != -1) {
				if(nc > 0 && !inReady())
					break;
				nBytes = in.read(bb);
			}
			if(nBytes == -1) {
				nBytes = 0;
				nc += flushInto(cbuf, off + nc, end);
				if(nc == 0)
					return -1;
				break;
			}
			nextByte = 0;
		}

		return nc;
	}

	private final boolean inReady() {
		try {
			return in.available() > 0;
		}
		catch(IOException ioexception) {
			return false;
		}
	}

	private final void ensureOpen() throws IOException {
		if(in == null)
			throw new IOException("Stream closed");
		else
			return;
	}

	public int read() throws IOException {
		char cb[] = new char[1];
		if(read(cb, 0, 1) == -1)
			return -1;
		else
			return cb[0];
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
}