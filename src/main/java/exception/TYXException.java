package exception;

import javax.swing.*;
import java.awt.*;

/**
 * @author TYX
 * @name TYXException
 * @description 各种异常（如空行、文法格式错误等）懒得建那么多异常类
 * @createTime 2021/3/21 19:21
 **/
public class TYXException extends Exception {
    public TYXException() {
        super();
    }

    /**
     * 带异常信息的构造器
     *
     * @param message 异常信息
     */
    public TYXException(String message) {
        super(message);
        show();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public void show(){
        JFrame frame=new JFrame();
        frame.setTitle("异常");
        frame.setBounds(400,200,600,400);
        JLabel label=new JLabel(this.getMessage());
        label.setFont(new Font(null,0,20));
        label.setForeground(Color.RED);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(label);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
