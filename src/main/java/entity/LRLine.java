package entity;

import java.util.HashSet;
import java.util.Set;

/**
 * @author TYX
 * @name LRLine
 * @description
 * @time 2021/3/19 14:19
 **/
public class LRLine {
    Character start;
    String content;
    Set<Character> forwardSearch;
    int productionNumber;

    public LRLine(Character start, String content, Set<Character> forwardSearch, int productionNumber) {
        this.start = start;
        this.content = content;
        this.forwardSearch = forwardSearch;
        this.productionNumber = productionNumber;
    }

    public LRLine(ProcessLine processLine) {
        this.start = processLine.getLine().charAt(0);
        this.content = "·"+processLine.getLine().substring(3);
        this.productionNumber = processLine.getLineNumber()+1;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setForwardSearch(Set<Character> forwardSearch) {
        this.forwardSearch = forwardSearch;
    }

    @Override
    public String toString() {
        return "LRLine{" +
                "start=" + start +
                ", content='" + content + '\'' +
                ", forwardSearch=" + forwardSearch +
                ", productionNumber=" + productionNumber +
                '}';
    }
}
