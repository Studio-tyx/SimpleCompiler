package entity;

import tool.CharacterTools;

import java.util.List;

/**
 * @author TYX
 * @name GOTO
 * @description Goto表的数据结构
 * @createTime 2021/5/5 20:12
 **/
public class GOTO {
    private String[][] Goto;    //Goto表
    private List<Character> terminals;  //终结符集合
    private List<Character> nonTerminals;   //非终结符集合
    private int statusNumber;   //状态数

    public GOTO(List<Character> terminals, List<Character> nonTerminals) {
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
    }

    public void setGoto(String[][] aGoto) {
        Goto = aGoto;
    }

    public void setStatusNumber(int statusNumber) {
        this.statusNumber = statusNumber;
    }

    public String[][] getGoto() {
        return Goto;
    }

    public String getByChar(int nowStatus, Character thisChar){
        return Goto[nowStatus][CharacterTools.isUpper(thisChar) ? nonTerminals.indexOf(thisChar) + terminals.size() : terminals.indexOf(thisChar)];  //
    }

    public String getByInt(int nowStatus,int no){
        return Goto[nowStatus][no];
    }

    public List<Character> getTerminals() {
        return terminals;
    }

    public List<Character> getNonTerminals() {
        return nonTerminals;
    }

    public int getStatusNumber() {
        return statusNumber;
    }
}
