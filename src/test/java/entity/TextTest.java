package entity;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

class TextTest {

    @Test
    void name() throws IOException {
        Text text = new Text();
        text.init("D:\\Languages\\Java\\Java Code\\SimpleCompiler\\test.txt");
//        while(!text.outLine()){
//            System.out.println(text.nextLine().toString());
//        }
//        System.out.println("------------------------");
        List<ProcessLine> all = text.getContent();
        for (ProcessLine pl : all) {
            System.out.println(pl.toString());
        }
    }
}