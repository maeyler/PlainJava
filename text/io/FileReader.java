package java.io;


// Referenced classes of package java.io:
//			InputStreamReader, FileNotFoundException, FileInputStream, File, 
//			FileDescriptor

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