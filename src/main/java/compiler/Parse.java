package compiler;

/**
 * @author TYX
 * @name Parse
 * @description
 * @time 2021/3/15 17:04
 **/
public class Parse {
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
}
