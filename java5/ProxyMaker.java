import java.util.*;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;

/* 
public class ProxyMaker<T> {  //generic version
    static ProxyMaker pm = new ProxyMaker();  //singular instance
    public ProxyMaker() { }   //other instances may be needed for generic code
    public T makeProxy(Class<T> c, T s) { //cannot be static!!
        if (!c.isInterface()) 
            throw new RuntimeException("Not an interface: "+c);
        if (!c.isInstance(s)) 
            throw new RuntimeException("Wrong interface: "+c);
        ClassLoader d = s.getClass().getClassLoader();
        Class[] ca = { c };
        InvocationHandler h = new IHLogger<T>(s);
        return (T)Proxy.newProxyInstance(d, ca, h);
    }   
    public static Object makeProxy(String cn, Object s) throws ClassNotFoundException { 
        return pm.makeProxy(Class.forName(cn), s);
    }   
    public static List newProxy(List s) {
        return new ProxyMaker<List>().makeProxy(List.class, s);
    }   
    public static Set newProxy(Set s) {
        return new ProxyMaker<Set>().makeProxy(Set.class, s);
    }
*/
public class ProxyMaker    {  //simple version
    ProxyMaker() { } //private constructor is hidden
    public static Set newProxy(Set s) {
        ClassLoader d = s.getClass().getClassLoader();
        Class[] ca = { Set.class };
        InvocationHandler h = new IHLogger(s);
        return (Set)Proxy.newProxyInstance(d, ca, h);
    }
    public static void main(String[] args) {
        Set s = newProxy(new HashSet());
        s.add("three");
        if (!s.contains("five")) s.add("four");
        System.out.println(s);    
    }
}

/*    class IHLogger<T> implements InvocationHandler {  //generic version
        final T obj;
        public IHLogger(T u)   { obj = u; }
*/
  class IHLogger    implements InvocationHandler {  //simple version
        final Set obj;
        public IHLogger(Set u) { obj = u; }
        public Object invoke(Object p, Method m, Object[] a) throws Throwable {
            String s = m.getName();  
            Object res = m.invoke(obj , a);
            //these two methods are invoked very often in SSS
            if (s.equals("size") || s.equals("toString")) return res;
            s += "(";  //arguments
            for (int i=0; a != null && i<a.length; i++) {
                if (i > 0) s += ", ";
                String ai = a[i].toString();
                if (ai.length() < 27) s += ai;
                else s += ai.substring(0,22)+"...";
            }
            s += ")";
            if (res != null) s += " -> "+res;
            System.out.print("  "); System.out.println(s);
            return res;
        }
    }
    
    class ArraySet<E> extends ArrayList<E> implements Set<E> {
        public ArraySet() { }
        public boolean add(E x) {
            if (contains(x)) return false;
            return super.add(x);
        }  
        public boolean addAll(Collection<? extends E> c) {
            boolean added = false;
            for (E x: c) if (add(x)) added = true;
            return added;
        }  
    }
   
/*  OUTPUT:
add(three) -> true
contains(five) -> false
add(four) -> true
toString() -> [three, four]
[three, four]
*/
