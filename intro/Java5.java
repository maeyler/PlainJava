import java.util.List;
import java.util.ArrayList;
import static java.lang.Math.PI;

/** Illustrates Java 5 features  @author: Akif Eyler 2004 */
class Java5 {
    /** Typesafe enumeration: objects of class Kind */
    enum Kind { zero, negative, positive, fractional }
    /** Classifies a given Number */
    public static Kind kindOf(Number n) {
        float i = n.floatValue();  
        if (i == 0) return Kind.zero;
        if (n instanceof Float || n instanceof Double) 
            return Kind.fractional;
        return (i < 0)? Kind.negative : Kind.positive;
    }
    static List<Number> bag = new ArrayList<Number>(); //Generics
    /** Makes a List of Numbers from an array of Numbers */
    public static void addToBag(Number n) { bag.add(n); }
    public static void addToBag(Number... numA) { //variable args
        for (Number n : numA) addToBag(n);
    }
    public static boolean removeAll(Kind k) {
        boolean removed = false;
        //for (Number n : bag) gives ConcurrentModificationException
        int i = bag.size();
        while (i > 0) {  
            i--; Number n = bag.get(i);
            if ((kindOf(n) == k) && bag.remove(n)) removed = true;
        }
        return removed;
    }
    public static void main(String... args) { //variable args
        addToBag(0, 10, 0.01);
        addToBag(-333333333333L, PI);
        for (Number n : bag)
            System.out.printf("  %s is %s \n", n, kindOf(n));
    }
}
