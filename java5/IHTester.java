import java.util.*;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;

public class IHTester<U> {
    public IHTester() { }
    public U newProxy(Class<?> c, U s) {
        InvocationHandler h = new IHLogger<U>(s);
        ClassLoader d = s.getClass().getClassLoader();
        return (U)Proxy.newProxyInstance(d, new Class[] { c }, h);
//OR        return (U)Proxy.getProxyClass(d, c).
//            getConstructor(InvocationHandler.class).newInstance(h);
    }   
    public static List newProxy(List s) {
        return new IHTester<List>().newProxy(List.class, s);
    }   
    public static Set newProxy(Set s) {
        return new IHTester<Set>().newProxy(Set.class, s);
    }   
    public static void main(String[] args) {
        Set s = newProxy(new HashSet());
        s.add("three");
        if (!s.contains("four")) s.add("four");
        System.out.println(s);    
    }
}

    class IHLogger<T> implements InvocationHandler {
        final T obj;
        public IHLogger(T u) {
            obj = u;
        }
        public String toString() {
            return "h"+obj.toString();
        }
        public Object invoke(Object p, Method m, Object[] a) throws Throwable {
            StringBuffer sb = new StringBuffer();
            //sb.append(p.getClass()); sb.append(" ");
            sb.append(m.getName()); sb.append("(");
            for (int i=0; a != null && i<a.length; i++) {
                if (i != 0) sb.append(", ");
                sb.append(a[i]);
            }
            sb.append(")");
            Object ret = m.invoke(obj , a);
            if (ret != null) {
                sb.append(" -> "); sb.append(ret);
            }
            System.out.println(sb);
            return ret;
        }
    }   
/*
add(three) -> true
contains(four) -> false
add(four) -> true
toString() -> [three, four]
[three, four]
*/
