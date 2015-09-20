// Author: Eyler --  3.12.2003 
//        modified:  5/05/2007
//      simplified: 20/10/2011
import java.util.Random;
import java.util.TreeSet;

public class Sim {
   //constants
   final static int mean_ARR = 50, mean_SER = 32, sdev_SER = 6;
   final static int TRACE = 100; //trace output for shorter runs
   //variables
   float clock, lastEvent;  
   int N, Q;   //system state
   boolean tracing;
   Random rng; //random number generator
   Stats sts;  //system statistics
   TreeSet<Event> fel; //future events list
   //constructor
   public Sim(long seed) {
      rng = new Random(seed);
      sts = new Stats();
      fel = new TreeSet<Event>();
   }
   public void run(float maxTime) {
      clock = 0; lastEvent = 0; 
      N = 0; Q = 0;
      tracing = (maxTime <= TRACE);
      scheduleArrival();
      while (clock < maxTime) {
         Event e = fel.first();
         fel.remove(e);
         if (tracing)
            System.out.println((int)clock+"["+Q+"]"+e);
         lastEvent = clock;
         clock = e.getTime();
         e.process();
      }
      sts.report(); 
   }
   void scheduleArrival() {// exponential distribution
      float d = -mean_ARR * (float)Math.log(rng.nextFloat());
      fel.add(new Arrival(d));
   }
   void startService() {// normal distribution
      Q--; N = 1;
      float d = mean_SER + sdev_SER * (float)rng.nextGaussian();
      fel.add(new Departure(d));
   }

   class Arrival extends Event {
      public Arrival(float d) { super(clock + d); }
      public String getType() { return "A"; }
      public void process() {
         Q++; 
         if (N == 0) startService();
         scheduleArrival();
         sts.addArrival();
      }
   }
   
   class Departure extends Event {
      float dur;
      public Departure(float d) { super(clock + d); dur = d; }
      public String getType() { return "D"; }
      public void process() {
         if (Q==0) N = 0;
         else startService();
         sts.addService(dur);
      }
   }

   class Stats {
      int arr, dep, maxQ;  
      float busy, total;
      void update() {
         busy += N * (clock - lastEvent);
         if (maxQ < Q) maxQ = Q;
      }
      void addArrival() {
         arr++; update();
      }
      void addService(float d) {
         dep++; total += d; update();
      }
      String oneDigit(float d) {
         return ""+(Math.rint(10 * d)/10);
      }
      public void report() {
         System.out.println("Clock:  "+(int)clock);
         System.out.println("Utilization: %"+oneDigit(100*busy/clock));
         System.out.println("Arrivals:     "+arr);
         System.out.println("Mean arrival: "+oneDigit(clock/arr));
         System.out.println("Departures:   "+dep);
         System.out.println("Mean service: "+oneDigit(total/dep));
         System.out.println("In queue: "+Q);
         System.out.println("Max Q:   "+maxQ);
      }
   }

   public static void main(long seed, float maxTime) {
      new Sim(seed).run(maxTime);
   }
   public static void main(String[] args) {
      main(444, 50000);
   }
}


   abstract class Event implements Comparable<Event> {
      float time;
      public Event(float t) { time = t; }
      public float getTime() { return time; }
      abstract void process();
      abstract String getType();
      public int compareTo(Event e) {
         if (equals(e))return 0;
         if (time < e.time) return -1;
         return +1;
      }
      public String toString() {
         return getType()+":"+(int)time;
      }
   }
