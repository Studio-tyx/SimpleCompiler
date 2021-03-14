package compiler;

import entity.ListGraph;
import entity.Text;
import exception.InputException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class LexerTest {

    //@Test
    void getPart() throws IOException {
        Text text=new Text();
        Lexer lexer=new Lexer();
//        text.init("D:\\Languages\\Java\\Java Code\\SimpleCompiler\\code.txt");
//        lexer.run(text);
        text.init("D:\\Languages\\Java\\Java Code\\SimpleCompiler\\grammar.txt");
//        try {
////            ListGraph listGraph=lexer.createNFA(text);
////            listGraph.show();
//        } catch (InputException e) {
//            //e.printStackTrace();
//            System.out.println(e.getMessage());
//        }



    }
}