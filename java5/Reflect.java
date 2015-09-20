import java.lang.reflect.Method;
import static java.lang.System.*;

class Reflect {
   static final Runtime RT = Runtime.getRuntime();
   public static void main(String[] args) throws Exception {
         out.println("Before: "+RT.freeMemory());
         Class sys = Class.forName("java.lang.System");
         Method gc = sys.getMethod("gc"); //no arguments
         gc.invoke(null);  //static method
         out.println("After:  "+RT.freeMemory());
   }
}
