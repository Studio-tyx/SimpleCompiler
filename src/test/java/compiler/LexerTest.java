package compiler;

import entity.Text;
import exception.TYXException;
import org.junit.jupiter.api.Test;

import java.io.IOException;


class LexerTest {
    /**
     * 代码与文法综合测试
     *
     * @throws IOException 文件读入异常
     */
    @Test
    void run() throws IOException {
        Text code = new Text(), grammar = new Text();
        Lexer lexer = new Lexer();
        code.init("code1.txt");
        grammar.init("lexerGrammar1.txt");
        try {
            lexer.run(code, grammar);
        } catch (TYXException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}