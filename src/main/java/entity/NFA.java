package entity;

import exception.InputException;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author TYX
 * @name NFA
 * @description
 * @time
 **/
public class NFA extends FA {


    public boolean isUpper(char ch) {
        return (ch >= 'A' && ch <= 'Z');
    }

    public boolean isLower(char ch) {
        return ((ch >= 'a' && ch <= 'z') || ch == '@');
    }

    public NFA() {
        terminal = new HashSet<Character>();
        statusGraph = new ListGraph();
    }

//    @Override
//    public Set<Character> getTerminal() {
//        return super.getTerminal();
//    }

    @Override
    public ListGraph getStatusGraph() {
        return (ListGraph) statusGraph;
    }

    public void init(Text text) throws InputException {
        for (ProcessLine processLine : text.getContent()) {
            String line = processLine.getLine();
            if (line.equals("")) {
                throw new InputException("Input grammar error at line " + processLine.getLineNumber() + ": Grammar has a blank line!");
                //break;
            }
            boolean isLegal = false;
            if (line.contains("->")) {
                String[] afterSplit = line.split("->");
                String former = afterSplit[0], latter = afterSplit[1];
                if (former.length() == 1) {
                    if (isUpper(former.charAt(0))) {
                        if (latter.length() == 1) {
                            if (isLower(latter.charAt(0))) {
                                isLegal = true;
                                statusGraph.addEdge(former.charAt(0), '$', latter.charAt(0));
                                if(latter.charAt(0)!='@')terminal.add(latter.charAt(0));
                            }
                        } else if (latter.length() == 2) {
                            if (isUpper(latter.charAt(0)) && isLower(latter.charAt(1))) {
                                isLegal = true;
                                statusGraph.addEdge(former.charAt(0), latter.charAt(0), latter.charAt(1));
                                if(latter.charAt(0)!='@')terminal.add(latter.charAt(1));
                            } else if (isLower(latter.charAt(0)) && isUpper(latter.charAt(1))) {
                                isLegal = true;
                                statusGraph.addEdge(former.charAt(0), latter.charAt(1), latter.charAt(0));
                                if(latter.charAt(0)!='@')terminal.add(latter.charAt(0));
                            }
                        }
                    }
                }
            }
            if (!isLegal)
                throw new InputException("Input grammar error at line " + processLine.getLineNumber() + ": Not a format of normal grammar!");
        }
    }

    @Override
    public void show() {
        statusGraph.show();
    }
}
