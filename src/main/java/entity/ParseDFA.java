package entity;

import tool.CharacterTools;
import tool.ShowTools;

import java.util.*;

/**
 * @author TYX
 * @name ParseDFA
 * @description
 * @time 2021/3/19 10:22
 **/
public class ParseDFA {
    /*
    逻辑：
        读文法
            封装成LRLine：开始符+内容+尾巴
            有一些什么样的函数呢：
                //set自身求闭包->下一个是大写字符 则加入它开头的String（加入的时候注意尾巴）
                //求尾巴->给定一个LRLine和text 根据大写字符后面有没有 分类//A->a·Bb,a/e   B->·c,e  e:如果b有东西的话 就用b b没东西 就用a/e
                总共是一个Closure（Set<LRLine>）
                还有一个move 移动·
            对于每个set的时候 先插入·将其加入set 然后根据·后面的值去寻找下一个要加入set的句子
        构造DFA->goto表
        读tokens
        checkParse
     */
    /*
    2021/03/19 20:11
    我觉得SetLRLineGraph和ParseDFA的结构还不是很明确
    感觉有点混在一起了 那几个函数可以再仔细考虑一下应该放在哪里

    然后今天的话
    目前的结构是这样的 大概把图结构单独做成泛型
    这样词法分析和语法分析都可以继续用这个图结构
    然后把基于Character实现的图结构和基于Set<LRLine>实现的图结构分开
    都做成图的子类 这样这两个类的findNext方法就具有重载性质了

    我还得感谢最近学的软件设计模式emmm但是我觉得用的也不太好
    正经的话应该要把LinkedGraph做成接口 但是我感觉还是有一些共同代码 做成泛型可能更好一些

    晚上做了move和closure函数 move函数测试完了 closure还没写完
    有点乱
     */

    LinkGraph<Set<LRLine>,Character> graph;

    public void createGraph(Text text){
        graph=new LinkGraph<Set<LRLine>,Character>();
        LRLine firstLR=new LRLine('%',"·S",new HashSet<Character>('#'),0);

    }




}
