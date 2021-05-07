package entity;

/**
 * @author TYX
 * @name ProcessLine
 * @description 行的封装类（包括行号与行内容）
 * @createTime 2021/3/8 15:40
 **/
public class ProcessLine {
    private int lineNumber; //行号
    private String line;    //行内容

    /**
     * 构造器
     */
    public ProcessLine() {
    }

    /**
     * 构造器
     *
     * @param lineNumber 行号 int
     * @param line       行的内容 String
     */
    public ProcessLine(int lineNumber, String line) {
        this.lineNumber = lineNumber;
        this.line = line;
    }

    /**
     * 返回行号
     *
     * @return 行号 int
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * 返回行的内容
     *
     * @return 行的内容 String
     */
    public String getLine() {
        return line;
    }

    /**
     * 重写了toString函数<br>
     *     便于输出
     *
     * @return 对象信息
     */
    @Override
    public String toString() {
        return "ProcessLine{" +
                "lineNumber=" + lineNumber +
                ", line='" + line + '\'' +
                '}';
    }
}
