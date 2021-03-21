package entity;

import exception.InputException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class FATest {
    /**
     * 测试NFA与DFA是否能识别单词
     *
     * @throws IOException 读写异常
     * @throws InputException 输入格式异常
     */
    @Test
    void testDFA() throws IOException, InputException {
        Text text = new Text();
        text.init("D:\\Languages\\Java\\Java Code\\SimpleCompiler\\lexerGrammar3.txt");
        LexerNFA lexerNFA = new LexerNFA();
        lexerNFA.init(text);
        LexerDFA lexerDFA = new LexerDFA();
        lexerDFA.init(lexerNFA);
        System.out.println("aa:" + lexerDFA.check("aa"));
        System.out.println("b:" + lexerDFA.check("b"));
        System.out.println("aaa:" + lexerDFA.check("aaa"));
        System.out.println("ababa:" + lexerDFA.check("ababa"));
    }
}