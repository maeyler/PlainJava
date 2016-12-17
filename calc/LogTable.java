class LogTable {
    final static double C = 10000/Math.log(10);
    static void header() {
        System.out.print("  ");
        for (int i=0; i<10; i++)
            System.out.printf("%4s  ", i);
        System.out.println();
    }
    static void oneLine(int a) {
        System.out.printf("%s  ", a);
        double x = a/10.0;
        for (int i=0; i<10; i++) {
            int n = (int)Math.round(C*Math.log(x));
            System.out.printf("%4s  ", n);
            x += 0.01;
        }
        System.out.println();
    }
    public static void main(String[] args) {
        header();
        for (int a=50; a<54; a++) oneLine(a);
    }
}
