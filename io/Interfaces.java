// Author: Eyler -- Dec 2001
// Modified Oct 2012
// finds all public members in java files within a given folder

import java.io.*;

class Interfaces extends FileCrawler {

    PrintStream P; String name;

    public Interfaces() { this(new File("."), "out.txt"); }
    public Interfaces(File f, String n) { super(f); name = n; }
    public void run()  {
	try {
	  P = new PrintStream(name);
	  P.println(path);
          super.run();
          P.close();
	} catch (IOException x) {
	  System.out.println("ERROR writing "+x);
	}
    }
    void process(BufferedReader R) throws IOException {
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
    }
    protected void doFile(File F)  {
        if (!F.getName().endsWith(".java")) return;
        try {
	  BufferedReader R = new BufferedReader(new FileReader(F));
          //System.out.println(F.getName());
	  P.println(); P.println(F.getName());
          fil++; process(R);
          R.close();
	} catch (IOException x)  {
	  System.out.println("ERROR reading "+x); 
	}
    }
    public static void main(String args[])  {
	new Interfaces().start();
    }
}
