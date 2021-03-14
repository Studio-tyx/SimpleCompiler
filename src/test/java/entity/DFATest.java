package entity;

import exception.InputException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DFATest {

    @Test
    void testChange() throws IOException, InputException {
        Text text=new Text();
        text.init("D:\\Languages\\Java\\Java Code\\SimpleCompiler\\grammar.txt");
        NFA nfa=new NFA();
        nfa.init(text);
        nfa.show();
        DFA dfa=new DFA();
        dfa.changeNFA(nfa);
        dfa.show();
        System.out.println("aa:"+dfa.check("aa"));
        System.out.println("b:"+dfa.check("b"));
        System.out.println("aaa:"+dfa.check("aaa"));
        System.out.println("ababa:"+dfa.check("ababa"));
    }

    //@Test
    void testHash() {
        Character ch='a';
        System.out.println((int)ch);
        int b=97;
        System.out.println((char)b);
    }

//    @Test
//    void testExtend() {
//        NFA nfa=new NFA();
//        nfa.getTerminal().add('a');
//        DFA dfa=new DFA();
//        dfa.getTerminal().add(1000);
//        System.out.println(nfa.terminal.equals(nfa.getTerminal()));
//        System.out.println(dfa.terminal.equals(dfa.getTerminal()));
//    }
    //基本可以认为nfa.terminal==nfa.getTerminal()
}