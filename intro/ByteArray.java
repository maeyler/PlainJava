//import java.util.Arrays;

class ByteArray implements CharSequence {
    final static String S = "yanýlmadan denemek mümkün mü"; 
    byte[] data;
    public ByteArray(byte[] a) { data = a; }
    public char charAt(int i) { return (char)data[i]; }
    public int length() { return data.length; }
    public CharSequence subSequence(int start, int end) {
        return null; 
        //new ByteArray(Arrays.copyOfRange(data, start, end)); DELETED
    }
    /* public String toString() { DELETED
        return new String(data);
    }*/
    public static void main(String[] args) {
        ByteArray b = new ByteArray(S.getBytes());
        System.out.println(b);
        System.out.printf("length()=%s  charAt(3)=%s", b.length(), b.charAt(3));
    }
}
