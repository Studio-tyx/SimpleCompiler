package compiler;

import entity.Text;
import exception.InputException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class LexerTest {
    /**
     * 代码与文法综合测试
     * @throws IOException 文件读入异常
     */
    @Test
    void run() throws IOException {
        Text code = new Text(), grammar = new Text();
        Lexer lexer = new Lexer();
        code.init("D:\\Languages\\Java\\Java Code\\SimpleCompiler\\code.txt");
        grammar.init("D:\\Languages\\Java\\Java Code\\SimpleCompiler\\grammar2.txt");
        try {
            lexer.run(code, grammar);
        } catch (InputException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}