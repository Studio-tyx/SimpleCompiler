package entity;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;


class ListGraphTest {
    //@Test
    void addEdge() {
        ListGraph listGraph=new ListGraph();
        listGraph.addEdge('S','A','a');
        listGraph.addEdge('A','A','b');
        listGraph.addEdge('B','A','b');
        listGraph.addEdge('A','B','a');
        listGraph.show();
    }

    @Test
    void testClosure() {
        ListGraph listGraph=new ListGraph();
        listGraph.addEdge('S','A','a');
        listGraph.addEdge('S','B','b');
        listGraph.addEdge('S','Z','@');
        listGraph.addEdge('A','B','a');
        listGraph.addEdge('A','A','b');
        listGraph.addEdge('B','S','a');
        listGraph.addEdge('B','A','b');
        listGraph.addEdge('B','Z','@');
        listGraph.show();
//        Set<Character> I=new HashSet<Character>();
//        I.add('S');
//        I=listGraph.findNext(I,'a');System.out.println("I+a:");
//        listGraph.show(I);
//        I=listGraph.findNext(I,'b');System.out.println("I+b:");
//        listGraph.show(I);
//        I=listGraph.findNext(I,'a');System.out.println("I+a:");
//        listGraph.show(I);
    }
}