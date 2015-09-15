class Father {
   int f;  static int z;
   void test(Object x) {
      System.out.println(x); z++;
   }
   void test(int i) {
      //Father doesn't have test(String)
      test("int "+i); f++;
   }
}
class Child extends Father {
   int n;
   void test(String s) {
      super.test("String "+s); n++;
   }
   void test(Integer x) {
      test("Integer "+x.intValue());
   }
   public static void main(String[] args) {
      Child C = new Child();
      Object x = new Integer(55);
      C.test(x);
      C.test(55);
      //C.test((String)x);
      C.test(x.toString());
      //C.test(x.intValue());
      C.test((Integer)x);
      System.out.print(C.n+" "+C.f+" "+C.z);
   }
}
