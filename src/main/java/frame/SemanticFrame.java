package frame;

import compiler.Parse;
import entity.ParseResult;
import entity.Text;
import entity.Tokens;
import exception.TYXException;
import tool.IOTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

/**
 * @author TYX
 * @name SemanticFrame
 * @description 语义分析窗口
 * @createTime 2021/5/5 18:51
 **/
public class SemanticFrame extends JFrame {
    private JPanel panel;
    private JLabel semanticSourceLabel;
    private JTextArea codeTextArea;
    private JTextArea semanticTextArea;
    private JTextArea quaternionTextArea;

    private String codeURL;
    private String semanticURL;

    private Tokens tokens;

    /**
     * 构造器
     */
    public SemanticFrame(){
        init();
    }

    /**
     * 初始化
     */
    public void init(){
        setTitle("语义分析");
        setBounds(100,100,750,600);
        setResizable(false);
        panel=new JPanel();
        panel.setLayout(null);

        JButton chooseSemanticButton = new JButton("选择语义");
        chooseSemanticButton.setFont(new Font("黑体", Font.PLAIN,20));
        chooseSemanticButton.setBounds(120,50,140,30);
        chooseSemanticButton.addActionListener(new ChooseSemanticActionListener());
        panel.add(chooseSemanticButton);

        JButton semanticAnalyseButton = new JButton("语义分析");
        semanticAnalyseButton.setFont(new Font("黑体", Font.PLAIN,20));
        semanticAnalyseButton.setBounds(120,100,140,30);
        semanticAnalyseButton.addActionListener(new SemanticAnalyseActionListener());
        panel.add(semanticAnalyseButton);

        JLabel codeLabel = new JLabel();
        codeLabel.setBounds(170,150,300,30);
        codeLabel.setText("code:");
        codeLabel.setFont(new Font("Courier", Font.PLAIN,20));
        panel.add(codeLabel);

        semanticSourceLabel=new JLabel();
        semanticSourceLabel.setSize(450,30);
        semanticSourceLabel.setLocation(300,50);
        semanticSourceLabel.setText("");
        panel.add(semanticSourceLabel);

        JLabel tokenLabel = new JLabel();
        tokenLabel.setSize(200,30);
        tokenLabel.setLocation(170,370);
        tokenLabel.setText("四元式:");
        tokenLabel.setFont(new Font("Courier", Font.PLAIN,20));
        panel.add(tokenLabel);

        codeTextArea=new JTextArea();
        JScrollPane codeScroll = new JScrollPane(codeTextArea);
        codeScroll.setBounds(30, 180, 350, 170);
        panel.add(codeScroll);

        semanticTextArea=new JTextArea();
        JScrollPane semanticScroll = new JScrollPane(semanticTextArea);
        semanticScroll.setBounds(430,100,300,400);
        panel.add(semanticScroll);

        quaternionTextArea=new JTextArea();
        JScrollPane quaternionScroll = new JScrollPane(quaternionTextArea);
        quaternionScroll.setBounds(30,400,350,100);
        panel.add(quaternionScroll);

        add(panel);
        setVisible(false);
    }

    /**
     * 代码初始化
     */
    public void turn(){
        codeTextArea.setText(IOTools.read(codeURL));
        codeTextArea.setFont(new Font(null, Font.PLAIN,15));
    }

    /**
     * 设置代码URL
     * @param codeURL 代码URL String
     */
    public void setCodeURL(String codeURL) {
        this.codeURL = codeURL;
    }

    /**
     * 设置Tokens
     * @param tokens Tokens Tokens
     */
    public void setTokens(Tokens tokens) {
        this.tokens = tokens;
    }

    /**
     * 选择语义按钮监听事件
     */
    class ChooseSemanticActionListener implements ActionListener{
        /**
         * 监听事件发生
         *
         * @param e 监听事件
         */
        public void actionPerformed(ActionEvent e) {
            semanticURL=IOTools.chooseFile();
            semanticSourceLabel.setText("语义路径:"+semanticURL);
            semanticTextArea.setText(IOTools.read(semanticURL));
            semanticTextArea.setFont(new Font(null, Font.PLAIN,15));
            panel.validate();
            validate();
            setVisible(true);
        }
    }

    /**
     * 语义分析按钮监听事件
     */
    class SemanticAnalyseActionListener implements ActionListener{
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
                    System.out.println("hey");
                    TYXException.printStackTrace();
                    return;
                }
            }
            if(semanticURL==null){
                try {
                    throw new TYXException("Input error: please input grammar!");
                } catch (TYXException TYXException) {
                    System.out.println("hey");
                    TYXException.printStackTrace();
                    return;
                }
            }
            Text code = new Text(), semanticGrammar = new Text();
            ParseResult parseResult=new ParseResult();
            try {
                code.init(codeURL);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Parse parse = new Parse();
            try {
                semanticGrammar.init(semanticURL);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            try {
                parseResult=parse.run(semanticGrammar, tokens);
            } catch (TYXException TYXException) {
                TYXException.show();
                TYXException.printStackTrace();
            }
            List<String> quaternions=parseResult.getQuaternions();
            String quaternionString="";
            for(String quarter:quaternions){
                quaternionString+=(quarter+"\n");
            }
            quaternionTextArea.setText(quaternionString);
            quaternionTextArea.setFont(new Font(null, Font.PLAIN,15));
            panel.validate();
            validate();
        }
    }
}
