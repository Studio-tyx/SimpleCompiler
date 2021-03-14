package compiler;

import entity.Text;
import exception.InputException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class LexerTest {

    @Test
    void getPart() throws IOException {
        Text code = new Text(), grammar = new Text();
        Lexer lexer = new Lexer();
        code.init("D:\\Languages\\Java\\Java Code\\SimpleCompiler\\code.txt");
        grammar.init("D:\\Languages\\Java\\Java Code\\SimpleCompiler\\grammar3.txt");
        try {
            lexer.run(code, grammar);
        } catch (InputException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}