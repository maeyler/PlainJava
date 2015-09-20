import static java.lang.System.*;

public class NanoTime {
   public static void main(String[] args) {
      long t = nanoTime()/1000000000L;
      String f = "uptime:  %s days %s hr %s min %s sec %n";
      long h = t/3600; t = t%3600;
      long d = h/24;   h = h%24;
      out.printf(f, d, h, t/60, t%60);
   }
}
