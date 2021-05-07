package frame;

import compiler.Parse;
import entity.ParseResult;
import entity.Text;
import entity.Tokens;
import exception.TYXException;
import tool.IOTools;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

/**
 * @author TYX
 * @name ParseFrame
 * @description 语法分析窗口
 * @createTime 2021/5/5 14:00
 **/
public class ParseFrame extends JFrame {
    private JPanel panel;
    private JTextArea codeTextArea;
    private JLabel parseSourceLabel;
    private JScrollPane stackScroll;
    private JTextArea parseTextArea;
    private JTextArea infoTextArea;

    private String codeURL;
    private String parseURL;

    private Tokens tokens;

    /**
     * 构造器
     */
    public ParseFrame(){
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        setTitle("语法分析");
        setBounds(100,100,1100,800);
        setResizable(false);
        panel=new JPanel();
        panel.setLayout(null);

        JButton chooseParseButton = new JButton("选择语法");
        chooseParseButton.setFont(new Font("黑体", Font.PLAIN,20));
        chooseParseButton.setBounds(100,50,140,30);
        chooseParseButton.addActionListener(new ChooseParseActionListener());
        panel.add(chooseParseButton);

        JButton showGotoButton = new JButton("展示Goto");
        showGotoButton.setFont(new Font("黑体", Font.PLAIN,20));
        showGotoButton.setBounds(100,100,140,30);
        showGotoButton.addActionListener(new showGotoActionListener());
        panel.add(showGotoButton);

        JButton parseAnalyseButton = new JButton("语法分析");
        parseAnalyseButton.setFont(new Font("黑体", Font.PLAIN,20));
        parseAnalyseButton.setBounds(100,150,140,30);
        parseAnalyseButton.addActionListener(new ParseAnalyseActionListener());
        panel.add(parseAnalyseButton);

        JLabel codeLabel = new JLabel();
        codeLabel.setBounds(150,220,300,30);
        codeLabel.setText("code:");
        codeLabel.setFont(new Font("Courier", Font.PLAIN,20));
        panel.add(codeLabel);

        parseSourceLabel=new JLabel();
        parseSourceLabel.setSize(500,30);
        parseSourceLabel.setLocation(350,50);
        parseSourceLabel.setText("");
        panel.add(parseSourceLabel);

        JLabel infoLabel = new JLabel();
        infoLabel.setSize(200,30);
        infoLabel.setLocation(850,50);
        infoLabel.setText("分析信息:");
        infoLabel.setFont(new Font("Courier", Font.PLAIN,20));
        panel.add(infoLabel);

        codeTextArea=new JTextArea();
        JScrollPane codeScroll = new JScrollPane(codeTextArea);
        codeScroll.setBounds(30, 250, 300, 150);
        panel.add(codeScroll);

        parseTextArea=new JTextArea();
        JScrollPane parseScroll = new JScrollPane(parseTextArea);
        parseScroll.setBounds(400,100,300,300);
        panel.add(parseScroll);

        infoTextArea=new JTextArea();
        JScrollPane infoScroll = new JScrollPane(infoTextArea);
        infoScroll.setBounds(750,80,300,320);
        panel.add(infoScroll);

        stackScroll=new JScrollPane();
        stackScroll.setBounds(30,420,1020,300);
        //panel.add(stackScroll);

        add(panel);
        setVisible(false);

    }

    /**
     * 代码展示（初始化）
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
     *
     * @param codeURL 代码URL String
     */
    public void setCodeURL(String codeURL) {
        this.codeURL = codeURL;
    }

    /**
     * 设置Tokens
     *
     * @param tokens Tokens Tokens
     */
    public void setTokens(Tokens tokens) {
        this.tokens = tokens;
    }

    /**
     * 选择语法按钮监听事件
     */
    class ChooseParseActionListener implements ActionListener{
        /**
         * 监听事件发生
         *
         * @param e 监听事件
         */
        public void actionPerformed(ActionEvent e) {
            parseURL=IOTools.chooseFile();
            parseSourceLabel.setText("语法路径:"+parseURL);
            parseTextArea.setText(IOTools.read(parseURL));
            parseTextArea.setFont(new Font(null, Font.PLAIN,15));
            panel.validate();
            validate();
            setVisible(true);
        }
    }

    /**
     * 展示Goto按钮监听事件
     */
    class showGotoActionListener implements ActionListener{
        /**
         * 监听事件发生
         *
         * @param e 监听事件
         */
        public void actionPerformed(ActionEvent e) {
            if(parseURL==null){
                try {
                    throw new TYXException("Input error: please input grammar!");
                } catch (TYXException TYXException) {
                    System.out.println("hey");
                    TYXException.printStackTrace();
                    return;
                }
            }
            Text parseGrammar=new Text();
            try {
                parseGrammar.init(parseURL);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Parse parse=new Parse();
            GotoFrame gotoFrame = new GotoFrame();
            try {
                gotoFrame.init(parse.createGoto(parseGrammar));
            } catch (TYXException TYXException) {
                TYXException.printStackTrace();
            }
            gotoFrame.setVisible(true);
        }
    }

    /**
     * 语法分析按钮监听事件
     */
    class ParseAnalyseActionListener implements ActionListener{
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
            if(parseURL==null){
                try {
                    throw new TYXException("Input error: please input grammar!");
                } catch (TYXException TYXException) {
                    System.out.println("hey");
                    TYXException.printStackTrace();
                    return;
                }
            }
            if(tokens==null){
                try {
                    throw new TYXException("Input error: please analyse lexer first!");
                } catch (TYXException TYXException) {
                    System.out.println("hey");
                    TYXException.printStackTrace();
                    return;
                }
            }
            Text code = new Text(), parseGrammar = new Text();
            ParseResult parseResult;
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
            } catch (TYXException TYXException) {
                TYXException.printStackTrace();
                return;
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
            TableColumn tableColumn;
            for (int i = 0; i < 4; i++) {
                tableColumn = table.getColumnModel().getColumn(i);
                if (i==0) tableColumn.setMaxWidth(300);
                if (i==1) tableColumn.setMaxWidth(700);
                if (i==2) tableColumn.setMaxWidth(80);
                if (i==3) tableColumn.setMaxWidth(120);
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
            infoTextArea.setFont(new Font(null, Font.PLAIN,15));
            validate();
        }
    }
}
