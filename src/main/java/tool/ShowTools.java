package tool;

import java.util.List;
import java.util.Set;

/**
 * @author TYX
 * @name ShowTools
 * @description
 * @time 2021/3/19 15:05
 **/
public final class ShowTools {
    /**
     * 集合展示 便于输出
     *
     * @param set 集合
     * @param <T> 指定状态
     */
    public static <T> void show(Set<T> set) {
        System.out.println("----set----------");
        for (T t : set) {
            System.out.print(t + ",");
        }
        System.out.println("\n---------------");
    }

    /**
     * 集合展示 便于输出
     *
     * @param list 集合
     * @param <T> 指定状态
     */
    public static <T> void show(List<T> list) {
        System.out.println("----list----------");
        for (T t : list) {
            System.out.print(t + ",");
        }
        System.out.println("\n---------------");
    }

}
