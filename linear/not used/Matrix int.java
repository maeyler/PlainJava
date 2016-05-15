import java.util.Arrays;
import static java.lang.Math.abs;

class Row {
   final int N;
   final float[] data;
   public Row(float[] d) { 
      N = d.length; data = Arrays.copyOf(d, N);
   }
   public float pivot(int j) {
       float p = data[j];
       multiply(1/p);
       return p;
   }
   public void multiply(float c) {
      System.out.printf("x %s \n", c);
      for (int j=0; j<N; j++)  {
          data[j] *= c;
      }
   }
   public void addRow(float c, Row r) {
      //System.out.printf("addRow %s x [%s] \n", c, r);
      for (int j=0; j<N; j++)  {
          data[j] += c * r.data[j];
      }
   }
   public String toString() { 
      String s = "";
      for (int j=0; j<N; j++)  {
         if (j > 0) s += "\t";
         s += data[j];
         if (s.endsWith(".0")) s = s.substring(0, s.length()-2);
      }
      return s;
   }
}

class Matrix {
   final int M;
   final Row[] row;
   float det = Float.NaN;
   public Matrix(float[][] a) {
      M = a.length; row = new Row[M];
      for (byte i=0; i<M; i++) row[i] = new Row(a[i]);
      System.out.printf("%sx%s matrix:\n", M, row[0].N);
   }
   public void printData() {
      for (int i=0; i<M; i++) {
         //System.out.printf("%5.0f\t", val(i, j));
         System.out.println(row[i]);
      }
      System.out.println();
   }
   public float val(int i, int j) {
      return row[i].data[j];
   }
   public int pickRow(int k) {
      int m = k;
      for (int i=k+1; i<M; i++) 
          if (abs(val(m, k)) < abs(val(i, k))) m = i;
      return m;
   }
   public void exchange(int i, int k) {
      if (i == k) return;
      det = -det;
      Row r = row[i]; row[i] = row[k]; row[k] = r; 
      System.out.printf("exchange %s <=> %s \n", i, k);
      //printData();
   }
   public void operate(int k) {
      det *= row[k].pivot(k);
      for (int i=k+1; i<M; i++) {
          row[i].addRow(-val(i, k), row[k]);
      }
      printData();
   }
   public void solve() {
      det = 1;
      for (int k=0; k<M; k++) {
          int i = pickRow(k);
          exchange(i, k);
          operate(k); 
      }
      System.out.printf("det = %s \n", det);
   }

   final static float[][] 
      A = { { 1, 4, 1 }, { 2, 2, 1 }, { 3, 0, 3 } },
      B = { { 2, 2, 1, 1 }, { 1, 4, 0, 1 }, { 3, 1, 2,-2 }, { 3, 0, 1, 2 } };
   public static void main(String[] args) {
      Matrix m = new Matrix(B);
      m.printData();
      m.solve();
   }
}
