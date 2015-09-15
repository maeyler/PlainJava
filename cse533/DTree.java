/**
 * Eyler  6.11.2003
 * Simplified from DefaultMutableTreeNode
 * 28.12  for exam
 */

class DTree {
    DTree parent; DTree[] child; String fruit;

    public DTree(String f) { fruit = f; }
    public DTree(DTree[] c) {
       child = c;  //each child has this node as its parent
       for (int i=0; i<c.length; i++) c[i].parent = this;
    }
    public DTree parent() { return parent; }
    public int childCount() { return child.length; }
    public DTree child(int i) { return child[i]; }
    public String fruit() { return fruit; }
    public boolean isLeaf() { return (child == null); }
    public void print() { print(""); }
    void print(String indent) {
       if (isLeaf()) 
           System.out.println(indent + fruit);
       else {
           System.out.println(indent + "+");
           for (int i=0; i<childCount(); i++) 
              child(i).print(indent +"  ");
       }
    }

    static DTree tree(String[] a) {
        DTree[] t = new DTree[a.length];
        for (int i=0; i<a.length; i++) 
           t[i] = new DTree(a[i]);
        return new DTree(t);
    }
    public static void main(String[] args) {
        DTree root;
        String[] a = { "a1", "a2" };
        String[] b = { "b1", "b2", "b3" };
        String[] c = { "c1", "c2" };
        DTree[] A = { tree(a), tree(b), tree(c) };
        DTree[] B = { new DTree(A), tree(c) };
        DTree[] C = { tree(a), new DTree(B) };
        DTree[] D = { new DTree(C), tree(c) };
        root = new DTree(D);
        //System.out.println(root.leafCount()+" leaves");
        root.print();
    }
}
