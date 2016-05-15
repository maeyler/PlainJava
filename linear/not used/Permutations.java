import java.util.ArrayList;

class Permutations<T> {

   ArrayList<T> list;  

   public Permutations(T[] a) { 
      list = new ArrayList<T>();
      for (T x : a) list.add(x);
      permute("");
   }
   void permute(String perm) {
      if (list.isEmpty()) System.out.println(perm);
      else for (int i=0; i<list.size(); i++) {
         T x = list.remove(i); 
         permute(perm+x);
         list.add(i, x); 
      }
   }
   
   final static String[] A = { "a", "b", "c" };
   final static Number[] N = { 0, 1, 2, 3 };
   public static void main(String[] args) {
      new Permutations<String>(A);  
      new Permutations<Number>(N); 
   }
}
