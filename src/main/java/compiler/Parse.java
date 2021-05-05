package compiler;

import entity.*;
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
    public ParseResult run(Text grammar, Tokens code) throws IOException, InputException {
        ParseResult res;
        ParseDFA parseDFA = new ParseDFA();
        parseDFA.createGraph(grammar);
        ParseGoto parseGoto = new ParseGoto();
        parseGoto.init(grammar);
        parseGoto.createGoto(parseDFA);
        GOTO gotoTable=parseGoto.getGotoTable();
        res= parseGoto.check(code);
        res.setGotoTable(gotoTable);
        //res.show();
        return res;
    }

    public GOTO createGoto(Text grammar) throws InputException {
        GOTO gotoTable;
        ParseDFA parseDFA=new ParseDFA();
        parseDFA.createGraph(grammar);
        ParseGoto parseGoto=new ParseGoto();
        parseGoto.init(grammar);
        parseGoto.createGoto(parseDFA);
        gotoTable=parseGoto.getGotoTable();
        return gotoTable;
    }
}
