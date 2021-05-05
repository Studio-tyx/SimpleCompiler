package compiler;

import entity.LexerDFA;
import entity.LexerNFA;
import entity.Text;
import entity.Tokens;
import exception.InputException;

import java.util.List;

/**
 * @author TYX
 * @name Lexer
 * @description 词法分析器主体
 * @createTime 2021/3/8 13:49
 **/
public class Lexer {
    public Lexer() {
    }

    /**
     * 词法分析器主体函数
     *
     * @param code    代码文件
     * @param grammar 文法文件
     * @throws InputException 输入异常（文法格式异常等）
     */
    public Tokens run(Text code, Text grammar) throws InputException {
        Tokens tokens = new Tokens();
        LexerNFA lexerNFA = new LexerNFA();
        LexerDFA lexerDFA = new LexerDFA();

        lexerNFA.init(grammar);
        lexerDFA.init(lexerNFA);

        tokens.separateNote(code.getContent());
        tokens.separateString();
        tokens.separateSpace();
        tokens.markKeyword();
        tokens.separateBoundary();
        tokens.markKeyword();
        tokens.separateLogistic();
        tokens.separateArithmetic();
        tokens.separateNumber();
        tokens.separatePlusOrMinus();
        tokens.separateNumber();
        tokens.separatePoint();
        tokens.separateIdentifier(lexerDFA);
        //System.out.println(tokens.showBySequence());
        //System.out.println(tokens.showByClass());

        return tokens;
    }

    public List<String> getNFA(Text grammar) throws InputException {
        LexerNFA lexerNFA = new LexerNFA();
        return lexerNFA.init(grammar);
    }

    public List<String> getDFA(Text grammar) throws InputException {
        LexerNFA lexerNFA = new LexerNFA();
        LexerDFA lexerDFA=new LexerDFA();
        lexerNFA.init(grammar);
        return lexerDFA.init(lexerNFA);
    }
}
