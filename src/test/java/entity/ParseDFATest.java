package entity;

import org.junit.jupiter.api.Test;
import tool.ShowTools;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

class ParseDFATest {
    //@Test
    void testMove() throws IOException {
        String testString=new String("a·Bb");
        LRLine lrLine=new LRLine('S',"a·Bb",new HashSet<Character>('#'),0);
        Set<LRLine> set=new HashSet<LRLine>();
        set.add(lrLine);
        set=new SetLRLineGraph().move(set,'B');
        ShowTools.show(set);
    }

    //@Test
    void testSet() {
        Set<String> a=new HashSet<String>();
        Set<String> b=new HashSet<String>();
        a.add("a");
        b.add("a");
        System.out.println(a.equals(b));
        a.add("v");
        b.add("b");
        System.out.println(a.equals(b));
    }

    //@Test
    void testGetFirstSearch() throws IOException {
        ParseDFA parseDFA=new ParseDFA();
        Text text=new Text();
        text.init("D:\\Languages\\Java\\Java Code\\SimpleCompiler\\parseGrammar2.txt");
        Set<Character> set=new SetLRLineGraph().findFirstSet(text,"Bp");
        ShowTools.show(set);
    }

    @Test
    void testSubstring() {
        String string="abc";
        System.out.println(string.substring(0,1));
        System.out.println();
        System.out.println(string.indexOf("c"));
        System.out.println(string.length());
    }
}