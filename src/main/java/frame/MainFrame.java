package frame;

import tool.IOTools;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author TYX
 * @name MainFrame
 * @description
 * @createTime 2021/5/3 18:47
 **/
public class MainFrame extends JFrame{
    private JButton chooseCodeButton;
    private JButton turnLexerButton;
    private JButton turnParseButton;
    private JButton turnSemanticButton;
    private JLabel codeSourceLabel;
    private JPanel panel;

    private LexerFrame lexerFrame;
    private ParseFrame parseFrame;
    private SemanticFrame semanticFrame;


    //LexerFrame
    //ParseFrame
    //SemanticFrame


    public MainFrame(){
        setTitle("Main");
        setBounds(100,100,800,600);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        panel=new JPanel();
        lexerFrame=new LexerFrame();
        parseFrame=new ParseFrame();
        semanticFrame=new SemanticFrame();

        panel.setLayout(null);

        chooseCodeButton = new JButton("选择代码");
        chooseCodeButton.setBounds(350,100,100,40);
        chooseCodeButton.addActionListener(new ChooseButtonActionListener());
        panel.add(chooseCodeButton);

        codeSourceLabel=new JLabel();
        codeSourceLabel.setSize(800,30);
        codeSourceLabel.setLocation(0,150);
        codeSourceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        codeSourceLabel.setText("");
        panel.add(codeSourceLabel);

        turnLexerButton = new JButton("词法分析");
        turnLexerButton.setBounds(150,300,100,40);
        turnLexerButton.addActionListener(new LexerButtonActionListener());
        panel.add(turnLexerButton);

        turnParseButton = new JButton("语法分析");
        turnParseButton.setBounds(350,300,100,40);
        turnParseButton.addActionListener(new ParseButtonActionListener());
        panel.add(turnParseButton);

        turnSemanticButton=new JButton("语义分析");
        turnSemanticButton.setBounds(550,300,100,40);
        turnSemanticButton.addActionListener(new SemanticButtonActionListener());
        panel.add(turnSemanticButton);

        add(panel);
        setVisible(true);
    }

    class ChooseButtonActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            String labelString= IOTools.chooseFile();
            codeSourceLabel.setText(labelString);
            System.out.println("main:"+labelString);
            lexerFrame.setCodeURL(labelString);
            parseFrame.setCodeURL(labelString);
            semanticFrame.setCodeURL(labelString);
        }
    }

    class LexerButtonActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            lexerFrame.setVisible(true);
            lexerFrame.turn();
            lexerFrame.setParseFrame(parseFrame);
            lexerFrame.setSemanticFrame(semanticFrame);
        }
    }

    class ParseButtonActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            parseFrame.setVisible(true);
            parseFrame.turn();
        }
    }

    class SemanticButtonActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            semanticFrame.setVisible(true);
            semanticFrame.turn();
        }
    }
}
