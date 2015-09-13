// Author: Eyler -- Dec 2001
import java.io.*;

class intf {

    PrintWriter P;

    void doFile (File F)  {
	try {
	  P.println();
	  P.println(F.getName());
	  BufferedReader R = new BufferedReader(new FileReader(F));
	  String S = R.readLine();
	  while (S != null) {
	    int k = S.indexOf("public");
	    if (k != -1 && k < 15) {
		if ((k=S.indexOf("{")) != -1) 
		   S = S.substring(0,k);
		//System.out.println(S+k);
		P.println(S);
		}
	    S = R.readLine();
	    }
          R.close();
	}
	catch (IOException e)  {
	  System.out.println("ERROR reading"); 
	}
    }

    public intf ()  {
	try {
	  File F = new File (".");
	  String S = F.getAbsolutePath();
	  System.out.println(S);
	  P = new PrintWriter (new FileOutputStream("intf.txt"));
	  P.println(S);

	  File[] L=F.listFiles();
	  for (int i=0; i<L.length; i++)
            if (L[i].isFile() && 
		L[i].getName().indexOf("java") != -1) {
	      doFile(L[i]);
	      System.out.println(L[i].getName());
	      }
          P.close();
	}
	catch (IOException e)  {
	  System.out.println("ERROR writing");
	}
    }

    public static void main(String args[])  {
	long start = System.currentTimeMillis();
	new intf ();
	System.out.println("Elapsed "
		+((System.currentTimeMillis()-start)/1000));
    }

}
