package entity;

import exception.InputException;
import tool.CharacterTools;
import tool.ShowTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author TYX
 * @name ParseGoto
 * @description
 * @time 2021/3/21 14:20
 **/
public class ParseGoto {
    private String[][] Goto;
    private List<Character> terminals;
    private List<Character> nonTerminals;
    private int statusNumber;

    public void init(Text text){
        terminals=new ArrayList<Character>();
        nonTerminals=new ArrayList<Character>();
        for(ProcessLine processLine:text.getContent()){
            String line=processLine.getLine();
            for(int i=0;i<line.length();i++){
                if(i==1||i==2) continue;
                Character character=line.charAt(i);
                if(CharacterTools.isUpper(character)){
                    if(!nonTerminals.contains(character))nonTerminals.add(character);
                }else{
                    if(!terminals.contains(character)) terminals.add(character);
                }
            }
        }
        terminals.add('#');
    }

    public void createGoto(ParseDFA parseDFA) throws InputException {
        statusNumber=parseDFA.getStatus().size();
        Goto=new String[statusNumber][terminals.size()+nonTerminals.size()];
        for(int i=0;i<statusNumber;i++){
            for(int j=0;j<terminals.size()+nonTerminals.size();j++){
                Goto[i][j]="-1";
            }
            Set<LRLine> thisSet=parseDFA.getStatus().get(i);
            for(LRLine lrLine:thisSet){
                if(endProduction(lrLine.getContent())){
                    for(Character forwardSearch:lrLine.getForwardSearch()){
                        if(lrLine.getProductionNumber()!=0) {
                            if(!Goto[i][terminals.indexOf(forwardSearch)].equals("-1"))
                                throw new InputException("Input grammar error:Not a type 2 grammar!");
                            else Goto[i][terminals.indexOf(forwardSearch)] = "r" + lrLine.getProductionNumber();
                        }
                    }
                }
            }
            for(int j=0;j<terminals.size();j++){
                LinkGraph<Set<LRLine>, Character> graph=parseDFA.getGraph();
                Set<LRLine> nextSet=graph.findNextVertex(thisSet,terminals.get(j));
                if(nextSet!=null) {
                    if(!Goto[i][j].equals("-1"))
                        throw new InputException("Input grammar error:Not a type 2 grammar!");
                    else Goto[i][j] = "S" + parseDFA.getStatus().indexOf(nextSet);
                }
            }
        }
        for(int i=0;i<parseDFA.getStatus().size();i++){
            Set<LRLine> status=parseDFA.getStatus().get(i);
            for(LRLine lrLine:status){
                if(lrLine.getStart()=='%'&&lrLine.getContent().equals("S·")){
                    if(!Goto[i][terminals.size()-1].equals("-1"))
                        throw new InputException("Input grammar error:Not a type 2 grammar!");
                    else Goto[i][terminals.size()-1]="acc";
                }
            }
        }
        for(int i=0;i<statusNumber;i++){
            Set<LRLine> thisSet=parseDFA.getStatus().get(i);
            LinkGraph<Set<LRLine>, Character> graph=parseDFA.getGraph();
            for(int j=0;j<nonTerminals.size();j++){
                Set<LRLine> nextSet=graph.findNextVertex(thisSet,nonTerminals.get(j));
                if(nextSet!=null) {
                    if(!Goto[i][j + terminals.size()].equals("-1"))
                        throw new InputException("Input grammar error:Not a type 2 grammar!");
                    else Goto[i][j + terminals.size()] = String.valueOf(parseDFA.getStatus().indexOf(nextSet));
                }
            }
        }
        for(Character ch:terminals) System.out.print(ch+"\t");
        for(Character ch:nonTerminals) System.out.print(ch+"\t");
        System.out.println();
        ShowTools.show(Goto);
    }

    public boolean endProduction(String line){
        int index=line.indexOf("·");
        if(line.length()==index+1)  return true;
        else return false;
    }
}
