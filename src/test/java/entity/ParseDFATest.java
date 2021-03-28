package entity;

import exception.InputException;
import org.junit.jupiter.api.Test;
import tool.ShowTools;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

class ParseDFATest {
    //@Test
    void testGotoInit() throws IOException {
        ParseDFA parseDFA = new ParseDFA();
        Text text = new Text();
        text.init("D:\\Languages\\Java\\Java Code\\SimpleCompiler\\parseGrammar1.txt");
        parseDFA.createGraph(text);
        ParseGoto parseGoto = new ParseGoto();
        parseGoto.init(text);
        try {
            parseGoto.createGoto(parseDFA);
        } catch (InputException e) {
            e.printStackTrace();
        }
    }

    //@Test
    void testStack() {
        Stack<Character> stack = new Stack<Character>();
        stack.push('a');
        stack.push('b');
        stack.push('c');
        for (Character ch : stack) {
            System.out.println(ch);
        }
    }

    //@Test
    void testForwardSearch() throws IOException {
        Text text = new Text();
        text.init("D:\\Languages\\Java\\Java Code\\SimpleCompiler\\parseGrammar2.txt");
        ParseDFA parseDFA = new ParseDFA();
        parseDFA.setText(text);
        Set<Character> set = new HashSet<Character>();
        set.add('#');
        LRLine lrLine1 = new LRLine('M', "ti(·P){AZ}M", set, 2);
        ShowTools.show(parseDFA.findForwardSearch(lrLine1));
    }

    //@Test
    void testNext() throws IOException {
        Text text = new Text();
        text.init("D:\\Languages\\Java\\Java Code\\SimpleCompiler\\parseGrammar2.txt");
        ParseDFA parseDFA = new ParseDFA();
        String testString = "a·Bb";
        LRLine lrLine1 = new LRLine('S', "·aAb", new HashSet<Character>('#'), 0);
        LRLine lrLine2 = new LRLine('A', "b·ab", new HashSet<Character>('#'), 1);
        Set<LRLine> set = new HashSet<LRLine>();
        set.add(lrLine1);
        set.add(lrLine2);
        set = parseDFA.findNextStatus(set, 'a');
        ShowTools.show(set);
    }

    //@Test
    void testSet() {
        Set<LRLine> a = new HashSet<LRLine>();
        Set<LRLine> b = new HashSet<LRLine>();
        LRLine lrLine1 = new LRLine('S', "·aAb", new HashSet<Character>('#'), 0);
        LRLine lrLine2 = new LRLine('S', "·aAb", new HashSet<Character>('#'), 0);
        a.add(lrLine1);
        b.add(lrLine2);
        ShowTools.show(a);
        ParseDFA parseDFA = new ParseDFA();
        System.out.println(parseDFA.setEquals(a, b));
        System.out.println(a.equals(b));
    }

    @Test
    void testGetFirstSearch() throws IOException {
        ParseDFA parseDFA = new ParseDFA();
        Text text = new Text();
        text.init("D:\\Languages\\Java\\Java Code\\SimpleCompiler\\parseGrammar1.txt");
        parseDFA.setText(text);
        ShowTools.show(parseDFA.findFirst("A"));
        //Set<Character> set=new ParseDFA().findFirstSet("Bp");
        //ShowTools.show(set);
    }

    //@Test
    void testSubstring() {
        String string = "abc";
        System.out.println(string.charAt(0));
        System.out.println();
        System.out.println(string.indexOf("c"));
        System.out.println(string.length());
    }
}