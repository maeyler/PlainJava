package java.io;


// Referenced classes of package java.io:
//			FilterInputStream, DataInput, IOException, InputStream, 
//			EOFException, PushbackInputStream, UTFDataFormatException

public class DataInputStream extends FilterInputStream
	implements DataInput {

	private char lineBuffer[];

	public final int read(byte b[]) throws IOException {
		return super.in.read(b, 0, b.length);
	}

	public final int read(byte b[], int off, int len) throws IOException {
		return super.in.read(b, off, len);
	}

	public final void readFully(byte b[]) throws IOException {
		readFully(b, 0, b.length);
	}

	public final void readFully(byte b[], int off, int len) throws IOException {
		if(len < 0)
			throw new IndexOutOfBoundsException();
		InputStream in = super.in;
		int count;
		for(int n = 0; n < len; n += count) {
			count = in.read(b, off + n, len - n);
			if(count < 0)
				throw new EOFException();
		}

	}

	public final int skipBytes(int n) throws IOException {
		InputStream in = super.in;
		int total = 0;
		for(int cur = 0; total < n && (cur = (int)in.skip(n - total)) > 0; total += cur);
		return total;
	}

	public final boolean readBoolean() throws IOException {
		int ch = super.in.read();
		if(ch < 0)
			throw new EOFException();
		else
			return ch != 0;
	}

	public final byte readByte() throws IOException {
		int ch = super.in.read();
		if(ch < 0)
			throw new EOFException();
		else
			return (byte)ch;
	}

	public final int readUnsignedByte() throws IOException {
		int ch = super.in.read();
		if(ch < 0)
			throw new EOFException();
		else
			return ch;
	}

	public final short readShort() throws IOException {
		InputStream in = super.in;
		int ch1 = in.read();
		int ch2 = in.read();
		if((ch1 | ch2) < 0)
			throw new EOFException();
		else
			return (short)((ch1 << 8) + ch2);
	}

	public final int readUnsignedShort() throws IOException {
		InputStream in = super.in;
		int ch1 = in.read();
		int ch2 = in.read();
		if((ch1 | ch2) < 0)
			throw new EOFException();
		else
			return (ch1 << 8) + ch2;
	}

	public final char readChar() throws IOException {
		InputStream in = super.in;
		int ch1 = in.read();
		int ch2 = in.read();
		if((ch1 | ch2) < 0)
			throw new EOFException();
		else
			return (char)((ch1 << 8) + ch2);
	}

	public final int readInt() throws IOException {
		InputStream in = super.in;
		int ch1 = in.read();
		int ch2 = in.read();
		int ch3 = in.read();
		int ch4 = in.read();
		if((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();
		else
			return (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + ch4;
	}

	public final long readLong() throws IOException {
		InputStream in = super.in;
		return ((long)readInt() << 32) + ((long)readInt() & 0xffffffffL);
	}

	public final float readFloat() throws IOException {
		return Float.intBitsToFloat(readInt());
	}

	public final double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}

	/**
	 * @deprecated Method readLine is deprecated
	 */

	public final String readLine() throws IOException {
		InputStream in = super.in;
		char buf[] = lineBuffer;
		if(buf == null)
			buf = lineBuffer = new char[128];
		int room = buf.length;
		int offset = 0;
		int c;
label0:
		do
			switch(c = in.read()) {
			case -1: 
			case 10: // '\n'
				break label0;

			case 13: // '\r'
				int c2 = in.read();
				if(c2 != 10 && c2 != -1) {
					if(!(in instanceof PushbackInputStream))
						in = super.in = new PushbackInputStream(in);
					((PushbackInputStream)in).unread(c2);
				}
				break label0;

			case 0: // '\0'
			case 1: // '\001'
			case 2: // '\002'
			case 3: // '\003'
			case 4: // '\004'
			case 5: // '\005'
			case 6: // '\006'
			case 7: // '\007'
			case 8: // '\b'
			case 9: // '\t'
			case 11: // '\013'
			case 12: // '\f'
			default:
				if(--room < 0) {
					buf = new char[offset + 128];
					room = buf.length - offset - 1;
					System.arraycopy(lineBuffer, 0, buf, 0, offset);
					lineBuffer = buf;
				}
				buf[offset++] = (char)c;
				break;
			}
		while(true);
		if(c == -1 && offset == 0)
			return null;
		else
			return String.copyValueOf(buf, 0, offset);
	}

	public final String readUTF() throws IOException {
		return readUTF(((DataInput) (this)));
	}

	public static final String readUTF(DataInput in) throws IOException {
		int utflen = in.readUnsignedShort();
		StringBuffer str = new StringBuffer(utflen);
		byte bytearr[] = new byte[utflen];
		int count = 0;
		in.readFully(bytearr, 0, utflen);
		while(count < utflen)  {
			int c = bytearr[count] & 0xff;
			switch(c >> 4) {
			case 0: // '\0'
			case 1: // '\001'
			case 2: // '\002'
			case 3: // '\003'
			case 4: // '\004'
			case 5: // '\005'
			case 6: // '\006'
			case 7: // '\007'
			{
				count++;
				str.append((char)c);
				break;
			}

			case 12: // '\f'
			case 13: // '\r'
			{
				if((count += 2) > utflen)
					throw new UTFDataFormatException();
				int char2 = bytearr[count - 1];
				if((char2 & 0xc0) != 128)
					throw new UTFDataFormatException();
				str.append((char)((c & 0x1f) << 6 | char2 & 0x3f));
				break;
			}

			case 14: // '\016'
			{
				if((count += 3) > utflen)
					throw new UTFDataFormatException();
				int char2 = bytearr[count - 2];
				int char3 = bytearr[count - 1];
				if((char2 & 0xc0) != 128 || (char3 & 0xc0) != 128)
					throw new UTFDataFormatException();
				str.append((char)((c & 0xf) << 12 | (char2 & 0x3f) << 6 | char3 & 0x3f));
				break;
			}

			case 8: // '\b'
			case 9: // '\t'
			case 10: // '\n'
			case 11: // '\013'
			default:
			{
				throw new UTFDataFormatException();
			}
			}
		}
		return new String(str);
	}

	public DataInputStream(InputStream in) {
		super(in);
	}
}