// Eyler 13.4.2002
// 14.3.2003  implement Set
// 26.1.2016  use HashSet
// 03.6.2018  use IntSet

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.AbstractSet;
import java.util.BitSet;
import java.util.Iterator;

class IntSet extends AbstractSet<Integer> {
   BitSet bs;
   public IntSet(int maxValue) {
      bs = new BitSet(maxValue);
   }
   public int size() {
      return bs.cardinality();
   }
   public void clear() {
      bs.clear();
   }
   public boolean contains(Object k) {
      if (!(k instanceof Integer)) return false;
      return bs.get((Integer)k);
   }
   public boolean remove(Object k) {
      if (!contains(k)) return false;
      bs.clear((Integer)k); return true;
   }
   public boolean add(Integer k) {
      if (contains(k)) return false;
      bs.set(k); return true;
   }
   public boolean containsAll(Collection<?> c) {
      return super.containsAll(c);
   }
   public boolean addAll(Collection<? extends Integer> c) {
      if (!(c instanceof IntSet)) 
         return super.addAll(c);
      int n = size(); IntSet b2 = (IntSet)c;
      bs.or(b2.bs); return n == size();
   }
   public boolean retainAll(Collection<?> c) {
      if (!(c instanceof IntSet)) 
         return super.retainAll(c);
      int n = size(); IntSet b2 = (IntSet)c;
      bs.and(b2.bs); return n == size();
   }
   public boolean removeAll(Collection<?> c) {
      if (!(c instanceof IntSet)) 
         return super.removeAll(c);
      int n = size(); IntSet b2 = (IntSet)c;
      bs.andNot(b2.bs); return n == size();
   }
   public Iterator<Integer> iterator() {
       return new Iter();
   }
   public String toString() {
      return bs.toString();
   }
   
   class Iter implements Iterator<Integer> {
       int x = bs.nextSetBit(0);
       public boolean hasNext() { return x>=0; }
       public Integer next() { 
           int t = x; x = bs.nextSetBit(t+1); return t; 
       }
       public void remove() {}
   }

   static void primes(int M) {
      long time = System.currentTimeMillis();
      Set<Integer> s = new IntSet(M);
//      Set<Integer> s = new HashSet<>(M);
//      SimpleSet s = new SimpleSet(M);
      for (int k=2; k<M; k++) s.add(k);
      for (int p=2; p*p<M; p++) 
         if (s.contains(p)) { //p is next prime
            System.out.print(p+"  ");
            for (int k=2*p; k<M; k=k+p)
               s.remove(k);
         }
      System.out.println(); 
      long t = System.currentTimeMillis() - time;
      System.out.println(s.size());
      t = System.currentTimeMillis() - time;
      System.out.println(t+" msec total");
   }
   public static void main(String args[]) {
      primes(50000);
   }
}

