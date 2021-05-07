package entity;

import java.util.List;

/**
 * @author TYX
 * @name TreeNode
 * @description 语法树结点
 * @createTime 2021/4/2 16:33
 **/
public class TreeNode {
    private final String name;
    private String value;
    private List<TreeNode> children;

    /**
     * 返回名称
     *
     * @return 名称 String
     */
    public String getName() {
        return name;
    }

    /**
     * 返回值
     *
     * @return 值 String
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置值
     * @param value 值 String
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 设置孩子结点
     *
     * @param children 孩子结点 List of TreeNode
     */
    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    /**
     * 构造器
     *
     * @param name 名称 String
     * @param children 孩子结点 List of TreeNode
     */
    public TreeNode(String name, List<TreeNode> children) {
        this.name = name;
        this.children = children;
    }

    /**
     * 构造器
     * @param name 名称 String
     */
    public TreeNode(String name) {
        this.name = name;
        this.children = null;
    }

    /**
     * 基于DFS的展示（测试用）
     */
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
