package frame;

import entity.GOTO;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author TYX
 * @name GotoFrame
 * @description
 * @createTime 2021/5/5 20:21
 **/
public class GotoFrame extends JFrame {
    private JPanel panel;
    private JScrollPane gotoScroll;

    public GotoFrame(){
        setTitle("Goto");
        setBounds(20,10,1600,900);
        setResizable(false);
        panel=new JPanel();
        panel.setLayout(null);
        gotoScroll=new JScrollPane();
//        gotoScroll.setBounds(20,20,1560,960);
//        panel.add(gotoScroll);
        add(panel);
        setVisible(false);
    }

    public void init(GOTO gotoTable){
        List<Character> terminals=gotoTable.getTerminals();
        List<Character> nonTerminals=gotoTable.getNonTerminals();
        int column=terminals.size()+nonTerminals.size();
        int statusNumber=gotoTable.getStatusNumber();
        String[][] expend=new String[statusNumber][column+1];
        String[][] gotoStrings=gotoTable.getGoto();
        for(int i=0;i<statusNumber;i++){
            expend[i][0]= String.valueOf(i);
            for(int j=0;j<column;j++){
                if(gotoStrings[i][j].equals("n")) expend[i][j+1]=" ";
                else expend[i][j+1]=gotoStrings[i][j];
            }
        }
        Object[][] rowData=expend;
        Object[] columnNames=new Object[column+1];
        columnNames[0]=" ";
        for(int i=0;i<terminals.size();i++){
            columnNames[i+1]=terminals.get(i);
        }
        for(int i=0;i<nonTerminals.size();i++){
            columnNames[i+1+terminals.size()]=nonTerminals.get(i);
        }
        JTable table=new JTable(rowData,columnNames);
        gotoScroll=new JScrollPane(table);
        gotoScroll.setBounds(20,20,1560,820);
        panel.add(gotoScroll);
        validate();
    }
}
