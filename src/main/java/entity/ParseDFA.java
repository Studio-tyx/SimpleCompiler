package entity;

import tool.CharacterTools;
import tool.ShowTools;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author TYX
 * @name ParseDFA
 * @description 将语法转换成DFA 结果为图结构
 * @createTime 2021/3/19 10:22
 **/
public class ParseDFA {
    private LinkGraph<Set<LRLine>, Character> graph;    //图结构
    private Text text;  //输入语法
    private List<Set<LRLine>> status;   //图结构中的状态集合

    public LinkGraph<Set<LRLine>, Character> getGraph() {
        return graph;
    }

    public List<Set<LRLine>> getStatus() {
        return status;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public void createGraph(Text text) {
        //初始化
        this.text = text;
        this.graph = new LinkGraph<Set<LRLine>, Character>();
        this.status = new ArrayList<Set<LRLine>>();

        //初态
        Set<Character> tmpSet = new HashSet<Character>();
        tmpSet.add('#');
        LRLine firstLR = new LRLine('%', "·S", tmpSet, 0);
        Set<LRLine> lrLineSet = new HashSet<LRLine>();
        lrLineSet.add(firstLR);
        lrLineSet = closure(lrLineSet);
        status.add(lrLineSet);

        for (int i = 0; i < status.size(); i++) {
            Set<LRLine> set = status.get(i);
            if (!moveEnd(set)) {    //集合中的所有元素没有都结束（还有句子需要移动）
                for (Character character : findNextMove(set)) { //下一个字符作为加入边
                    Set<LRLine> newSet = findNextStatus(set, character);    //找下一个集合
                    if (existSet(status, newSet)) { //如果是已有集合 就不动（加边的时候会比较）
                        graph.addEdge(set, status.get(getIndex(status, newSet)), character);
                    } else {
                        status.add(newSet); //不存在则加集合
                        graph.addEdge(set, newSet, character);  //加边
                    }
                }
            }
        }
        //graph.show();
    }

    /**
     * 找下一个可以移动的边
     * 如{"·abc,A·bcd"} 则返回{'a','b'}
     *
     * @param I 当前状态集
     * @return 下一个移动边的集合
     */
    public Set<Character> findNextMove(Set<LRLine> I) {
        Set<Character> res = new HashSet<Character>();
        for (LRLine lrLine : I) {
            String content = lrLine.getContent();
            int index = content.indexOf("·");
            if (index != content.length() - 1) {
                Character character = content.charAt(index + 1);
                if (character != '@') res.add(character);   //如果是空字符就不加
            }
        }
        return res;
    }

    /**
     * 求向前搜索符
     * 项目[A->α.Bβ,a]，当β能导出空串时，该项目的搜索符a传播到项目[B->…,a]
     * 当β不能导出空串时，搜索符为first(βa)
     * 上述关于空串的推导方法来源于 https://blog.csdn.net/qq_41734797/article/details/93253915
     *
     * @param lrLine 需要求first集的字符串
     * @return 向前搜素符集合
     */
    public Set<Character> findForwardSearch(LRLine lrLine) {
        Set<Character> res=new HashSet<Character>();
        String original = lrLine.getContent();
        int index = original.indexOf('·');
        if(original.length()==index+2){
            return lrLine.getForwardSearch();
        }
        else{
            for(int i=index+2;i<original.length();i++){
                if(canBeNUll(original.charAt(i))){//-> a b @ /->@
                    if(!isAllNUll(original.charAt(i))){//->a b @
                        res.addAll(findFirst(original.substring(i)));
                    }
                }
                else{//->a b
                    res.addAll(findFirst(original.substring(i)));
                    return res;
                }
            }
        }
        res.addAll(lrLine.getForwardSearch());//->@
        return res;
    }

    /**
     * 根据句子求first集
     * （如果句子只能推导出空字符串 则返回为空）
     *
     * @param string 句子
     * @return first集
     */
    public Set<Character> findFirst(String string) {
        Set<Character> res = new HashSet<Character>();
        Character first = string.charAt(0);
        if (string.length() == 1 && first == '@') return new HashSet<Character>();  //只能推导出空字符串
        if (CharacterTools.isUpper(first)) {    //如果为非终结符
            List<ProcessLine> start = text.getStartWith(first);
            for (ProcessLine processLine : start) {
                res.addAll(findFirst(processLine.getLine().substring(3)));  //如需要求"SA"的first集 即求"S->apb"中"apb"的first集
            }
        } else {
            res.add(first); //终结符直接加
        }
        return res;
    }

    /**
     * 某个非终结符是否会推导出空字符
     *
     * @param nonTerminal 非终结符
     * @return 是否会推导出空字符串
     */
    public boolean isAllNUll(Character nonTerminal) {
        boolean res=true;
        for(ProcessLine processLine: text.getStartWith(nonTerminal)){
            if (processLine.getLine().charAt(0) == nonTerminal) {
                if (processLine.getLine().charAt(3) != '@') res=false;
            }
        }
        return res;
    }

    /**
     * 可推空+非空
     *
     * @param nonTerminal
     * @return
     */
    public boolean canBeNUll(Character nonTerminal){
        boolean res=false;
        for(ProcessLine processLine: text.getStartWith(nonTerminal)){
            if (processLine.getLine().charAt(0) == nonTerminal) {
                if (processLine.getLine().charAt(3) == '@') res=true;
            }
        }
        return res;
    }

    /**
     * 某个状态是否已经到底
     *
     * @param set 需要判断的状态
     * @return 是否结束
     */
    public boolean moveEnd(Set<LRLine> set) {
        boolean res = true;
        for (LRLine lrLine : set) {
            if(!endProduction(lrLine.getContent())) res=false;
        }
        return res;
    }

    /**
     * 某个句子是否已经结束
     *
     * @param line 句子
     * @return 是否结束
     */
    public boolean endProduction(String line) {
        int index = line.indexOf("·");
        return line.length() == index + 1;
    }

    /**
     * 状态转换
     *
     * @param I      当前状态集
     * @param weight 输入的终结符
     * @return 下一个状态的集合
     */
    public Set<LRLine> findNextStatus(Set<LRLine> I, Character weight) {
        return closure(move(I, weight));
    }

    /**
     * 将终结符边加入到状态集
     *
     * @param I      当前状态集
     * @param weight 读入的终结符
     * @return 加入下一条边之后到达的状态
     */
    public Set<LRLine> move(Set<LRLine> I, Character weight) {
        Set<LRLine> res = new HashSet<LRLine>();
        for (LRLine lrLine : I) {
            LRLine newLine = new LRLine(lrLine);    //新句子
            int index = lrLine.getContent().indexOf('·');
            if (index + 1 == lrLine.getContent().length()) continue;    //已经移动到底了
            Character next = lrLine.getContent().charAt(index + 1); //"·A"的'A'
            if (next == weight || next == '@') {  //如果next正好等于weight则移动字符 或者是空字符（无需weight直接换位）
                StringBuilder stringBuilder = new StringBuilder(lrLine.getContent());
                stringBuilder.setCharAt(index, next);
                stringBuilder.setCharAt(index + 1, '·');
                newLine.setContent(stringBuilder.toString());
                res.add(newLine);
            }
        }
        return res;
    }

    /**
     * epsilon-closure 寻找当前状态的闭包
     * 例如S->a·Pm 则加入P->·p
     *
     * @param I 当前状态
     * @return 状态的闭包（加入epsilon边可以到达的状态集）
     */
    public Set<LRLine> closure(Set<LRLine> I) {
        //我比较closure是否增加的方法：比较变化前后两个集合的大小
        //增长组
        List<LRLine> plus = new ArrayList<LRLine>();
        for (LRLine lrLine : I) {
            plus.add(lrLine);
        }
        int formerSize = plus.size() - 1;

        //增长组>对照组
        while (plus.size() > formerSize) {
            formerSize = plus.size();
            for (int i = 0; i < plus.size(); i++) {
                LRLine lrLine = plus.get(i);
                int index = lrLine.getContent().indexOf('·');
                if (lrLine.getContent().length() == index + 1) continue;    //到底了

                Character next = lrLine.getContent().charAt(index + 1); //在点之后的字符 如"A·a"中的a
                if (CharacterTools.isUpper(next)) { //如果是非终结符
                    List<ProcessLine> newLines = text.getStartWith(next);   //在语法中寻找以该非终结符开始的句子
                    for (ProcessLine processLine : newLines) {
                        LRLine newLine = new LRLine(processLine);
                        if (newLine.getContent().contains("·@")) {  //如果是P->·@ 则直接换成P->@·加入
                            StringBuilder stringBuilder = new StringBuilder(newLine.getContent());
                            int indexE = newLine.getContent().indexOf('@');
                            stringBuilder.setCharAt(indexE, '·');
                            stringBuilder.setCharAt(indexE - 1, '@');
                            newLine.setContent(stringBuilder.toString());
                        }
                        newLine.setForwardSearch(findForwardSearch(lrLine));    //设置向前搜索符集合
                        if (!existLRLine(plus, newLine)){
                            plus.add(newLine); //如果已有的状态集合中没有该句子
                        }
                    }
                }
            }
        }
        return new HashSet<LRLine>(plus);
    }

    /**
     * 重写了List.exist方法（两个LRLine相等只需要内容相等即可）
     *
     * @param list   list
     * @param lrLine list中的对象
     * @return 是否存在某个LRLine
     */
    public boolean existLRLine(List<LRLine> list, LRLine lrLine) {
        for (LRLine line : list) {
            if (LRLineEqual(line, lrLine)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 重写了LRLine的equals方法
     * 即内容相等就可以相等了
     *
     * @param one   一个LRLine
     * @param other 另一个LRLine
     * @return 两个LRLine内容是否相等
     */
    public boolean LRLineEqual(LRLine one, LRLine other) {
        return one.getContent().equals(other.getContent()) && one.getStart().equals(other.getStart())
                && one.getProductionNumber() == other.getProductionNumber()
                && one.getForwardSearch().equals(other.getForwardSearch());
    }

    /**
     * 重写了Set的equals方法（根据内容判断）
     *
     * @param one   Set
     * @param other Set
     * @return 两个集合是否相等
     */
    public boolean setEquals(Set<LRLine> one, Set<LRLine> other) {
        if (one.size() != other.size()) return false;
        int count = 0;
        for (LRLine line1 : one) {
            for (LRLine line2 : other) {
                if (LRLineEqual(line1, line2)) count++;
            }
        }
        return count == one.size();
    }

    /**
     * 重写了Set作为List元素的exist方法（根据内容判断）
     *
     * @param list  List
     * @param other Set
     * @return list中是否存在other
     */
    public boolean existSet(List<Set<LRLine>> list, Set<LRLine> other) {
        for (Set<LRLine> set : list) {
            if (setEquals(other, set)) return true;
        }
        return false;
    }

    /**
     * 重写了Set作为List元素的IndexOf方法（根据内容判断）
     *
     * @param list      List
     * @param lrLineSet Set
     * @return 下标
     */
    public int getIndex(List<Set<LRLine>> list, Set<LRLine> lrLineSet) {
        for (int i = 0; i < list.size(); i++) {
            Set<LRLine> set = list.get(i);
            if (setEquals(set, lrLineSet)) return i;
        }
        return -1;
    }

}
