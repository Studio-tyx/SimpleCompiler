package frame;

import org.junit.jupiter.api.Test;

class MainFrameTest {
    @Test
    void testMainFrame() {
        MainFrame frame = new MainFrame();
        while(frame.isVisible()){
            try{
                Thread.sleep(100);//死循环中降低CPU占用
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}