package entity;

/**
 * @author TYX
 * @name ProcessLine
 * @description 行的封装类（包括行号与行内容）
 * @time 2021/3/8 15:40
 **/
public class ProcessLine {
    private int lineNumber; //行号
    private String line;    //行内容

    public ProcessLine() {
    }

    /**
     * 构造器
     *
     * @param lineNumber 行号
     * @param line 行的内容
     */
    public ProcessLine(int lineNumber, String line) {
        this.lineNumber = lineNumber;
        this.line = line;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getLine() {
        return line;
    }

    /**
     * 重写了toString函数 便于输出
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
