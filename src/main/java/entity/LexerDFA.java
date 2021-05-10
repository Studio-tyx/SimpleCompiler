package entity;

import java.util.*;

/**
 * @author TYX
 * @name LexerDFA
 * @description DFA（状态用表格展示）
 * @createTime 2021/3/14 14:51
 **/
public class LexerDFA {
    private int[][] matrix; //状态转移矩阵（不是邻接矩阵）
    private int statusNumber = 0;   //状态数（利于状态转移矩阵确定边界）
    private List<Character> terminalLink;   //NFA终结符与DFA终结符的对应关系（NFA终结符为character DFA为便于数组寻数 类型为int）
    private Set<Integer> terminals; //DFA的终态

    /**
     * 状态转移矩阵增加边
     *
     * @param thisVertex 当前结点 Integer
     * @param nextVertex 下一结点 Integer
     * @param weight     边的权重 Integer
     */
    public void addEdge(Integer thisVertex, Integer nextVertex, Integer weight) {
        matrix[thisVertex][weight] = nextVertex;
        statusNumber = thisVertex;
    }

    /**
     * NFA=》DFA<br>
     *     将NFA转换为DFA
     *
     * @param lexerNFA NFA LexerNFA
     * @return NFA展示String（Frame展示用） List of String
     */
    public List<String> init(LexerNFA lexerNFA) {
        List<Set<Character>> statusMap = new ArrayList<Set<Character>>();   //DFA状态与NFA子集的对应关系 int<->set<Character>
        terminals = new HashSet<Integer>(); //DFA的终态
        terminalLink = new ArrayList<Character>();  //NFA终结符与DFA终结符的对应关系（NFA终结符为character DFA为便于数组寻数 类型为int）

        //NFA终结符与DFA状态转移矩阵表头的对应关系初始化
        for (Object ch : lexerNFA.getTerminals()) {
            terminalLink.add((Character) ch);
        }

        //初态T0
        matrix = new int[101][terminalLink.size()]; //最多101个状态
        Set<Character> start = new HashSet();
        start.add(lexerNFA.getFirstElement());  //初态
        start = lexerNFA.closure(start);    //初态closure
        statusMap.add(start);   //DFA+初态
        for (Character ch : start) {
            if (ch == lexerNFA.getFinalStatus()) terminals.add(statusMap.indexOf(start));   //判断是否为终态
        }

        int maxStatus = 0;  //DFA总状态数
        for (int i = 0; i <= maxStatus; i++) {  //对于当前的所有状态 有增加状态会异常 所以没有用foreach
            Iterator<Character> it = lexerNFA.getTerminals().iterator();    //对于所有终结符
            while (it.hasNext()) {
                Character character = it.next();
                Set<Character> newSet = lexerNFA.findNext(statusMap.get(i), character);  //找closure
                if (newSet.isEmpty()) { //下一状态为空
                    addEdge(i, -1, terminalLink.indexOf(character));
                    continue;
                }

                boolean existStatus = false;
                for (int status = 0; status < statusMap.size(); status++) { //遍历已有的状态
                    if (statusMap.get(status).equals(newSet)) { //已经存在状态
                        addEdge(i, status, terminalLink.indexOf(character));    //状态转移矩阵加边（覆盖原有边）
                        existStatus = true;
                    }
                }
                if (!existStatus) { //状态不存在则新增状态
                    statusMap.add(newSet);  //新增状态
                    addEdge(i, statusMap.indexOf(newSet), terminalLink.indexOf(character)); //状态转移矩阵加边
                    for (Character DFAElement : newSet) {
                        if (DFAElement == lexerNFA.getFinalStatus()) {  //是终态
                            terminals.add(statusMap.indexOf(newSet));   //DFA终态新增
                            break;  //有一个状态是终态就可以了
                        }
                    }
                    maxStatus++;
                }
            }
        }
        statusNumber = maxStatus;   //总状态数
//        showDFA();
        return getDFA();
    }

    /**
     * 判断标识符是否符合文法
     *
     * @param word 单词 String
     * @return 是否符合文法 boolean
     */
    public boolean check(String word) {
        int status = 0;
        for (int i = 0; i < word.length(); i++) {   //对于标识符的每一字符
            Character character = word.charAt(i);
            if (!terminalLink.contains(character)) return false;    //不含此终结符
            status = matrix[status][terminalLink.indexOf(character)];   //下一状态
            if (status == -1) return false; //无效状态（Matrix初始值为-1）
        }
        for (Integer t : terminals) {
            if (status == t) return true;   //状态在终态中
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

    /**
     * DFA展示 便于输出
     *
     * @return DFA展示String（Frame展示用） List of String
     */
    public List<String> getDFA() {
        List<String> res=new ArrayList<String>();
        for (int i = 0; i <= statusNumber; i++) {
            for (int j = 0; j < terminalLink.size(); j++) {
                res.add(i + "->" + matrix[i][j] + ":" + terminalLink.get(j));
            }
        }
        return res;
    }
}
