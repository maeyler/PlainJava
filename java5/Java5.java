import java.util.*;
import static java.lang.Math.PI;
public class Java5 {
    public enum Kind { zero, negative, positive, fractional }
    public static Kind kindOf(Number n) {
        float i = n.floatValue();
        if (i == 0) return Kind.zero;
        if (n instanceof Float || n instanceof Double) 
            return Kind.fractional;
        return (i < 0)? Kind.negative : Kind.positive;
    }
    public static List<Number> bag;
    public static void toBag(Number... num) {
        bag = new ArrayList<Number>();
        for (Number n : num) bag.add(n);
    }
    public static void main(String... args) {
        toBag(0, 10, 0.01, -33333333333333L, PI);
        String f = "Number %s is %s \n";
        for (Number n : bag) System.out.printf(f, n, kindOf(n));
    }
}
