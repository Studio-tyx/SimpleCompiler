package entity;

import java.util.LinkedList;
import java.util.List;

/**
 * @author TYX
 * @name Tokens=List of Token
 * @description 词法分析器的分析结果
 * @time 2021/3/8 18:28
 **/

class Token {
    private int number; //行号
    private String content; //单词内容
    private int type;//1->keyword 2->boundary 3->operator 4->number 5->标识符 -1->待定 6->String常量 7->注释 8->错误标识符

    private static final String[] typeDescription = {"待定", "关键字", "界符", "运算符", "数字常量", "标识符", "String常量", "注释", "错误标识符"};

    public Token() {
    }

    /**
     * 构造器
     *
     * @param number 行号
     * @param content 单词内容
     * @param type 单词种类
     */
    public Token(int number, String content, int type) {
        this.number = number;
        this.content = content;
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 重写方法 便于输出
     *
     * @return 该对象的信息
     */
    @Override
    public String toString() {
        return "Token{" +
                "line number=" + number +
                ", content='" + content + '\'' +
                ", type=" + (type == -1 ? typeDescription[0] : typeDescription[type]) +
                '}';
    }
}

public class Tokens {
    private LinkedList<Token> tokens;   //token信息集合

    //关键字常量
    private static final String[] keywords = {"private", "public", "protected", "class", "new",
            "return", "if", "else", "for", "while", "try", "catch", "throws", "import", "package",
            "boolean", "char", "int", "double", "float", "TRUE", "FALSE", "this", "void"};
    //完整"private","protected","public","abstract","class","extends","final","implements",
    // "interface","native","new","static","strictfp","synchronized","transient","volatile",
    // "break","continue","return","do","while","if","else","for","instanceof","switch",
    // "case","default","try","catch","throw","throws","import","package","boolean","byte",
    // "char","double","float","int","long","short","super","this","void","goto","const"

    //界符常量
    private static final String[] boundaries = {"{", "}", "(", ")", "[", "]", ",", ";"};

    //运算符常量
    private static final String[] operators = {"!=", "==", "*", "/", "="};

    public Tokens() {
        tokens = new LinkedList<Token>();
    }

    public LinkedList<Token> getTokens() {
        return tokens;
    }

    /**
     * 分离注释 //content type=7
     * 必须在分离关键字与界符之前
     *
     * @param text 读入的文本
     */
    public void separateNote(List<ProcessLine> text) {
        for (ProcessLine pl : text) {
            Token tmpToken = new Token(pl.getLineNumber(), pl.getLine(), -1);
            if (pl.getLine().contains("//")) {
                String line = pl.getLine();
                tmpToken.setContent(line.substring(0, line.indexOf("//")));
                tokens.add(tmpToken);
                Token addToken = new Token(pl.getLineNumber(), line.substring(line.indexOf("//") + 2), 7);
                tokens.add(addToken);
            } else {
                tokens.add(tmpToken);
            }
        }
    }


    /**
     * 分离引号（String常量） "String constant" type=6
     * 必须在分离关键字之前完成
     */
    public void separateString() {
        boolean hasQuotation = false;
        String front, back;
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            String word = token.getContent();
            if (word.contains("\"")) {
                front = word.substring(0, word.indexOf("\""));
                back = word.substring(word.indexOf("\"") + 1);
                Token frontToken, backToken;
                if (!hasQuotation) {//'nn"yy"'
                    frontToken = new Token(token.getNumber(), front, -1);
                    backToken = new Token(token.getNumber(), back, 6);
                    hasQuotation = true;
                } else {//'yy"nn'
                    frontToken = new Token(token.getNumber(), "\"" + front + "\"", 6);
                    backToken = new Token(token.getNumber(), back, -1);
                    hasQuotation = false;
                }
                tokens.remove(token);
                if (!front.equals("")) tokens.add(i++, frontToken);
                if (!back.equals("")) tokens.add(i, backToken);
                i--;
            } else {
                continue;
            }
        }
    }

    /**
     * 分离（筛去）空格
     */
    public void separateSpace() {
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() == -1) {//unclassified
//                if(token.getContent().equals("")){
//                    token.setType(2);
//                    break;
//                }
//              上面这个是把空行处理成界符的 我觉得有点不太合适 还是删掉空行吧 如下：
                if (token.getContent().equals("")) {
                    tokens.remove(token);
                    i--;
                    continue;
                }
                String line = token.getContent();
                line.replace("\t", "");
                String[] words = line.split("\\s+");
                tokens.remove(token);
                for (String word : words) {
                    tokens.add(i++, new Token(token.getNumber(), word, -1));
                }
                i--;
            }
        }
    }

    /**
     * 分离界符 type=2
     */
    public void separateBoundary() {
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() != -1) continue;
            //if(token.getContent().length()==1) continue;
            for (String compare : boundaries) {
                if (token.getContent().contains(compare)) {
                    String splitUnit = compare;
                    if (compare.equals("{") || compare.equals("(") || compare.equals(")") || compare.equals("["))
                        splitUnit = String.format("\\%s", compare);
                    String[] split = token.getContent().split("((?<=" + splitUnit + ")|(?=" + splitUnit + "))");
                    int index = tokens.indexOf(token);
                    tokens.remove(token);
                    i--;
                    for (String tmp : split) {
                        if (tmp.equals(compare)) {
                            tokens.add(index++, new Token(token.getNumber(), tmp, 2));
                        } else {
                            tokens.add(index++, new Token(token.getNumber(), tmp, -1));
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * 分离关键字 type=1
     */
    public void markKeyword() {
        for (Token token : tokens) {
            for (String keyword : keywords) {
                if (token.getContent().equals(keyword)) {
                    token.setType(1);
                }
            }
        }
    }

    /**
     * 分离运算符（除了+/-） type=3
     * 必须在分离数字常量之前进行 否则无法分离如"3E+1 == 2+2i"
     */
    public void separateOperator() {
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() != -1) continue;
            for (String compare : operators) {
                if (token.getContent().contains(compare)) {
                    String splitUnit = compare;
                    String[] split = token.getContent().split("((?<=" + splitUnit + ")|(?=" + splitUnit + "))");
                    int index = tokens.indexOf(token);
                    tokens.remove(token);
                    i--;
                    for (String tmp : split) {
                        if (tmp.equals(compare)) {
                            tokens.add(index++, new Token(token.getNumber(), tmp, 3));
                        } else {
                            tokens.add(index++, new Token(token.getNumber(), tmp, -1));
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * 是否为数字
     *
     * @param ch 需要判断的字符
     * @return 是数字或者不是
     */
    public boolean isDigit(char ch) {
        return (ch >= '0' && ch <= '9');
    }

    /**
     * 是否为实数（用了DFA） 格式：xx.xx
     *
     * @param word 需要判断的单词
     * @return 是否为实数
     */
    public boolean isRealNumber(String word) {
        if (word.equals("")) return true;
        int status = 0;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (status == 0 && isDigit(ch)) status = 1;
            else if (status == 1 && isDigit(ch)) status = 1;
            else if (status == 1 && ch == '.') status = 2;
            else if (status == 2 && isDigit(ch)) status = 3;
            else if (status == 3 && isDigit(ch)) status = 3;
            else return false;
        }
        return status == 1 || status == 3;
    }

    /**
     * 是否为数字（包括科学计数法和虚数）
     * 此处并不考虑科学计数法与虚数的结合情况（语法分析内容）
     * 如：（3E+1）+(2E-10)i 在此只会分隔成 (|3E+1|)|+|(|2E-10|)|i
     * 科学计数法格式：xE+x/xE-x 通过E+或E-分割 判断分割后两侧是否均为实数
     * 虚数格式：xi/x+xi/x-xi 通过i与+/-判断
     *
     * @param word 需要判断的单词
     * @return 是否为数字
     */
    public boolean isNumber(String word) {
        boolean res = false;
        boolean plusE = word.contains("E+"), minusE = word.contains("E-");
        if (plusE || minusE) {  // 3E+10 / 2E-10
            String[] after;
            if (plusE) after = word.split("E\\+");
            else after = word.split("E-");
            if (after.length == 2) {
                String former = after[0];
                String later = after[1];
                res = isRealNumber(former) && isRealNumber(later);
            }
        } else if (word.charAt(word.length() - 1) == 'i') { // 3i / 1+2i / 1-2i
            String exceptI = word.substring(0, word.length() - 1);
            res = isRealNumber(exceptI);    //如果为3i此处应为true
            boolean plus = exceptI.contains("+"), minus = exceptI.contains("-");
            if (plus || minus) {
                String[] after;
                if (plus) after = exceptI.split("\\+");
                else after = exceptI.split("-");
                if (after.length == 2) {
                    String former = after[0];
                    String later = after[1];
                    res = isRealNumber(former) && isRealNumber(later);
                }
            }
        } else {    // 3.12 / 300
            res = isRealNumber(word);
        }
        return res;
    }

    /**
     * 分离数字常量 type=4
     */
    public void separateNumber() {
        for (Token token : tokens) {
            if (token.getType() == -1) {
                if (isNumber(token.getContent())) token.setType(4);
            }
        }
    }

    /**
     * 分离+/- type=3
     * 必须在分离数字常量之后 可以保证3E+10是一个整体
     */
    public void separatePlusOrMinus() {
        String[] plusOrMinus = {"+", "-"};
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() != -1) continue;
            if (token.getContent().length() == 1) continue;
            for (String compare : plusOrMinus) {
                if (token.getContent().contains(compare)) {
                    String splitUnit = compare;
                    if (compare.equals("+")) splitUnit = "\\+";
                    String[] split = token.getContent().split("((?<=" + splitUnit + ")|(?=" + splitUnit + "))");
                    int index = tokens.indexOf(token);
                    tokens.remove(token);
                    i--;
                    for (String tmp : split) {
                        if (tmp.equals(compare)) {
                            tokens.add(index++, new Token(token.getNumber(), tmp, 3));
                        } else {
                            tokens.add(index++, new Token(token.getNumber(), tmp, -1));
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * 分离点号 如System.out.println=>System|.|out|.|println
     * 必须在分离数字常量之后 否则无法保证如"3.14"的完整
     */
    public void separatePoint() {
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() != -1) continue;
            if (token.getContent().length() == 1) continue;
            if (token.getContent().contains(".")) {
                String[] split = token.getContent().split("((?<=\\.)|(?=\\.))");
                int index = tokens.indexOf(token);
                tokens.remove(token);
                i--;
                for (String tmp : split) {
                    if (tmp.equals(".")) {
                        tokens.add(index++, new Token(token.getNumber(), tmp, 2));
                    } else {
                        tokens.add(index++, new Token(token.getNumber(), tmp, -1));
                    }
                }
                break;
            }
        }
    }

    /**
     * 分离标识符（识别标识符）
     * 标识符符合文法则type=5 否则type=8
     *
     * @param matrixFA 文法
     */
    public void separateIdentifier(MatrixFA matrixFA) {
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() != -1) continue;
            if (matrixFA.check(token.getContent())) token.setType(5);
            else token.setType(8);
            tokens.set(i, token);
        }
    }

    /**
     * 按分类展示
     */
    public void showByClass() {
        System.out.println("*************CLASS**************");
        System.out.println("---------keywords----------");
        for (Token token : tokens) {
            if (token.getType() == 1) System.out.println(token.toString());
        }
        System.out.println("---------boundaries----------");
        for (Token token : tokens) {
            if (token.getType() == 2) System.out.println(token.toString());
        }
        System.out.println("---------operators----------");
        for (Token token : tokens) {
            if (token.getType() == 3) System.out.println(token.toString());
        }
        System.out.println("---------numbers----------");
        for (Token token : tokens) {
            if (token.getType() == 4) System.out.println(token.toString());
        }
        System.out.println("---------identifiers----------");
        for (Token token : tokens) {
            if (token.getType() == 5) System.out.println(token.toString());
        }
        System.out.println("---------\"****\"----------");
        for (Token token : tokens) {
            if (token.getType() == 6) System.out.println(token.toString());
        }
        System.out.println("---------//----------");
        for (Token token : tokens) {
            if (token.getType() == 7) System.out.println(token.toString());
        }
        System.out.println("---------wrong----------");
        for (Token token : tokens) {
            if (token.getType() == 8) System.out.println(token.toString());
        }
        System.out.println("---------unclassified----------");
        for (Token token : tokens) {
            if (token.getType() == -1) System.out.println(token.toString());
        }
        System.out.println();
    }

    /**
     * 按输入序列展示
     */
    public void showBySequence() {
        System.out.println("*************SEQUENCE**************");
        for (Token token : tokens) {
            System.out.println(token.toString());
        }
        System.out.println();
    }

}
