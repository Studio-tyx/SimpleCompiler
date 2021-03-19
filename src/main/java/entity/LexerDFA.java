package entity;

import java.util.*;

/**
 * @author TYX
 * @name LexerDFA
 * @description DFA（状态用表格展示）
 * @time 2021/3/14 14:51
 **/
public class LexerDFA {
    private int[][] matrix; //状态转移矩阵（不是邻接矩阵）
    private int statusNumber = 0;   //状态数（利于状态转移矩阵确定边界）
    private List<Character> terminalLink;   //NFA终结符与DFA终结符的对应关系（NFA终结符为character DFA为便于数组寻数 类型为int）
    private Set<Integer> terminals; //DFA的终态

    /**
     * 状态转移矩阵增加边
     *
     * @param thisVertex 当前结点
     * @param nextVertex 下一结点
     * @param weight 边的权重
     */
    public void addEdge(Integer thisVertex, Integer nextVertex, Integer weight) {
        matrix[thisVertex][weight] = nextVertex;
        statusNumber = thisVertex;
    }

    /**
     * NFA->DFA
     * @param lexerNFA NFA
     */
    public void init(LexerNFA lexerNFA) {
        List<Set<Character>> statusMap = new ArrayList<Set<Character>>();   //DFA状态与NFA子集的对应关系 int<->set<Character>
        terminals = new HashSet<Integer>();
        terminalLink = new ArrayList<Character>();

        //NFA终结符与DFA状态转移矩阵表头的对应关系初始化
        for (Object ch : lexerNFA.getTerminals()) {
            terminalLink.add((Character) ch);
        }

        //初态T0
        matrix = new int[101][terminalLink.size()];
        Set<Character> start = new HashSet();
        start.add(lexerNFA.getFirstElement());
        start = lexerNFA.getGraph().closure(start);
        statusMap.add(start);
        for (Character ch : start) {
            if (ch == lexerNFA.getFinalStatus()) terminals.add(statusMap.indexOf(start));
        }

        int maxStatus = 0;  //DFA总状态数
        for (int i = 0; i <= maxStatus; i++) {
            Iterator<Character> it = lexerNFA.getTerminals().iterator();
            while (it.hasNext()) {
                Character character = it.next();
                Set<Character> newSet = lexerNFA.getGraph().findNext(statusMap.get(i), character);  //找closure
                if (newSet.isEmpty()) {
                    addEdge(i, -1, terminalLink.indexOf(character));
                    continue;
                }

                boolean existStatus = false;
                for (int status = 0; status < statusMap.size(); status++) { //遍历已有的状态
                    if (statusMap.get(status).equals(newSet)) { //已经存在状态
                        addEdge(i, status, terminalLink.indexOf(character));    //状态转移矩阵加边
                        existStatus = true;
                    }
                }
                if (!existStatus) { //状态不存在则新增状态
                    statusMap.add(newSet);  //新增状态
                    addEdge(i, statusMap.indexOf(newSet), terminalLink.indexOf(character)); //状态转移矩阵加边
                    for (Character DFAElement : newSet) {
                        if (DFAElement == lexerNFA.getFinalStatus()) {   //DFA终态新增
                            terminals.add(statusMap.indexOf(newSet));
                            break;
                        }
                    }
                    maxStatus++;
                }
            }
        }
        statusNumber = maxStatus;   //总状态数
        showDFA();
    }

    /**
     * 判断标识符是否符合文法
     *
     * @param word 单词
     * @return 是否符合文法
     */
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

    /**
     * DFA展示 便于输出
     */
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
