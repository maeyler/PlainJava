import java.lang.annotation.*; 
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME) //allows access by reflection
@interface Debug {  //Annotation type with two attributes
     boolean OK() default false;
     int count() default 1; 
}
public class MetaTest { //define some methods & annotate them
     @Debug public void one() {}
     @Debug(count=2) public void two() {}
     @Debug(OK=true) @Deprecated public void three() {}
     @Debug(OK=true, count=4) public void four() {}
     @Override public String toString() { return ""; }
     public static void println(Method m) {
         for (Annotation a : m.getAnnotations()) 
             System.out.print(a+" ");
         System.out.println(m);
     }
     public static void main(String[] a) {
         for (Method m : MetaTest.class.getDeclaredMethods())
             println(m);
     }   
}
