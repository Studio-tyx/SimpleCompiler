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
        int i = 0;
        for (T t : set) {
            System.out.print(i + ":" + t + ",");
            i++;
        }
        System.out.println("\n---------------");
    }

    /**
     * 集合展示 便于输出
     *
     * @param list 集合
     * @param <T>  指定状态
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
     * 集合展示 便于输出
     *
     * @param some 单独字符
     * @param <T>  指定状态
     */
    public static <T> void show(T[][] some) {
        System.out.println("----数组----------");
        for(T[] t:some){
            for(T tt:t){
                System.out.print(tt+"\t");
            }
            System.out.println();
        }
        System.out.println("\n---------------");
    }

    /**
     * 集合展示 便于输出
     *
     * @param some 单独字符
     * @param <T>  指定状态
     */
    public static <T> void show(T some) {
        System.out.println("----some----------");
        System.out.println(some);
        System.out.println("\n---------------");
    }

}
