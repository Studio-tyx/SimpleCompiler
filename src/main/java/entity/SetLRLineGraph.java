package entity;

import tool.CharacterTools;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author TYX
 * @name SetLRLineGraph
 * @description
 * @time 2021/3/19 17:09
 **/
public class SetLRLineGraph extends LinkGraph<Set<LRLine>,Character>{
    /**
     * NFA->DFA 根据读入的终结符寻找下一个状态
     *
     * @param I 当前状态集
     * @param weight 输入的终结符
     * @return 下一个状态的集合
     */
    public Set<LRLine> findNext(Set<LRLine> I, Character weight,Text text) {
        return closure(move(I, weight),text);
    }

    public Set<Character> findFirstSet(Text text,String production){//production="aBc"/"BC"
        Set<Character> res=new HashSet<Character>();
        List<ProcessLine> start=text.getStartWith(production.charAt(0));
        for(ProcessLine processLine:start){
            Character character=processLine.getLine().charAt(3);
            if(CharacterTools.isLower(character))   res.add(character);
            else    res.addAll(findFirstSet(text,processLine.getLine().substring(3)));
        }
        return res;
    }


    /**
     * 将终结符边加入到状态集
     *
     * @param I 当前状态集
     * @param weight 读入的终结符
     * @return 加入下一条边之后到达的状态
     */
    public Set<LRLine> move(Set<LRLine> I, Character weight) {
        Set<LRLine> res = new HashSet<LRLine>();
        for(LRLine lrLine:I){
            int index=lrLine.content.indexOf('·');
            Character tmp=lrLine.content.charAt(index+1);
            if(tmp==weight){
                StringBuilder stringBuilder=new StringBuilder(lrLine.content);
                stringBuilder.setCharAt(index,tmp);
                stringBuilder.setCharAt(index+1,'·');
                lrLine.setContent(stringBuilder.toString());
                System.out.println(lrLine);
                res.add(lrLine);
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
    public Set<LRLine> closure(Set<LRLine> I,Text text) {
        for(LRLine lrLine:I){
            int index=lrLine.content.indexOf('·');
            Character tmp=lrLine.content.charAt(index+1);
            if(CharacterTools.isUpper(tmp)){
                List<ProcessLine> newLines = text.getStartWith(tmp);
                for(ProcessLine processLine:newLines){
                    String newString=processLine.getLine();
                    LRLine newLine=new LRLine(processLine);
                    if(lrLine.content.length()==index+2){//S->·A
                        newLine.setForwardSearch(lrLine.forwardSearch);
                    }
                    else{//S->·Aa
                        //把尾巴总合成一个String 然后用FindFirst
                    }
                }
            }
        }
        return I;
    }

}
