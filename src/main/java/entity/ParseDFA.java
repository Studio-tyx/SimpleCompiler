package entity;

import tool.CharacterTools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author TYX
 * @name ParseDFA
 * @description
 * @time 2021/3/19 10:22
 **/
public class ParseDFA {
    private LinkGraph<Set<LRLine>, Character> graph;
    private Text text;
    private List<Set<LRLine>> status;

    public LinkGraph<Set<LRLine>, Character> getGraph() {
        return graph;
    }

    public List<Set<LRLine>> getStatus() {
        return status;
    }

    public Text getText() {
        return text;
    }

    /*
    还有空格的处理
     */
    public void createGraph(Text text) {
        this.text = text;
        this.graph = new LinkGraph<Set<LRLine>, Character>();
        this.status = new ArrayList<Set<LRLine>>();

        Set<Character> tmpSet = new HashSet<Character>();
        tmpSet.add('#');
        LRLine firstLR = new LRLine('%', "·S", tmpSet, 0);
        Set<LRLine> lrLineSet = new HashSet<LRLine>();
        lrLineSet.add(firstLR);
        lrLineSet = closure(lrLineSet);
        status.add(lrLineSet);
        for (int i = 0; i < status.size(); i++) {
            Set<LRLine> set = status.get(i);
            if (!moveEnd(set)) {
                for (Character character : findNextChar(set)) {
                    Set<LRLine> newSet = findNextStatus(set, character);
                    graph.addEdge(set, newSet, character);
                    if(existSet(status,newSet)){
                    } else status.add(newSet);
                }
            }
        }
        graph.show();
    }


    /**
     * @param I      当前状态集
     * @param weight 输入的终结符
     * @return 下一个状态的集合
     */
    public Set<LRLine> findNextStatus(Set<LRLine> I, Character weight) {
        return closure(move(I, weight));
    }

    public Set<Character> findNextChar(Set<LRLine> I) {
        Set<Character> res = new HashSet<Character>();
        for (LRLine lrLine : I) {
            String content = lrLine.getContent();
            if (content.indexOf("·") != content.length() - 1) {
                res.add(content.charAt(content.indexOf("·") + 1));
            }
        }
        return res;
    }

    public Set<Character> findFirstSet(String production) {//production="aBc"/"BC" return set<a,b,..>
        Set<Character> res = new HashSet<Character>();
        Character first = production.charAt(0);
        if (CharacterTools.isLower(first)||CharacterTools.isPunctuation(first)) {
            res.add(first);
        } else {
            List<ProcessLine> start = text.getStartWith(first);
            for (ProcessLine processLine : start) {
                Character character = processLine.getLine().charAt(3);
                if (CharacterTools.isLower(character)||CharacterTools.isPunctuation(first)) res.add(character);
                else res.addAll(findFirstSet(processLine.getLine().substring(3)));
            }
        }
        return res;
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
            LRLine tmpLine = new LRLine(lrLine);
            int index = lrLine.getContent().indexOf('·');
            if (index + 1 == lrLine.getContent().length()) continue;
            Character tmp = lrLine.getContent().charAt(index + 1);
            if (tmp == weight) {
                StringBuilder stringBuilder = new StringBuilder(lrLine.getContent());
                stringBuilder.setCharAt(index, tmp);
                stringBuilder.setCharAt(index + 1, '·');
                tmpLine.setContent(stringBuilder.toString());
                res.add(tmpLine);
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
    public Set<LRLine> closure(Set<LRLine> I) {
        List<LRLine> plus = new ArrayList<LRLine>();
        for (LRLine lrLine : I) {
            plus.add(lrLine);
        }
        int formerSize = plus.size() - 1;
        while (plus.size() > formerSize) {
            formerSize = plus.size();
            for (int i = 0; i < plus.size(); i++) {
                LRLine lrLine = plus.get(i);
                int index = lrLine.getContent().indexOf('·');
                if (lrLine.getContent().length() == index + 1) continue;
                Character tmp = lrLine.getContent().charAt(index + 1);
                if (CharacterTools.isUpper(tmp)) {
                    List<ProcessLine> newLines = text.getStartWith(tmp);
                    for (ProcessLine processLine : newLines) {
                        LRLine newLine = new LRLine(processLine);
                        if (lrLine.getContent().length() == index + 2) {//S->·A
                            newLine.setForwardSearch(lrLine.getForwardSearch());
                        } else {//S->·Aa
                            newLine.setForwardSearch(findFirstSet(lrLine.getContent().substring(index + 2)));
                        }
                        if (!existLRLine(plus, newLine)) plus.add(newLine);
                    }
                }
            }
        }
        return new HashSet<LRLine>(plus);
    }

    public boolean existLRLine(List<LRLine> set, LRLine lrLine) {
        for (LRLine line : set) {
            //内容相等
            if (line.getContent().equals(lrLine.getContent()) && line.getStart().equals(lrLine.getStart())
                    && line.getProductionNumber() == lrLine.getProductionNumber()
                    && line.getForwardSearch().equals(lrLine.getForwardSearch())) {
                return true;
            }
        }
        return false;
    }

    public boolean existSet(List<Set<LRLine>> list,Set<LRLine> other){
        for(Set<LRLine> set:list){
            if(setEquals(other,set))return true;
        }
        return false;
    }

    public boolean setEquals(Set<LRLine> one,Set<LRLine> other){
        if(one.size()!=other.size())return false;
        int count=0;
        for(LRLine line1:one){
            for(LRLine line2:other){
                if(line1.equals(line2))count++;
            }
        }
        if(count==one.size())return true;
        else return false;
    }

    public boolean moveEnd(Set<LRLine> set) {
        boolean res = true;
        for (LRLine lrLine : set) {
            if (lrLine.getContent().indexOf("·") == lrLine.getContent().length() - 1) {

            } else res = false;
        }
        return res;
    }



}
