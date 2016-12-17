class Gravitation {
    final static double
        G = 6.674E-11,// N m2/kg2 Gravitational constant

        M = 5.972E24, // kg  mass of earth

        R = 6.371E6,  // m  radius of earth

        m = 7.347E22, // kg  mass of moon

        d = 3.844E8;  // m  distance to moon
        
    public static void main(String[] args) {
        System.out.printf("%.4f %n", G*M/R/R);
        System.out.printf("%.5f %n", G*m/d/d);
    }
}
/*
GM/R2 = 6.674E-11 * 5.972E24 / (6.371E6*6.371E6) = 9.8195

Gm/d2 = 6.674E-11 * 7.347E22 / (3.844E8*3.844E8) = 0.00003 = 3.32E-5
*/
