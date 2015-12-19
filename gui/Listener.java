import java.awt.*;
import java.awt.event.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import javax.swing.JTree;
import javax.swing.JFrame;
import javax.swing.JPanel;

class Listener extends MouseAdapter implements TreeSelectionListener {
    JTree tree;
    public Listener() { this(new JTree()); }
    public Listener(JTree t) {
        tree = t; 
        int i = TreeSelectionModel.SINGLE_TREE_SELECTION;
        tree.getSelectionModel().setSelectionMode(i);
        tree.addMouseListener(this);
        tree.addTreeSelectionListener(this);
    }
    public void mouseEntered(MouseEvent e) {
        System.out.println(" mouseEntered ");
    }
    public void mouseExited(MouseEvent e) {
        System.out.println(" mouseExited ");
    }
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node 
            = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if (node == null) return;
        Object nodeInfo = node.getUserObject();
        System.out.println(nodeInfo+" "+node.isLeaf());
    }

    public static void main(String[] a) {
        JFrame f = new JFrame("TreeSelectionListener");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.add(new Listener().tree); 
        f.pack(); f.setVisible(true);
    }
}
