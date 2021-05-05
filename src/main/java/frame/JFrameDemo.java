package frame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author TYX
 * @name JFrameDemo
 * @description
 * @createTime 2021/5/3 16:27
 **/
public class JFrameDemo {
    public static void main(String[] args) {
        new MyJFrame().init();
    }
}

class MyJFrame extends JFrame{
    otherFrame otherFrame;
    public void init(){
        otherFrame=new otherFrame(this);
        otherFrame.setVisible(false);
        setBounds(100,100,500,300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //content.setBackground(Color.cyan);
//        content.setLayout(null);
//        JButton jButton = new JButton("click me!");
//        jButton.setBounds(20,20,100,20);
//        jButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                otherFrame.setVisible(true);
//                setVisible(false);
//            }
//        });
//        content.add(jButton);

        JPanel panel=new JPanel();
        //panel.setLayout(null);
//        JTextArea text= new JTextArea(10,10);
//        text.setLocation(0,0);
//        //text.setBounds(40,40,100,100);
//        text.setText("hey\nthis\nis\ntyx!\nt\nt\nt\nt\nt\nt\nt\nt\nt\nt");
//        JScrollPane jScrollPane = new JScrollPane(text);
//        panel.add(jScrollPane);

        Object[] columnNames = {"姓名", "语文", "数学", "英语", "总分"};

        // 表格所有行数据
        Object[][] rowData = {
                {"张三", 80, 80, 80, 240},
                {"John", 70, 80, 90, 240},
                {"Sue", 70, 70, 70, 210},
                {"Jane", 80, 70, 60, 210},
                {"Joe", 80, 70, 60, 210}
        };
        // 以Names和playerInfo为参数，创建一个表格

        JTable table = new JTable(rowData, columnNames);
        System.out.println(table);

        JScrollPane jScrollPane=new JScrollPane(table);
        panel.add(jScrollPane);
        add(panel);
        setVisible(true);
        //content.add(jScrollPane);
    }
}

class otherFrame extends JFrame{
    public otherFrame(final MyJFrame myJFrame){
        setVisible(true);
        setBounds(400,100,200,200);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container content = getContentPane();
        content.setBackground(Color.lightGray);
        content.setLayout(null);
        JButton jButton = new JButton("return!");
        jButton.setBounds(20,20,100,20);
        jButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                myJFrame.setVisible(true);
                setVisible(false);
            }
        });
        content.add(jButton);
    }
}
