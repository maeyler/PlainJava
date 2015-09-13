import java.util.*;

class Subsets2 implements Runnable {
    int cnt;
    final boolean trace;
    final Object[] x;
    final List<Object> c;  
    
    public Subsets2(int n, boolean t) {
        this(generate(n), t);
    }
    public Subsets2(Object[] a, boolean t) {
        cnt = 0; x = a; trace = t;
        c = new ArrayList<Object>(Arrays.asList(a));
    }
    public Subsets2(List<Object> a, boolean t) {
        cnt = 0; c = a; trace = t; 
        x = c.toArray(new Object[a.size()]);
    }
    public void run() {
        long t = System.currentTimeMillis();
        sub(c.size()-1);
        t = System.currentTimeMillis() - t;
        System.out.printf("%2s %9s  %s %n", c.size(), cnt, t);
    }
    void sub(int i) {
        if (i < 0) {
            cnt++;
            if (trace) 
                System.out.println(c);
        } else {
            sub(i-1);
            c.remove(x[i]);
            sub(i-1);
            c.add(x[i]);
        }
    }
    
    public static List<Object> generate(int n) {
        List<Object> L = new ArrayList<Object>(n);
        for (int i=0; i<n; i++) L.add(""+(char)('A'+i));
        return L;
    }
    public static void main(String[] args) {
        for (int i=3; i<=22; i++)
            new Subsets2(i, i<4).run();
    }
}
