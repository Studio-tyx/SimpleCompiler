package compiler;

import entity.NFA;
import entity.Text;
import entity.Tokens;
import exception.InputException;

/**
 * @author TYX
 * @name Lexer
 * @description
 * @time
 **/
public class Lexer {
    public Lexer() {
    }

    public void run(Text text) throws InputException {
        Tokens tokens = new Tokens();
        tokens.separate(text);
        NFA nfa = new NFA();
        nfa.init(text);
    }




    /*
    接下来要有
    ->NFA->DFA 对于String的处理
    我觉得可以放在tokens

    还有文法读入
     */


}
