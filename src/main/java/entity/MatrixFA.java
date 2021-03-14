package entity;

import java.util.*;

/**
 * @author TYX
 * @name MatrixFA
 * @description
 * @time
 **/
public class MatrixFA {

    private int[][] matrix;
    private int statusNumber = 0;
    private List<Character> terminalLink;
    private Set<Integer> terminals;

    //增加一条从this到next的边
    public void addEdge(Integer thisVertex, Integer nextVertex, Integer weight) {
        matrix[thisVertex][weight] = nextVertex;
        statusNumber = thisVertex;
    }

    public void init(GraphFA graphFA) {
        //set&int的对应 T0{0}->status0
        List<Set<Character>> statusMap = new ArrayList<Set<Character>>();
        terminals = new HashSet<Integer>();

        terminalLink = new ArrayList<Character>();
        for (Object ch : graphFA.getTerminals()) {
            terminalLink.add((Character) ch);
        }

        matrix = new int[101][terminalLink.size()];

        //初态T0={nfa首元素}
        Set<Character> start = new HashSet();
        start.add(graphFA.getVertices().get(0).name);
        start = graphFA.closure(start);
        statusMap.add(start);
        for (Character ch : start) {
            if (ch == graphFA.getFinalStatus()) terminals.add(statusMap.indexOf(start));
        }

        //DFA总状态数
        int maxStatus = 0;

        for (int i = 0; i <= maxStatus; i++) {
            Iterator<Character> it = graphFA.getTerminals().iterator();
            while (it.hasNext()) {

                Character character = it.next();
                //找closure
                Set<Character> newSet = graphFA.findNext(statusMap.get(i), character);
                if (newSet.isEmpty()) {
                    addEdge(i, -1, terminalLink.indexOf(character));
                    continue;
                }

                //遍历已有的status集
                boolean existStatus = false;
                for (int status = 0; status < statusMap.size(); status++) {
                    //已经有了
                    if (statusMap.get(status).equals(newSet)) {
                        addEdge(i, status, terminalLink.indexOf(character));
                        existStatus = true;
                    }
                }
                if (!existStatus) {
                    statusMap.add(newSet);
                    addEdge(i, statusMap.indexOf(newSet), terminalLink.indexOf(character));
                    for (Character DFAElement : newSet) {
                        if (DFAElement == graphFA.getFinalStatus()) {
                            terminals.add(statusMap.indexOf(newSet));
                            break;
                        }
                    }
                    maxStatus++;
                }

            }
        }
        statusNumber = maxStatus;
        showDFA();
    }

    public boolean check(String word) {
        int status = 0;
        for (int i = 0; i < word.length(); i++) {
            Character character = word.charAt(i);
            if (!terminalLink.contains(character)) return false;
            status = matrix[status][terminalLink.indexOf(character)];
            if (status == -1) return false;
        }
        for (Integer t : terminals) {
            if (status == t) return true;
        }
        return false;
    }

    public <T> void show(Set<T> set) {
        System.out.println("----set----------");
        for (T t : set) {
            System.out.print(t + ",");
        }
        System.out.println("\n---------------");
    }

    public void showDFA() {
        System.out.println("---------DFA---------");
        for (int i = 0; i <= statusNumber; i++) {
            for (int j = 0; j < terminalLink.size(); j++) {
                System.out.println(i + "->" + matrix[i][j] + ":" + terminalLink.get(j));
            }
        }
        System.out.println("---------------------");
    }

}
