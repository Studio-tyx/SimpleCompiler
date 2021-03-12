package entity;

/**
 * @author TYX
 * @name ProcessLine
 * @description
 * @time
 **/
public class ProcessLine {
    private int lineNumber;
    private String line;

    public ProcessLine() {
    }

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

    @Override
    public String toString() {
        return "ProcessLine{" +
                "lineNumber=" + lineNumber +
                ", line='" + line + '\'' +
                '}';
    }
}
