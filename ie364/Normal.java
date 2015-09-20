class Random extends java.util.Random {  //*C*
   int count;
   protected int next(int b) {
      count++; return super.next(b);
   }
   public void clearCount() {
      count = 0;
   }
   public int getCount() {
      return count;
   }
}

public class Normal {
   Random rng; 
   char kind;
   byte[] ba = new byte[12];
   public Normal() {
      rng = new Random();
   }
   public void setSeed(long seed) {
      rng.setSeed(seed);
   }
   public void run() {
      for (char c='1'; c<='3'; c++) test(1000000, c);
      System.out.println();
   }
   public void test(int n, char c) {
      kind = c;  
      rng.clearCount();  //*C*
      long start = System.currentTimeMillis();
      float s = 0, ss = 0; 
      for (int i=0; i<n; i++) {
          float x = normal(50, 10) ;
          s += x;  ss += x*x;
      }
      long dur = System.currentTimeMillis() - start;
      int k = rng.getCount();  //*C*
      System.out.println(kind+". method: "+dur+" msec  "+k+" calls");
      float sd = (float)Math.sqrt((ss-s*s/n)/(n-1));
      System.out.println("Mean="+(s/n)+"  Std dev="+sd);
   }
   public float exponential(float mean) {
      return -mean * (float)Math.log(rng.nextFloat());
   }
   public float normal(float mean, float sdev) {
      return mean + sdev * stdNormal();
   }
   public float stdNormal() {
      if (kind == '1') { //approx 2.5 next() calls per call
          return (float)rng.nextGaussian();
      }
      if (kind == '2') { //12 next() calls per call
          float s = 0;
          for (int i=0; i<12; i++)
             s += rng.nextFloat();
          return (s - 6);
      }
      else { //exactly 3 next() calls per call
          rng.nextBytes(ba);
          float s = 0;
          for (int i=0; i<12; i++)
             s += ba[i];
          return (s + 6) / 255;
      }
   }
   public static void main(String[] args) {
      new Normal().run();
   }
}
/* result for a million calls:
1. method: 691 msec
Mean=49.99539  Std dev=10.005405
2. method: 1742 msec
Mean=50.003437  Std dev=9.996292
3. method: 661 msec
Mean=49.99705  Std dev=10.032962

After the counters: code marked by //*C*
1. method: 821 msec  2548664 calls
Mean=50.009087  Std dev=10.000069
2. method: 1913 msec  12000000 calls
Mean=49.981426  Std dev=10.011289
3. method: 681 msec  3000000 calls
Mean=49.99573  Std dev=10.0459795
*/
