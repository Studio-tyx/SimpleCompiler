package compiler;

import entity.*;
import exception.TYXException;

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
     * @return ParseResult
     * @throws IOException    文件读写异常
     * @throws TYXException 语法格式异常
     */
    public ParseResult run(Text grammar, Tokens code) throws IOException, TYXException {
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

    /**
     * 返回Goto表（用于展示）
     *
     * @param grammar 文法文件
     * @return GOTO
     * @throws TYXException 语法格式异常
     */
    public GOTO createGoto(Text grammar) throws TYXException {
        GOTO gotoTable;
        ParseDFA parseDFA=new ParseDFA();
        parseDFA.createGraph(grammar);  //创建语法图
        ParseGoto parseGoto=new ParseGoto();
        parseGoto.init(grammar);    //语法初始化
        parseGoto.createGoto(parseDFA); //创建Goto表
        gotoTable=parseGoto.getGotoTable(); //返回
        return gotoTable;
    }
}
