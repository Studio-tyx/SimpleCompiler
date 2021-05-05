package frame;

import javax.swing.*;
import java.awt.*;

/**
 * @author TYX
 * @name FAFrame
 * @description
 * @createTime 2021/5/5 21:11
 **/
public class FAFrame extends JFrame {
    private JPanel panel;
    private JScrollPane nfaScroll;
    private JScrollPane dfaScroll;
    private JTextArea nfaTextArea;
    private JTextArea dfaTextArea;
    private JLabel nfaLabel;
    private JLabel dfaLabel;
    public FAFrame() {
    }
    public void init(java.util.List<String> NFA, java.util.List<String> DFA){
        setTitle("NFA&DFA");
        setBounds(100,100,670,700);
        setResizable(false);
        panel=new JPanel();
        panel.setLayout(null);

        nfaLabel=new JLabel();
        nfaLabel.setBounds(150,20,300,30);
        nfaLabel.setText("NFA:");
        panel.add(nfaLabel);

        dfaLabel=new JLabel();
        dfaLabel.setBounds(480,20,300,30);
        dfaLabel.setText("DFA:");
        panel.add(dfaLabel);

        String nfa="";
        for(String str:NFA){
            nfa+=(str+"\n");
        }
        nfaTextArea=new JTextArea(nfa);
        nfaScroll=new JScrollPane(nfaTextArea);
        nfaScroll.setBounds(20,50,300,570);
        panel.add(nfaScroll);

        String dfa="";
        for(String str:DFA){
            dfa+=(str+"\n");
        }
        dfaTextArea=new JTextArea(dfa);
        dfaScroll=new JScrollPane(dfaTextArea);
        dfaScroll.setBounds(350,50,300,570);
        panel.add(dfaScroll);

        add(panel);
        setVisible(true);
    }
}
