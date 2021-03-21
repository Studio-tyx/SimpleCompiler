package entity;

import exception.InputException;
import org.junit.jupiter.api.Test;
import tool.ShowTools;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

class ParseDFATest {
    @Test
    void testGotoInit() throws IOException {
        ParseDFA parseDFA=new ParseDFA();
        Text text=new Text();
        text.init("D:\\Languages\\Java\\Java Code\\SimpleCompiler\\parseGrammar1.txt");
        parseDFA.createGraph(text);
        ParseGoto parseGoto=new ParseGoto();
        parseGoto.init(text);
        try {
            parseGoto.createGoto(parseDFA);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    //@Test
    void testNext() throws IOException {
        Text text=new Text();
        text.init("D:\\Languages\\Java\\Java Code\\SimpleCompiler\\parseGrammar2.txt");
        ParseDFA parseDFA=new ParseDFA();
        String testString=new String("a·Bb");
        LRLine lrLine1=new LRLine('S',"·aAb",new HashSet<Character>('#'),0);
        LRLine lrLine2=new LRLine('A',"b·ab",new HashSet<Character>('#'),1);
        Set<LRLine> set=new HashSet<LRLine>();
        set.add(lrLine1);set.add(lrLine2);
        set=parseDFA.findNextStatus(set,'a');
        ShowTools.show(set);
    }

    //@Test
    void testSet() {
        Set<LRLine> a=new HashSet<LRLine>();
        Set<LRLine> b=new HashSet<LRLine>();
        LRLine lrLine1=new LRLine('S',"·aAb",new HashSet<Character>('#'),0);
        LRLine lrLine2=new LRLine('S',"·aAb",new HashSet<Character>('#'),0);
        a.add(lrLine1);
        b.add(lrLine2);
        ShowTools.show(a);
        ParseDFA parseDFA=new ParseDFA();
        System.out.println(parseDFA.setEquals(a,b));
        System.out.println(a.equals(b));
    }

    //@Test
    void testGetFirstSearch() throws IOException {
        ParseDFA parseDFA=new ParseDFA();
        Text text=new Text();
        text.init("D:\\Languages\\Java\\Java Code\\SimpleCompiler\\parseGrammar2.txt");
        Set<Character> set=new ParseDFA().findFirstSet("Bp");
        ShowTools.show(set);
    }

    //@Test
    void testSubstring() {
        String string="abc";
        System.out.println(string.substring(0,1));
        System.out.println();
        System.out.println(string.indexOf("c"));
        System.out.println(string.length());
    }
}