import number.Number;
import number.Factory;
import javax.swing.event.TableModelListener;

class Row implements Cloneable {
   final int N;
   final Number[] data;
   public Row(Number[] na) { 
      N = na.length; data = na;
   }
   public void multiply(Number c) {
      for (int j=0; j<N; j++)  {
          data[j] = data[j].mult(c);
      }
   }
   public void addRow(Number c, Row r) {
      for (int j=0; j<N; j++)  {
          data[j] = data[j].add(r.data[j].mult(c));
      }
   }
   public Row clone() {
      Number[] x = new Number[N];
      for (int j=0; j<N; j++) 
          x[j] = data[j];
      return new Row(x);
   }
   public Row augmentZero() { 
      Number[] x = new Number[2*N];
      for (int j=0; j<N; j++) {
          x[j] = data[j]; x[N+j] = Matrix.ZERO;
      }
      return new Row(x);
   }
   public String toString() { 
      String s = "";
      for (int j=0; j<N; j++)  {
         if (j > 0) s += " \t";
         s += data[j].toString();
      }
      return s;
   }
}

class Matrix implements Cloneable, javax.swing.table.TableModel {
   static final String[] 
      NAME = { "x", "y", "z", "p", "q", "s", "t" };
   static final Number ZERO = Factory.newWhole(0);      
   static final Number ONE = Factory.newWhole(1);      
   static final Number MINUS = Factory.newWhole(-1);      
   final int M;
   final Row[] row;
   final boolean notTooManyVars;
   Number det = ONE;
   public Matrix() { this(toRows(B)); }
   public Matrix(Row[] ra) {
      M = ra.length; row = ra;
      notTooManyVars = (ra[0].N < NAME.length);
   }
   public boolean isSquare() {
      return getRowCount() == getColumnCount();
   }
   public void printData() {
      for (int i=0; i<M; i++) System.out.println(row[i]);
      System.out.println();
   }
   float abs_val(int i, int j) {
      return Math.abs(getValueAt(i, j).value());
   }
   int pickRow(int k) {
      int m = k;
      for (int i=k+1; i<M; i++) 
          if (abs_val(m, k) < abs_val(i, k)) m = i;
      return m;
   }
   public void exchange(int i, int k) {
      if (i == k) return;
      Row r = row[i]; row[i] = row[k]; row[k] = r; 
      det = minus(det);  //-det;
      System.out.printf("exchange row %s <=> row %s \n", i, k);
   }
   public void multiply(int i, int num, int den) { 
      multiply(i, Factory.newRational(num, den));
   }
   public void multiply(int i, Number c) {
      float v = c.value();
      if (v == 0 || v == 1) return;
      row[i].multiply(c); det = det.mult(c.inverse());
      System.out.printf("mult row %s by %s \n", i, c);
   }
   public void addRow(int i, int num, int den, int k) { 
      addRow(i, Factory.newRational(num, den), k); 
   }
   public void addRow(int i, Number c, int k) {
      if (i == k || c.value() == 0) return;
      row[i].addRow(c, row[k]); 
      System.out.printf("add to row %s  %s x row %s\n", i, c, k);
   }
   boolean forward(int k) { //finds pivot column j in row k
       //returns true if work is completed
       int j = 0;
       while (j < getColumnCount() && abs_val(k, j) < 1E-10) j++;
       //if (abs_val(k, k) < 1E-10) { int j = pickRow(k); exchange(j, k); }
       if (j > k) det = ZERO; 
       if (j == getColumnCount()) return true; //cannot continue
       Number p = getValueAt(k, j);
       multiply(k, p.inverse());
       for (int i=k+1; i<M; i++) 
           addRow(i, minus(getValueAt(i, j)), k);  //-val(i, j)
       return (k == M-1);
   }
   void backward() {
       for (int k=M-1; k>0; k--) 
           for (int i=0; i<k; i++) 
               addRow(i, minus(getValueAt(i, k)), k);
   }
   public void solve(boolean print) {
       int k = 0; 
       if (print) printData(); 
       boolean done = false;
       while (!done) {
           done = forward(k);
           k++; if (print) printData();
       }
       System.out.printf("det = %s \n", det);
       if (isSquare()) return;
       backward(); if (print) printData();
   }
   public Class<?> getColumnClass(int j) { return Number.class; }
   public int getRowCount() { return M; }
   public int getColumnCount() { return row[0].N; }
   public Number getValueAt(int i, int j) { return row[i].data[j]; }
   public void setValueAt(Object v, int i, int j) { 
       row[i].data[j] = (Number)v; 
   }
   public String getColumnName(int j) { 
       if (notTooManyVars) return NAME[j]; 
       return "x"+(j+1);   //Character.toString((char)('p'+j)); 
   }
   public boolean isCellEditable(int i, int j) { return false; }
   public void addTableModelListener(TableModelListener l) { }
   public void removeTableModelListener(TableModelListener l) { }
   public Matrix clone() { 
      Row[] a = new Row[M];
      for (int i=0; i<M; i++) 
          a[i] = row[i].clone();
      return new Matrix(a);
   }
   public Matrix augmentID() { 
      Row[] a = new Row[M];
      for (int i=0; i<M; i++) { //for each row
          a[i] = row[i].augmentZero();
          a[i].data[M+i] = ONE;
      }
      return new Matrix(a);
   }
   public String toString() {
       return M+"x"+row[0].N+" matrix";
   }

   static final int[][] 
      A = { { 1, 4, 1 }, { 2, 2, 1 }, { 3, 0, 3 } },
      B = { { 2, 2, 1, 1, 2 }, { 1, 4, 0, 1,-1 }, 
            { 3, 1, 2,-2,-2 }, { 3, 0, 1, 2, 7 } },
      C = { { 2, 2, 1, 1 }, { 1, 4, 0, 1 }, { 3, 1, 2,-2 }, { 3, 0, 1, 2 } };
   static Number minus(Number n) {
      return n.mult(MINUS);
   }
   public static Row[] toRows(int[][] aa) {
      Row[] ra = new Row[aa.length];
      for (int i=0; i<aa.length; i++) 
          ra[i] = new Row(toNumbers(aa[i]));
      return ra;
   }
   public static Number[] toNumbers(int[] d) { 
      Number[] a = new Number[d.length]; 
      for (int j=0; j<d.length; j++) 
          a[j] = Factory.newWhole(d[j]);
      return a;
   }
   public static Row[] toRows(String[] sa) {
      Row[] ra = new Row[sa.length];
      for (int i=0; i<sa.length; i++) 
          ra[i] = new Row(Factory.parseRow(sa[i]));
      return ra;
   }
    public static Matrix fromCB() {
        final java.awt.datatransfer.Clipboard 
            CB = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        final java.awt.datatransfer.DataFlavor 
            STR = java.awt.datatransfer.DataFlavor.stringFlavor;
        try {
            String s = (String)CB.getData(STR);
            return new Matrix(toRows(s.split("\\n")));
        //UnsupportedFlavorException  IOException
        } catch(Exception e) { 
            return new Matrix();  //throw new RuntimeException(e);
        }
    }
   public static void main(String[] args) {
      new Matrix().solve(true);
   }
}
