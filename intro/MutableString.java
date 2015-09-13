import java.net.URL;
import java.io.Serializable;
import java.io.InputStream;
import java.lang.reflect.Field;

public class MutableString implements Serializable, Comparable<String>, CharSequence {
    final char[] ca;
    final String str;
    int modCount;
    public MutableString(InputStream in) { this(streamToString(in)); }
    public MutableString(String s) { str = s; ca = getValue(s); }
    //public char[] array() { return ca; }
    public String toString() { return str; }
    //public int hashCode() { return str.hashCode(); }
    //public boolean equals(Object s) { return str.equals(s); }
    public int compareTo(String s) { return str.compareTo(s); }
    public int length() { return ca.length; }
    public char charAt(int p) { return ca[p]; }
    public CharSequence subSequence(int start, int end) { 
        return str.subSequence(start, end); 
    }
    public int modificationCount() { return modCount; }
    public void put(int p, char c) { modCount++; ca[p] = c; }
    public void put(int p, String s) { put(p, (CharSequence)s); }
    public void put(int p, CharSequence s) {
        modCount++; 
        for (int i=0; p<ca.length && i<s.length(); p++, i++) 
            ca[p] = s.charAt(i); 
    }
    public int replaceAll(char c, char r) { 
        int n = 0;
        for (int p=0; p<ca.length; p++) if (ca[p] == c) {
            n++; ca[p] = r; 
        }
        return n; 
    }
    
    public static String streamToString(InputStream in) { //question 2a
        try {
            byte[] ba = new byte[in.available()];
            in.read(ba);
            return new String(ba);
        } catch (Exception x) { 
            return null;
        }
    }
    public static char[] getValue(String s) { //returns "value" field of s 
        return (char[])getFieldValue(s, "value"); 
    }
    public static Object getFieldValue(Object b, String name) {
        try {
            Class c = b.getClass();
            Field f = c.getDeclaredField(name);
            f.setAccessible(true);
            return f.get(b);
        } catch (Exception x) { 
            return null;
        }
    }
    public static String[] question2c(String r) throws Exception {
        //return streamToString(new URL(r).openStream()).split("\\n");
        URL u = new URL(r);
        InputStream i = u.openStream();
        String s = streamToString(i);
        return s.split("\\n");
    }
    final static MutableString M = new MutableString("Small is beautiful");
    public static void modify() {
        CharSequence t = M.subSequence(9, 18); //"beautiful"
        M.put(0, t); M.put(9, ' '); M.put(10, "question");
    }
    public static void main(String[] args) {
        System.out.println(M); //prints the original String
        modify();
        System.out.println(M); //prints the modified String
    }
}
