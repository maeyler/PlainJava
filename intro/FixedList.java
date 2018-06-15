class FixedList extends java.util.AbstractList<String> {
    String[] x;
    public FixedList(String[] a) {
        x = a;
    }
    public int size() {
        return x.length;
    }
    public String get(int k) {
        //0<=k && k<x.length
        return x[k];
    }
    public String set(int k, String s) {
        String old = x[k];
        x[k] = s;
        return old;
    }

    static final String[] S = {"C", "A", "P", "E"};
    public static void main(String[] args) {
        System.out.println(new FixedList(S));
    }
}
