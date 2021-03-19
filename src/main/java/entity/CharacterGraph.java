package entity;

import java.util.HashSet;
import java.util.Set;

/**
 * @author TYX
 * @name CharacterGraph
 * @description
 * @time 2021/3/19 17:03
 **/
public class CharacterGraph extends LinkGraph<Character,Character>{

    /**
     * NFA->DFA 根据读入的终结符寻找下一个状态
     *
     * @param I 当前状态集
     * @param weight 输入的终结符
     * @return 下一个状态的集合
     */
    public Set<Character> findNext(Set<Character> I, Character weight) {
        return closure(move(I, weight));
    }

    /**
     * 将终结符边加入到状态集
     *
     * @param I 当前状态集
     * @param weight 读入的终结符
     * @return 加入下一条边之后到达的状态
     */
    public Set<Character> move(Set<Character> I, Character weight) {
        Set<Character> res = new HashSet<Character>();
        for (Character ch : I) {
            for (Vertex<Character,Character> vertex : vertices) {
                if (ch.equals(vertex.name) ) {
                    for (Edge<Character,Character> edge : vertex.edges) {
                        if (edge.weight.equals(weight)) {
                            res.add(edge.nextVertex);
                        }
                    }
                }
            }
        }
        return res;
    }

    /**
     * epsilon-closure 寻找当前状态的闭包
     *
     * @param I 当前状态
     * @return 状态的闭包（加入epsilon边可以到达的状态集）
     */
    public Set<Character> closure(Set<Character> I) {
        for (Character ch : I) {
            for (Vertex<Character,Character> vertex : vertices) {
                if (ch.equals(vertex.name)) {
                    for (Edge<Character,Character> edge : vertex.edges) {
                        if (edge.weight.equals('@')) {
                            I.add(edge.nextVertex);
                        }
                    }
                }
            }
        }
        return I;
    }


}
