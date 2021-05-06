package entity;


import java.util.List;

/**
 * @author TYX
 * @name ParseResult
 * @description 语法分析的结果数据结构
 * @createTime 2021/5/5 14:42
 **/
public class ParseResult {
    private List<Line> stack;   //分析栈
    private boolean res;    //分析结果
    private String information; //分析信息（期待的下一字符）
    private List<String> quaternions;   //四元式
    private GOTO gotoTable; //Goto表

    public ParseResult() {
    }

    public ParseResult(List<Line> stack, boolean res,List<String> quaternions) {
        this(stack,res,"",quaternions);
    }

    public ParseResult(List<Line> stack, boolean res, String information, List<String> quaternions) {
        this.stack = stack;
        this.res = res;
        this.information = information;
        this.quaternions=quaternions;
    }

    public void setGotoTable(GOTO gotoTable) {
        this.gotoTable = gotoTable;
    }

    public void show(){
        for(Line line:stack){
            System.out.println(line);
        }
        System.out.println(res+","+information);
    }

    public GOTO getGotoTable() {
        return gotoTable;
    }

    public List<String> getQuaternions() {
        return quaternions;
    }

    public List<Line> getStack() {
        return stack;
    }

    public boolean isRes() {
        return res;
    }

    public String getInformation() {
        return information;
    }

    public int getRows(){
        return stack.size();
    }

    public static class Line{
        public String characters;
        public String status;
        public String next;
        public String action;

        public Line(String characters, String status, String next, String action) {
            this.characters = characters;
            this.status = status;
            this.next = next;
            this.action = action;
        }

        @Override
        public String toString() {
            return "Line{" +
                    "characters='" + characters + '\'' +
                    ", status='" + status + '\'' +
                    ", next='" + next + '\'' +
                    ", action='" + action + '\'' +
                    '}';
        }
    }
}
