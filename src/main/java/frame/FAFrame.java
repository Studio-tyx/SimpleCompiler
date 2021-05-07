package frame;

import javax.swing.*;
import java.awt.*;

/**
 * @author TYX
 * @name FAFrame
 * @description NFA&DFA的展示窗口
 * @createTime 2021/5/5 21:11
 **/
public class FAFrame extends JFrame {

    /**
     * 构造器
     */
    public FAFrame() {
    }

    /**
     * 初始化
     *
     * @param NFA NFA java.util.List of String
     * @param DFA DFA java.util.List of String
     */
    public void init(java.util.List<String> NFA, java.util.List<String> DFA){
        setTitle("NFA&DFA");
        setBounds(100,100,670,700);
        setResizable(false);
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel nfaLabel = new JLabel();
        nfaLabel.setBounds(150,20,300,30);
        nfaLabel.setText("NFA:");
        nfaLabel.setFont(new Font("Courier", Font.PLAIN,20));
        panel.add(nfaLabel);

        JLabel dfaLabel = new JLabel();
        dfaLabel.setBounds(480,20,300,30);
        dfaLabel.setText("DFA:");
        dfaLabel.setFont(new Font("Courier", Font.PLAIN,20));
        panel.add(dfaLabel);

        String nfa="";
        for(String str:NFA){
            nfa+=(str+"\n");
        }
        JTextArea nfaTextArea = new JTextArea(nfa);
        nfaTextArea.setFont(new Font(null, Font.PLAIN,15));
        JScrollPane nfaScroll = new JScrollPane(nfaTextArea);
        nfaScroll.setBounds(20,50,300,570);
        panel.add(nfaScroll);

        String dfa="";
        for(String str:DFA){
            dfa+=(str+"\n");
        }
        JTextArea dfaTextArea = new JTextArea(dfa);
        dfaTextArea.setFont(new Font(null, Font.PLAIN,15));
        JScrollPane dfaScroll = new JScrollPane(dfaTextArea);
        dfaScroll.setBounds(350,50,300,570);
        panel.add(dfaScroll);

        add(panel);
        setVisible(true);
    }
}
