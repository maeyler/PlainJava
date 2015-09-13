import java.util.HashSet;
import java.util.HashMap;
import java.util.Map.Entry;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

class HashCounter { //collect collision statistics on a HashMap

    //final HashMap map;
    final Entry[] table; //java.util.Map.Entry
    final Object[] key;
    final int[] count;
    int size, sum, used, max;
    Field next;  //a little concern for efficiency

    public HashCounter(HashSet s) {
        this((HashMap)getFieldValue(s, "map"));
    }
    public HashCounter(HashMap m) {
        table = (Entry[])getFieldValue(m, "table");
        size = m.size();  //map = m; 
        int n = table.length;
        key = new Object[n];
        count = new int[n];
        for (int i=0; i<n; i++) {
            Entry e = table[i];
            if (e == null) continue;
            if (next == null) 
                next = getField(e.getClass(), "next");
            key[i] = e.getKey(); int c = 0;
            while (e != null) {
                e = (Entry)getFieldValue(e, next); c++;
            }
            count[i] = c; sum+= c; used++;
            if (c > max) max = c;
        }
    }
    public void report(String fName) {
        PrintStream out;
        try {
            out = new PrintStream(fName);
         } catch (Exception x) { //FileNotFoundException
            out = System.out;
        }
        int k = report(out);
        out.close();
        System.out.printf("%s entries written to %s %n", k, fName);
    }
    public int report(PrintStream out) {
        int k = 0;
        int n = table.length;
        out.printf("%n Table size: %s %n", n);
        for (int i=0; i<n; i++) 
            if (table[i] != null) {
                out.printf("%25s %s %n", key[i], count[i]); k++;
            }
        out.printf(" Map size: %s  sum: %s %n", size, sum);
        out.printf("     used: %s  max: %s %n", used, max);
        return k; 
    }

    //static methods: a brief introduction to java.lang.reflect
    
    public static Field getField(Class c, String name) {
        try {
            Field f = c.getDeclaredField(name);
            f.setAccessible(true);
            return f;
        } catch (Exception x) { //NoSuchFieldException
            return null;
        }
    }
    public static Object getFieldValue(Object b, Field f) {
        try {
            return f.get(b);
        } catch (Exception x) { //IllegalAccessException
            return null;
        }
    }
    public static Object getFieldValue(Object b, String name) {
        Field f = getField(b.getClass(), name);
        return getFieldValue(b, f);
    }
    public static Method[] publicMethods(Object x) {
        Method[] M = x.getClass().getMethods(); 
        for (Method m: M) m.setAccessible(true);
        return M;
    }
    public static Field[] declaredFields(Object x) {
        Field[] F = x.getClass().getDeclaredFields();
        for (Field f: F) f.setAccessible(true);
        return F;
    }
    public static void printFields(Object x) {
        System.out.printf("%n Reflection on %s: %n", x.getClass().getName());
        for (Field f: declaredFields(x)) 
            if (!Modifier.isStatic(f.getModifiers())) {
                String val = ""+getFieldValue(x, f); //might be null
                if (val.length()>50) val = val.substring(0,45)+" ...";
                System.out.printf("%s = %s %n", f.getName(), val);
            }
    }
    public static void main(String[] a) {
        printFields("Something");
        printFields(new Float(3.14159));
        
        HashMap<String, String> m = new HashMap<String, String>();
        m.put("small", null);
        m.put("is->was", null);
        m.put("beautiful", null);
        m.put("over here", null);
        printFields(m);
        
        HashCounter h = new HashCounter(m);
        printFields(h); h.report(System.out);
    }
}
