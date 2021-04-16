package entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TreeNodeTest {
    @Test
    void testTree() {
        TreeNode child1=new TreeNode("1");
        TreeNode child2=new TreeNode("2");
        List<TreeNode> children=new ArrayList<TreeNode>();
        children.add(child1);
        children.add(child2);
        TreeNode parent=new TreeNode("3",children);

        TreeNode child3=new TreeNode("4");
        List<TreeNode> parent2=new ArrayList<TreeNode>();
        parent2.add(child3);
        parent2.add(parent);
        TreeNode total=new TreeNode("5",parent2);

        total.DFSShow();

        /*
            5
        4       3
            1       2
         */
    }
}