package entity;

import java.util.Set;

/**
 * @author TYX
 * @name LRLine
 * @description
 * @time 2021/3/19 14:19
 **/
public class LRLine {
    private final Character start;
    private String content;
    private Set<Character> forwardSearch;
    private final int productionNumber;

    public LRLine(Character start, String content, Set<Character> forwardSearch, int productionNumber) {
        this.start = start;
        this.content = content;
        this.forwardSearch = forwardSearch;
        this.productionNumber = productionNumber;
    }

    public LRLine(LRLine other) {
        this.start = other.start;
        this.content = other.content;
        this.forwardSearch = other.forwardSearch;
        this.productionNumber = other.productionNumber;
    }

    public LRLine(ProcessLine processLine) {
        this.start = processLine.getLine().charAt(0);
        this.content = "·" + processLine.getLine().substring(3);
        this.productionNumber = processLine.getLineNumber() + 1;
    }

    @Override
    public boolean equals(Object obj) {
        LRLine other = (LRLine) obj;
        if (this.content.equals(other.content) && this.start.equals(other.start)
                && this.productionNumber == other.productionNumber
                && this.forwardSearch.equals(other.forwardSearch)) {
            return true;
        }
        return false;
    }

    public String getContent() {
        return content;
    }

    public Set<Character> getForwardSearch() {
        return forwardSearch;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setForwardSearch(Set<Character> forwardSearch) {
        this.forwardSearch = forwardSearch;
    }

    public Character getStart() {
        return start;
    }

    public int getProductionNumber() {
        return productionNumber;
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
