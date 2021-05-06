package entity;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author TYX
 * @name Tokens=List of Token
 * @description 词法分析器的分析结果
 * @createTime 2021/3/8 18:28
 **/

class Token {
    private int number; //行号
    private String content; //单词内容
    private char type;
    /*
    a->arithmetic:+ - * /
    b->boundaries:{ } ( ) [ ] , ; .
    c->class
    d
    e->error identifier:SSS
    f->for
    g
    h
    i->identifier:main a x b
    j
    k->keywords(other):
    l->logistic:!= == > <
    m
    n->note //
    o
    p->if
    q->else
    r->realNumber 1.24
    s->String const:"xxx"
    t->type:void boolean char int double float
    u->unclassified
    v->visibility:private public protected
    w->while
    x
    y
    z->return
     */

    private static final String[] typeDescription = {"算术运算符", "界符", "class", "d", "错误标识符", "for", "g", "h", "标识符",
            "j", "其他关键字", "逻辑运算符", "m", "注释", "o", "p", "q", "数字常量", "String常量", "类型", "未分类", "可视性", "while", "x", "y", "return"};

    public Token() {
    }

    /**
     * 构造器
     *
     * @param number  行号
     * @param content 单词内容
     * @param type    单词种类
     */
    public Token(int number, String content, char type) {
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

    public char getType() {
        return type;
    }

    public void setType(char type) {
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
                ", type=" + typeDescription[type - 'a'] +
                '}';
    }
}

public class Tokens {
    private final LinkedList<Token> tokens;   //token信息集合

    //关键字常量
    private static final String[] otherKeywords = {"new", "try", "catch", "throws", "import", "package", "this"};
    //完整"private","protected","public","abstract","class","extends","final","implements",
    // "interface","native","new","static","strictfp","synchronized","transient","volatile",
    // "break","continue","return","do","while","if","else","for","instanceof","switch",
    // "case","default","try","catch","throw","throws","import","package","boolean","byte",
    // "char","double","float","int","long","short","super","this","void","goto","const"

    private static final String[] type = {"void", "boolean", "char", "int", "double", "float", "String"};//t
    private static final String[] visibility = {"private", "public", "protected"};//v
    private static final String[] arithmetic = {"*", "/", "="};//a
    private static final String[] logistic = {"!", ">", "<"};//l
    //class->c
    //while->w

    //界符常量
    private static final String[] boundaries = {"{", "}", "(", ")", "[", "]", ",", ";"};

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
            Token tmpToken = new Token(pl.getLineNumber(), pl.getLine(), 'u');
            if (pl.getLine().contains("//")) {
                String line = pl.getLine();
                tmpToken.setContent(line.substring(0, line.indexOf("//")));
                tokens.add(tmpToken);
                Token addToken = new Token(pl.getLineNumber(), line.substring(line.indexOf("//") + 2), 'n');
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
                    frontToken = new Token(token.getNumber(), front, 'u');
                    backToken = new Token(token.getNumber(), back, 's');
                    hasQuotation = true;
                } else {//'yy"nn'
                    frontToken = new Token(token.getNumber(), "\"" + front + "\"", 's');
                    backToken = new Token(token.getNumber(), back, 'u');
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
            if (token.getType() == 'u') {//unclassified
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
                Pattern p = Pattern.compile("\\t");
                Matcher m = p.matcher(line);
                line = m.replaceAll("");
                String[] words = line.split("\\s+");
                tokens.remove(token);
                for (String word : words) {
                    tokens.add(i++, new Token(token.getNumber(), word, 'u'));
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
            if (token.getType() != 'u') continue;
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
                            tokens.add(index++, new Token(token.getNumber(), tmp, 'b'));
                        } else {
                            tokens.add(index++, new Token(token.getNumber(), tmp, 'u'));
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
            if (token.getContent().equals("class"))
                token.setType('c');
            else if (token.getContent().equals("for"))
                token.setType('f');
            else if (token.getContent().equals("while"))
                token.setType('w');
            else if (token.getContent().equals("return"))
                token.setType('z');
            else if (token.getContent().equals("if"))
                token.setType('p');
            else if (token.getContent().equals("else"))
                token.setType('q');
            else {
                for (String keyword : type) {
                    if (token.getContent().equals(keyword)) {
                        token.setType('t');
                        break;
                    }
                }
                for (String keyword : visibility) {
                    if (token.getContent().equals(keyword)) {
                        token.setType('v');
                        break;
                    }
                }
                for (String keyword : otherKeywords) {
                    if (token.getContent().equals(keyword)) {
                        token.setType('t');
                    }
                }
            }
        }
    }

    /**
     * 分离运算符（除了+/-） type=3
     * 必须在分离数字常量之前进行 否则无法分离如"3E+1 == 2+2i"
     */
    public void separateLogistic() {
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() != 'u') continue;
            for (String compare : logistic) {
                if (token.getContent().contains(compare)) {
                    String splitUnit = compare;
                    String[] split = token.getContent().split("((?<=" + splitUnit + ")|(?=" + splitUnit + "))");
                    int index = tokens.indexOf(token);
                    tokens.remove(token);
                    i--;
                    for (String tmp : split) {
                        if (tmp.equals(compare)) {
                            tokens.add(index++, new Token(token.getNumber(), tmp, 'l'));
                        } else {
                            tokens.add(index++, new Token(token.getNumber(), tmp, 'u'));
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * 分离运算符（除了+/-） type=3
     * 必须在分离数字常量之前进行 否则无法分离如"3E+1 == 2+2i"
     */
    public void separateArithmetic() {
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() != 'u') continue;
            for (String compare : arithmetic) {
                if (token.getContent().contains(compare)) {
                    String splitUnit = compare;
                    if (compare.equals("*"))
                        splitUnit = String.format("\\%s", compare);
                    String[] split = token.getContent().split("((?<=" + splitUnit + ")|(?=" + splitUnit + "))");
                    int index = tokens.indexOf(token);
                    tokens.remove(token);
                    i--;
                    for (String tmp : split) {
                        if (tmp.equals(compare)) {
                            tokens.add(index++, new Token(token.getNumber(), tmp, 'a'));
                        } else {
                            tokens.add(index++, new Token(token.getNumber(), tmp, 'u'));
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
            if (token.getType() == 'u') {
                if (isNumber(token.getContent())) token.setType('r');
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
            if (token.getType() != 'u') continue;
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
                            tokens.add(index++, new Token(token.getNumber(), tmp, 'a'));
                        } else {
                            tokens.add(index++, new Token(token.getNumber(), tmp, 'u'));
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
            if (token.getType() != 'u') continue;
            if (token.getContent().length() == 1) continue;
            if (token.getContent().contains(".")) {
                String[] split = token.getContent().split("((?<=\\.)|(?=\\.))");
                int index = tokens.indexOf(token);
                tokens.remove(token);
                i--;
                for (String tmp : split) {
                    if (tmp.equals(".")) {
                        tokens.add(index++, new Token(token.getNumber(), tmp, 'b'));
                    } else {
                        tokens.add(index++, new Token(token.getNumber(), tmp, 'u'));
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
     * @param lexerDFA 文法
     */
    public void separateIdentifier(LexerDFA lexerDFA) {
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() != 'u') continue;
            if (lexerDFA.check(token.getContent())) token.setType('i');
            else token.setType('e');
            tokens.set(i, token);
        }
    }

    /**
     * 按分类展示
     *
     * @return String 便于Frame输出
     */
    public String showByClass() {
        String res="";
        res+="*************CLASS**************\n";
        res+="---------keywords----------";
        for (Token token : tokens) {
            switch (token.getType()) {
                case 'c':
                case 'f':
                case 'k':
                case 'p':
                case 'q':
                case 't':
                case 'v':
                case 'w':
                    res+=(token.toString()+"\n");
                    break;
                default:
            }
        }
        System.out.println("---------boundaries----------");
        for (Token token : tokens) {
            if (token.getType() == 'b') res+=(token.toString()+"\n");
        }
        System.out.println("---------operators----------");
        for (Token token : tokens) {
            if (token.getType() == 'a' || token.getType() == 'l')
                res+=(token.toString()+"\n");
        }
        System.out.println("---------numbers----------");
        for (Token token : tokens) {
            if (token.getType() == 'r') res+=(token.toString()+"\n");
        }
        System.out.println("---------identifiers----------");
        for (Token token : tokens) {
            if (token.getType() == 'i') res+=(token.toString()+"\n");
        }
        System.out.println("---------\"****\"----------");
        for (Token token : tokens) {
            if (token.getType() == 's') res+=(token.toString()+"\n");
        }
        System.out.println("---------//----------");
        for (Token token : tokens) {
            if (token.getType() == 'n') res+=(token.toString()+"\n");
        }
        System.out.println("---------wrong----------");
        for (Token token : tokens) {
            if (token.getType() == 'e') res+=(token.toString()+"\n");
        }
        System.out.println("---------unclassified----------");
        for (Token token : tokens) {
            if (token.getType() == 'u') res+=(token.toString()+"\n");
        }
        res+="\n";
        return res;
    }

    /**
     * 按输入序列展示
     *
     * @return String 便于Frame输出
     */
    public String showBySequence() {
        String res="";
        //res+="*************SEQUENCE**************\n";
        for (Token token : tokens) {
            res+=(token.toString()+"\n");
        }
        res+="\n";
        return res;
    }

}
