package java.io;


// Referenced classes of package java.io:
//			InputStream, FileNotFoundException, IOException, FileDescriptor, 
//			File

public class FileInputStream extends InputStream {

	private FileDescriptor fd;

	private final native void open(String s) throws FileNotFoundException;

	public native int read() throws IOException;

	private final native int readBytes(byte abyte0[], int i, int j) throws IOException;

	public int read(byte b[]) throws IOException {
		return readBytes(b, 0, b.length);
	}

	public int read(byte b[], int off, int len) throws IOException {
		return readBytes(b, off, len);
	}

	public native long skip(long l) throws IOException;

	public native int available() throws IOException;

	public native void close() throws IOException;

	public final FileDescriptor getFD() throws IOException {
		if(fd != null)
			return fd;
		else
			throw new IOException();
	}

	private static final native void initIDs();

	protected void finalize() throws IOException {
		if(fd != null && fd != FileDescriptor.in)
			close();
	}

	public FileInputStream(String name) throws FileNotFoundException {
		SecurityManager security = System.getSecurityManager();
		if(security != null)
			security.checkRead(name);
		fd = new FileDescriptor();
		open(name);
	}

	public FileInputStream(File file) throws FileNotFoundException {
		this(file.getPath());
	}

	public FileInputStream(FileDescriptor fdObj) {
		SecurityManager security = System.getSecurityManager();
		if(fdObj == null)
			throw new NullPointerException();
		if(security != null)
			security.checkRead(fdObj);
		fd = fdObj;
	}

	static final  {
		initIDs();
	}
}