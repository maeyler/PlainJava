import java.io.File;
import java.util.*;
import javax.tools.*;

//stackoverflow.com/questions/2543439/null-pointer-exception-while-using-java-compiler-api
//www.programcreek.com/java-api-examples/index.php?api=javax.tools.JavaCompiler

class CompileTest {
    final static JavaCompiler tool = ToolProvider.getSystemJavaCompiler();
        //If I add tools.jar to "jre/lib/ext" folder then this returns null. 
        //But if I add tools.jar to the "jre/lib" folder then it works.
        //http://bugs.java.com/bugdatabase/view_bug.do?bug_id=7181951
    public static int compile(String... s) {
        if (tool == null) 
            throw new RuntimeException("cannot locate tools.jar");
        return tool.run(null, null, null, s);
    }
    public static int compile(File d) {
        if (d.isDirectory()) {
            List<String> L = new ArrayList<>();
            for (String f: d.list())
                if (f.endsWith(".java")) L.add(d+File.separator+f);
            return compile(L.toArray(new String[0]));
        } else {
            return compile(d.getAbsolutePath());
        }
    }
    public static void main(String[] args) {
        //List s = Arrays.asList("a", "b", "c");
        File f = new File("Colored.java");
        System.out.println("compilationResult is "+compile(f));
    }
}
