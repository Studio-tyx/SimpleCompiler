package compiler;

import entity.GraphFA;
import entity.MatrixFA;
import entity.Text;
import entity.Tokens;
import exception.InputException;

/**
 * @author TYX
 * @name Lexer
 * @description 词法分析器主体
 * @time 2021/3/8 13:49
 **/
public class Lexer {
    public Lexer() {
    }

    /**
     * 词法分析器主体函数
     *
     * @param code 代码文件
     * @param grammar 文法文件
     * @throws InputException 输入异常（文法格式异常等）
     */
    public void run(Text code, Text grammar) throws InputException {
        Tokens tokens = new Tokens();
        GraphFA graphFA = new GraphFA();
        MatrixFA matrixFA = new MatrixFA();

        graphFA.init(grammar);
        matrixFA.init(graphFA);

        tokens.separateNote(code.getContent());
        tokens.separateString();
        tokens.separateSpace();
        tokens.markKeyword();
        tokens.separateBoundary();
        tokens.markKeyword();
        tokens.separateLogistic();
        tokens.separateArithmetic();
        tokens.separateNumber();
        tokens.separatePlusOrMinus();
        tokens.separateNumber();
        tokens.separatePoint();
        tokens.separateIdentifier(matrixFA);
        tokens.showBySequence();
        tokens.showByClass();

    }
}
