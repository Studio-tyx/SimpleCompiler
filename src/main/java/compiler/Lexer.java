package compiler;

import entity.GraphFA;
import entity.MatrixFA;
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

    public void run(Text code, Text grammar) throws InputException {
        Tokens tokens = new Tokens();
        GraphFA graphFA = new GraphFA();
        MatrixFA matrixFA = new MatrixFA();

        graphFA.init(grammar);
        matrixFA.init(graphFA);
        tokens.separate(code, matrixFA);
    }




    /*
    接下来要有
    ->NFA->DFA 对于String的处理
    我觉得可以放在tokens

    还有文法读入
     */


}
