package entity;

import javax.sound.midi.Soundbank;
import java.lang.annotation.ElementType;
import java.rmi.StubNotFoundException;
import java.util.*;

/**
 * @author TYX
 * @name DFA
 * @description
 * @time
 **/
public class DFA extends FA {
    private List<Character> terminalMap;

    public DFA() {
        terminal = new HashSet<Integer>();//存的是character的ascii值
        //statusGraph = new MatrixGraph();
    }

//    @Override
//    public Set<Integer> getTerminal() {
//        return super.terminal();
//    }

    /*
    map<integer,Set<Character>>
    标记初态 int=0 closure（0）=findNext(listGraph,{0},@);
    for(i<maxStatus) maxStatus++
    for(t:terminal) newSet=findNext(list,map[i],t);比较newSet与map中的set是否重复
    如果重复则把序号加边 不重复则map+1 新序号加边 maxStatus++
     */

    public void changeNFA(NFA nfa) {

        //一定要想清楚什么是什么
        //nfa的终结符是character dfa的终结符是int
        //terminal和graph最好用get 要不然是继承的 不是

        /*
        有个问题 一定要把数组表头和character对应起来
         */

        //set&int的对应 T0{0}->status0
        List<Set<Character>> statusMap=new ArrayList<Set<Character>>();

        terminalMap=new ArrayList<Character>();
        for(Object ch:nfa.terminal){
            terminalMap.add((Character) ch);
        }

        statusGraph=new MatrixGraph(nfa.terminal.size());//建图（根据大小建矩阵）


        //初态T0={nfa首元素}
        Set start=new HashSet();
        start.add(nfa.statusGraph.getFirst());
        statusMap.add(start);


        //DFA总状态数
        int maxStatus=0;

        for(int i=0;i<=maxStatus;i++){
            Iterator<Character> it=nfa.terminal.iterator();
            while(it.hasNext()){

                Character character=it.next();
                //找closure
                Set<Character> newSet=findNext(nfa.getStatusGraph(),statusMap.get(i),character);

                //遍历已有的status集
                //不连通怎么办
                boolean existStatus=false;
                for(int status=0;status<statusMap.size();status++){
                    //已经有了
                    if(statusMap.get(status).equals(newSet)){
                        statusGraph.addEdge(i,status,terminalMap.indexOf(character));
                        existStatus=true;
                    }
                }
                if(!existStatus){
                    statusMap.add(newSet);
                    statusGraph.addEdge(i,statusMap.indexOf(newSet),terminalMap.indexOf(character));
                    for(Character DFAElement:newSet){
                        if(DFAElement=='$'){
                            terminal.add(statusMap.indexOf(newSet));
                            break;
                        }
                    }
                    maxStatus++;
                }

            }
        }
    }

    public boolean check(String word){

        Object status=0;
        for(int i=0;i<word.length();i++){
            Character character=word.charAt(i);
            status= statusGraph.nextStatus(status,terminalMap.indexOf(character));
            //System.out.println("status:"+status+",character:"+character+",characterMap:"+terminalMap.indexOf(character));
        }
        for(Object t:terminal){
            if(status==t)return true;
        }
        return false;
    }


    public void show(Set<Character> set){
        for(Character ch:set){
            System.out.print(ch+",");
        }
        System.out.println();
    }

    @Override
    public void show() {
        statusGraph.show();
    }

    //分两遍 第一遍找有的 第二遍找closure
    public Set<Character> findNext(ListGraph listGraph, Set<Character> I, char weight) {
        return closure(listGraph, move(listGraph, I, weight));
    }

    //如果ch==顶点 把所有权重与weight相等的点加入res
    public Set<Character> move(ListGraph listGraph, Set<Character> I, char weight) {
        Set<Character> res = new HashSet<Character>();
        for (char ch : I) {
            for (Vertex vertex : listGraph.getVertices()) {
                if (ch == vertex.name) {
                    for (Edge edge : vertex.edges) {
                        if (edge.weight == weight) {
                            res.add(edge.nextVertex);
                        }
                    }
                }
            }
        }
        return res;
    }

    public Set<Character> closure(ListGraph listGraph, Set<Character> I) {
        for (char ch : I) {
            for (Vertex vertex : listGraph.getVertices()) {
                if (ch == vertex.name) {
                    for (Edge edge : vertex.edges) {
                        if (edge.weight == '@') {
                            I.add(edge.nextVertex);
                        }
                    }
                }
            }
        }
        return I;
    }



}
