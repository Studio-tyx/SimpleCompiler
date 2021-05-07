package entity;

import java.util.Set;

/**
 * @author TYX
 * @name LRLine
 * @description LR分析中的句子（含向前搜索符）
 * @createTime 2021/3/19 14:19
 **/
public class LRLine {
    private final Character start;
    private String content;
    private Set<Character> forwardSearch;
    private final int productionNumber;

    /**
     * 构造器
     *
     * @param start 产生式左部 Character
     * @param content 产生式内容 String
     * @param forwardSearch 含向前搜索符的集合 Set of Character
     * @param productionNumber 产生式行数 int
     */
    public LRLine(Character start, String content, Set<Character> forwardSearch, int productionNumber) {
        this.start = start;
        this.content = content;
        this.forwardSearch = forwardSearch;
        this.productionNumber = productionNumber;
    }

    /**
     * 构造器（由另一句子构造）
     *
     * @param other 另一句子 LRLine
     */
    public LRLine(LRLine other) {
        this.start = other.start;
        this.content = other.content;
        this.forwardSearch = other.forwardSearch;
        this.productionNumber = other.productionNumber;
    }

    /**
     * 构造器（由文本某一行初始化）
     *
     * @param processLine 文本的某一行 ProcessLine
     */
    public LRLine(ProcessLine processLine) {
        this.start = processLine.getLine().charAt(0);
        if (processLine.getLine().contains("#")) {
            String tmp = processLine.getLine().split("#")[0];
            this.content = "·" + tmp.substring(3);
        } else {
            this.content = "·" + processLine.getLine().substring(3);
        }
        this.productionNumber = processLine.getLineNumber() + 1;
    }

    @Override
    public boolean equals(Object obj) {
        LRLine other = (LRLine) obj;
        return this.content.equals(other.content) && this.start.equals(other.start)
                && this.productionNumber == other.productionNumber
                && this.forwardSearch.equals(other.forwardSearch);
    }

    /**
     * 返回句子内容
     *
     * @return 句子内容 String
     */
    public String getContent() {
        return content;
    }

    /**
     * 返回含向前搜索符的集合
     * @return 含向前搜索符的集合 Set of Character
     */
    public Set<Character> getForwardSearch() {
        return forwardSearch;
    }

    /**
     * 设置句子内容
     * @param content 句子内容 String
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 设置含向前搜索符的集合
     * @param forwardSearch 含向前搜索符的集合 Set of Character
     */
    public void setForwardSearch(Set<Character> forwardSearch) {
        this.forwardSearch = forwardSearch;
    }

    /**
     * 返回产生式左部
     *
     * @return 产生式左部 Character
     */
    public Character getStart() {
        return start;
    }

    /**
     * 返回产生式行数
     *
     * @return 产生式行数 int
     */
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
