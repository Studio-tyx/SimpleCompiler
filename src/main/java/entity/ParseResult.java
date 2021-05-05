package entity;


import java.util.List;

/**
 * @author TYX
 * @name ParseResult
 * @description
 * @createTime 2021/5/5 14:42
 **/
public class ParseResult {
    private List<Line> stack;
    private boolean res;
    private String information;
    private List<String> quaternions;
    private GOTO gotoTable;

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
