import java.io.*;

class Unicode {
    public static void firstUnicode () {
        try {
            InputStream in = new FileInputStream("UTF-8.txt");
            byte[] ba = new byte[in.available()];
            in.read(ba); //--> 47
            in.close();
            String s = new String(ba, "UTF-8");
            System.out.println(s);
            System.out.println((int)s.charAt(0));
        } catch(Exception x) { 
            throw new RuntimeException(x);
        }
    }
    public static void printASCII() {
        for (char c=32, i=1; c<256; c++, i++) {
            System.out.printf("%s ", c);
            if (i < 16) continue;
            i=0; System.out.println(); 
        }
    }
    public static void main(String[] args) {
        firstUnicode();
        printASCII(); 
    }
}
