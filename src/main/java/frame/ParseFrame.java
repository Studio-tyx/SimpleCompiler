package frame;

import compiler.Lexer;
import compiler.Parse;
import entity.ParseResult;
import entity.Text;
import entity.Tokens;
import exception.InputException;
import tool.IOTools;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

/**
 * @author TYX
 * @name ParseFrame
 * @description
 * @createTime 2021/5/5 14:00
 **/
public class ParseFrame extends JFrame {
    private JPanel panel;
    private JButton chooseParseButton;
    private JButton showGotoButton;
    private JButton parseAnalyseButton;
    private JTextArea codeTextArea;
    private JLabel codeLabel;
    private JLabel parseSourceLabel;
    private JLabel infoLabel;
    private JScrollPane codeScroll;
    private JScrollPane parseScroll;
    private JScrollPane infoScroll;
    private JScrollPane stackScroll;
    private JTextArea parseTextArea;
    private JTextArea infoTextArea;

    private String codeURL;
    private String parseURL;

    private Tokens tokens;

    private GotoFrame gotoFrame;

    public ParseFrame(){
        init();
    }
    private void init(){
        setTitle("语法分析");
        setBounds(100,100,1100,800);
        setResizable(false);
        panel=new JPanel();
        panel.setLayout(null);

        chooseParseButton=new JButton("选择语法");
        chooseParseButton.setBounds(120,50,100,30);
        chooseParseButton.addActionListener(new ChooseParseActionListener());
        panel.add(chooseParseButton);

        showGotoButton=new JButton("展示Goto");
        showGotoButton.setBounds(120,100,100,30);
        showGotoButton.addActionListener(new showGotoActionListener());
        panel.add(showGotoButton);

        parseAnalyseButton=new JButton("语法分析");
        parseAnalyseButton.setBounds(120,150,100,30);
        parseAnalyseButton.addActionListener(new ParseAnalyseActionListener());
        panel.add(parseAnalyseButton);

        codeLabel=new JLabel();
        codeLabel.setBounds(150,220,300,30);
        codeLabel.setText("code:");
        panel.add(codeLabel);

        parseSourceLabel=new JLabel();
        parseSourceLabel.setSize(800,30);
        parseSourceLabel.setLocation(350,50);
        parseSourceLabel.setText("");
        panel.add(parseSourceLabel);

        infoLabel=new JLabel();
        infoLabel.setSize(200,30);
        infoLabel.setLocation(870,50);
        infoLabel.setText("分析信息:");
        panel.add(infoLabel);

        codeTextArea=new JTextArea();
        codeScroll=new JScrollPane(codeTextArea);
        codeScroll.setBounds(30, 250, 300, 150);
        panel.add(codeScroll);

        parseTextArea=new JTextArea();
        parseScroll=new JScrollPane(parseTextArea);
        parseScroll.setBounds(400,100,300,300);
        panel.add(parseScroll);

        infoTextArea=new JTextArea();
        infoScroll=new JScrollPane(infoTextArea);
        infoScroll.setBounds(750,80,300,320);
        panel.add(infoScroll);

        stackScroll=new JScrollPane();
        stackScroll.setBounds(30,420,1020,300);
        //panel.add(stackScroll);

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

    public void setTokens(Tokens tokens) {
        this.tokens = tokens;
    }

    class ChooseParseActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            parseURL=IOTools.chooseFile();
            parseSourceLabel.setText("语法路径:"+parseURL);
            parseTextArea.setText(IOTools.read(parseURL));
            panel.validate();
            validate();
            setVisible(true);
        }
    }

    class showGotoActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            Text parseGrammar=new Text();
            try {
                parseGrammar.init(parseURL);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Parse parse=new Parse();
            gotoFrame=new GotoFrame();
            try {
                gotoFrame.init(parse.createGoto(parseGrammar));
            } catch (InputException inputException) {
                inputException.printStackTrace();
            }
            gotoFrame.setVisible(true);
        }
    }

    class ParseAnalyseActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            Text code = new Text(), parseGrammar = new Text();
            ParseResult parseResult=new ParseResult();
            try {
                code.init(codeURL);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Parse parse = new Parse();
            try {
                parseGrammar.init(parseURL);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            try {
                parseResult=parse.run(parseGrammar, tokens);
            }catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (InputException inputException) {
                inputException.printStackTrace();
            }
            Object[][] rowData=new Object[parseResult.getRows()][4];
            List<ParseResult.Line> lines=parseResult.getStack();
            for(int i=0;i<parseResult.getRows();i++){
                rowData[i][0]=lines.get(i).characters;
                rowData[i][1]=lines.get(i).status;
                rowData[i][2]=lines.get(i).next;
                rowData[i][3]=lines.get(i).action;
            }
            Object[] columnNames = {"分析栈", "状态栈", "下一字符",  "action"};
            JTable table = new JTable(rowData, columnNames);
//
//            table.getColumnModel().getColumn(0).setWidth(300);
//            table.getColumnModel().getColumn(1).setWidth(400);
//            table.getColumnModel().getColumn(2).setWidth(100);
//            table.getColumnModel().getColumn(3).setWidth(200);
            TableColumn tableColumn = null;
            for (int i = 0; i < 4; i++) {
                tableColumn = table.getColumnModel().getColumn(i);
                if (i==0) tableColumn.setMaxWidth(400);
                if (i==1) tableColumn.setMaxWidth(400);
                if (i==2) tableColumn.setMaxWidth(100);
                if (i==3) tableColumn.setMaxWidth(250);
            }
            stackScroll=new JScrollPane(table);
            stackScroll.setBounds(30,420,1020,300);
            panel.add(stackScroll);

            if(!parseResult.isRes()){
                infoTextArea.setText(parseResult.isRes()+"\n期望的下一字符为："+parseResult.getInformation());
            }
            else {
                infoTextArea.setText(String.valueOf(parseResult.isRes()));
            }
            validate();
        }
    }
}
