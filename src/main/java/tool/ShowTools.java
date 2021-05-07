package tool;

import entity.TreeNode;

import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * @author TYX
 * @name ShowTools
 * @description 工具类 输出用
 * @createTime 2021/3/19 15:05
 **/
public final class ShowTools {
    /**
     * Set输出
     *
     * @param set 集合 Set of T
     * @param <T> 指定类型 T
     */
    public static <T> void show(Set<T> set) {
        System.out.println("----set----------");
        int i = 0;
        for (T t : set) {
            System.out.print(i + ":" + t + ",");
            i++;
        }
        System.out.println("\n---------------");
    }

    /**
     * List输出
     *
     * @param list 集合  List of T
     * @param <T>  指定类型 T
     */
    public static <T> void show(List<T> list) {
        System.out.println("----list----------");
        int i = 0;
        for (T t : list) {
            //System.out.print(i + ":" + t + ",");
            System.out.print(t + "\t");
            i++;
        }
        System.out.println("\n---------------");
    }

    /**
     * 二维数组输出
     *
     * @param some 二维数组 T[][]
     * @param <T>  指定类型 T
     */
    public static <T> void show(T[][] some) {
        System.out.println("----数组----------");
        for (T[] t : some) {
            for (T tt : t) {
                System.out.print(tt + "\t");
            }
            System.out.println();
        }
        System.out.println("\n---------------");
    }

    /**
     * Stack输出
     *
     * @param stack 栈 Stack of T
     * @param <T>   指定类型 T
     */
    public static <T> void show(Stack<T> stack) {
        System.out.println("----------stack---------------");
        for (T t : stack) {
            System.out.print(t + ",");
        }
        System.out.println("\n-------------------------");
    }

    /**
     * 输出单个元素
     *
     * @param some 单个元素 T
     * @param <T>  指定类型 T
     */
    public static <T> void show(T some) {
        System.out.println("----some----------");
        System.out.println(some);
        System.out.println("\n---------------");
    }

    /**
     * 返回孩子结点的表示
     *
     * @param children 孩子结点 List of TreeNode
     * @return 孩子结点的表示 String
     */
    public static String getQuaternion(List<TreeNode> children) {
        String res = "";
        for (int i = 0; i < children.size(); i++) {
            res += "," + children.get(i).getName();
        }
        return res;
    }

}
