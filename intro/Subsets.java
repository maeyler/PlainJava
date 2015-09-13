import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collection;

class Subsets {
    Object[] x;  int cnt;
    Collection<Object> c;
    public Subsets(int n) {
        this(generate(n));
    }
    public Subsets(Object[] a) {
        x = a; cnt = 0;
        c = new HashSet<Object>();
          //new ArrayList<Object>(); 
    }
    public void run() {
        long t = System.currentTimeMillis();
        start(0);
        t = System.currentTimeMillis() - t;
        System.out.printf("%2s %9s  %s %n",x.length, cnt, t);
    }
    void start(int i) {
        if (i == x.length) {
            cnt++;
            if (x.length < 5) System.out.println(c);
        } else {
            start(i+1);
            c.add(x[i]);
            start(i+1);
            c.remove(x[i]);
        }
    }
    
    static final String[] S = {"C", "A", "P", "E"};
    public static String[] generate(int n) {
        ArrayList<String> L = new ArrayList<String>(n);
        for (int i=0; i<n; i++) L.add(""+(char)('A'+i));
        String[] a = new String[n]; L.toArray(a);
        return a;
    }
    public static void main(String[] args) {
        for (int i=4; i<27; i++)
            new Subsets(i).run();
    }
}
