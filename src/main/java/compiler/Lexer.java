package compiler;

import entity.ListGraph;
import entity.ProcessLine;
import entity.Text;
import entity.Tokens;
import exception.InputException;

import java.util.List;

/**
 * @author TYX
 * @name Lexer
 * @description
 * @time
 **/
public class Lexer {
    public Lexer() {
    }

    public void run(Text text) {
        Tokens tokens = new Tokens();
        tokens.separate(text);
    }

    public boolean isUpper(char ch) {
        return (ch >= 'A' && ch <= 'Z');
    }

    public boolean isLower(char ch){
        return ((ch>='a'&&ch<='z')||ch=='@');
    }

    /*
    @算小写的！！！！！
    $算Z
     */

    public ListGraph createNFA(Text text) throws InputException {
        List<ProcessLine> content = text.getContent();
        ListGraph listGraph=new ListGraph();
        for (ProcessLine processLine : content) {
            String line=processLine.getLine();
            if(line.equals("")){
                throw new InputException("Input grammar error at line "+processLine.getLineNumber()+": Grammar has a blank line!");
                //break;
            }
            boolean isLegal=false;
            if(line.contains("->")){
                String[] afterSplit=line.split("->");
                String former=afterSplit[0],latter=afterSplit[1];
                if(former.length()==1){
                    if(isUpper(former.charAt(0))){
                        if(latter.length()==1){
                            if(isLower(latter.charAt(0))){
                                isLegal=true;
                                listGraph.addEdge(former.charAt(0),'$',latter.charAt(0));
                            }
                        }
                        else if(latter.length()==2){
                            if(isUpper(latter.charAt(0))&&isLower(latter.charAt(1))){
                                isLegal=true;
                                listGraph.addEdge(former.charAt(0),latter.charAt(0),latter.charAt(1));
                            }
                            else if(isLower(latter.charAt(0))&&isUpper(latter.charAt(1))){
                                isLegal=true;
                                listGraph.addEdge(former.charAt(0),latter.charAt(1),latter.charAt(0));
                            }
                        }
                    }
                }
            }
            if(!isLegal)throw new InputException("Input grammar error at line "+processLine.getLineNumber()+": Not a format of normal grammar!");
        }
        return listGraph;
    }


    /*
    接下来要有
    ->NFA->DFA 对于String的处理
    我觉得可以放在tokens

    还有文法读入
     */


}
