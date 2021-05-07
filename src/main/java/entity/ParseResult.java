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

    /**
     * 构造器
     */
    public ParseResult() {
    }

    /**
     * 构造器
     *
     * @param stack 分析栈 List of Line
     * @param res 分析结果 boolean
     * @param quaternions 四元式 List of String
     */
    public ParseResult(List<Line> stack, boolean res,List<String> quaternions) {
        this(stack,res,"",quaternions);
    }

    /**
     * 构造器
     *
     * @param stack 分析栈 List of Line
     * @param res 分析结果 boolean
     * @param information 分析信息（期待的下一字符） String
     * @param quaternions 四元式 List of String
     */
    public ParseResult(List<Line> stack, boolean res, String information, List<String> quaternions) {
        this.stack = stack;
        this.res = res;
        this.information = information;
        this.quaternions=quaternions;
    }

    /**
     * 设置Goto表
     *
     * @param gotoTable Goto表 GOTO
     */
    public void setGotoTable(GOTO gotoTable) {
        this.gotoTable = gotoTable;
    }

    /**
     * 展示（测试用）
     */
    public void show(){
        for(Line line:stack){
            System.out.println(line);
        }
        System.out.println(res+","+information);
    }

    /**
     * 返回Goto表
     *
     * @return Goto表 GOTO
     */
    public GOTO getGotoTable() {
        return gotoTable;
    }

    /**
     * 返回四元式
     *
     * @return 四元式 List of String
     */
    public List<String> getQuaternions() {
        return quaternions;
    }

    /**
     * 返回分析栈
     *
     * @return 分析栈 List of Line
     */
    public List<Line> getStack() {
        return stack;
    }

    /**
     * 返回分析结果
     *
     * @return 分析结果 boolean
     */
    public boolean isRes() {
        return res;
    }

    /**
     * 返回分析信息
     *
     * @return 分析信息（期待的下一字符） String
     */
    public String getInformation() {
        return information;
    }

    /**
     * 返回分析栈行数
     *
     * @return 分析栈行数 int
     */
    public int getRows(){
        return stack.size();
    }

    public static class Line{
        public String characters;
        public String status;
        public String next;
        public String action;

        /**
         * 构造器
         *
         * @param characters 分析栈 String
         * @param status 状态栈 String
         * @param next 下一字符 String
         * @param action Action String
         */
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
