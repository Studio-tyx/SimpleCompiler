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
    private final List<Character> terminals;  //终结符集合
    private final List<Character> nonTerminals;   //非终结符集合
    private int statusNumber;   //状态数

    /**
     * 构造器
     *
     * @param terminals 终结符 List of Character
     * @param nonTerminals 非终结符 List of Character
     */
    public GOTO(List<Character> terminals, List<Character> nonTerminals) {
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
    }

    /**
     * 设置Goto表的内容
     *
     * @param aGoto Goto表的内容 String[][]
     */
    public void setGoto(String[][] aGoto) {
        Goto = aGoto;
    }

    /**
     * 设置状态数
     *
     * @param statusNumber 状态数 int
     */
    public void setStatusNumber(int statusNumber) {
        this.statusNumber = statusNumber;
    }

    /**
     * 返回Goto表
     *
     * @return Goto表 String[][]
     */
    public String[][] getGoto() {
        return Goto;
    }

    /**
     * 返回Goto表的某一项（以Character为表头搜索）
     *
     * @param nowStatus 当前状态 int
     * @param thisChar 表头 Character
     * @return Goto表的相应表项 String
     */
    public String getByChar(int nowStatus, Character thisChar){
        return Goto[nowStatus][CharacterTools.isUpper(thisChar) ? nonTerminals.indexOf(thisChar) + terminals.size() : terminals.indexOf(thisChar)];  //
    }

    /**
     * 返回Goto表的某一项（以int为表头搜索）
     *
     * @param nowStatus 当前状态 int
     * @param no 表头 int
     * @return Goto表的相应表项 String
     */
    public String getByInt(int nowStatus,int no){
        return Goto[nowStatus][no];
    }

    /**
     * 返回终结符集合
     *
     * @return 终结符集合 List of Character
     */
    public List<Character> getTerminals() {
        return terminals;
    }

    /**
     * 返回非终结符集合
     *
     * @return 非终结符集合 List of Character
     */
    public List<Character> getNonTerminals() {
        return nonTerminals;
    }

    /**
     * 返回状态数（集合数）
     *
     * @return 状态数 int
     */
    public int getStatusNumber() {
        return statusNumber;
    }
}
