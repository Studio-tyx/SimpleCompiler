package entity;

import exception.InputException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class FATest {
    /**
     * 测试NFA与DFA是否能识别单词
     *
     * @throws IOException 读写异常
     * @throws InputException 输入格式异常
     */
    @Test
    void testDFA() throws IOException, InputException {
        Text text = new Text();
        text.init("D:\\Languages\\Java\\Java Code\\SimpleCompiler\\grammar3.txt");
        GraphFA graphFA = new GraphFA();
        graphFA.init(text);
        MatrixFA matrixFA = new MatrixFA();
        matrixFA.init(graphFA);
        System.out.println("aa:" + matrixFA.check("aa"));
        System.out.println("b:" + matrixFA.check("b"));
        System.out.println("aaa:" + matrixFA.check("aaa"));
        System.out.println("ababa:" + matrixFA.check("ababa"));
    }
}