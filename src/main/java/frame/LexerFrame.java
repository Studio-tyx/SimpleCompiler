package frame;

import compiler.Lexer;
import entity.Text;
import entity.Tokens;
import exception.TYXException;
import tool.IOTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * @author TYX
 * @name LexerFrame
 * @description 词法分析窗口
 * @createTime 2021/5/4 13:58
 **/
public class LexerFrame extends JFrame {
    private JPanel panel;
    private JLabel lexerSourceLabel;
    private JTextArea codeTextArea;
    private JTextArea lexerTextArea;
    private JTextArea tokensTextArea;

    private String codeURL;
    private String lexerURL;

    private ParseFrame parseFrame;
    private SemanticFrame semanticFrame;

    /**
     * 构造器
     */
    public LexerFrame(){
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        setTitle("词法分析");
        setBounds(100,100,1100,800);
        setResizable(false);
        panel=new JPanel();

        panel.setLayout(null);

        JButton chooseLexerButton = new JButton("选择词法");
        chooseLexerButton.setFont(new Font("黑体", Font.PLAIN,20));
        chooseLexerButton.setBounds(100,50,140,30);
        chooseLexerButton.addActionListener(new ChooseLexerActionListener());
        panel.add(chooseLexerButton);

        JButton showLexerStatusButton = new JButton("NFA&DFA");
        showLexerStatusButton.setFont(new Font("黑体", Font.PLAIN,20));
        showLexerStatusButton.setBounds(100,100,140,30);
        showLexerStatusButton.addActionListener(new ShowLexerActionListener());
        panel.add(showLexerStatusButton);

        JButton lexerAnalyseButton = new JButton("词法分析");
        lexerAnalyseButton.setFont(new Font("黑体", Font.PLAIN,20));
        lexerAnalyseButton.setBounds(100,150,140,30);
        lexerAnalyseButton.addActionListener(new LexerAnalyseActionListener());
        panel.add(lexerAnalyseButton);

        JLabel codeLabel = new JLabel();
        codeLabel.setBounds(150,220,300,30);
        codeLabel.setText("code:");
        codeLabel.setFont(new Font("Courier", Font.PLAIN,20));
        panel.add(codeLabel);

        lexerSourceLabel=new JLabel();
        lexerSourceLabel.setSize(470,30);
        lexerSourceLabel.setLocation(280,50);
        lexerSourceLabel.setText("");
        panel.add(lexerSourceLabel);

        JLabel tokenLabel = new JLabel();
        tokenLabel.setSize(200,30);
        tokenLabel.setLocation(750,50);
        tokenLabel.setText("tokens:");
        tokenLabel.setFont(new Font("Courier", Font.PLAIN,20));
        panel.add(tokenLabel);

        codeTextArea=new JTextArea();
        JScrollPane codeScroll = new JScrollPane(codeTextArea);
        codeScroll.setBounds(30, 250, 300, 450);
        panel.add(codeScroll);

        lexerTextArea=new JTextArea();
        JScrollPane lexerScroll = new JScrollPane(lexerTextArea);
        lexerScroll.setBounds(400,100,150,600);
        panel.add(lexerScroll);

        tokensTextArea=new JTextArea();
        JScrollPane tokensScroll = new JScrollPane(tokensTextArea);
        tokensScroll.setBounds(580,80,450,620);
        panel.add(tokensScroll);

        add(panel);
        setVisible(false);
    }

    /**
     * 代码初始化界面
     */
    public void turn(){
        codeTextArea.setText(IOTools.read(codeURL));
        codeTextArea.setFont(new Font(null, Font.PLAIN,15));
        panel.validate();
        validate();
        setVisible(true);
    }

    /**
     * 设置代码URL
     * @param codeURL 代码URL String
     */
    public void setCodeURL(String codeURL) {
        this.codeURL = codeURL;
    }

    /**
     * 设置语法分析界面（绑定）
     *
     * @param parseFrame 语法分析界面 ParseFrame
     */
    public void setParseFrame(ParseFrame parseFrame) {
        this.parseFrame = parseFrame;
    }

    /**
     * 设置语义分析界面（绑定）
     *
     * @param semanticFrame 语义分析界面 semanticFrame
     */
    public void setSemanticFrame(SemanticFrame semanticFrame) {
        this.semanticFrame = semanticFrame;
    }

    /**
     * 选择词法按钮监听事件
     */
    class ChooseLexerActionListener implements ActionListener{
        /**
         * 监听事件发生
         *
         * @param e 监听事件
         */
        public void actionPerformed(ActionEvent e) {
            lexerURL=IOTools.chooseFile();
            lexerSourceLabel.setText("词法路径:"+lexerURL);
            lexerTextArea.setText(IOTools.read(lexerURL));
            lexerTextArea.setFont(new Font(null, Font.PLAIN,15));
            panel.validate();
            validate();
            setVisible(true);
        }
    }

    /**
     * 展示NFA&DFA按钮监听事件
     */
    class ShowLexerActionListener implements ActionListener{
        /**
         * 监听事件发生
         *
         * @param e 监听事件
         */
        public void actionPerformed(ActionEvent e) {
            if(lexerURL==null){
                try {
                    throw new TYXException("Input error: please input grammar!");
                } catch (TYXException TYXException) {
                    TYXException.printStackTrace();
                    return;
                }
            }
            FAFrame faFrame = new FAFrame();
            Text grammar=new Text();
            try {
                grammar.init(lexerURL);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Lexer lexer=new Lexer();
            try {
                faFrame.init(lexer.getNFA(grammar),lexer.getDFA(grammar));
            } catch (TYXException TYXException) {
                TYXException.printStackTrace();
                return;
            }
            faFrame.setVisible(true);
        }
    }

    /**
     * 词法分析按钮监听事件
     */
    class LexerAnalyseActionListener implements ActionListener{
        /**
         * 监听事件发生
         *
         * @param e 监听事件
         */
        public void actionPerformed(ActionEvent e) {
            if(codeURL==null){
                try {
                    throw new TYXException("Input error: please input code!");
                } catch (TYXException TYXException) {
                    TYXException.printStackTrace();
                    return;
                }
            }
            if(lexerURL==null){
                try {
                    throw new TYXException("Input error: please input grammar!");
                } catch (TYXException TYXException) {
                    TYXException.printStackTrace();
                    return;
                }
            }
            Text code = new Text(), grammar = new Text();
            Lexer lexer = new Lexer();
            Tokens tokens;
            try {
                code.init(codeURL);
                grammar.init(lexerURL);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            try {
                tokens=lexer.run(code, grammar);
            } catch (TYXException TYXException) {
                TYXException.printStackTrace();
                return;
            }
            parseFrame.setTokens(tokens);
            semanticFrame.setTokens(tokens);
            tokensTextArea.setText(tokens.showBySequence());
            tokensTextArea.setFont(new Font(null, Font.PLAIN,15));
            panel.validate();
            validate();
            setVisible(true);
        }
    }
}
