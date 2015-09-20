import java.io.*;

class CopyText {

   public static void convert(String title) throws IOException {
      Reader rdr = new FileReader(title+".txt");
      BufferedReader in = new BufferedReader(rdr);
      OutputStream os = new FileOutputStream(title+".out");
      PrintStream out = new PrintStream(os);
      String s; int n = 0;
      while ((s = in.readLine()) != null) {
         int k = s.indexOf(9); //TAB
         int m = s.length();
         if (k<=0 || k==m-1 || s.charAt(k+1)!=' ') k = m;
         out.println(s.substring(0,k)); n++;
      }
      in.close(); out.close();
      System.out.println(n+" lines copied");
   }
   public static void main(String[] args) throws IOException {
      convert("Test");
   }
}
