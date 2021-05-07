package frame;

import tool.IOTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author TYX
 * @name MainFrame
 * @description 主窗口
 * @createTime 2021/5/3 18:47
 **/
public class MainFrame extends JFrame{
    private final JLabel codeSourceLabel;

    private final LexerFrame lexerFrame;
    private final ParseFrame parseFrame;
    private final SemanticFrame semanticFrame;

    /**
     * 主方法
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        MainFrame mainFrame=new MainFrame();
    }

    /**
     * 构造器
     */
    public MainFrame(){
        setTitle("Main");
        setBounds(100,100,800,600);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        lexerFrame=new LexerFrame();
        parseFrame=new ParseFrame();
        semanticFrame=new SemanticFrame();

        panel.setLayout(null);

        JButton chooseCodeButton = new JButton("选择代码");
        chooseCodeButton.setFont(new Font("黑体", Font.PLAIN,20));
        chooseCodeButton.setBounds(330,100,140,40);
        chooseCodeButton.addActionListener(new ChooseButtonActionListener());
        panel.add(chooseCodeButton);

        codeSourceLabel=new JLabel();
        codeSourceLabel.setSize(800,30);
        codeSourceLabel.setLocation(0,150);
        codeSourceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        codeSourceLabel.setText("");
        panel.add(codeSourceLabel);

        JButton turnLexerButton = new JButton("词法分析");
        turnLexerButton.setFont(new Font("黑体", Font.PLAIN,20));
        turnLexerButton.setBounds(130,300,140,40);
        turnLexerButton.addActionListener(new LexerButtonActionListener());
        panel.add(turnLexerButton);

        JButton turnParseButton = new JButton("语法分析");
        turnParseButton.setFont(new Font("黑体", Font.PLAIN,20));
        turnParseButton.setBounds(330,300,140,40);
        turnParseButton.addActionListener(new ParseButtonActionListener());
        panel.add(turnParseButton);

        JButton turnSemanticButton = new JButton("语义分析");
        turnSemanticButton.setFont(new Font("黑体", Font.PLAIN,20));
        turnSemanticButton.setBounds(530,300,140,40);
        turnSemanticButton.addActionListener(new SemanticButtonActionListener());
        panel.add(turnSemanticButton);

        add(panel);
        setVisible(true);
    }

    /**
     * 选择代码按钮监听事件
     */
    class ChooseButtonActionListener implements ActionListener{
        /**
         * 监听事件发生
         *
         * @param e 监听事件
         */
        public void actionPerformed(ActionEvent e) {
            String labelString= IOTools.chooseFile();
            codeSourceLabel.setText(labelString);
            lexerFrame.setCodeURL(labelString);
            parseFrame.setCodeURL(labelString);
            semanticFrame.setCodeURL(labelString);
        }
    }

    /**
     * 词法分析按钮监听事件
     */
    class LexerButtonActionListener implements ActionListener{
        /**
         * 监听事件发生
         *
         * @param e 监听事件
         */
        public void actionPerformed(ActionEvent e) {
            lexerFrame.setVisible(true);
            lexerFrame.turn();
            lexerFrame.setParseFrame(parseFrame);
            lexerFrame.setSemanticFrame(semanticFrame);
        }
    }

    /**
     * 语法分析按钮监听事件
     */
    class ParseButtonActionListener implements ActionListener{
        /**
         * 监听事件发生
         *
         * @param e 监听事件
         */
        public void actionPerformed(ActionEvent e) {
            parseFrame.setVisible(true);
            parseFrame.turn();
        }
    }

    /**
     * 语义分析按钮监听事件
     */
    class SemanticButtonActionListener implements ActionListener{
        /**
         * 监听事件发生
         *
         * @param e 监听事件
         */
        public void actionPerformed(ActionEvent e) {
            semanticFrame.setVisible(true);
            semanticFrame.turn();
        }
    }
}
