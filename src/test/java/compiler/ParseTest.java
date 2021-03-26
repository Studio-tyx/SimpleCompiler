package compiler;

import entity.*;
import exception.InputException;
import org.junit.jupiter.api.Test;
import tool.ShowTools;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

class ParseTest {
    @Test
    void testParse() throws IOException {
        Text code = new Text(), lexerGrammar = new Text(), parseGrammar = new Text();
        Lexer lexer = new Lexer();
        code.init("D:\\Languages\\Java\\Java Code\\SimpleCompiler\\code2.txt");
        lexerGrammar.init("D:\\Languages\\Java\\Java Code\\SimpleCompiler\\lexerGrammar2.txt");
        Tokens tokens = null;
        try {
            tokens = lexer.run(code, lexerGrammar);
        } catch (InputException e) {
            e.printStackTrace();
        }

        Parse parse = new Parse();
        parseGrammar.init("D:\\Languages\\Java\\Java Code\\SimpleCompiler\\parseGrammar1.txt");
        try {
            parse.run(parseGrammar, tokens);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    //@Test
    void testFirst() throws IOException {
        Text parseGrammar = new Text();
        ProcessLine processLine = new ProcessLine(1, "M->ti(P){·AZ}M");
        //Character start, String content, Set<Character> forwardSearch, int productionNumber
        Set<Character> set = new HashSet<Character>();
        set.add('}');
        LRLine lrLine = new LRLine('M', "ti(P){·AZ}M", set, 1);
        ParseDFA parseDFA = new ParseDFA();
        Set<LRLine> I = new HashSet<LRLine>();
        I.add(lrLine);
        parseDFA.createGraph(parseGrammar);
        ShowTools.show(parseDFA.closure(I));
    }
}