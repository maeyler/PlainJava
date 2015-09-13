import java.util.*;

class HW4 {
    public static Collection test(Collection c) {
        System.out.println(c.size()+" "+c); return c;
    }
    public static void main(String[] args) throws Exception {
        test(new TreeSet(test(Arrays.asList(
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
            .getData(java.awt.datatransfer.DataFlavor.stringFlavor)
            .toString().toLowerCase().split("\\p{Space}|\\p{Punct}")
        ))));
    }
}
