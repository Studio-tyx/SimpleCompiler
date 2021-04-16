package entity;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

class TextTest {

    /**
     * 测试文件读入
     *
     * @throws IOException 读写异常
     */
    @Test
    void testFile() throws IOException {
        Text text = new Text();
        text.init("test.txt");
//        while(!text.outLine()){
//            System.out.println(text.nextLine().toString());
//        }
//        System.out.println("------------------------");
        List<ProcessLine> all = text.getContent();
        for (ProcessLine pl : all) {
            System.out.println(pl.toString());
        }
    }

    //@Test
    void testRegex() {
        String string="hey#this";
        String[] split = string.split("#");
        for(String s:split){
            System.out.println(s);
        }
    }
}