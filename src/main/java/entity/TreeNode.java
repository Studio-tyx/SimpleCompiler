package entity;

import java.util.List;

/**
 * @author TYX
 * @name TreeNode
 * @description
 * @createTime 2021/4/2 16:33
 **/
public class TreeNode {
    private final String name;
    private String value;
    private List<TreeNode> children;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    //怎么整呢
    //基本上是每次规约 如果是非终结符就找那棵子树 终结符就用已有的新建结点
    public TreeNode(String name, List<TreeNode> children) {
        this.name = name;
        this.children = children;
    }

    public TreeNode(String name) {
        this.name = name;
        this.children = null;
    }

    public void DFSShow() {
        System.out.print("parent:" + name + ",child:");
        if (children == null) {
            System.out.println();
            return;
        }
        for (TreeNode child : children) {
            System.out.print(child.name + ",");
        }
        System.out.println();
        for (TreeNode child : children) {
            child.DFSShow();
        }
    }

}
