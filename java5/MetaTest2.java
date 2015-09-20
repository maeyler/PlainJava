import java.lang.annotation.*; 
import java.lang.reflect.*;

@Retention(RetentionPolicy.RUNTIME) //allows access by reflection
@interface Debug {  //Annotation type with two attributes
     boolean build() default false;
     int counter() default 1; 
}
public class MetaTest { //define some methods & annotate them
     @Debug() public void one() {}
     @Debug(counter=2) public void two() {}
     @Debug(build=true) public void three() {}
     @Debug(build=true, counter=4) public void four() {}
     public void report(String s) throws NoSuchMethodException {
         Method m = getClass().getMethod(s);
         for (Annotation a : m.getAnnotations()) 
             System.out.println(m+":  "+a);
     }
     public static void main(String[] a) throws NoSuchMethodException {
         MetaTest mt = new MetaTest(); 
         mt.report("one"); mt.report("two"); mt.report("three");
     }   
}
