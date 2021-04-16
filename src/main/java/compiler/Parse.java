package compiler;

import entity.ParseDFA;
import entity.ParseGoto;
import entity.Text;
import entity.Tokens;
import exception.InputException;

import java.io.IOException;

/**
 * @author TYX
 * @name Parse
 * @description 语法分析主体
 * @createTime 2021/3/15 17:04
 **/
public class Parse {
    /**
     * 语法分析主体函数
     *
     * @param grammar 语法
     * @param code    代码
     * @throws IOException    文件读写异常
     * @throws InputException 语法格式异常
     */
    public void run(Text grammar, Tokens code) throws IOException, InputException {
        ParseDFA parseDFA = new ParseDFA();
        parseDFA.createGraph(grammar);
        ParseGoto parseGoto = new ParseGoto();
        parseGoto.init(grammar);
        parseGoto.createGoto(parseDFA);
        System.out.println(parseGoto.check(code));
    }
}
