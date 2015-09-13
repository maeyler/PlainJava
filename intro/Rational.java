public class Rational implements Comparable<Rational> {
    
    long num, den;
    
    public Rational(long n, long d) {
        if (d == 0) 
            throw new IllegalArgumentException("Zero denominator");
        //long k = gcd(n, d);
        //num = n/k;  den = d/k; 
        num = n;  den = d; 
    }
    public Rational add(Rational r) {
        return new Rational(num*r.den + r.num*den, den*r.den);
    }
    public Rational mult(Rational r) {
        return new Rational(num * r.num, den * r.den);
    }
    public Rational inverse() {
        return new Rational(den, num);
    }
    public float value() {
        return (float)num/(float)den;
    }
    public String toString() {
        return num+"/"+den;
    }
    public boolean equals(Object x) {
        if (x instanceof Rational) 
             return (value() == ((Rational)x).value());
        else return false;
    }
    public int hashCode() {
        return new Float(value()).hashCode();
    }
    public int compareTo(Rational r) {
        float v = value();
        float n = r.value();
        if (v == n) return 0;
        else if (v < n) return -1;
        else return 1;
    }

    public static long gcd(long a, long b) {
        if (a < 0) a = -a;
        if (b < 0) b = -b;
        while (a!=0 && b!=0) 
            if (a < b) b = b % a;
            else a = a % b;
        if (a == 0) a = b;
        if (a == 0) return 1;
        return a;
    }
    public static void main(String[] args) {
        Rational x = new Rational(2, 5);
        System.out.println(x.toString() +" "+ x.value());
        Rational y = x.mult(x);
        System.out.println(y.toString() +" "+ y.value());
        Rational z = x.inverse();
        System.out.println(z.toString() +" "+ z.value());
    }
}
