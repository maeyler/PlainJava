package number;

/** Defines static methods to construct Numbers.
 * <P>
 * Factory methods for constructing Whole, Rational, Decimal, Complex. 
 * <P>
 * These methods are preferred to the constructors, because they 
 * select the proper constructor depending on the arguments.
 *
 * @author Eyler
 * @version 2.0
 */
public class Factory {
    
    static final Cruncher crunch = new Cruncher();

    Factory() {} //in order to exclude from JavaDoc & SSS
   
    /** Returns Cruncher instance for calculation on Numbers */    
    public static Cruncher cruncher() {
        return crunch;
    }
    /** Factory method that constructs a {@link mae.tut.num.Whole}.
     * <P>
     * This is equivalant to <CODE>new Whole(n)</CODE>.
     */
    public static Number newWhole(long n) {
        return new Whole(n);
    }
    /** Factory method that constructs a {@link mae.tut.num.Rational}
     * or a {@link mae.tut.num.Whole}.
     * <P>
     * If <CODE>den==gcd(num,den)</CODE>, 
     * a {@link mae.tut.num.Whole} is returned.
     */    
    public static Number newRational(long num, long den) {
        long g = Rational.gcd(num, den);
        if (g == den) 
             return new Whole(num/g);
        else return new Rational(num/g, den/g);
    }
    /** Factory method that constructs a {@link mae.tut.num.Decimal}
     * or a {@link mae.tut.num.Whole}.
     * <P>
     * If String value of x ends with ".0", x represents an integer,
     * a Whole is returned.
     */    
    public static Number newDecimal(float x) {
        if ((""+x).endsWith(".0")) 
             return new Whole((long)x);
        else return new Decimal(x);
    }
    /** Factory method that constructs a {@link mae.tut.num.Complex},
     * a {@link mae.tut.num.Decimal} or a {@link mae.tut.num.Whole}.
     * <P>
     * If <CODE>im==0</CODE>, newDecimal() is invoked.
     */    
    public static Number newComplex(float re, float im) {
        if (im == 0) 
             return newDecimal(re);
        else return new Complex(re, im);
    }
    
    public static Number parseWhole(String s) {
        try { 
            return newWhole(Integer.parseInt(s));
        } catch (Exception e) {
            return null;
        }
    }
    public static Number parseRational(String s) {
        String[] a = s.split("/");
        if (a.length != 2) return null;
        try { 
            int n = Integer.parseInt(a[0]);
            int m = Integer.parseInt(a[1]);
            return newRational(n, m);
        } catch (Exception e) {
            return null;
        }
    }
    public static Number parseDecimal(String s) {
        try { 
            return newDecimal(Float.parseFloat(s));
        } catch (Exception e) {
            return null;
        }
    }
    public static Number parseNumber(String s) {
        Number n = parseWhole(s);
        if (n != null) return n;
        n = parseRational(s);
        if (n != null) return n;
        return parseDecimal(s);
    }
    public static Number[] parseRow(String s) {
        String[] a = s.split("(\\s|,)+");
        Number[] n = new Number[a.length];
        for (int i=0; i<a.length; i++) n[i] = parseNumber(a[i]);
        return n;
    }
    
    public static void main(String[] args) {
        String s = "10/34  -23 0000\t00.75, 1/2/3 \t1.2.3 -30/20";
        Number[] n = parseRow(s);
        for (Number t : n) System.out.println(t+" ");
    }
}
