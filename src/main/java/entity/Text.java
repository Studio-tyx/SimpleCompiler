package entity;

import tool.ShowTools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author TYX
 * @name Text
 * @description 读取文件封装类
 * @time 2021/3/8 15:36
 **/


public class Text {
    private int totalLine;  //文件总行数
    private final List<ProcessLine> content;    //文件内容（以ProcessLine行为单位）

    /**
     * 构造函数 初始化行数为0
     */
    public Text() {
        totalLine = 0;
        content = new ArrayList<ProcessLine>();
    }

    /**
     * 读取内容
     * 将path文件中的内容读入
     *
     * @param path 文件路径
     */
    public void init(String path) throws IOException {
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        int currentLine = 0;
        while (line != null) {
            content.add(new ProcessLine(currentLine++, line));
            line = bufferedReader.readLine();
            totalLine++;
        }
        bufferedReader.close();
        fileReader.close();
    }


    /**
     * 返回文件内容
     *
     * @return 文件内容
     */
    public List<ProcessLine> getContent() {
        return content;
    }

    public List<ProcessLine> getStartWith(Character start){
        List<ProcessLine> res=new ArrayList<ProcessLine>();
        for(ProcessLine processLine:content){
            if(processLine.getLine().charAt(0)==start){
                res.add(processLine);
            }
        }
        return res;
    }

}
