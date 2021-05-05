package frame;

import compiler.Lexer;
import entity.Text;
import entity.Tokens;
import exception.InputException;
import tool.IOTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * @author TYX
 * @name LexerFrame
 * @description
 * @createTime 2021/5/4 13:58
 **/
public class LexerFrame extends JFrame {
    private JPanel panel;
    private JButton chooseLexerButton;
    private JButton showLexerStatusButton;
    private JButton lexerAnalyseButton;
    private JLabel codeLabel;
    private JLabel lexerSourceLabel;
    private JLabel tokenLabel;
    private JScrollPane codeScroll;
    private JScrollPane lexerScroll;
    private JScrollPane tokensScroll;
    private JTextArea codeTextArea;
    private JTextArea lexerTextArea;
    private JTextArea tokensTextArea;

    private String codeURL;
    private String lexerURL;

    private FAFrame faFrame;
    private ParseFrame parseFrame;
    private SemanticFrame semanticFrame;

    public LexerFrame(){
        init();
    }

    private void init(){
        setTitle("词法分析");
        setBounds(100,100,1100,800);
        setResizable(false);
        panel=new JPanel();

        panel.setLayout(null);

        chooseLexerButton=new JButton("选择词法");
        chooseLexerButton.setBounds(120,50,100,30);
        chooseLexerButton.addActionListener(new ChooseLexerActionListener());
        panel.add(chooseLexerButton);

        showLexerStatusButton=new JButton("NFA&DFA");
        showLexerStatusButton.setBounds(120,100,100,30);
        showLexerStatusButton.addActionListener(new ShowLexerActionListener());
        panel.add(showLexerStatusButton);

        lexerAnalyseButton=new JButton("词法分析");
        lexerAnalyseButton.setBounds(120,150,100,30);
        lexerAnalyseButton.addActionListener(new LexerAnalyseActionListener());
        panel.add(lexerAnalyseButton);

        codeLabel=new JLabel();
        codeLabel.setBounds(150,220,300,30);
        codeLabel.setText("code:");
        panel.add(codeLabel);

        lexerSourceLabel=new JLabel();
        lexerSourceLabel.setSize(800,30);
        lexerSourceLabel.setLocation(350,50);
        lexerSourceLabel.setText("");
        panel.add(lexerSourceLabel);

        tokenLabel=new JLabel();
        tokenLabel.setSize(200,30);
        tokenLabel.setLocation(850,50);
        tokenLabel.setText("tokens:");
        panel.add(tokenLabel);

        codeTextArea=new JTextArea();
        codeScroll=new JScrollPane(codeTextArea);
        codeScroll.setBounds(30, 250, 300, 450);
        panel.add(codeScroll);

        lexerTextArea=new JTextArea();
        lexerScroll=new JScrollPane(lexerTextArea);
        lexerScroll.setBounds(400,100,300,600);
        panel.add(lexerScroll);

        tokensTextArea=new JTextArea();
        tokensScroll=new JScrollPane(tokensTextArea);
        tokensScroll.setBounds(730,80,300,620);
        panel.add(tokensScroll);

        add(panel);
        setVisible(false);
    }

    public void turn(){
        codeTextArea.setText(IOTools.read(codeURL));
        panel.validate();
        validate();
        setVisible(true);
    }

    public void setCodeURL(String codeURL) {
        this.codeURL = codeURL;
    }

    public void setParseFrame(ParseFrame parseFrame) {
        this.parseFrame = parseFrame;
    }

    public void setSemanticFrame(SemanticFrame semanticFrame) {
        this.semanticFrame = semanticFrame;
    }

    class ChooseLexerActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            lexerURL=IOTools.chooseFile();
            lexerSourceLabel.setText("词法路径:"+lexerURL);
            lexerTextArea.setText(IOTools.read(lexerURL));
            panel.validate();
            validate();
            setVisible(true);
        }
    }

    class ShowLexerActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            faFrame=new FAFrame();
            Text grammar=new Text();
            try {
                grammar.init(lexerURL);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Lexer lexer=new Lexer();
            try {
                faFrame.init(lexer.getNFA(grammar),lexer.getDFA(grammar));
            } catch (InputException inputException) {
                inputException.printStackTrace();
            }
            faFrame.setVisible(true);
        }
    }

    class LexerAnalyseActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            Text code = new Text(), grammar = new Text();
            Lexer lexer = new Lexer();
            Tokens tokens = new Tokens();
            try {
                code.init(codeURL);
                grammar.init(lexerURL);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            try {
                tokens=lexer.run(code, grammar);
            } catch (InputException inputException) {
                //e.printStackTrace();
                System.out.println(inputException.getMessage());
            }
            parseFrame.setTokens(tokens);
            semanticFrame.setTokens(tokens);
            tokensTextArea.setText(tokens.showBySequence());
            panel.validate();
            validate();
            setVisible(true);
        }
    }
}
