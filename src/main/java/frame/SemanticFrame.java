package frame;

import compiler.Parse;
import entity.ParseResult;
import entity.Text;
import entity.Tokens;
import exception.TYXException;
import tool.IOTools;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

/**
 * @author TYX
 * @name SemanticFrame
 * @description
 * @createTime 2021/5/5 18:51
 **/
public class SemanticFrame extends JFrame {
    private JPanel panel;
    private JButton chooseSemanticButton;
    private JButton semanticAnalyseButton;
    private JLabel codeLabel;
    private JLabel semanticSourceLabel;
    private JLabel tokenLabel;
    private JScrollPane codeScroll;
    private JScrollPane semanticScroll;
    private JScrollPane quaternionScroll;
    private JTextArea codeTextArea;
    private JTextArea semanticTextArea;
    private JTextArea quaternionTextArea;

    private String codeURL;
    private String semanticURL;

    private Tokens tokens;

    public SemanticFrame(){
        init();
    }

    public void init(){
        setTitle("语义分析");
        setBounds(100,100,750,600);
        setResizable(false);
        panel=new JPanel();
        panel.setLayout(null);

        chooseSemanticButton=new JButton("选择语义");
        chooseSemanticButton.setBounds(120,50,100,30);
        chooseSemanticButton.addActionListener(new ChooseSemanticActionListener());
        panel.add(chooseSemanticButton);

        semanticAnalyseButton=new JButton("语义分析");
        semanticAnalyseButton.setBounds(120,100,100,30);
        semanticAnalyseButton.addActionListener(new SemanticAnalyseActionListener());
        panel.add(semanticAnalyseButton);

        codeLabel=new JLabel();
        codeLabel.setBounds(150,150,300,30);
        codeLabel.setText("code:");
        panel.add(codeLabel);

        semanticSourceLabel=new JLabel();
        semanticSourceLabel.setSize(800,30);
        semanticSourceLabel.setLocation(300,50);
        semanticSourceLabel.setText("");
        panel.add(semanticSourceLabel);

        tokenLabel=new JLabel();
        tokenLabel.setSize(200,30);
        tokenLabel.setLocation(150,370);
        tokenLabel.setText("tokens:");
        panel.add(tokenLabel);

        codeTextArea=new JTextArea();
        codeScroll=new JScrollPane(codeTextArea);
        codeScroll.setBounds(30, 180, 300, 170);
        panel.add(codeScroll);

        semanticTextArea=new JTextArea();
        semanticScroll=new JScrollPane(semanticTextArea);
        semanticScroll.setBounds(400,100,300,400);
        panel.add(semanticScroll);

        quaternionTextArea=new JTextArea();
        quaternionScroll=new JScrollPane(quaternionTextArea);
        quaternionScroll.setBounds(30,400,300,100);
        panel.add(quaternionScroll);

        add(panel);
        setVisible(false);
    }

    public void turn(){
        codeTextArea.setText(IOTools.read(codeURL));
    }

    public void setCodeURL(String codeURL) {
        this.codeURL = codeURL;
    }

    public void setTokens(Tokens tokens) {
        this.tokens = tokens;
    }

    class ChooseSemanticActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            semanticURL=IOTools.chooseFile();
            semanticSourceLabel.setText(semanticURL);
            semanticTextArea.setText(IOTools.read(semanticURL));
            panel.validate();
            validate();
            setVisible(true);
        }
    }

    class SemanticAnalyseActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
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
            }catch (IOException ioException) {
                ioException.printStackTrace();
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
            panel.validate();
            validate();
        }
    }
}
